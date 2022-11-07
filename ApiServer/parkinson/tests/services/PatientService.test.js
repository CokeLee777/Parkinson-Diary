const patientModel = require('../../models/PatientModel');
const testDataConfig = require('../config/TestDataConfig');
const testDataSetUp = require('../config/TestDataSetUp');
const PatientService = require('../../services/PatientService');
const { InvalidPatientNumberError } = require('../../error/PatientServiceError');

const patientService = new PatientService(patientModel);

describe('PatientService test', () => {

  testDataSetUp.beforeEach;
  testDataSetUp.afterEach;

  test('정상 로그인', async () => {
    const loginResponse = await patientService
      .login(testDataConfig.Patients.patient_num);

    await expect(loginResponse.access_token.substring(0, 7)).toEqual(process.env.JWT_PREFIX);
  });

  test('비정상 유저 로그인', async () => {

    await expect(async () => {
      await patientService.login(1212);
    }).rejects.toThrow(InvalidPatientNumberError);
  });
});