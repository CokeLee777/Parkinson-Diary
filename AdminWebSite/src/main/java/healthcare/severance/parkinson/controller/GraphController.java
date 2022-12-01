package healthcare.severance.parkinson.controller;

import healthcare.severance.parkinson.domain.Survey;
import healthcare.severance.parkinson.dto.patient.PatientMedicineTableForm;
import healthcare.severance.parkinson.dto.patient.PatientSurveyTableForm;
import healthcare.severance.parkinson.service.MedicineService;
import healthcare.severance.parkinson.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class GraphController {

    private final SurveyService surveyService;
    private final MedicineService medicineService;

    @RequestMapping("/patient/{patientId}/graph")
    public String GraphForm(@ModelAttribute Survey survey,
                             @PathVariable("patientId") Long patientId,
                             @RequestParam(value = "selectedDate")
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                 Optional<LocalDate> selectedDate,
                             Model model) {

        List<PatientSurveyTableForm> surveyList = surveyService.getSurveyTable(patientId, selectedDate.orElse(LocalDate.now()));
        if (surveyList.isEmpty()) {
            model.addAttribute("noSurveyError", "설문조사 결과가 존재하지 않습니다.");
        }

        List<PatientMedicineTableForm> medicineList = medicineService.getMedicineTable(patientId, selectedDate.orElse(LocalDate.now()));
        if (medicineList.isEmpty()) {
            model.addAttribute("noMedicineError", "복용 정보가 존재하지 않습니다.");
        }

        model.addAttribute("surveys", surveyList);
        model.addAttribute("medicines", medicineList);
        model.addAttribute("selectedDate", selectedDate.orElse(LocalDate.now()));
        return "patient/patientGraph";
    }
}
