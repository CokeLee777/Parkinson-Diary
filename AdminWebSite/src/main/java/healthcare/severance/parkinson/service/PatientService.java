package healthcare.severance.parkinson.service;

import healthcare.severance.parkinson.domain.Patient;
import healthcare.severance.parkinson.domain.User;
import healthcare.severance.parkinson.dto.patient.PatientEditForm;
import healthcare.severance.parkinson.dto.patient.PatientForm;
import healthcare.severance.parkinson.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    @Transactional
    public void createPatient(PatientForm form) {
        patientRepository.save(form.toPatient());
    }

    public List<Patient> findPatientByName(String name) {
        return patientRepository.findByName(name);
    }

    public Patient findPatientById(Long id) {
        return patientRepository.findById(id).get();
    }

    public List<Patient> findAllPatient() {
        return patientRepository.findAll();
    }

    @Transactional
    public void editPatient(Long id, PatientEditForm form) {
        Patient patient = patientRepository.findById(id).get();
        patient.EditPatient(form.getInChargeUser(), form.getName());
    }

    @Transactional
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}
