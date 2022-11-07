const patientModel = require('../../models/PatientModel');
const medicineModel = require('../../models/MedicineModel');
const testDataConfig = require('../config/TestDataConfig');
const testDataSetUp = require('../config/TestDataSetUp');
const DiaryService = require('../../services/DiaryService');
const { InvalidPatientNumberError } = require('../../error/PatientServiceError');
const { DiaryCreateRequest } = require('../../dto/DiaryRequestDto');

const diaryService = new DiaryService(patientModel, medicineModel);

describe('PatientService test', () => {

  testDataSetUp.beforeEach;
  testDataSetUp.afterEach;

  test('정상 다이어리 조회', async () => {

    const diaryInfoResponse = await diaryService
      .getDiary(testDataConfig.Patients.patient_num);

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
        testDataConfig.Patients.patient_num,
        new DiaryCreateRequest('23:00', '09:00', [{take_time: '13:00'}])
      )

    const diaryInfoResponse = await diaryService
      .getDiary(testDataConfig.Patients.patient_num);

    await expect(diaryInfoResponse.sleep_start_time).toEqual('23:00:00');
    await expect(diaryInfoResponse.sleep_end_time).toEqual('09:00:00');
    await expect(diaryInfoResponse.take_times).toHaveLength(2);
    await expect(diaryInfoResponse.take_times[1].take_time).not.toBeUndefined();
  });

  test('다이어리 수정', async () => {

    await diaryService
      .updateDiary(
        testDataConfig.Patients.patient_num,
        new DiaryCreateRequest('23:00', '09:00', [{take_time: '13:00'}])
      )

    const diaryInfoResponse = await diaryService
      .getDiary(testDataConfig.Patients.patient_num);

    await expect(diaryInfoResponse.sleep_start_time).toEqual('23:00:00');
    await expect(diaryInfoResponse.sleep_end_time).toEqual('09:00:00');
    await expect(diaryInfoResponse.take_times[0].take_time).toEqual('13:00:00');
    await expect(diaryInfoResponse.take_times).toHaveLength(1);
  });
});