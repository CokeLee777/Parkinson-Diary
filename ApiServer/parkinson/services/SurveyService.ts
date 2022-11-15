import {SurveyModel} from "../models/SurveyModel";
import {SurveyCreateRequest} from "../dto/SurveyRequestDto";

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
}