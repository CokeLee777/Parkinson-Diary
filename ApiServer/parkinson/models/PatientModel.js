const knex = require('../config/knex');

module.exports = {

  findByPatientNum: async (patientNum) => {
    return await knex
      .select('*')
      .from('patients AS p')
      .where('p.patient_num', patientNum)
      .then((patient) => {
          return patient;
      })
      .catch((error) => {
          throw error;
      });
  }
}