import express, {NextFunction, Request, Response} from 'express';
import {verifyToken} from './AuthRouter';

import {AppConfig} from '../config/AppConfig';
import {NotEnoughInputDataError} from "../error/CommonError";

const router = express.Router();
const medicineHistoryService = AppConfig.medicineHistoryService;

router.route('/').post(verifyToken, async (request: Request, response: Response, next: NextFunction) => {
    const patientNum = (<any>request.decodedToken).patientNum;
    try {
        const medicineHistoryId = await parseRequestBody(request);
        await medicineHistoryService
            .createHistory(medicineHistoryId, patientNum);

        return response.sendStatus(200);
    } catch(error) {
        next(error);
    }
}).put(verifyToken, async (request: Request, response: Response, next: NextFunction) => {
    const patientNum = (<any>request.decodedToken).patientNum;
    try {
        const medicineHistoryId = await parseRequestBody(request);
        await medicineHistoryService
            .updateHistoryIsTake(medicineHistoryId, patientNum);

        return response.sendStatus(200);
    } catch(error) {
        next(error);
    }
});

//사용자 입력정보 파싱
async function parseRequestBody(request: Request){
    const requestBody = request.body;

    if(requestBody === undefined
        || requestBody.medicine_history_id === undefined){
        throw new NotEnoughInputDataError('부적합한 요청 정보 입니다.');
    }

    return requestBody.medicine_history_id;
}

export default router;