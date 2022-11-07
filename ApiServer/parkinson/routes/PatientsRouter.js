const express = require('express');
const router = express.Router();
const { NotEnoughInputDataError, InvalidInputTypeError } = require('../error/CommonError');
const { InvalidPatientNumberError } = require('../error/PatientServiceError');
const patientModel = require('../models/PatientModel');
const PatientService = require('../services/PatientService');

const patientService = new PatientService(patientModel);

/**
 * ENDPOINT: /api/patients/login
 * 환자가 갤럭시워치에 로그인할 때 요청하는 API
 */
router.post('/login', async (request, response, next) => {
    
    try {
        const patientNum = await parseRequestBody(request);
        const loginResponse = await patientService.login(patientNum);

        return response
            .status(200)
            .contentType('application/json')
            .send(loginResponse.serialize());
    } catch (error) {
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

// 사용자 입력값 검증 메서드
async function parseRequestBody(request){
    const patientNum = request.body.patient_num;
    if(patientNum === undefined || patientNum == null){
        throw new NotEnoughInputDataError('부적합한 요청 정보 입니다.');
    } else if(isNaN(patientNum)){
        throw new InvalidInputTypeError('부적절한 요청 정보 입니다.');
    }

    return patientNum;
}

module.exports = router;