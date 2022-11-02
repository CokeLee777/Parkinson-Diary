const patientModel = require('../../models/PatientModel');
const medicineModel = require('../../models/MedicineModel');
const testPatientData = require('../config/TestDataConfig').testPatientData;
const testMedicineData = require('../config/TestDataConfig').testMedicineData;
const knex = require('../../config/knex');
const DiaryService = require('../../services/DiaryService');
const { InvalidPatientNumberError } = require('../../error/PatientServiceError');
const { DiaryCreateRequest } = require('../../dto/DiaryRequestDto');

const diaryService = new DiaryService(patientModel, medicineModel);

describe('PatientService test', () => {

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

  test('정상 다이어리 조회', async () => {

    const diaryInfoResponse = await diaryService.getDiary(121212);

    await expect(diaryInfoResponse.sleep_start_time).toEqual('22:00:00');
    await expect(diaryInfoResponse.sleep_end_time).toEqual('08:00:00');
    await expect(diaryInfoResponse.take_times).toHaveLength(1);
    await expect(diaryInfoResponse.take_times[0].take_time).toEqual('12:00:00');
  });

  test('비정상 다이어리 조회', async () => {

    await expect(async () => {
      await diaryService.getDiary(1212);
    }).rejects.toThrow(InvalidPatientNumberError);
  });

  test('다이어리 추가', async () => {

    await diaryService
      .createDiary(
        121212, new DiaryCreateRequest('23:00', '09:00', [{take_time: '13:00'}])
      )

    const diaryInfoResponse = await diaryService.getDiary(121212);

    await expect(diaryInfoResponse.sleep_start_time).toEqual('23:00:00');
    await expect(diaryInfoResponse.sleep_end_time).toEqual('09:00:00');
    await expect(diaryInfoResponse.take_times).toHaveLength(2);
    await expect(diaryInfoResponse.take_times[1].take_time).not.toBeUndefined();
  });

  test('다이어리 수정', async () => {

    await diaryService
      .updateDiary(
        121212, new DiaryCreateRequest('23:00', '09:00', [{take_time: '13:00'}])
      )

    const diaryInfoResponse = await diaryService.getDiary(121212);

    await expect(diaryInfoResponse.sleep_start_time).toEqual('23:00:00');
    await expect(diaryInfoResponse.sleep_end_time).toEqual('09:00:00');
    await expect(diaryInfoResponse.take_times[0].take_time).toEqual('13:00:00');
    await expect(diaryInfoResponse.take_times).toHaveLength(1);
  });
});