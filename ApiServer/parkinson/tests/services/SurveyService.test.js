const testDataConfig = require('../config/TestDataConfig');
const testDataSetUp = require('../config/TestDataSetUp');
const { SurveyCreateRequest } = require('../../dto/SurveyRequestDto');
const knex = require('../../config/DBConfig');

const appConfig = require('../../config/AppConfig');
const surveyService = appConfig.surveyService;

describe('SurveyService test', () => {

  testDataSetUp.beforeEach;
  testDataSetUp.afterEach;

  test('설문조사 정보 추가', async () => {

    await surveyService
      .createSurvey(
        testDataConfig.Patients.patient_num,
        new SurveyCreateRequest(
          testDataConfig.Survey.abnormal_movement, 
          testDataConfig.Survey.medicinal_effect,
          testDataConfig.Survey.patient_condition
        )
      );

    const survey = await knex
      .select('*')
      .from('survey AS s')
      .where('s.patient_num', testDataConfig.Survey.patient_num)
      .then((survey) => {
        return survey;
      });

    await expect(survey).toHaveLength(1);
    await expect(survey[0].abnormal_movement).toEqual(testDataConfig.Survey.abnormal_movement);
    await expect(survey[0].medicinal_effect).toEqual(testDataConfig.Survey.medicinal_effect);
    await expect(survey[0].patient_condition).toEqual(testDataConfig.Survey.patient_condition);
  });

});