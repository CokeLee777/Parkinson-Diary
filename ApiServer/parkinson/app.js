const createError = require('http-errors');
const express = require('express');
const path = require('path');
const cookieParser = require('cookie-parser');
const logger = require('morgan');
const knex = require('./config/knex');

// 라우터 세팅
const diaryRouter = require('./routes/diary');
const surveyRouter = require('./routes/survey');
const patientsRouter = require('./routes/PatientsRouter');

const app = express();

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());

app.use('/api/diary', diaryRouter);
app.use('/api/survey', surveyRouter);
app.use('/api/patients', patientsRouter);

//전역적으로 JSON 파싱 오류 처리
app.use((error, request, response, next) => {
  if(error instanceof SyntaxError){
    return response.status(400).send('부적절한 JSON 형식입니다.');
  }
})

knex.raw('SELECT 1')
  .then((result) => {
    app.listen(process.env.SERVER_PORT);
    console.log('CONNECTED TO MYSQL');
    console.log(`CONNECT TO node.js SERVER PORT=${process.env.SERVER_PORT}`);
  })
  .catch((error) => {
    console.error(`CONNECTED FAILED TO MYSQL=${error}`);
  })

module.exports = app;
