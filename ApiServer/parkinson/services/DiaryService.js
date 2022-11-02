const DiaryInfoResponse = require('../dto/DiaryResponseDto');
const { InvalidPatientNumberError } = require('../error/PatientServiceError');
require('dotenv').config();

module.exports = class DiaryService {

  constructor(patientModel, medicineModel){
    this.patientModel = patientModel;
    this.medicineModel = medicineModel;
  }

  async getDiary(patientNum){
    const patient = await this.patientModel.findByPatientNum(patientNum);
    const medicine = await this.medicineModel.findByPatientNum(patientNum);
    
    if(patient.length == 0 || patient[0] === undefined) {
      throw new InvalidPatientNumberError('유효하지 않은 환자번호 입니다.');
    }

    return new DiaryInfoResponse(
      patient[0].sleep_start_time,
      patient[0].sleep_end_time,
      medicine
    );
  }

  async createDiary(patientNum, diaryCreateRequest){
    await this.patientModel
      .updateSleepTimeByPatientNum(
        patientNum,
        diaryCreateRequest.sleepStartTime,
        diaryCreateRequest.sleepEndTime
      );
    for(let i = 0; i < diaryCreateRequest.takeTimes.length; i++){
      const takeTime = diaryCreateRequest.takeTimes[i].take_time;
      await this.medicineModel.createMedicineTakeTime(patientNum, takeTime);
    }
  }

  async updateDiary(patientNum, diaryCreateRequest){
    await this.patientModel
      .updateSleepTimeByPatientNum(
        patientNum,
        diaryCreateRequest.sleepStartTime,
        diaryCreateRequest.sleepEndTime
      );

    await this.medicineModel.deleteAllMedicineTakeTime(patientNum);

    for(let i = 0; i < diaryCreateRequest.takeTimes.length; i++){
      const takeTime = diaryCreateRequest.takeTimes[i].take_time;
      await this.medicineModel.createMedicineTakeTime(patientNum, takeTime);
    }
  }
}