import {PatientModel} from '../models/PatientModel';
import {MedicineModel} from '../models/MedicineModel';
import {SurveyModel} from '../models/SurveyModel';
import {DiaryService} from '../services/DiaryService';
import {PatientService} from '../services/PatientService';
import {SurveyService} from '../services/SurveyService';

export const AppConfig = {
  patientModel: PatientModel.getInstance(),
  medicineModel: MedicineModel.getInstance(),
  surveyModel: SurveyModel.getInstance(),
  diaryService: DiaryService
    .getInstance(PatientModel.getInstance(), MedicineModel.getInstance()),
  patientService: PatientService.getInstance(PatientModel.getInstance()),
  surveyService: SurveyService.getInstance(SurveyModel.getInstance())
}