const express = require('express');
const router = express.Router();
const knex = require('../config/knex');
const { verifyToken } = require('./auth');

router.route('/').post(verifyToken, async (request, response, next) => {
    const patientNum = request.decodedToken.patientNum;
    const surveyInfo = request.body;
    await knex.insert({
        abnormal_movement: surveyInfo.has_abnormal_movement,
        medicinal_effect: surveyInfo.has_medicinal_effect,
        patient_condition: surveyInfo.patient_condition,
        survery_time: new Date(),
        patient_num: patientNum
    })
    .into('survey')
    .then((result) => {
        return response.sendStatus(200);
    })
    .catch((error) => {
        console.error(`DB QUERY ERROR=${error}`);
        return response.status(500).send('Internal server error');
    });
});

module.exports = router;