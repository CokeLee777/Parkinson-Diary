import {AppConfig} from '../../config/AppConfig';
import {TestDataSetUp} from '../config/TestDataSetUp';
import {TestDataConfig} from "../config/TestDataConfig";
import {knex} from "../../config/DBConfig";

const medicineHistoryModel = AppConfig.medicineHistoryModel;

describe('MedicineHistoryModel test', () => {

  TestDataSetUp.beforeAll;
  TestDataSetUp.afterAll;

  beforeAll(async () => {
      await medicineHistoryModel
          .createMedicineHistory("testId", TestDataConfig.Patients.patient_num);
  })
  
  test('약 복용 유무 히스토리 추가 - 복용 미완료', async () => {
    const medicineHistory = await knex
        .select('*')
        .from('medicine_history AS mh')
        .where('mh.patient_num', TestDataConfig.Patients.patient_num)
        .then((medicineHistory) => {
          return medicineHistory;
        })
        .catch((error: Error) => {
          throw error;
        });

    expect(medicineHistory).toHaveLength(1);
    expect(medicineHistory[0].actual_take_time).toBeNull();
    expect(medicineHistory[0].is_take).toBeFalsy();
  });

  test('약 복용 유무 히스토리 수정 - 복용 완료', async () => {
    await medicineHistoryModel
        .updateMedicineHistoryIsTakeByIdAndPatientNum("testId", TestDataConfig.Patients.patient_num);

    //when
    const medicineHistory = await knex
        .select('*')
        .from('medicine_history AS mh')
        .where('mh.patient_num', TestDataConfig.Patients.patient_num)
        .then((medicineHistory) => {
          return medicineHistory;
        })
        .catch((error: Error) => {
          throw error;
        });

    expect(medicineHistory).toHaveLength(1);
    expect(medicineHistory[0].actual_take_time).not.toBeNull();
    expect(medicineHistory[0].is_take).toBeTruthy();
  });

});