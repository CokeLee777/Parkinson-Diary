package healthcare.severance.parkinson.controller;

import healthcare.severance.parkinson.dto.user.RegisterForm;
import healthcare.severance.parkinson.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final UserService userService;

    @GetMapping
    public String register(@ModelAttribute("form") RegisterForm form){
        log.debug("Access register form");
        return "/register";
    }

    @PostMapping
    public String register(@Validated @ModelAttribute("form") RegisterForm form, BindingResult bindingResult){
        log.info("form = {}", form.toString());
        if(bindingResult.hasErrors()){
            log.debug("필드 검증 오류={}", bindingResult);
            return "/register";
        }

        if(userService.isDuplicateUser(form.getIdentifier())){
            log.debug("아이디 중복 오류 identifier={}", form.getIdentifier());
            bindingResult.rejectValue("identifier", "duplicatedUser", "아이디가 중복됩니다.");
            return "/register";
        }

        if(!form.getPassword().equals(form.getRepeatPassword())){
            log.debug("비밀번호 불일치={},{}", form.getPassword(), form.getRepeatPassword());
            bindingResult.reject("notSamePassword", "비밀번호가 일치하지 않습니다.");
            return "/register";
        }

        userService.createUser(form);

        return "redirect:/";
    }
}

