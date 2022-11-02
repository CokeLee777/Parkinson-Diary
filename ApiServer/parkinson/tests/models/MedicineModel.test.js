const medicineModel = require('../../models/MedicineModel');
const testPatientData = require('../config/TestDataConfig').testPatientData;
const testMedicineData = require('../config/TestDataConfig').testMedicineData;
const knex = require('../../config/knex');

describe('MedicineModel test', () => {

  beforeEach(async () => {
    //환자정보 삽입
    await knex
      .insert(testPatientData)
      .into('patients')
      .then((result) => {
        console.log(`success insert test data=${result}`);
      })
      .catch((error) => {
        console.error(error);
      });
    //환자 약 복용정보 삽입
    await knex
      .insert(testMedicineData)
      .into('medicine')
      .then((result) => {
        console.log(`success insert test data=${result}`);
      })
      .catch((error) => {
        throw error;
      })
  });

  afterEach(async () => {
    await knex('medicine AS m')
      .where('m.patient_num', testMedicineData.patient_num)
      .del()
      .then((result) => {
        console.log(`success delete test data=${result}`);
      })
      .catch((error) => {
        throw error;
      })
    
    await knex('patients AS p')
      .where('p.patient_num', testPatientData.patient_num)
      .del()
      .then((result) => {
        console.log(`success delete test data=${result}`);
      })
      .catch((error) => {
        console.error(error);
      })
    
  });

  test('유효한 환자번호로 환자의 약 복용정보 조회', async () => {
    const medicine = await medicineModel.findByPatientNum(121212);
    const takeTime = await medicine[0].take_time;

    expect(medicine).toHaveLength(1);
    expect(medicine).not.toBeNull();
    expect(takeTime).toEqual('12:00:00');
  });

  test('유효하지 않은 환자번호로 환자의 약 복용정보 조회', async () => {
    const medicine = await medicineModel.findByPatientNum(1212);
    
    expect(medicine).toHaveLength(0);
    expect(medicine[0]).toBeUndefined();
  });

  test('특정 환자의 약 복용정보 추가', async () => {
    await medicineModel.createMedicineTakeTime(121212, "15:00");
    await medicineModel.createMedicineTakeTime(121212, "16:00");

    const medicine = await medicineModel.findByPatientNum(121212);
    
    expect(medicine).toHaveLength(3);
  });

  test('특정 환자의 약 복용정보 모두 삭제', async () => {
    await medicineModel.deleteAllMedicineTakeTime(121212);

    const medicine = await medicineModel.findByPatientNum(121212);
    
    expect(medicine).toHaveLength(0);
    expect(medicine[0]).toBeUndefined();
  });

});