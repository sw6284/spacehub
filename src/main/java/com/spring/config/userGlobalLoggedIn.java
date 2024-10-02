package com.spring.config;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpSession;


@ControllerAdvice
public class userGlobalLoggedIn {
    @ModelAttribute
    public void addAttributes(Model model, HttpSession session) {
        String loggedInUserId = (String) session.getAttribute("loggedInUser");
        boolean loggedIn = loggedInUserId != null;
        model.addAttribute("loggedIn", loggedIn);
    }
}
