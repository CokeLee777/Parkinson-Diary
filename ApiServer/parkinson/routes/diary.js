const express = require('express');
const router = express.Router();
const createError = require('http-errors');
const knex = require('../config/knex');
const { verifyToken } = require('./auth');

/**
 * ENDPOINT: /api/diary
 * METHOD: GET, POST, PUT
 * DESCRIPTION: 환자의 약 복용정보 API
 */
router.route('/').get(verifyToken, async (request, response, next) => {
  const patientInfo = request.decodedToken;

  await knex.select('m.take_time', 'm.is_take')
    .from('medicine AS m')
    .where('m.patient_num', patientInfo.patientNum)
    .then((medicineInfo) => {
      const responseBody = {
        patient_name: patientInfo.patientName,
        sleep_start_time: patientInfo.sleepStartTime,
        sleep_end_time: patientInfo.sleepEndTime,
        medicine_info: medicineInfo
      };
      return response.status(200).json({data: responseBody});
    })
    .catch((error) => {
      console.error(`DB QUERY ERROR=${error}`);
      return response.status(500).send('Internal server error');
    });

}).post(verifyToken, async (request, response, next) => {
  const patientInfo = request.decodedToken;
  // 사용자 입력정보 파싱
  const inputMedicineData = await parseInputData(request)
    .catch((error) => {
      if(error.name = TypeError.name){
        return response.status(400).send(error.message);
      }
    });
  //환자가 작성한 복용 정보 DB에 세팅
  for(let i = 0; i < inputMedicineData.length; i++){

    const takeTime = inputMedicineData[i];
    await knex.insert({
      patient_num: patientInfo.patientNum,
      take_time: new Date(takeTime) //수정 필요
    })
    .into('medicine')
    .then((result) => {
      return response.sendStatus(200);
    })
    .catch((error) => {
      console.error(`DB QUERY ERROR=${error}`);
      return response.status(500).send('Internal server error');
    })
  }
  // TODO: Add diary edit logic
}).put(verifyToken, (request, response, next) => {
  const patientNum = request.decodedToken.patientNum;
});

async function parseInputData(request){
  const inputMedicineData = request.body;
  if(inputMedicineData.length === 0 || inputMedicineData === undefined){
      throw new TypeError('Bad type request');
  }

  return inputMedicineData;
}

module.exports = router;
