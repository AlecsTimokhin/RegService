package com.myorg.mainpack.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myorg.mainpack.web.interceptor.SecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

import javax.sql.DataSource;

import static com.myorg.mainpack.util.json.JacksonObjectMapper.getMapper;


@Configuration
public class SpringConfig implements WebMvcConfigurer {


    public DataSource dataSource;
    public DataSource getDataSource() { return dataSource; }
    @Autowired
    public void setDataSource(DataSource dataSource) { this.dataSource = dataSource; }

    public SecurityInterceptor securityInterceptor;
    public SecurityInterceptor getSecurityInterceptor() { return securityInterceptor; }
    @Autowired
    public void setSecurityInterceptor(SecurityInterceptor securityInterceptor) { this.securityInterceptor = securityInterceptor; }



    @Bean
    public JdbcTemplate getJdbcTemplate(){
        return new JdbcTemplate(dataSource);
    }


    // For Thymeleaf
    @Bean
    public SpringResourceTemplateResolver thymeleafTemplateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("classpath:templates/");
        templateResolver.setSuffix(".html");
        //templateResolver.setTemplateMode("HTML5");
        return templateResolver;
    }


    @Bean
    public ObjectMapper getJacksonObjectMapper() {
        return getMapper();
    }


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //registry.addViewController("/login").setViewName("users/login");  // Уже настроено в UserController
    }


/*    @Override
    public void customize( ConfigurableEmbeddedServletContainer container ) {
        container.addErrorPages(new ErrorPage( HttpStatus.NOT_FOUND, "/badRequest" ));
    }*/


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(securityInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/static/**");
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

    }


}
