package healthcare.severance.parkinson.service;

import healthcare.severance.parkinson.domain.Patient;
import healthcare.severance.parkinson.dto.patient.PatientEditForm;
import healthcare.severance.parkinson.dto.patient.PatientForm;
import healthcare.severance.parkinson.exception.CustomException;
import healthcare.severance.parkinson.exception.ErrorCode;
import healthcare.severance.parkinson.repository.patient.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

import static healthcare.severance.parkinson.exception.ErrorCode.DUPLICATE_RESOURCE;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public void createPatient(PatientForm form) {
        if (patientRepository.existsByPatientNum(form.getPatientNum())) {
            throw new CustomException(DUPLICATE_RESOURCE);
        }
        patientRepository.save(form.toPatient());
    }

    public Page<Patient> findPatientByName(String name, Pageable pageable) {
        return patientRepository.findByName(name, pageable);
    }


    public Patient findPatientByPatientNum(Long patientNum) {
        return patientRepository.findByPatientNum(patientNum);
    }

    @Transactional
    public void editPatient(Long patientNum, PatientEditForm form) {
        Patient patient = patientRepository.findByPatientNum(patientNum);

        patient.EditPatient(
                form.getInChargeUser(),
                form.getName(),
                LocalTime.parse(form.getSleepStartTime()),
                LocalTime.parse(form.getSleepEndTime())
        );
    }

    public void deletePatient(Long patientNum) {
        if (!patientRepository.existsByPatientNum(patientNum)) {
            throw new CustomException(ErrorCode.PATIENT_NUM_NOT_FOUND);
        }
        patientRepository.deleteByPatientNum(patientNum);
    }

    public Page<Patient> pageList(Pageable pageable) {
        return patientRepository.findAll(pageable);
    }
}
