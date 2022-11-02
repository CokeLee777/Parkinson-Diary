const app = require('../../app');
const request = require('supertest');
const testDataSetUp = require('../config/TestDataSetUp');
const testDataConfig = require('../config/TestDataConfig');

describe('PatientsRoute test', () => {

  testDataSetUp.beforeEach;
  testDataSetUp.afterEach;

  test('다이어리 조회 API', async () => {
    const accessToken = await testDataSetUp
      .issueJwtToken(testDataConfig.Patients.patient_num);
    
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
    const accessToken = await testDataSetUp
      .issueJwtToken(testDataConfig.Patients.patient_num);
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
    const accessToken = await testDataSetUp
      .issueJwtToken(testDataConfig.Patients.patient_num);
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