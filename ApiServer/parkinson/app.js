const createError = require('http-errors');
const express = require('express');
const path = require('path');
const cookieParser = require('cookie-parser');
const logger = require('morgan');
const db = require('./config/db');

// 라우터 세팅
const diaryRouter = require('./routes/diary');
const surveyRouter = require('./routes/survey');
const patientsRouter = require('./routes/patients');
const medicineRouter = require('./routes/medicine');

const app = express();

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());

app.use('/api/diary', diaryRouter);
app.use('/api/survey', surveyRouter);
app.use('/api/patients', patientsRouter);
app.use('/api/medicine', medicineRouter);

db.connect((error) => {
  const serverPort = 80;
  if(error){
    console.error('FAILED TO CONNECT TO MYSQL');
    console.error(error);
  } else {
    app.listen(serverPort);
    console.log('CONNECTED TO MYSQL');
    console.log(`CONNECT TO node.js SERVER PORT=${serverPort}`);
  }
})

module.exports = app;
