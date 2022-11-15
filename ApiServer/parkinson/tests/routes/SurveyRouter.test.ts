import app from '../../app';
import request from 'supertest';
import {TestDataSetUp} from '../config/TestDataSetUp';
import {TestDataConfig} from "../config/TestDataConfig";

describe('SurveyRouter test', () => {

    TestDataSetUp.beforeEach;
    TestDataSetUp.afterEach;

  test('설문조사 추가', async () => {
    const accessToken = await TestDataSetUp
      .issueJwtToken(TestDataConfig.Patients.patient_num);
    
    const response = await request(app)
      .post('/api/survey')
      .set('Accept', 'application/json')
      .set('ACCESS_TOKEN', accessToken)
      .send({
        has_abnormal_movement: true,
        has_medicinal_effect: false,
        patient_condition: 50
      });
    
      await expect(response.status).toBe(200);
  });

  test('잘못된 형식으로 설문조사 추가 #1', async () => {
    const accessToken = await TestDataSetUp
      .issueJwtToken(TestDataConfig.Patients.patient_num);
    
    const response = await request(app)
      .post('/api/survey')
      .set('Accept', 'application/json')
      .set('ACCESS_TOKEN', accessToken)
      .send({
        has_abnormal_movement: true,
        has_medicinal_effect: false
      });
    
      await expect(response.status).toBe(400);
      await expect(response.body.message).toEqual('부적합한 요청 정보 입니다.');
  });

  test('잘못된 형식으로 설문조사 추가 #2', async () => {
    const accessToken = await TestDataSetUp
      .issueJwtToken(TestDataConfig.Patients.patient_num);
    
    const response = await request(app)
      .post('/api/survey')
      .set('Accept', 'application/json')
      .set('ACCESS_TOKEN', accessToken)
      .send({
        has_abnormal_movement: true,
        has_medicinal_effect: false,
        patient_condition: 'test'
      });
    
      await expect(response.status).toBe(400);
      await expect(response.body.message).toEqual('부적절한 요청 정보 입니다.');
  });

});