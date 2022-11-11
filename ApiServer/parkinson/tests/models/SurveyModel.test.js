// const surveyModel = require('../../models/SurveyModel');
const appConfig = require('../../config/AppConfig');
const surveyModel = appConfig.surveyModel;
const testDataSetUp = require('../config/TestDataSetUp');
const testDataConfig = require('../config/TestDataConfig');
const knex = require('../../config/DBConfig');

describe('MedicineModel test', () => {

  testDataSetUp.beforeEach;
  testDataSetUp.afterEach;

  test('유효한 환자번호로 설문조사 정보 추가', async () => {
    await surveyModel
      .createSurvey(
        testDataConfig.Survey.patient_num,
        true,
        false,
        50
      );
    
    const survey = await knex
      .select('*')
      .from('survey AS s')
      .where('s.patient_num', testDataConfig.Survey.patient_num)
      .then((survey) => {
        return survey;
      });

    expect(survey).toHaveLength(1);
    expect(survey[0].abnormal_movement).toEqual(true);
    expect(survey[0].medicinal_effect).toEqual(false);
    expect(survey[0].patient_condition).toEqual(50);
  });

});