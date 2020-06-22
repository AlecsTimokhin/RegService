package com.myorg.mainpack.util.auth;

import com.myorg.mainpack.model.Role;
import com.myorg.mainpack.model.User;
import com.myorg.mainpack.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;


@Component
public class MyAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    UserService userService;



    public MyAuthenticationProvider(){}



    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

         User user = (User) userService.loadUserByUsername(username);

        if(user == null) {
            throw new BadCredentialsException("Пользователь не существует! Код ошибки: 3");
        }

        if(!password.equals(user.getPassword())) {
            throw new BadCredentialsException("Логин или пароль неверные! Код ошибки: 4");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        for( Role role : user.getRoles() ){
            authorities.add( new SimpleGrantedAuthority( role.toString() ) );
        }

        return new UsernamePasswordAuthenticationToken(user, password, authorities);

    }


    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(UsernamePasswordAuthenticationToken.class);
    }


}