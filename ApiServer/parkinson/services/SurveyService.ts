import {SurveyModel} from "../models/SurveyModel";
import {SurveyCreateRequest} from "../dto/SurveyRequestDto";
import schedule, {scheduleJob} from "node-schedule";
import {fcmAdmin} from "../config/FcmConfig";

export class SurveyService {

  private surveyModel: SurveyModel;

  private static instance: SurveyService;

  public static getInstance(surveyModel: SurveyModel){
    if(this.instance !== undefined){
      return this.instance;
    }
    return new SurveyService(surveyModel);
  }

  private constructor(surveyModel: SurveyModel){
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

  public async notifySurvey(registrationToken: string){
    const message = {
      data: {
        title: '파킨슨 다이어리 - 설문조사',
        body: '설문조사에 답해주세요.'
      },
      token: registrationToken
    }
    // 설문조사를 시행할 간격 설정
    const rule = new schedule.RecurrenceRule();
    rule.tz = 'Asia/Seoul';
    rule.hour = new schedule.Range(9, 21, 1);
    rule.minute = 0;
    // 설문조사 스케줄러 시작
    scheduleJob(rule, async () => {
      await fcmAdmin.messaging()
          .send(message)
          .then((response) => {
            console.debug('설문조사 알림 전송 완료');
          })
          .catch((error) => {
            console.error('설문조사 알림 전송 실패');
          })
    });
  }
}