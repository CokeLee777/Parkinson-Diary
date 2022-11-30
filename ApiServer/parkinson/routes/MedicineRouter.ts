import express, {NextFunction, Request, Response} from 'express';
import {verifyToken} from './AuthRouter';

import {AppConfig} from '../config/AppConfig';

const router = express.Router();
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