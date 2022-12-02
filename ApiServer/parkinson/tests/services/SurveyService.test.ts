import {AppConfig} from '../../config/AppConfig';
import {TestDataSetUp} from '../config/TestDataSetUp';
import {TestDataConfig} from "../config/TestDataConfig";
import { SurveyCreateRequest } from '../../dto/SurveyRequestDto';
import {knex} from '../../config/DBConfig';

const surveyService = AppConfig.surveyService;

describe('SurveyService test', () => {

  TestDataSetUp.beforeAll;
  TestDataSetUp.afterAll;

  test('설문조사 정보 추가', async () => {

    await surveyService
      .createSurvey(
        TestDataConfig.Patients.patient_num,
        new SurveyCreateRequest(
          TestDataConfig.Survey.abnormal_movement,
          TestDataConfig.Survey.medicinal_effect,
          TestDataConfig.Survey.patient_condition
        )
      );

    const survey = await knex
      .select('*')
      .from('survey AS s')
      .where('s.patient_num', TestDataConfig.Survey.patient_num)
      .then((survey) => {
        return survey;
      });

    await expect(survey).toHaveLength(1);
    await expect(survey[0].abnormal_movement).toEqual(TestDataConfig.Survey.abnormal_movement);
    await expect(survey[0].medicinal_effect).toEqual(TestDataConfig.Survey.medicinal_effect);
    await expect(survey[0].patient_condition).toEqual(TestDataConfig.Survey.patient_condition);
  });

});