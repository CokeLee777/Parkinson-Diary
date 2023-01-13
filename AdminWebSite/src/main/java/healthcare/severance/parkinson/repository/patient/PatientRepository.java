package healthcare.severance.parkinson.repository.patient;

import healthcare.severance.parkinson.domain.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatientRepository {

    void save(Patient patient);

    Page<Patient> findByName(String name, Pageable pageable);

    Patient findByPatientNum(Long patientNum);

    void deleteByPatientNum(Long patientNum);

    boolean existsByPatientNum(Long patientNum);

    Page<Patient> findAll(Pageable pageable);
}
