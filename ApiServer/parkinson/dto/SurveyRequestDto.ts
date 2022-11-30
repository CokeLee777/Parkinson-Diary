export class SurveyCreateRequest {

  private readonly _hasAbnormalMovement: boolean;
  private readonly _hasMedicinalEffect: boolean;
  private readonly _patientCondition: number;

  public constructor(hasAbnormalMovement: boolean, hasMedicinalEffect: boolean, patientCondition: number){
    this._hasAbnormalMovement = hasAbnormalMovement;
    this._hasMedicinalEffect = hasMedicinalEffect;
    this._patientCondition = patientCondition;
  }


  get hasAbnormalMovement(): boolean {
    return this._hasAbnormalMovement;
  }

  get hasMedicinalEffect(): boolean {
    return this._hasMedicinalEffect;
  }

  get patientCondition(): number {
    return this._patientCondition;
  }

  public serialize(){
    return JSON.stringify(this);
  }
}