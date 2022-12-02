import {MedicineHistoryModel} from "../models/MedicineHistoryModel";

export class MedicineHistoryService {

    private medicineHistoryModel: MedicineHistoryModel;

    private static instance: MedicineHistoryService;

    public static getInstance(medicineHistoryModel: MedicineHistoryModel){
        if(this.instance !== undefined){
            return this.instance;
        }
        return new MedicineHistoryService(medicineHistoryModel);
    }

    private constructor(medicineHistoryModel: MedicineHistoryModel){
        this.medicineHistoryModel = medicineHistoryModel;
    }

    public async createHistory(medicine_history_id: string, patientNum: number) {
        await this.medicineHistoryModel
            .createMedicineHistory(medicine_history_id, patientNum);
    }

    public async updateHistoryIsTake(medicineHistoryId: string, patientNum: number) {
        await this.medicineHistoryModel
            .updateMedicineHistoryIsTakeByIdAndPatientNum(medicineHistoryId, patientNum);
    }

}