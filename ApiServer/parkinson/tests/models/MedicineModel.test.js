const medicineModel = require('../../models/MedicineModel');
const testDataSetUp = require('../config/TestDataSetUp');
const testDataConfig = require('../config/TestDataConfig');

describe('MedicineModel test', () => {

  testDataSetUp.beforeEach;
  testDataSetUp.afterEach;

  test('유효한 환자번호로 환자의 약 복용정보 조회', async () => {
    const medicine = await medicineModel
      .findByPatientNum(testDataConfig.Patients.patient_num);
    const takeTime = await medicine[0].take_time;

    expect(medicine).toHaveLength(1);
    expect(medicine).not.toBeNull();
    expect(new Date(`July 1, 1999, ${takeTime}`)).toEqual(testDataConfig.Medicine.take_time);
  });

  test('유효하지 않은 환자번호로 환자의 약 복용정보 조회', async () => {
    const medicine = await medicineModel.findByPatientNum(1212);
    
    expect(medicine).toHaveLength(0);
    expect(medicine[0]).toBeUndefined();
  });

  test('특정 환자의 약 복용정보 추가', async () => {
    await medicineModel
      .createMedicineTakeTime(testDataConfig.Medicine.patient_num, "15:00");
    await medicineModel
      .createMedicineTakeTime(testDataConfig.Medicine.patient_num, "16:00");

    const medicine = await medicineModel
      .findByPatientNum(testDataConfig.Medicine.patient_num);
    
    expect(medicine).toHaveLength(3);
  });

  test('특정 환자의 약 복용정보 모두 삭제', async () => {
    await medicineModel
      .deleteAllMedicineTakeTime(testDataConfig.Medicine.patient_num);

    const medicine = await medicineModel
      .findByPatientNum(testDataConfig.Medicine.patient_num);
    
    expect(medicine).toHaveLength(0);
    expect(medicine[0]).toBeUndefined();
  });

});