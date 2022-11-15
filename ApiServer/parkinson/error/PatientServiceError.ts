export class InvalidPatientNumberError extends Error {

  private code: string;

  public constructor(message: string) {
    super(message)
    this.code = 'ERR_INVALID_PATIENT_NUMBER'
    this.name = 'InvalidPatientNumberError'
    this.stack = `${this.message}\n${super.stack}`
  }
}