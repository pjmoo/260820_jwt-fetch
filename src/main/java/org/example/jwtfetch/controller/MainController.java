package org.example.jwtfetch.controller;

import lombok.RequiredArgsConstructor;
import org.example.jwtfetch.dto.SignUpForm;
import org.example.jwtfetch.service.UserAccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class MainController {
    private final UserAccountService userAccountService;

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("form", new SignUpForm("", ""));
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(
            @Validated @ModelAttribute("form") SignUpForm dto
            // , BindingResult result
    ) {
        userAccountService.signUp(dto.toEntity());
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
