import {knex} from '../config/DBConfig';

export class MedicineModel {

  private static instance: MedicineModel;

  public static getInstance(){
    if(this.instance !== undefined){
      return this.instance;
    }
    return new MedicineModel();
  }

  private constructor(){}

  public async findByPatientNum(patientNum: number) {
    return await knex
      .select('m.take_time')
      .from('medicine AS m')
      .where('m.patient_num', patientNum)
      .then((medicine) => {
        return medicine;
      })
      .catch((error: Error) => {
        throw error;
      });
  }

  public async createMedicineTakeTime(patientNum: number, takeTime: string) {
    await knex
      .insert({
        patient_num: patientNum,
        take_time: new Date(`July 1, 1999, ${takeTime}`)
      })
      .into('medicine')
      .catch((error: Error) => {
        throw error;
      })
  }

  public async deleteAllMedicineTakeTime(patientNum: number) {
    await knex('medicine AS m')
      .where('m.patient_num', patientNum)
      .del()
      .catch((error: Error) => {
        throw error;
      });
  }
}