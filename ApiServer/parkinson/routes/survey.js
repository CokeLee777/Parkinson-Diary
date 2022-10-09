const express = require('express');
const router = express.Router();
const db = require('../config/db');
const { verifyToken } = require('./auth');

router.route('/').post(verifyToken, (request, response, next) => {
    const surveyInfo = request.body;
    db.query(
        'INSERT INTO survey (abnormal_movement, medicinal_effect, patient_condition) VALUES (?, ?, ?)',
        [surveyInfo.has_abnormal_movement, surveyInfo.has_medicinal_effect, surveyInfo.patient_condition],
        (error, result) => {
        
        if(error){
            console.error(`DB QUERY ERROR=${error}`);
            response.status(500).send('Internal server error');
        } else {
            response.status(200);
        }
    });
});

module.exports = router;