import {knex} from '../config/DBConfig';

export class PatientModel {

  private static instance: PatientModel;

  public static getInstance(){
    if(this.instance !== undefined){
      return this.instance;
    }
    return new PatientModel();
  }

  private constructor(){}

  public async findByPatientNum(patientNum: number) {
    return await knex
      .select('*')
      .from('patients AS p')
      .where('p.patient_num', patientNum)
      .then((patient) => {
        return patient;
      })
      .catch((error: Error) => {
        throw error;
      });
  }
    
  public async updateSleepTimeByPatientNum(patientNum: number, sleepStartTime: string, sleepEndTime: string) {
    return await knex('patients AS p')
      .update({
        sleep_start_time: new Date(`July 1, 1999, ${sleepStartTime}`),
        sleep_end_time: new Date(`July 1, 1999, ${sleepEndTime}`)
      })
      .where('p.patient_num', patientNum)
      .catch((error: Error) => {
        throw error;
      });
  }
}