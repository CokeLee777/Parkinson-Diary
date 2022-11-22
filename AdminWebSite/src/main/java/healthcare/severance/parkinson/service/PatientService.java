package healthcare.severance.parkinson.service;

import healthcare.severance.parkinson.domain.Patient;
import healthcare.severance.parkinson.dto.patient.PatientEditForm;
import healthcare.severance.parkinson.dto.patient.PatientForm;
import healthcare.severance.parkinson.exception.CustomException;
import healthcare.severance.parkinson.exception.ErrorCode;
import healthcare.severance.parkinson.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

import static healthcare.severance.parkinson.exception.ErrorCode.DUPLICATE_RESOURCE;
import static healthcare.severance.parkinson.exception.ErrorCode.PATIENT_NUM_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    @Transactional
    public void createPatient(PatientForm form) {
        IsExistPatient(form.getPatientNum());
        patientRepository.save(form.toPatient());
    }

    public Page<Patient> findPatientByName(String name, Pageable pageable) {
        return patientRepository.findByName(name, pageable);
    }

    public Patient findPatientByPatientNum(Long patientNum) {
        return patientRepository.findById(patientNum).orElseThrow(() -> new CustomException(PATIENT_NUM_NOT_FOUND));
    }

    @Transactional
    public void editPatient(Long patientNum, PatientEditForm form) {
        Patient patient = patientRepository.findById(patientNum).orElseThrow(() -> new CustomException(PATIENT_NUM_NOT_FOUND));
        patient.EditPatient(form.getInChargeUser(), form.getName(), LocalTime.parse(form.getSleepStartTime()), LocalTime.parse(form.getSleepEndTime()));
    }

    @Transactional
    public void deletePatient(Long patientNum) {
        if (patientRepository.existsByPatientNum(patientNum)) {
            patientRepository.deleteById(patientNum);
        } else {
            throw new CustomException(PATIENT_NUM_NOT_FOUND);
        }
    }

    private void IsExistPatient(Long patientNum) {
        if (patientRepository.existsByPatientNum(patientNum)) {
            throw new CustomException(DUPLICATE_RESOURCE);
        }
    }

    @Transactional(readOnly = true)
    public Page<Patient> pageList(Pageable pageable) {
        return patientRepository.findAll(pageable);
    }
}
