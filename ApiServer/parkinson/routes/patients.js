const express = require('express');
const router = express.Router();
const jwt = require('jsonwebtoken');
const db = require('../config/db');
require('dotenv').config();

/**
 * ENDPOINT: /api/patients/login
 * 환자가 갤럭시워치에 로그인할 때 요청하는 API
 */
router.post('/login', (request, response, next) => {
    const patientNum = request.body.patient_num;
    db.query('SELECT patient_name FROM patients WHERE patient_num = ?', patientNum, (error, result) => {
        if(error) {
            console.error(`DB QUERY ERROR=${error}`);
            response.status(500).send('Internal server error');
        } else if(result.length === 0){
            console.error('Invalid patient number');
            response.status(401).send('Invalid patient number');
        }

        const accessToken = issueJwtToken(patientNum, result[0].patientName);
        const responseBody = {
            access_token: accessToken
        };
        response.status(200).json({data: responseBody});
    });
});

// JWT 토큰 발급 메서드
function issueJwtToken(patientNum, patientName){
    const accessToken = jwt.sign({
        type: 'JWT',
        patientNum: patientNum,
        patientName: patientName
    }, process.env.JWT_SECRET_KEY, {
        expiresIn: process.env.JWT_ACCESS_EXPIRATION,
        issuer: process.env.JWT_ISSUER
    });

    console.log(`ISSUE JWT TOKEN=${process.env.JWT_PREFIX + accessToken}`)

    return process.env.JWT_PREFIX + accessToken;
}

module.exports = router;