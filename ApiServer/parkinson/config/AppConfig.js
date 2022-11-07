const patientModel = require('../models/PatientModel');
const medicineModel = require('../models/MedicineModel');
const surveyModel = require('../models/SurveyModel');
const diaryService = require('../services/DiaryService');
const patientService = require('../services/PatientService');
const surveyService = require('../services/SurveyService');

module.exports = {
  patientModel: patientModel.getInstance(),
  medicineModel: medicineModel.getInstance(),
  surveyModel: surveyModel.getInstance(),
  diaryService: diaryService
    .getInstance(patientModel.getInstance(), medicineModel.getInstance()),
  patientService: patientService.getInstance(patientModel.getInstance()),
  surveyService: surveyService.getInstance(surveyModel.getInstance())
}