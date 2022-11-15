import express, {Express, NextFunction, Request, Response} from 'express';
const router = express.Router();
import {NotEnoughInputDataError, InvalidInputTypeError, DatabaseConnectError} from '../error/CommonError';
import { InvalidPatientNumberError } from '../error/PatientServiceError';

import {AppConfig} from '../config/AppConfig';
const patientService = AppConfig.patientService;

/**
 * ENDPOINT: /api/patients/login
 * 환자가 갤럭시워치에 로그인할 때 요청하는 API
 */
router.post('/login', async (request: Request, response: Response, next: NextFunction) => {
    
    try {
        const patientNum = await parseRequestBody(request);
        const loginResponse = await patientService.login(patientNum);

        return response
            .status(200)
            .contentType('application/json')
            .send(loginResponse.serialize());
    } catch (error) {
        next(error);
    }
});

// 사용자 입력값 검증 메서드
async function parseRequestBody(request: Request){
    const patientNum = request.body.patient_num;
    if(patientNum === undefined || patientNum == null){
        throw new NotEnoughInputDataError('부적합한 요청 정보 입니다.');
    } else if(isNaN(patientNum)){
        throw new InvalidInputTypeError('부적절한 요청 정보 입니다.');
    }

    return patientNum;
}

export default router;