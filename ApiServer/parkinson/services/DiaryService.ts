import {PatientModel} from "../models/PatientModel";
import {MedicineModel} from "../models/MedicineModel";
import {DiaryCreateRequest} from "../dto/DiaryRequestDto";

import {DiaryInfoResponse} from '../dto/DiaryResponseDto';
import {InvalidPatientNumberError} from '../error/PatientServiceError';

export class DiaryService {

  private patientModel: PatientModel;
  private medicineModel: MedicineModel;

  private static instance: DiaryService;

  public static getInstance(patientModel: PatientModel, medicineModel: MedicineModel){
    if(this.instance !== undefined){
      return this.instance;
    }
    return new DiaryService(patientModel, medicineModel);
  }

  private constructor(patientModel: PatientModel, medicineModel: MedicineModel){
    this.patientModel = patientModel;
    this.medicineModel = medicineModel;
  }

  public async getDiary(patientNum: number){
    const patient = await this.patientModel.findByPatientNum(patientNum);
    const medicine = await this.medicineModel.findByPatientNum(patientNum);
    
    if(patient.length == 0 || patient[0] === undefined) {
      throw new InvalidPatientNumberError('유효하지 않은 환자번호 입니다.');
    }

    return new DiaryInfoResponse(
      patient[0].sleep_start_time,
      patient[0].sleep_end_time,
      medicine
    );
  }

  public async createDiary(patientNum: number, diaryCreateRequest: DiaryCreateRequest){
    await this.patientModel
      .updateSleepTimeByPatientNum(
        patientNum,
        diaryCreateRequest.sleepStartTime,
        diaryCreateRequest.sleepEndTime
      );
    for(let i = 0; i < diaryCreateRequest.takeTimes.length; i++){
      const takeTime = diaryCreateRequest.takeTimes[i].take_time;
      await this.medicineModel.createMedicineTakeTime(patientNum, takeTime);
    }
  }

  public async updateDiary(patientNum: number, diaryCreateRequest: DiaryCreateRequest){
    await this.patientModel
      .updateSleepTimeByPatientNum(
        patientNum,
        diaryCreateRequest.sleepStartTime,
        diaryCreateRequest.sleepEndTime
      );

    await this.medicineModel.deleteAllMedicineTakeTime(patientNum);

    for(let i = 0; i < diaryCreateRequest.takeTimes.length; i++){
      const takeTime = diaryCreateRequest.takeTimes[i].take_time;
      await this.medicineModel.createMedicineTakeTime(patientNum, takeTime);
    }
  }

}