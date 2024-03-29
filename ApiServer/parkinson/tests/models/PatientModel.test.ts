import {AppConfig} from '../../config/AppConfig';
import {TestDataSetUp} from '../config/TestDataSetUp';
import {TestDataConfig} from "../config/TestDataConfig";

const patientModel = AppConfig.patientModel;

describe('PatientModel test', () => {

  TestDataSetUp.beforeAll;
  TestDataSetUp.afterAll;

  test('유효한 환자번호로 환자 조회', async () => {
    const patient = await patientModel
      .findByPatientNum(TestDataConfig.Patients.patient_num);
    const findPatientName = await patient[0].patient_name;

    expect(patient).toHaveLength(1);
    expect(patient).not.toBeNull();
    expect(findPatientName).toEqual(TestDataConfig.Patients.patient_name);
  });

  test('유효하지 않는 환자번호로 환자 조회', async () => {
    const patient = await patientModel.findByPatientNum(1212);
    const findPatient = await patient[0];
    
    expect(patient).toHaveLength(0);
    expect(findPatient).toBeUndefined();
  });

});