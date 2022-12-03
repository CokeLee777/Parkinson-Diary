import jwt from 'jsonwebtoken';
import {knex, redisClient} from '../../config/DBConfig';
import {TestDataConfig} from './TestDataConfig';

export const TestDataSetUp = {
  
  beforeAll: beforeAll(async () => {
    //환자정보 삽입
    await knex
      .insert(TestDataConfig.Patients)
      .into('patients')
      .catch((error) => {
        console.error(error);
      });
    //환자 약 복용정보 삽입
    await knex
      .insert(TestDataConfig.Medicine)
      .into('medicine')
      .catch((error) => {
        console.error(error);
      });
    if(!redisClient.isOpen){
        redisClient.connect()
            .then(() => {
                console.log(`CONNECTED TO REDIS`);
            });
    }
    // 회원정보 REDIS에 저장
    await redisClient
        .set(
            String(TestDataConfig.Patients.patient_num),
            issueJwtToken(TestDataConfig.Patients.patient_num))
        .catch((error: Error) => {
            throw error;
        });
  }),

  afterAll: afterAll(async () => {
    await knex('survey AS s')
      .where('s.patient_num', TestDataConfig.Survey.patient_num)
      .del()
      .catch((error) => {
        console.error(error);
      });
    
    await knex('medicine AS m')
      .where('m.patient_num', TestDataConfig.Patients.patient_num)
      .del()
      .catch((error) => {
        console.error(error);
      });

    await knex('medicine_history AS mh')
      .where('mh.patient_num', TestDataConfig.Patients.patient_num)
      .del()
      .catch((error) => {
          console.error(error);
      });
    
    await knex('patients AS p')
      .where('p.patient_num', TestDataConfig.Medicine.patient_num)
      .del()
      .catch((error) => {
        console.error(error);
      });

    redisClient.disconnect()
        .then(() => {
            console.log('DISCONNECTED TO REDIS');
        })
  }),

  getJwtToken: async function(patientNum: number){
      return issueJwtToken(patientNum);
  }
}

const issueJwtToken = (patientNum: number) => {

    const accessToken = jwt.sign({
        type: 'JWT',
        patientNum: patientNum,
        patientName: TestDataConfig.Patients.patient_name,
    }, String(process.env.JWT_SECRET_KEY), {
        expiresIn: process.env.JWT_ACCESS_EXPIRATION,
        issuer: process.env.JWT_ISSUER
    });

    return process.env.JWT_PREFIX + accessToken;
}


