const express = require('express');
const router = express.Router();
const db = require('../config/db');

router.post('/', function(request, response, next) {
  const { patientName, medicineInfo } = request.body;
  // 환자 이름으로 회원 PK를 가져온다.
  db.query('SELECT patient_id FROM patients WHERE patient_name = ?', patientName, function(result, error){
    if(error) throw error;

    const patientId = result[0].patient_id;
    //환자가 작성한 다이어리 DB에 세팅
    for(let i = 0; i < medicineInfo.length; i++){
      db.query('INSERT INTO medicine (patient_id, take_time, is_take) VALUES (?, ?, ?)', 
        [patientId, medicineInfo[i], false], function(result, error2){

        if(error2) throw error2;
      })
    }
  });

  response.sendStatus(200).send('ok');
});

module.exports = router;
