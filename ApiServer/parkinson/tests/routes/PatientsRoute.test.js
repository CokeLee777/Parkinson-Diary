const knex = require('../../config/knex');
const app = require('../../app');
const request = require('supertest');
const testPatientData = require('../config/TestDataConfig').testPatientData;

describe('PatientsRoute test', () => {

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
    const response = await request(app)
      .post('/api/patients/login')
      .set('Accept', 'application/json')
      .send({
        patient_num: 121212
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
        patient_nu: 121212
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