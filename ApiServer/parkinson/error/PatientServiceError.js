
exports.InvalidPatientNumberError = class InvalidPatientNumberError extends Error {
  constructor(...args) {
    super(...args)
    this.code = 'ERR_INVALID_PATIENT_NUMBER'
    this.name = 'InvalidPatientNumberError'
    this.stack = `${this.message}\n${super.stack}`
  }
}