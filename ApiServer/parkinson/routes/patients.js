const express = require('express');
const router = express.Router();
const jwt = require('jsonwebtoken');
const knex = require('../config/knex');
require('dotenv').config();

/**
 * ENDPOINT: /api/patients/login
 * 환자가 갤럭시워치에 로그인할 때 요청하는 API
 */
router.post('/login', async (request, response, next) => {

    console.log('로그인 요청');
    try {
        const patientNum = await parseInputData(request);
        const responseBody = await knex
            .select('*')
            .from('patients AS p')
            .where('p.patient_num', patientNum)
            .then((patientInfo) => {
                if(patientInfo.length === 0) {
                    throw new ReferenceError('Invalid patient number');
                }
                const accessToken = issueJwtToken(patientInfo);
                const responseBody = {
                    access_token: accessToken
                };
                return responseBody;
            })
            .catch((error) => {
                throw new SyntaxError('Internal server error');
            });
            
        return response.status(200).json({data: responseBody});
    } catch(error) {
        console.error(error);
        if(error.name === TypeError.name){
            return response.status(400).send(error.message);
        } else if(error.name === ReferenceError.name){
            return response.status(401).send(error.message);
        } else if(error.name === SyntaxError.name){
            return response.status(500).send(error.message);
        } else {
            return response.status(500).send('Internal server error');
        }
    }
});

// JWT 토큰 발급 메서드
function issueJwtToken(patientInfo){
    const accessToken = jwt.sign({
        type: 'JWT',
        patientNum: patientInfo[0].patient_num,
        patientName: patientInfo[0].patient_name,
        sleepStartTime: patientInfo[0].sleep_start_time,
        sleepEndTime: patientInfo[0].sleep_end_time,
        userId: patientInfo[0].user_id
    }, process.env.JWT_SECRET_KEY, {
        expiresIn: process.env.JWT_ACCESS_EXPIRATION,
        issuer: process.env.JWT_ISSUER
    });

    return process.env.JWT_PREFIX + accessToken;
}

// 사용자 입력값 검증 메서드
async function parseInputData(request){
    const patientNum = request.body.patient_num;
    if(patientNum === undefined){
        throw new TypeError('Bad type request');
    }

    return patientNum;
}

module.exports = router;