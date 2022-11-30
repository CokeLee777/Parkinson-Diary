import express, {NextFunction, Request, Response} from 'express';
import {DiaryCreateRequest} from '../dto/DiaryRequestDto';
import {NotEnoughInputDataError} from '../error/CommonError';
import {verifyToken} from './AuthRouter';

import {AppConfig} from '../config/AppConfig';

const router = express.Router();
const diaryService = AppConfig.diaryService;
const medicineService = AppConfig.medicineService;

/**
 * ENDPOINT: /api/diary
 * METHOD: GET, POST, PUT
 * DESCRIPTION: 환자의 약 복용정보 API
 */
router.route('/').get(verifyToken, async (request: Request, response: Response, next: NextFunction) => {

  const patientNum = (<any>request.decodedToken).patientNum;
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
}).post(verifyToken, async (request: Request, response: Response, next: NextFunction) => {

  const patientNum = (<any>request.decodedToken).patientNum;
  try {
    const diaryCreateRequest = await parseRequestBody(request);
    await diaryService.createDiary(patientNum, diaryCreateRequest);
    //알람 스케줄러 등록
    await medicineService.notifyMedicineTakeTime(patientNum);

    return response.sendStatus(200);
  } catch (error) {
    next(error);
  }
//파킨슨 다이어리 재시작하기
}).put(verifyToken, async (request: Request, response: Response, next: NextFunction) => {

  const patientNum = (<any>request.decodedToken).patientNum;
  try {
    //기존 알람 스케줄러 삭제
    await medicineService.stopNotifyMedicineTakeTime(patientNum);
    // 다이어리 정보 업데이트
    const diaryCreateRequest: DiaryCreateRequest = await parseRequestBody(request);
    await diaryService.updateDiary(patientNum, diaryCreateRequest);
    //알람 스케줄러 업데이트
    await medicineService.notifyMedicineTakeTime(patientNum);

    return response.sendStatus(200);
  } catch (error) {
    next(error);
  }
});

//사용자 입력정보 파싱
async function parseRequestBody(request: Request){
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

export default router;
