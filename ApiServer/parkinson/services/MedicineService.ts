import {PatientModel} from "../models/PatientModel";
import {MedicineModel} from "../models/MedicineModel";
import schedule, {Job} from "node-schedule";
import {fcmAdmin} from "../config/FcmConfig";

export class MedicineService {

    private patientModel: PatientModel;
    private medicineModel: MedicineModel;

    private static instance: MedicineService;

    public static getInstance(patientModel: PatientModel, medicineModel: MedicineModel){
        if(this.instance !== undefined){
            return this.instance;
        }
        return new MedicineService(patientModel, medicineModel);
    }

    private constructor(patientModel: PatientModel, medicineModel: MedicineModel){
        this.patientModel = patientModel;
        this.medicineModel = medicineModel;
    }

    public async notifyMedicineTakeTime(patientNum: number){
        const patient = await this.patientModel.findByPatientNum(patientNum);
        const medicines = await this.medicineModel.findByPatientNum(patientNum);

        const fcmRegistrationToken = patient[0].fcm_registration_token;
        const message = {
            data: {
                type: 'medicine',
                title: '약 복용시간 알람',
                body: '약을 복용해주세요'
            },
            token: fcmRegistrationToken
        }

        for (let i = 0; i < medicines.length; i++) {
            const scheduleName = `medicine_${patientNum}_${medicines[i].take_time}`;
            // 스케줄링 규칙 설정 - 9~21시까지 1시간마다 반복
            const rule = new schedule.RecurrenceRule();
            rule.tz = 'Asia/Seoul';
            rule.hour = Number(medicines[i].take_time.substring(0, 2));
            rule.minute = Number(medicines[i].take_time.substring(3, 5));

            schedule.scheduleJob(scheduleName, rule, async () => {
                await fcmAdmin.messaging()
                    .send(message)
                    .then((response) => {
                        console.debug(`${new Date()}: 약 복용시간 알람 전송 완료`);
                    })
                    .catch((error) => {
                        console.error(`${new Date()}: 약 복용시간 알람 전송 실패`);
                    });
            });
        }
    }

    public async stopNotifyMedicineTakeTime(patientNum: number) {
        const medicines = await this.medicineModel.findByPatientNum(patientNum);
        // 설문조사 스케줄러 취소
        this.cancelMedicineSchedule(patientNum, medicines);
    }

    private cancelMedicineSchedule(patientNum: number, medicines: Array<any>) {
        for(let i = 0; i < medicines.length; i++){
            const scheduleName = `medicine_${patientNum}_${medicines[i].take_time}`;
            const scheduledSurveyJob: Job = schedule.scheduledJobs[scheduleName];
            if(schedule.cancelJob(scheduledSurveyJob)){
                console.debug(`${new Date()}: 약 복용시간 알람 취소`);
            }
        }
    }

}