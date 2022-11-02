const app = require('../../app');
const request = require('supertest');
const testDataSetUp = require('../config/TestDataSetUp');
const testDataConfig = require('../config/TestDataConfig');

describe('PatientsRoute test', () => {

  testDataSetUp.beforeEach;
  testDataSetUp.afterEach;

  test('정상 로그인', async () => {
    const response = await request(app)
      .post('/api/patients/login')
      .set('Accept', 'application/json')
      .send({
        patient_num: testDataConfig.Patients.patient_num
      });
    
      await expect(response.status).toBe(200);
      await expect(response.body.access_token).not.toBeUndefined();
  });

  test('실제 환자번호가 아닌 정보로 로그인을 하였을 때', async () => {
    const response = await request(app)
      .post('/api/patients/login')
      .set('Accept', 'application/json')
      .send({
        patient_num: 1212
      });

    await expect(response.status).toBe(401);
    await expect(response.body.message).toEqual('유효하지 않은 환자번호 입니다.');
  });

  test('부적합한 정보로 로그인을 하였을 때', async () => {
    const response = await request(app)
      .post('/api/patients/login')
      .set('Accept', 'application/json')
      .send({
        patient_nu: testDataConfig.Patients.patient_num
      });

    await expect(response.status).toBe(400);
    await expect(response.body.message).toEqual('부적합한 요청 정보 입니다.');
  });

  test('부적절한(번호가 아닌) 정보로 로그인을 하였을 때', async () => {
    const response = await request(app)
      .post('/api/patients/login')
      .set('Accept', 'application/json')
      .send({
        patient_num: 'test'
      });

    await expect(response.status).toBe(400);
    await expect(response.body.message).toEqual('부적절한 요청 정보 입니다.');
  });
});