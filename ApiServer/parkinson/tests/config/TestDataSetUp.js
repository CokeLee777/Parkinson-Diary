const jwt = require('jsonwebtoken');
const knex = require('../../config/DBConfig');
const testDataConfig = require('./TestDataConfig');

module.exports = {
  
  beforeEach: beforeEach(async () => {
    //환자정보 삽입
    await knex
      .insert(testDataConfig.Patients)
      .into('patients')
      .then((result) => {
        console.log(`success insert test data=${result}`);
      })
      .catch((error) => {
        console.error(error);
      });
    //환자 약 복용정보 삽입
    await knex
      .insert(testDataConfig.Medicine)
      .into('medicine')
      .then((result) => {
        console.log(`success insert test data=${result}`);
      })
      .catch((error) => {
        throw error;
      })
  }),

  afterEach: afterEach(async () => {
    await knex('survey AS s')
      .where('s.patient_num', testDataConfig.Survey.patient_num)
      .del()
      .then((result) => {
        console.log(`success delete test data=${result}`);
      })
      .catch((error) => {
        console.error(error);
      });
    
    await knex('medicine AS m')
      .where('m.patient_num', testDataConfig.Patients.patient_num)
      .del()
      .then((result) => {
        console.log(`success delete test data=${result}`);
      })
      .catch((error) => {
        throw error;
      });
    
    await knex('patients AS p')
      .where('p.patient_num', testDataConfig.Medicine.patient_num)
      .del()
      .then((result) => {
        console.log(`success delete test data=${result}`);
      })
      .catch((error) => {
        console.error(error);
      });
  }),

  issueJwtToken: async function(patientNum){

    const accessToken = await jwt.sign({
        type: 'JWT',
        patientNum: patientNum,
        patientName: testDataConfig.Patients.patient_name,
    }, process.env.JWT_SECRET_KEY, {
        expiresIn: process.env.JWT_ACCESS_EXPIRATION,
        issuer: process.env.JWT_ISSUER
    });
  
    return process.env.JWT_PREFIX + accessToken;
  }
}


