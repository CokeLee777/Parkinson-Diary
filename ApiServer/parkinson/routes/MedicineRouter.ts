import express, {NextFunction, Request, Response} from 'express';
const router = express.Router();
import { verifyToken } from './AuthRouter';

import {AppConfig} from '../config/AppConfig';
const medicineService = AppConfig.medicineService;


router.route('/notification').post(verifyToken, async (request: Request, response: Response, next: NextFunction) => {
    const patientNum = (<any>request.decodedToken).patientNum;
    try {
        await medicineService
            .notifyMedicineTakeTime(patientNum);

        return response.sendStatus(200);
    } catch(error) {
        next(error);
    }
}).delete(verifyToken, async (request: Request, response: Response, next: NextFunction) => {
    const patientNum = (<any>request.decodedToken).patientNum;
    try {
        await medicineService
            .stopNotifyMedicineTakeTime(patientNum);

        return response.sendStatus(200);
    } catch(error) {
        next(error);
    }
});

export default router;