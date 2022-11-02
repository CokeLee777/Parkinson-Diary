package healthcare.severance.parkinson.controller;

import healthcare.severance.parkinson.domain.Patient;
import healthcare.severance.parkinson.dto.patient.PatientEditForm;
import healthcare.severance.parkinson.dto.patient.PatientForm;
import healthcare.severance.parkinson.service.PatientService;
import healthcare.severance.parkinson.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @RequestMapping("/patientList")
    public String patientSearchList(Model model, @RequestParam(value = "keyword", required = false) String keyword,
                                    @PageableDefault(sort = "patientNum", direction = Sort.Direction.DESC)Pageable pageable) {
        if (!StringUtils.hasText(keyword)) {
            setPatientListPage(model, pageable, patientService.pageList(pageable));
            return "/patient/patientList";
        }
        setPatientListPage(model, pageable, patientService.findPatientByName(keyword, pageable));
        model.addAttribute("keyword", keyword);
        return "/patient/patientList";
    }

    @GetMapping("/add")
    public String addPatientForm(@ModelAttribute("form") PatientForm form, Model model) {
        model.addAttribute("users", getAllUsernamesAndIds());
        return "/patient/patientForm";
    }

    @PostMapping("/add")
    public String addPatient(@Validated @ModelAttribute("form") PatientForm form,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return addPatientForm(form, model);
        }
        if (!form.getName().equals(form.getRepeatName())) {
            bindingResult.addError(new FieldError("name","name","이름을 동일하게 입력해주세요."));
            return addPatientForm(form, model);
        }
        try {
            patientService.createPatient(form);
        } catch (Exception e) {
            bindingResult.addError(new FieldError("patientNum","patientNum","이미 존재하는 환자 번호입니다."));
            return addPatientForm(form, model);
        }
        return "redirect:/patient/patientList";
    }

    @GetMapping("/{patientNum}")
    public String patientDetail(@PathVariable("patientNum") Long patientNum, Model model) {
        model.addAttribute("patient", patientService.findPatientByPatientNum(patientNum));
        return "/patient/patientDetail";
    }

    @GetMapping("/{patientNum}/edit")
    public String patientEditForm(@PathVariable("patientNum") Long patientNum,
                                  @ModelAttribute("form") PatientEditForm form, Model model) {
        model.addAttribute("patient", patientService.findPatientByPatientNum(patientNum));
        model.addAttribute("users", getAllUsernamesAndIds());
        return "/patient/patientEditForm";
    }

    @PostMapping("/{patientNum}/edit")
    public String patientEditOrDelete(@PathVariable("patientNum") Long patientNum,
                                      @Validated @ModelAttribute("form") PatientEditForm form,
                                      BindingResult bindingResult,
                                      @RequestParam("buttonValue") String buttonValue,
                                      Model model) {
        if (bindingResult.hasErrors()) {
            return patientEditForm(patientNum, form, model);
        }
        if (buttonValue.equals("update")) {
            patientService.editPatient(patientNum, form);
            return "redirect:/patient/" + patientNum;
        } else if (buttonValue.equals("delete")) {
            patientService.deletePatient(patientNum);
            return "redirect:/patient/patientList";
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
