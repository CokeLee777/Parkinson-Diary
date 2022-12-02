import {AppConfig} from '../../config/AppConfig';
import {TestDataSetUp} from '../config/TestDataSetUp';
import {TestDataConfig} from "../config/TestDataConfig";
import { InvalidPatientNumberError } from '../../error/PatientServiceError';

const patientService = AppConfig.patientService;

describe('PatientService test', () => {

  TestDataSetUp.beforeAll;
  TestDataSetUp.afterAll;

  test('정상 로그인', async () => {
    const loginResponse = await patientService
      .login(TestDataConfig.Patients.patient_num, TestDataConfig.Patients.fcm_registration_token);

    await expect(loginResponse.access_token.substring(0, 7)).toEqual(process.env.JWT_PREFIX);
  });

  test('비정상 유저 로그인', async () => {

    await expect(async () => {
      await patientService.login(1212, TestDataConfig.Patients.fcm_registration_token);
    }).rejects.toThrow(InvalidPatientNumberError);
  });
});