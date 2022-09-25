package healthcare.severance.parkinson.controller;

import healthcare.severance.parkinson.domain.Patient;
import healthcare.severance.parkinson.dto.patient.PatientEditForm;
import healthcare.severance.parkinson.dto.patient.PatientForm;
import healthcare.severance.parkinson.service.PatientService;
import healthcare.severance.parkinson.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Slf4j
@Controller
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final UserService userService;

    @GetMapping("/patientList")
    public String patientList(Model model) {
        model.addAttribute("patients", patientService.findAllPatient());
        return "/patient/patientList";
    }

    @PostMapping("/patientList")
    public String patientSearchList(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        if (!StringUtils.hasText(keyword)) {
            model.addAttribute("patients", patientService.findAllPatient());
            return "/patient/patientList";
        }
        model.addAttribute("patients", patientService.findPatientByName(keyword));
        return "/patient/patientList";
    }

    @GetMapping("/add")
    public String addPatient(@ModelAttribute("form") PatientForm form, Model model) {
        model.addAttribute("users", getAllUsernamesAndIds());
        return "/patient/patientForm";
    }

    @PostMapping("/add")
    public String addPatient(@Validated @ModelAttribute("form") PatientForm form,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            log.debug("필드 검증 오류={}", bindingResult);
            model.addAttribute("users", getAllUsernamesAndIds());
            return "/patient/patientForm";
        }
        if (!form.getName().equals(form.getRepeatName())) {
            model.addAttribute("users", getAllUsernamesAndIds());
            bindingResult.addError(new FieldError("form", "name", "초성을 동일하게 입력 해주세요."));
            return "/patient/patientForm";
        }
        patientService.createPatient(form);
        return "redirect:/patient/patientList";
    }

    @GetMapping("/{id}")
    public String patientDetail(@PathVariable("id") Long id, Model model) {
        Patient patient = patientService.findPatientById(id);
        model.addAttribute("patient", patient);
        return "/patient/patientDetail";
    }

    @GetMapping("/{id}/edit")
    public String patientEditForm(@PathVariable("id") Long id,@ModelAttribute("form") PatientEditForm form, Model model) {
        model.addAttribute("patient", patientService.findPatientById(id));
        model.addAttribute("users", getAllUsernamesAndIds());
        return "/patient/patientEditForm";
    }

    @PostMapping("/{id}/edit")
    public String patientEditOrDelete(@PathVariable("id") Long id, @Validated @ModelAttribute("form") PatientEditForm form, @RequestParam("buttonValue") String buttonValue, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("patient", patientService.findPatientById(id));
            model.addAttribute("users", getAllUsernamesAndIds());
            return "/patient/patientEditForm";
        }
        if (buttonValue.equals("update")) {
            patientService.editPatient(id, form);
            return "redirect:/patient/" + id;
        } else {
            patientService.deletePatient(id);
            return "/patient/patientList";
        }
    }

    private HashMap<Long, String> getAllUsernamesAndIds() {
        HashMap<Long, String> allUsernamesAndIds = userService.findAllUsernamesAndIds();
        return allUsernamesAndIds;
    }
}
