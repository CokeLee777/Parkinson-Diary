const express = require('express');
const router = express.Router();
const knex = require('../config/knex');
const { verifyToken } = require('./auth');

router.route('/').post(verifyToken, async (request, response, next) => {
    const patientNum = request.decodedToken.patientNum;
    try {
        const surveyInputData = await parseInputData(request);
        await knex.insert({
            abnormal_movement: surveyInputData.has_abnormal_movement,
            medicinal_effect: surveyInputData.has_medicinal_effect,
            patient_condition: surveyInputData.patient_condition,
            survery_time: new Date(),
            patient_num: patientNum
        })
        .into('survey')
        .then((result) => {
            return response.sendStatus(200);
        })
        .catch((error) => {
            throw new SyntaxError('Internal server error');
        });
    } catch(error) {
        console.error(error);
        if(error.name == TypeError.name){
            return response.status(400).send(error.message);
        } else if(error.name === SyntaxError.name){
            return response.status(500).send('Internal server error');
        } else {
            return response.status(500).send('Internal server error');
        }
    }
    
});

async function parseInputData(request){
    const inputSurveyData = request.body;

    if(inputSurveyData === undefined){
        throw new TypeError('Bad input request');
    } else if(inputSurveyData.abnormal_movement === undefined){
        throw new TypeError('Bad input request');
    } else if(inputSurveyData.medicinal_effect === undefined){
        throw new TypeError('Bad input request');
    } else if(inputSurveyData.patient_condition === undefined){
        throw new TypeError('Bad input request');
    } else {
        return inputSurveyData;
    }
}

module.exports = router;