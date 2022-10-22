package healthcare.severance.parkinson.controller;

import healthcare.severance.parkinson.domain.Patient;
import healthcare.severance.parkinson.domain.RoleType;
import healthcare.severance.parkinson.domain.User;
import healthcare.severance.parkinson.dto.patient.PatientEditForm;
import healthcare.severance.parkinson.dto.patient.PatientForm;
import healthcare.severance.parkinson.dto.user.RegisterForm;
import healthcare.severance.parkinson.repository.PatientRepository;
import healthcare.severance.parkinson.repository.UserRepository;
import healthcare.severance.parkinson.service.PatientService;
import healthcare.severance.parkinson.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.time.LocalTime;
import java.util.HashMap;

@Slf4j
@Controller
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final UserService userService;

    @RequestMapping("/patientList")
    public String patientSearchList(Model model, @RequestParam(value = "keyword", required = false) String keyword, @PageableDefault(sort = "patientNum", direction = Sort.Direction.DESC)Pageable pageable) {
        if (!StringUtils.hasText(keyword)) {
            setPatientListPage(model, pageable, patientService.pageList(pageable));
            return "/patient/patientList";
        }
        setPatientListPage(model, pageable, patientService.findPatientByName(keyword, pageable));
        model.addAttribute("keyword", keyword);
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
        if (form.getPatientNum() == null) {
            model.addAttribute("users", getAllUsernamesAndIds());
            bindingResult.addError(new FieldError("form", "patientNum", "환자 번호를 입력 해주세요."));
            return "/patient/patientForm";
        }
        if (!form.getName().equals(form.getRepeatName())) {
            model.addAttribute("users", getAllUsernamesAndIds());
            bindingResult.addError(new FieldError("form", "name", "초성을 동일하게 입력 해주세요."));
            return "/patient/patientForm";
        }
        if (patientService.IsExistPatient(form.getName())) {
            model.addAttribute("users", getAllUsernamesAndIds());
            bindingResult.addError(new FieldError("form", "name", "이미 존재하는 이름입니다."));
            return "/patient/patientForm";
        }
        patientService.createPatient(form);
        return "redirect:/patient/patientList";
    }

    @GetMapping("/{patientNum}")
    public String patientDetail(@PathVariable("patientNum") Long patientNum, Model model) {
        Patient patient = patientService.findPatientByPatientNum(patientNum);
        model.addAttribute("patient", patient);
        return "/patient/patientDetail";
    }

    @GetMapping("/{patientNum}/edit")
    public String patientEditForm(@PathVariable("patientNum") Long patientNum,@ModelAttribute("form") PatientEditForm form, Model model) {
        model.addAttribute("patient", patientService.findPatientByPatientNum(patientNum));
        model.addAttribute("users", getAllUsernamesAndIds());
        return "/patient/patientEditForm";
    }

    @PostMapping("/{patientNum}/edit")
    public String patientEditOrDelete(@PathVariable("patientNum") Long patientNum, @Validated @ModelAttribute("form") PatientEditForm form, @RequestParam("buttonValue") String buttonValue, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("patient", patientService.findPatientByPatientNum(patientNum));
            model.addAttribute("users", getAllUsernamesAndIds());
            return "/patient/patientEditForm";
        }
        if (buttonValue.equals("update")) {
            patientService.editPatient(patientNum, form);
            return "redirect:/patient/" + patientNum;
        } else if (buttonValue.equals("delete")) {
            patientService.deletePatient(patientNum);
            return "/patient/patientList";
        }
        model.addAttribute("patient", patientService.findPatientByPatientNum(patientNum));
        model.addAttribute("users", getAllUsernamesAndIds());
        return "/patient/patientEditForm";
    }

    private HashMap<Long, String> getAllUsernamesAndIds() {
        HashMap<Long, String> allUsernamesAndIds = userService.findAllUsernamesAndIds();
        return allUsernamesAndIds;
    }

    private void setPatientListPage(Model model, Pageable pageable, Page<Patient> patients) {
        int nowPage = patients.getPageable().getPageNumber()-1;
        int startPage =  Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage+9, patients.getTotalPages());
        model.addAttribute("patients", patients);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
    }
}
