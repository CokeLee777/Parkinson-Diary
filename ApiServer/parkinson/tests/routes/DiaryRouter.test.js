const knex = require('../../config/knex');
const jwt = require('jsonwebtoken');
const app = require('../../app');
const request = require('supertest');
const testPatientData = require('../config/TestDataConfig').testPatientData;
const testMedicineData = require('../config/TestDataConfig').testMedicineData;

async function issueJwtToken(patientNum){

  const accessToken = await jwt.sign({
      type: 'JWT',
      patientNum: patientNum,
      patientName: testPatientData.patient_name,
  }, process.env.JWT_SECRET_KEY, {
      expiresIn: process.env.JWT_ACCESS_EXPIRATION,
      issuer: process.env.JWT_ISSUER
  });

  return process.env.JWT_PREFIX + accessToken;
}

describe('PatientsRoute test', () => {

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

  test('다이어리 조회 API', async () => {
    const accessToken = await issueJwtToken(121212);
    const response = await request(app)
      .get('/api/diary')
      .set('Accept', 'application/json')
      .set('ACCESS_TOKEN', accessToken);
    
      await expect(response.status).toBe(200);
      await expect(response.body.sleep_start_time).toEqual('22:00:00');
      await expect(response.body.sleep_end_time).toEqual('08:00:00');
      await expect(response.body.take_times).toHaveLength(1);
      await expect(response.body.take_times[0].take_time).toEqual('12:00:00');
  });

  test('다이어리 추가 API', async () => {
    const accessToken = await issueJwtToken(121212);
    const postResponse = await request(app)
      .post('/api/diary')
      .set('Accept', 'application/json')
      .set('ACCESS_TOKEN', accessToken)
      .send({
        sleep_start_time: '20:00',
        sleep_end_time: '09:00',
        take_times: [
          {
            take_time: '18:00'
          }
        ]
      });

      const getResponse = await request(app)
      .get('/api/diary')
      .set('Accept', 'application/json')
      .set('ACCESS_TOKEN', accessToken);
    
      await expect(postResponse.status).toBe(200);
      await expect(getResponse.status).toBe(200);
      await expect(getResponse.body.sleep_start_time).toEqual('20:00:00');
      await expect(getResponse.body.sleep_end_time).toEqual('09:00:00');
      await expect(getResponse.body.take_times).toHaveLength(2);
  });

  test('다이어리 수정 API', async () => {
    const accessToken = await issueJwtToken(121212);
    const postResponse = await request(app)
      .put('/api/diary')
      .set('Accept', 'application/json')
      .set('ACCESS_TOKEN', accessToken)
      .send({
        sleep_start_time: '23:00',
        sleep_end_time: '11:00',
        take_times: [
          {
            take_time: '18:00'
          }
        ]
      });

      const getResponse = await request(app)
      .get('/api/diary')
      .set('Accept', 'application/json')
      .set('ACCESS_TOKEN', accessToken);
    
      await expect(postResponse.status).toBe(200);
      await expect(getResponse.status).toBe(200);
      await expect(getResponse.body.sleep_start_time).toEqual('23:00:00');
      await expect(getResponse.body.sleep_end_time).toEqual('11:00:00');
      await expect(getResponse.body.take_times).toHaveLength(1);
  });

});