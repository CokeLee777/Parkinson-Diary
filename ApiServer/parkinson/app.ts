import express, {Express, NextFunction, Request, Response} from 'express';
import logger from 'morgan';
import {knex} from './config/DBConfig';

const app: Express = express();

import {DatabaseConnectError, InvalidInputTypeError, NotEnoughInputDataError} from "./error/CommonError";
import {InvalidPatientNumberError} from "./error/PatientServiceError";

import healthCheckRouter from "./routes/HealthCheckRouter";
import diaryRouter from "./routes/DiaryRouter";
import surveyRouter from "./routes/SurveyRouter";
import patientsRouter from "./routes/PatientsRouter";

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));

// 라우터 세팅
app.use('/api', healthCheckRouter);
app.use('/api/diary', diaryRouter);
app.use('/api/survey', surveyRouter);
app.use('/api/patients', patientsRouter);

//전역적으로 JSON 파싱 오류 처리
app.use((error: Error, request: Request, response: Response, next: NextFunction) => {

    if(error instanceof NotEnoughInputDataError){
        return response.status(400).json({message: error.message});
    } else if(error instanceof InvalidInputTypeError){
        return response.status(400).json({message: error.message});
    } else if(error instanceof InvalidPatientNumberError){
        return response.status(401).json({message: error.message});
    } else if(error instanceof DatabaseConnectError){
        return response.status(500).json({message: error.message});
    } else if(error instanceof SyntaxError){
        return response.status(400).json({message: '부적절한 JSON 형식입니다.'});
    } else {
        return response.status(500).json({message: error.message});
    }
});

app.use((request: Request, response: Response) => {
    response.status(404).send('Not Found!');
});

knex.raw('SELECT 1')
  .then(() => {
    if(process.env.NODE_ENV !== 'test'){
      app.listen(process.env.SERVER_PORT);
    }
    console.log('CONNECTED TO MYSQL');
    console.log(`CONNECT TO node.js SERVER PORT=${process.env.SERVER_PORT}`);
  })
  .catch((error: Error) => {
    console.error(`CONNECTED FAILED TO MYSQL=${error}`);
  })

export default app;