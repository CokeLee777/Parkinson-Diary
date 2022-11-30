import {knex} from '../config/DBConfig';
import {getLocalTime} from "../config/TimeConfig";

export class SurveyModel {

  private static instance: SurveyModel;

  public static getInstance(){
    if(this.instance !== undefined){
      return this.instance;
    }
    return new SurveyModel();
  }

  private constructor(){}

  public async createSurvey(
      patientNum: number,
      hasAbnormalMovement: boolean,
      hasMedicinalEffect: boolean,
      patientCondition: number) {
    await knex
      .insert({
        abnormal_movement: hasAbnormalMovement,
        medicinal_effect: hasMedicinalEffect,
        patient_condition: patientCondition,
        survey_time: getLocalTime(),
        patient_num: patientNum
      })
      .into('survey')
      .catch((error: Error) => {
          throw error;
      });
  }
}