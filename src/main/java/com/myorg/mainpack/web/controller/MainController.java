package com.myorg.mainpack.web.controller;

import com.myorg.mainpack.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


@Controller
public class MainController {

    @Value("${mainTitle}")
    private String mainTitle;

    @Value("${loginTitle}")
    private String loginTitle;



    @GetMapping("/")
    public String getMainPage(Model model){
        model.addAttribute("title", mainTitle);
        return "mainPage";
    }


    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request){
        Exception exception = (Exception) request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        model.addAttribute("errorCode", 0); // По умолчанию все OK
        if( exception != null ){
            if( "UserDetailsService returned null, which is an interface contract violation".equals( exception.getMessage() ) ) {
                model.addAttribute("message", "Пользователь не существует!");
                model.addAttribute("status", "error");
                model.addAttribute("errorCode", 3);
            }
            if( "Bad credentials".equals( exception.getMessage() ) ){
                model.addAttribute("message", "Пароль неверный!");
                model.addAttribute("status", "error");
                model.addAttribute("errorCode", 4);
            }
        }
        model.addAttribute("title", loginTitle);
        return "users/login";
    }


/*    @PostMapping(value = "/login")
    public ModelAndView getLoginPage(@RequestParam Optional<String> error) {
        return new ModelAndView("users/login", "error", error);
    }*/


}
