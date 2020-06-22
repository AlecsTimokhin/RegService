package com.myorg.mainpack.web.interceptor;

import com.myorg.mainpack.model.Role;
import com.myorg.mainpack.model.User;
//import com.sun.istack.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class SecurityInterceptor implements HandlerInterceptor /*extends HandlerInterceptorAdapter*/ {

    public SecurityInterceptor(){}



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser;
        if (auth != null) {
            Object principal = auth.getPrincipal();
            currentUser = (principal instanceof User) ? (User) principal : null;
        }
        else{
            currentUser = null;
        }


        request.getSession().setAttribute("currentUser", currentUser); // Сохраняем в сессии текущего пользователя

        boolean canDoActions = false;
        if( currentUser != null ) {
            if( currentUser.getRoles() != null && currentUser.getRoles().contains(Role.ADMIN) ){
                canDoActions = true;
            }
        }
        request.getSession().setAttribute("canDoActions", canDoActions);

        return true;  // Пропускается любой запрос

    }


    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           /*@Nullable*/ ModelAndView modelAndView) throws Exception {

        if (modelAndView != null && !modelAndView.isEmpty()) {
            modelAndView.getModelMap().addAttribute("currentUser", request.getSession().getAttribute("currentUser"));
            modelAndView.getModelMap().addAttribute("canDoActions", request.getSession().getAttribute("canDoActions"));
        }

    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Обнуляем последнюю ошибку Spring Secutity
        request.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION", null);
    }


}