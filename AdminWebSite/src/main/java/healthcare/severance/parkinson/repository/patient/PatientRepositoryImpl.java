package healthcare.severance.parkinson.repository.patient;

import healthcare.severance.parkinson.domain.Patient;
import healthcare.severance.parkinson.exception.CustomException;
import healthcare.severance.parkinson.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
public class PatientRepositoryImpl implements PatientRepository {

    private final PatientJpaRepository patientJpaRepository;

    @Override
    @Transactional
    public void save(Patient patient) {
        patientJpaRepository.save(patient);
    }

    @Override
    @Transactional(readOnly = true)
    public Patient findByPatientNum(Long patientNum) {
        return patientJpaRepository.findById(patientNum)
                .orElseThrow(() -> new CustomException(ErrorCode.PATIENT_NUM_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Patient> findByName(String name, Pageable pageable) {
        return patientJpaRepository.findByName(name, pageable);
    }

    @Override
    @Transactional
    public void deleteByPatientNum(Long patientNum) {
        patientJpaRepository.deleteById(patientNum);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByPatientNum(Long patientNum) {
        return patientJpaRepository.existsByPatientNum(patientNum);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Patient> findAll(Pageable pageable) {
        return patientJpaRepository.findAll(pageable);
    }
}
