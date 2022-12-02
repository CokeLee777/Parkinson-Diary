import {knex} from '../config/DBConfig';
import {getLocalTime} from "../config/TimeConfig";

export class MedicineHistoryModel {

  private static instance: MedicineHistoryModel;

  public static getInstance(){
    if(this.instance !== undefined){
      return this.instance;
    }
    return new MedicineHistoryModel();
  }

  private constructor(){}

  public async createMedicineHistory(medicineHistoryId: string, patientNum: number) {
    await knex
      .insert({
        medicine_history_id: medicineHistoryId,
        patient_num: patientNum,
        reserved_take_time: getLocalTime()
      })
      .into('medicine_history')
      .catch((error: Error) => {
        throw error;
      })
  }

  public async updateMedicineHistoryIsTakeByIdAndPatientNum(medicineHistoryId: string, patientNum: number) {
    await knex('medicine_history AS mh')
        .update({
          actual_take_time: getLocalTime(),
          is_take: true
        })
        .where('mh.medicine_history_id', medicineHistoryId)
        .andWhere('mh.patient_num', patientNum)
        .catch((error: Error) => {
          throw error;
        });
  }
}