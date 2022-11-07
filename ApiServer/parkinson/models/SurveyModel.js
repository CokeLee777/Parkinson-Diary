const knex = require('../config/knex');

module.exports = {

  createSurvey: async (patientNum, hasAbnormalMovement, hasMedicinalEffect, patientCondition) => {
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