import app from '../../app';
import request, {Response} from 'supertest';
import {TestDataSetUp} from '../config/TestDataSetUp';
import {TestDataConfig} from "../config/TestDataConfig";

describe('DiaryRouter test', () => {

  TestDataSetUp.beforeAll;
  TestDataSetUp.afterAll;

  test('다이어리 조회', async () => {
    const accessToken = await TestDataSetUp
      .getJwtToken(TestDataConfig.Patients.patient_num);
    
    const response: Response = await request(app)
      .get('/api/diary')
      .set('Accept', 'application/json')
      .set('ACCESS_TOKEN', accessToken);

    await expect(response.status).toBe(200);
    await expect(new Date(`July 1, 1999, ${response.body.sleep_start_time}`))
      .toEqual(TestDataConfig.Patients.sleep_start_time);
    await expect(new Date(`July 1, 1999, ${response.body.sleep_end_time}`))
      .toEqual(TestDataConfig.Patients.sleep_end_time);
    await expect(response.body.take_times).toHaveLength(1);
    await expect(new Date(`July 1, 1999, ${response.body.take_times[0].take_time}`))
      .toEqual(TestDataConfig.Medicine.take_time);
  });

  test('다이어리 추가', async () => {
    const accessToken = await TestDataSetUp
      .getJwtToken(TestDataConfig.Patients.patient_num);
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

  test('다이어리 수정', async () => {
    const accessToken = await TestDataSetUp
      .getJwtToken(TestDataConfig.Patients.patient_num);
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