const knex = require('../config/DBConfig');

module.exports = class MedicineModel {

  static #instance;

  static getInstance(){
    if(this.#instance !== undefined){
      return this.#instance;
    }
    return new MedicineModel();
  }

  constructor(){}

  async findByPatientNum(patientNum) {
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
  }

  async createMedicineTakeTime(patientNum, takeTime) {
    await knex
      .insert({
        patient_num: patientNum,
        take_time: new Date(`July 1, 1999, ${takeTime}`)
      })
      .into('medicine')
      .catch((error) => {
        throw error;
      })
  }

  async deleteAllMedicineTakeTime(patientNum) {
    await knex('medicine AS m')
      .where('m.patient_num', patientNum)
      .del()
      .catch((error) => {
        throw error;
      });
  }
}