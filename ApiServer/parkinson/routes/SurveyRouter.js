const express = require('express');
const router = express.Router();
const knex = require('../config/knex');
const { SurveyCreateRequest } = require('../dto/SurveyRequestDto');
const { NotEnoughInputDataError, InvalidInputTypeError } = require('../error/CommonError');
const { verifyToken } = require('./AuthRouter');
const surveyModel = require('../models/SurveyModel');
const SurveyService = require('../services/SurveyService');

const diaryService = new SurveyService(surveyModel);

router.route('/').post(verifyToken, async (request, response, next) => {
    const patientNum = request.decodedToken.patientNum;
    try {
        const surveyCreateRequest = await parseRequestBody(request);
        await diaryService.createSurvey(patientNum, surveyCreateRequest);

        return response.sendStatus(200);
    } catch(error) {
        next(error);
    }
    
});

router.use((error, request, response, next) => {
    if(error instanceof NotEnoughInputDataError){
        return response.status(400).json({message: error.message});
    } else if(error instanceof InvalidInputTypeError){
        return response.status(400).json({message: error.message});
    } else if(error instanceof InvalidPatientNumberError){
        return response.status(401).json({message: error.message});
    } else if(error instanceof DatabaseConnectError){
        return response.status(500).json({message: error.message});
    } else {
        return response.status(500).json({message: error.message});
    }
});

async function parseRequestBody(request){
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

module.exports = router;