const express = require('express');
const router = express.Router();
const db = require('../config/db');
const { verifyToken } = require('./auth');

/**
 * ENDPOINT: /api/diary
 * METHOD: GET, POST, PUT
 * DESCRIPTION: 환자의 약 복용정보 API
 */
router.route('/').get(verifyToken, (request, response, next) => {

  const patientNum = request.decodedToken.patientNum;
  db.query('SELECT patient_name, sleep_end_time, sleep_start_time FROM patients WHERE patient_num = ?', patientNum, (error, patientInfo) => {
    db.query('SELECT take_time, is_take FROM medicine WHERE patient_num = ?', patientNum, (error2, medicineInfo) => {

      if(error || error2){
        console.error(`DB QUERY ERROR=${error}`);
        response.status(500).send('Internal server error');
      } else {
        console.log('Search patient and medicine info');
        const responseBody = {
          patient_name: patientInfo.patient_name,
          sleep_start_time: patientInfo.sleep_start_time,
          sleep_end_time: patientInfo.sleep_end_time,
          medicine_info: medicineInfo
        };
        response.status(200).json({data: responseBody});
      }
    })
  })
}).post(verifyToken, (request, response, next) => {

  const patientNum = request.decodedToken.patientNum;
  const medicineInfo = request.body;
  //환자가 작성한 복용 정보 DB에 세팅
  for(let i = 0; i < medicineInfo.length; i++){
    const takeTime = medicineInfo[i];
    db.query('INSERT INTO medicine (patient_num, take_time) VALUES (?, ?)', [patientNum, takeTime], (error, result) => {
      
      if(error) {
        console.error(`DB QUERY ERROR=${error}`);
        response.status(500).send('Internal server error');
      } else {
        console.log(`INSERT medicine info, take_time=${takeTime}`);
        response.status(200);
      }
    });
  }
  // TODO: Add diary edit logic
}).put(verifyToken, (request, response, next) => {
  const patientNum = request.decodedToken.patientNum;
});

module.exports = router;
