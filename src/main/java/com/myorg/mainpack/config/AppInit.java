package com.myorg.mainpack.config;

import org.springframework.security.config.BeanIds;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import javax.servlet.*;


// https://qarus.ru/20277883-ispolzovanie-spring-mvc-webapplicationinitializer-applicationcontextinitializer-i-contextloaderlistener/
public class AppInit implements WebApplicationInitializer {

    private static final Class<?>[] configurationClasses = new Class<?>[]{
            SpringConfig.class, SpringWebSecurityConfig.class
    };



    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        // ApplicationContext
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.register(configurationClasses);

        // DispatcherServlet
        DispatcherServlet dispatcherServlet = new DispatcherServlet(appContext);
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("SpringDispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        // UtF8 Charactor Filter.
        FilterRegistration.Dynamic fr = servletContext.addFilter("encodingFilter", CharacterEncodingFilter.class);
        fr.setInitParameter("encoding", "UTF-8");
        fr.setInitParameter("forceEncoding", "true");
        fr.addMappingForUrlPatterns(null, true, "/*");

        // Spring Security Filter
        registerSpringSecurityFilterChain(servletContext);

        // Context Listener
        registerListener(servletContext);

    }


    private void registerSpringSecurityFilterChain(ServletContext servletContext) {
        FilterRegistration.Dynamic springSecurityFilterChain = servletContext.addFilter(
                BeanIds.SPRING_SECURITY_FILTER_CHAIN,
                new DelegatingFilterProxy());
        springSecurityFilterChain.addMappingForUrlPatterns(null, false, "/*");
    }


    private void registerListener(ServletContext servletContext) {
        WebApplicationContext rootContext = createContext(configurationClasses);
        servletContext.addListener(new ContextLoaderListener(rootContext));
//        servletContext.addListener(new RequestContextListener());
    }


    private WebApplicationContext createContext(final Class<?>... annotatedClasses) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(annotatedClasses);
//        context.refresh();
        return context;
    }


}
