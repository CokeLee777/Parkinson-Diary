import jwt from 'jsonwebtoken';

import {PatientModel} from "../models/PatientModel";
import {LoginResponse} from '../dto/LoginResponseDto';
import {InvalidPatientNumberError} from "../error/PatientServiceError";
import {TokenExpiredError} from "../error/CommonError";

export class PatientService {

  private patientModel: PatientModel;

  private static instance: PatientService;

  public static getInstance(patientModel: PatientModel){
    if(this.instance !== undefined){
      return this.instance;
    }
    return new PatientService(patientModel);
  }

  private constructor(patientModel: PatientModel){
    this.patientModel = patientModel;
  }

  public async login(patientNum: number, fcmRegistrationToken: string){

    const patient = await this.patientModel
      .findByPatientNum(patientNum);
    if(patient.length == 0 || patient[0] === undefined) {
      console.error('Invalid patient number');
      throw new InvalidPatientNumberError('유효하지 않은 환자번호 입니다.');
    }

    const accessToken = await this.issueJwtToken(patient[0]);
    //Redis DB에 엑세스 토큰 저장
    await this.patientModel.saveAccessToken(patientNum, accessToken);
    // FCM 등록 토큰 저장
    await this.patientModel.saveFcmRegistrationToken(patientNum, fcmRegistrationToken);

    return new LoginResponse(accessToken);
  }

  public async authorizeAccessToken(patientNum: number){
    const accessToken = await this.patientModel.getAccessToken(patientNum);
    if(accessToken == null){
      console.error('Token not found in redis db');
      throw new TokenExpiredError("토큰이 만료되었습니다.");
    }
  }

  public async reIssueFcmRegistrationToken(patientNum: number, fcmRegistrationToken: string) {
    await this.patientModel
        .saveFcmRegistrationToken(patientNum, fcmRegistrationToken);
  }

  // JWT 토큰 발급 메서드
  private async issueJwtToken(patient: Patient) {

    const accessToken = jwt.sign({
      type: 'JWT',
      patientNum: patient.patient_num,
      patientName: patient.patient_name,
      userId: patient.user_id
    }, String(process.env.JWT_SECRET_KEY), {
      expiresIn: process.env.JWT_ACCESS_EXPIRATION,
      issuer: process.env.JWT_ISSUER
    });

    return process.env.JWT_PREFIX + accessToken;
  }

}

type Patient = {
  patient_num: number,
  user_id: number,
  patient_name: string,
  sleep_start_time: string,
  sleep_end_time: string
}