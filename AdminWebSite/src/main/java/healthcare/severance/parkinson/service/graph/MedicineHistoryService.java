package healthcare.severance.parkinson.service.graph;

import healthcare.severance.parkinson.dto.patient.PatientMedicineTableForm;
import java.time.LocalDate;
import java.util.List;

public interface MedicineHistoryService {
    public List<PatientMedicineTableForm> getMedicineTable(Long patientNum, LocalDate time);
}
