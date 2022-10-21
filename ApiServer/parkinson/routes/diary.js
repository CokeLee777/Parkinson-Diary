const express = require('express');
const router = express.Router();
const knex = require('../config/knex');
const { verifyToken } = require('./auth');

/**
 * ENDPOINT: /api/diary
 * METHOD: GET, POST, PUT
 * DESCRIPTION: 환자의 약 복용정보 API
 */
router.route('/').get(verifyToken, async (request, response, next) => {

  const patientNum = request.decodedToken.patientNum;
  try {
    const patientInfo = await knex
      .select('p.sleep_start_time', 'p.sleep_end_time')
      .from('patients AS p')
      .where('p.patient_num', patientNum)
      .then((patientInfo) => {
        return patientInfo[0];
      });

    const medicineInfo = await knex
      .select('m.take_time', 'm.is_take')
      .from('medicine AS m')
      .where('m.patient_num', patientNum)
      .then((medicineInfo) => {
        return medicineInfo;
      })
      .catch((error) => {
        throw new SyntaxError('Internal server error');
      });
    
    const responseBody = {
      sleep_start_time: patientInfo.sleep_start_time,
      sleep_end_time: patientInfo.sleep_end_time,
      take_times: medicineInfo
    };
    
    return response.status(200).json({data: responseBody});

  } catch(error) {
    console.error(error);
    if(error.name === SyntaxError.name){
        return response.status(500).send(error.message);
    } else {
      return response.status(500).send('Internal server error');
    }
  }

}).post(verifyToken, async (request, response, next) => {

  const patientNum = request.decodedToken.patientNum;
  try {
    //사용자 입력정보 파싱
    const diaryInputData = await parseInputData(request);
    //취침, 기상시간 DB에 세팅
    await knex('patients AS p')
      .update({
        sleep_start_time: new Date(`July 1, 1999, ${diaryInputData.sleep_start_time}`),
        sleep_end_time: new Date(`July 1, 1999, ${diaryInputData.sleep_end_time}`)
      })
      .where('p.patient_num', patientNum)
      .catch((error) => {
        throw new SyntaxError('Internal server error');
      });

    //환자가 입력한 약 복용시간 DB에 세팅
    for(let i = 0; i < diaryInputData.take_times.length; i++){
      const takeTime = diaryInputData.take_times[i].take_time;
      await knex.insert({
        patient_num: patientNum,
        take_time: new Date(`July 1, 1999, ${takeTime}`)
      })
      .into('medicine')
      .catch((error) => {
        throw new SyntaxError(`Internal server error`);
      })
    }
    return response.sendStatus(200);
  } catch (error) {
    console.error(error);
    if(error.name === TypeError.name){
      return response.status(400).send(error.message);
    } else if(error.name === SyntaxError.name){
      return response.status(500).send(error.message);
    } else {
      return response.status(500).send('Internal server error');
    }
  }
//파킨슨 다이어리 재시작하기
}).put(verifyToken, async (request, response, next) => {

  const patientNum = request.decodedToken.patientNum;
  try {
    //사용자 입력정보 파싱
    const diaryInputData = await parseInputData(request);
    //취침, 기상시간 DB에 세팅
    await knex('patients AS p')
      .update({
        sleep_start_time: new Date(`July 1, 1999, ${diaryInputData.sleep_start_time}`),
        sleep_end_time: new Date(`July 1, 1999, ${diaryInputData.sleep_end_time}`)
      })
      .where('p.patient_num', patientNum)
      .catch((error) => {
        throw new SyntaxError('Internal server error');
      });
    
      //설정되었던 복용시간 모두 삭제
    await knex('medicine AS m')
      .where('m.patient_num', patientNum)
      .del()
      .catch((error) => {
        throw new SyntaxError(`Internal server error`);
      });

    //환자가 입력한 약 복용시간 DB에 세팅
    for(let i = 0; i < diaryInputData.take_times.length; i++){
      const takeTime = diaryInputData.take_times[i].take_time;
      await knex.insert({
        patient_num: patientNum,
        take_time: new Date(`July 1, 1999, ${takeTime}`)
      })
      .into('medicine')
      .catch((error) => {
        throw new SyntaxError(`Internal server error`);
      })
    }
    return response.sendStatus(200);
  } catch (error) {
    console.error(error);
    if(error.name === TypeError.name){
      return response.status(400).send(error.message);
    } else if(error.name === SyntaxError.name){
      return response.status(500).send(error.message);
    } else {
      return response.status(500).send('Internal server error');
    }
  }
});

async function parseInputData(request){
  const inputMedicineData = request.body;
  
  if(inputMedicineData === undefined){
      throw new TypeError('Bad input request');
  } else if(inputMedicineData.sleep_start_time === undefined ||
    inputMedicineData.sleep_end_time === undefined){
      throw new TypeError('Bad input request');
  } else if(inputMedicineData.take_times.length === 0){
    throw new TypeError('Bad input request');
  } else if(inputMedicineData.take_times[0].take_time === undefined){
    throw new TypeError('Bad input request');
  } else {
    return inputMedicineData;
  }
}

module.exports = router;
