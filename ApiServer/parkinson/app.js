const createError = require('http-errors');
const express = require('express');
const path = require('path');
const cookieParser = require('cookie-parser');
const logger = require('morgan');
const knex = require('./config/knex');

// 라우터 세팅
const diaryRouter = require('./routes/diary');
const surveyRouter = require('./routes/survey');
const patientsRouter = require('./routes/patients');


const app = express();

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());

app.use('/api/diary', diaryRouter);
app.use('/api/survey', surveyRouter);
app.use('/api/patients', patientsRouter);

knex.raw('SELECT 1')
  .then((result) => {
    const serverPort = 80;
    app.listen(serverPort);
    console.log('CONNECTED TO MYSQL');
    console.log(`CONNECT TO node.js SERVER PORT=${serverPort}`);
  })
  .catch((error) => {
    console.error('CONNECTED FAILED TO MYSQL');
  })

module.exports = app;
