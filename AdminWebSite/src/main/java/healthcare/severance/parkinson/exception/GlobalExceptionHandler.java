package healthcare.severance.parkinson.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import javax.validation.ConstraintViolationException;
import java.util.Map;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { CustomException.class })
    protected ModelAndView customExceptionHandler(CustomException e, Model model) {
        log.error("handleCustomException throw CustomException : {}", e.getErrorCode());
        model.addAttribute("errorCode", e.getErrorCode().getMessage());
        return new ModelAndView("/error/customErrorPage", (Map<String, ?>) model) ;
    }

    @ExceptionHandler(value = { ConstraintViolationException.class })
    protected ModelAndView constraintViolationExceptionHandler(ConstraintViolationException e, Model model) {
        log.error("handleCustomException throw CustomException : {}", e.getMessage());
        model.addAttribute("errorCode", e.getMessage());
        return new ModelAndView("/error/customErrorPage", (Map<String, ?>) model) ;
    }
    @ExceptionHandler(value = { AuthenticationException.class })
    protected ModelAndView authenticationExceptionHandler(AuthenticationException e, Model model) {
        log.error("handleCustomException throw CustomException : {}", e.getMessage());
        model.addAttribute("errorCode", "권한이 없습니다.");
        return new ModelAndView("/error/customErrorPage", (Map<String, ?>) model) ;
    }

}