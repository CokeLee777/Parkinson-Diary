package healthcare.severance.parkinson.controller;

import healthcare.severance.parkinson.domain.User;
import healthcare.severance.parkinson.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @GetMapping("/setting/membership")
    public String getMembershipForm(Model model) {
        model.addAttribute("unsignedUsers", userService.findUnsignedUser());
        model.addAttribute("signedUsers", userService.findSignedUser());
        return "membershipForm";
    }

    @PostMapping("/signUser")
    public String signUser(@RequestParam(value = "unsignedUser", required = false) List<User> users, Model model) {
        if (users == null) {
            model.addAttribute("NullCheckedUsers", true);
            return getMembershipForm(model);
        }
        userService.signUser(users);
        return "redirect:/admin/setting/membership";
    }

    @PostMapping("/unsignUser")
    public String unsignUser(@RequestParam(value = "signedUser", required = false) List<User> users, Model model) {
        if (users == null) {
            model.addAttribute("NullCheckedUsers", true);
            return getMembershipForm(model);
        }
        userService.unsignUser(users);
        return "redirect:/admin/setting/membership";
    }
}
