package com.myorg.mainpack.config;

import com.myorg.mainpack.service.UserService;
import com.myorg.mainpack.util.auth.MyAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${passwordEncoderStrength}")
    private int passwordEncoderStrength;

    @Autowired
    @Qualifier("myAuthenticationProvider")
    AuthenticationProvider authenticationProvider;

    @Autowired
    private UserService userService;



    @Bean
    public AuthenticationProvider getAuthenticationProvider(){
        return new MyAuthenticationProvider();
    }


    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(passwordEncoderStrength);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/login", "/static/**", "/users/registration", "/rest/users").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")           // Если надо стандартную форму, то просто закомментировать это присвоение
                .defaultSuccessUrl("/users/balance")
                .permitAll()
                .and()
                .rememberMe()
                .and()
                .logout()
                .permitAll();

        //http.csrf().disable();
        // Отключение _csrf для REST запросов
        http.csrf().ignoringAntMatchers("/rest/**");

    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(this.getPasswordEncoder());
                //.passwordEncoder(NoOpPasswordEncoder.getInstance());
    }


}