const patientModel = require('../../models/PatientModel');
const testPatientData = require('../config/TestDataConfig').testPatientData;
const knex = require('../../config/knex');

describe('PatientModel test', () => {

  beforeEach(async () => {
    await knex
      .insert(testPatientData)
      .into('patients')
      .then((result) => {
        console.log(`success insert test data=${result}`);
      })
      .catch((error) => {
        console.error(error);
      });
  });

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

  test('유효한 환자번호로 환자 조회', async () => {
    const patient = await patientModel.findByPatientNum(121212);
    const findPatientName = await patient[0].patient_name;

    expect(patient).toHaveLength(1);
    expect(patient).not.toBeNull();
    expect(findPatientName).toEqual(testPatientData.patient_name);
  });

  test('유효하지 않는 환자번호로 환자 조회', async () => {
    const patient = await patientModel.findByPatientNum(1212);
    const findPatient = await patient[0];
    
    expect(patient).toHaveLength(0);
    expect(findPatient).toBeUndefined();
  });

});