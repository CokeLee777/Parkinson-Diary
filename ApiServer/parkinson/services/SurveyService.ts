import {SurveyModel} from "../models/SurveyModel";
import {SurveyCreateRequest} from "../dto/SurveyRequestDto";
import schedule from "node-schedule";
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

    // 이미 등록된 스케줄러가 있다면 취소
    if(schedule.scheduledJobs[registrationToken] !== undefined){
      schedule.scheduledJobs[registrationToken].cancel();
    }
    // 스케줄링 규칙 설정 - 9~21시까지 1시간마다 반복
    const rule = new schedule.RecurrenceRule();
    rule.tz = 'Asia/Seoul';
    rule.hour = new schedule.Range(9, 21, 1);

    schedule.scheduleJob(registrationToken, rule, async () => {
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

  public async stopNotifySurvey(registrationToken: string) {
    // 설문조사 스케줄러 취소
    console.debug(`${new Date()}: 설문조사 알림 취소`);
    schedule.scheduledJobs[registrationToken].cancel();
  }
}