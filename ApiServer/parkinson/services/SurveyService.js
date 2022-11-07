require('dotenv').config();

module.exports = class SurveyService {

  #surveyModel;

  static #instance;

  static getInstance(surveyModel){
    if(this.#instance !== undefined){
      return this.#instance;
    }
    return new SurveyService(surveyModel);
  }

  constructor(surveyModel){
    this.#surveyModel = surveyModel;
  }

  async createSurvey(patientNum, surveyCreateRequest){
    await this.#surveyModel
      .createSurvey(
        patientNum,
        surveyCreateRequest.hasAbnormalMovement,
        surveyCreateRequest.hasMedicinalEffect,
        surveyCreateRequest.patientCondition
      );
  }
}