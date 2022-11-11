const express = require('express');
const router = express.Router();
const { DiaryCreateRequest } = require('../dto/DiaryRequestDto');
const { NotEnoughInputDataError, DatabaseConnectError } = require('../error/CommonError');
const { verifyToken } = require('./AuthRouter');

const diaryService = require('../config/AppConfig').diaryService;

/**
 * ENDPOINT: /api/diary
 * METHOD: GET, POST, PUT
 * DESCRIPTION: 환자의 약 복용정보 API
 */
router.route('/').get(verifyToken, async (request, response, next) => {

  const patientNum = request.decodedToken.patientNum;
  try {
    const diaryResponse = await diaryService.getDiary(patientNum);
    
    return response
      .status(200)
      .contentType('application/json')
      .send(diaryResponse.serialize());

  } catch(error) {
    next(error);
  }
//파킨슨 다이어리 시작하기
}).post(verifyToken, async (request, response, next) => {

  const patientNum = request.decodedToken.patientNum;
  try {
    const diaryCreateRequest = await parseRequestBody(request);
    await diaryService.createDiary(patientNum, diaryCreateRequest);

    return response.sendStatus(200);
  } catch (error) {
    next(error);
  }
//파킨슨 다이어리 재시작하기
}).put(verifyToken, async (request, response, next) => {

  const patientNum = request.decodedToken.patientNum;
  try {
    const diaryCreateRequest = await parseRequestBody(request);
    await diaryService.updateDiary(patientNum, diaryCreateRequest);

    return response.sendStatus(200);
  } catch (error) {
    next(error);
  }
});

router.use((error, request, response, next) => {
  if(error instanceof NotEnoughInputDataError){
      return response.status(400).json({message: error.message});
  } else if(error instanceof InvalidInputTypeError){
      return response.status(400).json({message: error.message});
  } else if(error instanceof InvalidPatientNumberError){
      return response.status(401).json({message: error.message});
  } else if(error instanceof DatabaseConnectError){
      return response.status(500).json({message: error.message});
  } else {
      return response.status(500).json({message: error.message});
  }
});

//사용자 입력정보 파싱
async function parseRequestBody(request){
  const requestBody = request.body;
  
  if(requestBody === undefined
    || requestBody.sleep_start_time === undefined
    || requestBody.sleep_end_time === undefined
    || requestBody.take_times.length === 0
    || requestBody.take_times[0] === undefined){
      throw new NotEnoughInputDataError('부적합한 요청 정보 입니다.');
  }

  return new DiaryCreateRequest(
    requestBody.sleep_start_time,
    requestBody.sleep_end_time,
    requestBody.take_times
  )
}

module.exports = router;
