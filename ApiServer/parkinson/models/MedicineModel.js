const knex = require('../config/knex');

module.exports = {

  findByPatientNum: async (patientNum) => {
    return await knex
      .select('m.take_time', 'm.is_take')
      .from('medicine AS m')
      .where('m.patient_num', patientNum)
      .then((medicine) => {
        return medicine;
      })
      .catch((error) => {
        throw error;
      });
  },

  createMedicineTakeTime: async (patientNum, takeTime) => {
    await knex
      .insert({
        patient_num: patientNum,
        take_time: new Date(`July 1, 1999, ${takeTime}`)
      })
      .into('medicine')
      .catch((error) => {
        throw error;
      })
  },

  deleteAllMedicineTakeTime: async (patientNum) => {
    await knex('medicine AS m')
      .where('m.patient_num', patientNum)
      .del()
      .catch((error) => {
        throw error;
      });
  }
}