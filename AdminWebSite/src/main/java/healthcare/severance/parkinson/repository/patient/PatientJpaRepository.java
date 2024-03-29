package healthcare.severance.parkinson.repository.patient;

import healthcare.severance.parkinson.domain.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PatientJpaRepository extends JpaRepository<Patient, Long> {
    Page<Patient> findByName(String name, Pageable pageable);

    boolean existsByPatientNum(Long patientNum);
}
