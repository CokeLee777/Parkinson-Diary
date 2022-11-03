
exports.SurveyCreateRequest = class SurveyCreateRequest {

  constructor(hasAbnormalMovement, hasMedicinalEffect, patientCondition){
    this.hasAbnormalMovement = hasAbnormalMovement;
    this.hasMedicinalEffect = hasMedicinalEffect;
    this.patientCondition = patientCondition;
  }

  serialize(){
    return JSON.stringify(this);
  }
}