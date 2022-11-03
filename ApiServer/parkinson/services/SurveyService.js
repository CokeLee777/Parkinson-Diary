require('dotenv').config();

module.exports = class SurveyService {

  constructor(surveyModel){
    this.surveyModel = surveyModel;
  }

  async createSurvey(patientNum, surveyCreateRequest){
    await this.surveyModel
      .createSurvey(
        patientNum,
        surveyCreateRequest.hasAbnormalMovement,
        surveyCreateRequest.hasMedicinalEffect,
        surveyCreateRequest.patientCondition
      );
  }
}