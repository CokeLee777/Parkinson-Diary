require('dotenv').config();
const jwt = require('jsonwebtoken');
const LoginResponse = require('../dto/LoginResponseDto');
const { InvalidPatientNumberError } = require('../error/PatientServiceError');

module.exports = class PatientService {

  #patientModel;

  static #instance;

  static getInstance(patientModel){
    if(this.#instance !== undefined){
      return this.#instance;
    }
    return new PatientService(patientModel);
  }

  constructor(patientModel){
    this.#patientModel = patientModel;
  }

  async login(patientNum){

    const patient = await this.#patientModel
      .findByPatientNum(patientNum);
    if(patient.length == 0 || patient[0] === undefined) {
      throw new InvalidPatientNumberError('유효하지 않은 환자번호 입니다.');
    }

    const accessToken = await this.#issueJwtToken(patient[0]);
    return new LoginResponse(accessToken);
  }

  // JWT 토큰 발급 메서드
  async #issueJwtToken(patient){

    const accessToken = await jwt.sign({
        type: 'JWT',
        patientNum: patient.patient_num,
        patientName: patient.patient_name,
        userId: patient.user_id
    }, process.env.JWT_SECRET_KEY, {
        expiresIn: process.env.JWT_ACCESS_EXPIRATION,
        issuer: process.env.JWT_ISSUER
    });

    return process.env.JWT_PREFIX + accessToken;
  }

}