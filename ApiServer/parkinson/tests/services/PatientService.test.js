const patientModel = require('../../models/PatientModel');
const testPatientData = require('../config/TestDataConfig').testPatientData;
const knex = require('../../config/knex');
const PatientService = require('../../services/PatientService');
const { InvalidPatientNumberError } = require('../../error/PatientServiceError');

describe('PatientService test', () => {

  beforeAll(async () => {
    await knex.insert(testPatientData)
      .into('patients')
      .then((result) => {
        console.log(`success insert test data=${result}`);
      })
      .catch((error) => {
        console.error(error);
      })
  })

  afterAll(async () => {
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
    const patientService = new PatientService(patientModel);

    const accessToken = await patientService.login(121212);
    await expect(accessToken.substring(0, 7)).toEqual(process.env.JWT_PREFIX);
  });

  test('비정상 유저 로그인', async () => {
    const patientService = new PatientService(patientModel);

    // const accessToken = await patientService.login(1212);
    await expect(async () => {
      await patientService.login(1212);
    }).rejects.toThrow(InvalidPatientNumberError);
  });
});