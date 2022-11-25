import express, {Express, NextFunction, Request, Response} from 'express';
const router = express.Router();
import { SurveyCreateRequest } from '../dto/SurveyRequestDto';
import {NotEnoughInputDataError, InvalidInputTypeError, DatabaseConnectError} from '../error/CommonError';
import { verifyToken } from './AuthRouter';

import {AppConfig} from '../config/AppConfig';
import {InvalidPatientNumberError} from "../error/PatientServiceError";
const surveyService = AppConfig.surveyService;

router.route('/').post(verifyToken, async (request: Request, response: Response, next: NextFunction) => {
    const patientNum = (<any>request.decodedToken).patientNum;
    try {
        const surveyCreateRequest = await parseRequestBody(request);
        await surveyService.createSurvey(patientNum, surveyCreateRequest);
        
        return response.sendStatus(200);
    } catch(error) {
        next(error);
    }
    
});

router.route('/notification').post(verifyToken, async (request: Request, response: Response, next: NextFunction) => {
    const registrationToken = request.body.fcm_registration_token;
    try {
        await surveyService
            .notifySurvey(registrationToken)

        return response.sendStatus(200);
    } catch(error) {
        next(error);
    }
}).delete(verifyToken, async (request: Request, response: Response, next: NextFunction) => {
    const registrationToken = request.body.fcm_registration_token;
    try {
        await surveyService
            .stopNotifySurvey(registrationToken)

        return response.sendStatus(200);
    } catch(error) {
        next(error);
    }
});

async function parseRequestBody(request: Request){
    const requestBody = request.body;
    
    if(requestBody === undefined
        || requestBody.has_abnormal_movement === undefined
        || requestBody.has_medicinal_effect === undefined
        || requestBody.patient_condition === undefined){
        throw new NotEnoughInputDataError('부적합한 요청 정보 입니다.');
    } else if(typeof requestBody.has_abnormal_movement !== 'boolean'
        || typeof requestBody.has_medicinal_effect !== 'boolean'
        || typeof requestBody.patient_condition !== 'number'){
        throw new InvalidInputTypeError('부적절한 요청 정보 입니다.');
    }
    return new SurveyCreateRequest(
        requestBody.has_abnormal_movement,
        requestBody.has_medicinal_effect,
        requestBody.patient_condition
    )
}

export default router;