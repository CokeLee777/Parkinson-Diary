package healthcare.severance.parkinson.controller;

import healthcare.severance.parkinson.domain.Survey;
import healthcare.severance.parkinson.dto.patient.PatientGraphForm;
import healthcare.severance.parkinson.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    @RequestMapping("/patient/{patientId}/survey")
    public String surveyForm(@ModelAttribute Survey survey,
                             @PathVariable("patientId") Long patientId,
                             @RequestParam(value = "selectedDate")
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                 Optional<LocalDate> selectedDate,
                             Model model) {
        ArrayList<PatientGraphForm> surveyList =
                surveyService.getSurvey(patientId, selectedDate.orElse(LocalDate.now()));
        if (surveyList.isEmpty()) {
            model.addAttribute("noSurveyError", "설문조사 결과가 존재하지 않습니다.");
        }
        model.addAttribute("surveys", surveyList);
        return "patient/patientGraph";
    }
}
