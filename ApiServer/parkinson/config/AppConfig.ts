import {PatientModel} from '../models/PatientModel';
import {MedicineModel} from '../models/MedicineModel';
import {SurveyModel} from '../models/SurveyModel';
import {DiaryService} from '../services/DiaryService';
import {PatientService} from '../services/PatientService';
import {SurveyService} from '../services/SurveyService';
import {MedicineService} from "../services/MedicineService";
import {MedicineHistoryModel} from "../models/MedicineHistoryModel";
import {MedicineHistoryService} from "../services/MedicineHistoryService";

export const AppConfig = {
  patientModel: PatientModel.getInstance(),
  medicineModel: MedicineModel.getInstance(),
  medicineHistoryModel: MedicineHistoryModel.getInstance(),
  surveyModel: SurveyModel.getInstance(),
  diaryService: DiaryService
    .getInstance(PatientModel.getInstance(), MedicineModel.getInstance()),
  patientService: PatientService.getInstance(PatientModel.getInstance()),
  surveyService: SurveyService.getInstance(PatientModel.getInstance(), SurveyModel.getInstance()),
  medicineService: MedicineService.getInstance(PatientModel.getInstance(), MedicineModel.getInstance()),
  medicineHistoryService: MedicineHistoryService.getInstance(MedicineHistoryModel.getInstance())
}