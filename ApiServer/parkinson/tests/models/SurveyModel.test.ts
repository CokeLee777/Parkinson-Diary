import {AppConfig} from '../../config/AppConfig';
import {TestDataSetUp} from '../config/TestDataSetUp';
import {TestDataConfig} from "../config/TestDataConfig";
import {knex} from '../../config/DBConfig';

const surveyModel = AppConfig.surveyModel;

describe('SurveyModel test', () => {

  TestDataSetUp.beforeAll;
  TestDataSetUp.afterAll;

  test('유효한 환자번호로 설문조사 정보 추가', async () => {
    await surveyModel
      .createSurvey(
        TestDataConfig.Survey.patient_num,
        true,
        false,
        50
      );
    
    const survey = await knex
      .select('*')
      .from('survey AS s')
      .where('s.patient_num', TestDataConfig.Survey.patient_num)
      .then((survey) => {
        return survey;
      });

    expect(survey).toHaveLength(1);
    expect(survey[0].abnormal_movement).toEqual(true);
    expect(survey[0].medicinal_effect).toEqual(false);
    expect(survey[0].patient_condition).toEqual(50);
  });

});