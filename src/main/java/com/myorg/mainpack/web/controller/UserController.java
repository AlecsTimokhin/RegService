package com.myorg.mainpack.web.controller;

import com.myorg.mainpack.dto.UserDto;
import com.myorg.mainpack.model.Role;
import com.myorg.mainpack.model.User;
import com.myorg.mainpack.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
@RequestMapping("/users")
public class UserController {

    @Value("${balanceTitle}")
    private String balanceTitle;

    @Value("${userNotFound}")
    private String userNotFound;

    @Value("${allUsersTitle}")
    private String allUsersTitle;

    private UserService userService;
    public UserService getUserService() { return userService; }
    @Autowired
    public void setUserService(UserService userService) { this.userService = userService; }



    @GetMapping("/balance")
    public String balance(Model model){
        model.addAttribute("title", balanceTitle);
        return "users/balance";
    }


    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String allUsers(Model model){
        List<UserDto> users = userService.getAll();
        model.addAttribute("users", users);
        model.addAttribute("count", users.size());
        model.addAttribute("title", allUsersTitle);
        return "users/userList";
    }


    @GetMapping("/registration")
    public String registrationGet(HttpServletRequest request, Model model){

        User currentUser = (User) request.getSession().getAttribute("currentUser");

        if( currentUser != null ){
            if( currentUser.getId() != null ){
                if( currentUser.getId() > 0 ){
                    if( !currentUser.getRoles().contains(Role.ADMIN) ){
                        return "redirect:/login";
                    }
                }
            }
        }

        model.addAttribute("roles", Role.values());
        //model.addAttribute("title", "Регистрация нового пользователя");  // Она в модальном окне
        return "users/registration";
    }


    @GetMapping("/profile")
    public String userProfileGet(Model model, HttpServletRequest request) {

        User currentUser = (User) request.getSession().getAttribute("currentUser");

        model.addAttribute("roles", Role.values());
        //model.addAttribute("title", "Редактирование вашего профиля");  // В модальном окне

        if( currentUser == null ){
            model.addAttribute("message", userNotFound);
            model.addAttribute("status", "error");
        }

        return "users/profile";
    }


}
