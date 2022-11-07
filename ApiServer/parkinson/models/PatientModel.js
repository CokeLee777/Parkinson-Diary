const knex = require('../config/DBConfig');

module.exports = class PatientModel {

  static #instance;

  static getInstance(){
    if(this.#instance !== undefined){
      return this.#instance;
    }
    return new PatientModel();
  }

  constructor(){}

  async findByPatientNum(patientNum) {
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
    
  async updateSleepTimeByPatientNum(patientNum, sleepStartTime, sleepEndTime) {
    return await knex('patients AS p')
      .update({
        sleep_start_time: new Date(`July 1, 1999, ${sleepStartTime}`),
        sleep_end_time: new Date(`July 1, 1999, ${sleepEndTime}`)
      })
      .where('p.patient_num', patientNum)
      .catch((error) => {
        throw error;
      });
  }
}