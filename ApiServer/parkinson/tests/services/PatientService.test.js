const patientModel = require('../../models/PatientModel');
const testPatientData = require('../config/TestDataConfig').testPatientData;
const knex = require('../../config/knex');
const PatientService = require('../../services/PatientService');
const { InvalidPatientNumberError } = require('../../error/PatientServiceError');

const patientService = new PatientService(patientModel);

describe('PatientService test', () => {

  beforeEach(async () => {
    await knex.insert(testPatientData)
      .into('patients')
      .then((result) => {
        console.log(`success insert test data=${result}`);
      })
      .catch((error) => {
        console.error(error);
      })
  })

  afterEach(async () => {
    await knex('patients AS p')
      .where('p.patient_num', testPatientData.patient_num)
      .del()
      .then((result) => {
        console.log(`success delete test data=${result}`);
      })
      .catch((error) => {
        console.error(error);
      })
  })

  test('정상 로그인', async () => {
    const loginResponse = await patientService.login(121212);

    await expect(loginResponse.access_token.substring(0, 7)).toEqual(process.env.JWT_PREFIX);
  });

  test('비정상 유저 로그인', async () => {

    await expect(async () => {
      await patientService.login(1212);
    }).rejects.toThrow(InvalidPatientNumberError);
  });
});