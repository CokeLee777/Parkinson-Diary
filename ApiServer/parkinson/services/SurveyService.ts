import {SurveyModel} from "../models/SurveyModel";
import {SurveyCreateRequest} from "../dto/SurveyRequestDto";
import schedule, {Job} from "node-schedule";
import {fcmAdmin} from "../config/FcmConfig";
import {PatientModel} from "../models/PatientModel";

export class SurveyService {

  private surveyModel: SurveyModel;
  private patientModel: PatientModel;

  private static instance: SurveyService;

  public static getInstance(patientModel: PatientModel, surveyModel: SurveyModel){
    if(this.instance !== undefined){
      return this.instance;
    }
    return new SurveyService(patientModel, surveyModel);
  }

  private constructor(patientModel: PatientModel, surveyModel: SurveyModel){
    this.patientModel = patientModel;
    this.surveyModel = surveyModel;
  }

  public async createSurvey(patientNum: number, surveyCreateRequest: SurveyCreateRequest){
    await this.surveyModel
      .createSurvey(
        patientNum,
        surveyCreateRequest.hasAbnormalMovement,
        surveyCreateRequest.hasMedicinalEffect,
        surveyCreateRequest.patientCondition
      );
  }

  public async notifySurvey(patientNum: number){
    const patient = await this.patientModel.findByPatientNum(patientNum);
    const fcmRegistrationToken = patient[0].fcm_registration_token;

    const message = {
      data: {
        type: 'survey',
        title: '설문조사 알림',
        body: '설문조사에 답해주세요'
      },
      token: fcmRegistrationToken
    }
    // 스케줄링 규칙 설정 - 9~21시까지 1시간마다 반복
    const scheduleName = `survey_${patientNum}`;

    const startHour = Number(patient[0].sleep_end_time.substring(0, 2)) + 1;
    const endHour = Number(patient[0].sleep_start_time.substring(0, 2)) - 1;
    let rule = `*/59 ${startHour}-${endHour} * * *`;
    schedule.scheduleJob(scheduleName, rule, async () => {
      await fcmAdmin.messaging()
          .send(message)
          .then((response) => {
            console.debug(`${new Date()}: 설문조사 알림 전송 완료`);
          })
          .catch((error) => {
            console.error(`${new Date()}: 설문조사 알림 전송 실패`);
          });
    });
  }

  public async stopNotifySurvey(patientNum: number) {
    // 설문조사 스케줄러 취소
    const scheduleName = `survey_${patientNum}`;
    const scheduledSurveyJob: Job = schedule.scheduledJobs[scheduleName];
    if(schedule.cancelJob(scheduledSurveyJob)){
      console.debug(`${new Date()}: 설문조사 알림 취소`);
    }
  }
}