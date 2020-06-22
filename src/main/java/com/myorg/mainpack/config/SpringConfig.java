package com.myorg.mainpack.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myorg.mainpack.web.interceptor.SecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import javax.sql.DataSource;
import static com.myorg.mainpack.util.json.JacksonObjectMapper.getMapper;


@Configuration
@EnableWebMvc
@PropertySource("classpath:application.properties")
@ComponentScan("com.myorg.mainpack")
public class SpringConfig implements WebMvcConfigurer {


    public DataSource dataSource;
    public DataSource getDataSource() { return dataSource; }
    @Autowired
    public void setDataSource(DataSource dataSource) { this.dataSource = dataSource; }

    public SecurityInterceptor securityInterceptor;
    public SecurityInterceptor getSecurityInterceptor() { return securityInterceptor; }
    @Autowired
    public void setSecurityInterceptor(SecurityInterceptor securityInterceptor) { this.securityInterceptor = securityInterceptor; }

    ApplicationContext applicationContext;
    public ApplicationContext getApplicationContext() { return applicationContext; }
    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) { this.applicationContext = applicationContext; }




    @Bean
    public DelegatingFilterProxy getDelegatingFilterProxy(){
        DelegatingFilterProxy delegatingFilterProxy = new DelegatingFilterProxy();
        return new DelegatingFilterProxy();
    }


    @Bean
    public CharacterEncodingFilter getCharacterEncodingFilter(){
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        return characterEncodingFilter;
    }


    // DataSourse
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("classpath:db/init_DB.sql")
                .addScript("classpath:db/populateDB.sql")
                .build();
    }


    @Bean
    public DataSourceTransactionManager getTransactionManager(){
        return new DataSourceTransactionManager(dataSource);
    }



    @Bean
    public JdbcTemplate getJdbcTemplate(){
        return new JdbcTemplate(dataSource);
    }


    @Bean
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate(){
        return new NamedParameterJdbcTemplate(dataSource);
    }


    // For Thymeleaf
    /*
     * STEP 1 - Create SpringResourceTemplateResolver
     * */
/*    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        //templateResolver.setPrefix("/WEB-INF/views/templates/");
        templateResolver.setPrefix("classpath:templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }
    *//*
     * STEP 2 - Create SpringTemplateEngine
     * *//*
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }
    *//*
     * STEP 3 - Register ThymeleafViewResolver
     * *//*
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");
        registry.viewResolver(resolver);
    }*/


/*    // For JSP
    @Bean
    public InternalResourceViewResolver setupViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);
        return resolver;
    }*/

    // For Apache Tiles
    @Bean
    public ViewResolver getViewResolver() {
        UrlBasedViewResolver viewResolver = new UrlBasedViewResolver();
        viewResolver.setViewClass(TilesView.class);

        return viewResolver;
    }
    @Bean
    public TilesConfigurer getTilesConfigurer() {
        TilesConfigurer tilesConfigurer = new TilesConfigurer();
        tilesConfigurer.setDefinitions("/WEB-INF/tiles.xml");
        return tilesConfigurer;
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
