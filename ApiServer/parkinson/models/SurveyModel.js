const knex = require('../config/DBConfig');

module.exports = class SurveyModel {

  static #instance;

  static getInstance(){
    if(this.#instance !== undefined){
      return this.#instance;
    }
    return new SurveyModel();
  }

  constructor(){}

  async createSurvey(patientNum, hasAbnormalMovement, hasMedicinalEffect, patientCondition) {
    await knex
      .insert({
        abnormal_movement: hasAbnormalMovement,
        medicinal_effect: hasMedicinalEffect,
        patient_condition: patientCondition,
        survey_time: new Date(),
        patient_num: patientNum
      })
      .into('survey')
      .catch((error) => {
          throw error;
      });
  }
}