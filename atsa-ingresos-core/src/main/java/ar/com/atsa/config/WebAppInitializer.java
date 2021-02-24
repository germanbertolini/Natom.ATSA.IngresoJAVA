package ar.com.atsa.config;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.sun.jersey.spi.spring.container.servlet.SpringServlet;


public class WebAppInitializer implements WebApplicationInitializer {

  private static Logger LOG = LoggerFactory.getLogger(WebAppInitializer.class);

    @Override
    public void onStartup(ServletContext servletContext) {
        // Create the 'root' Spring application context
       /* AnnotationConfigWebApplicationContext rootContext =
                new AnnotationConfigWebApplicationContext();
        rootContext.register(AppConfiguration.class);

        // Manage the lifecycle of the root application context
        container.addListener(new ContextLoaderListener(rootContext));

        // Create the dispatcher servlet's Spring application context
        AnnotationConfigWebApplicationContext dispatcherContext =
                new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(AppConfiguration.class);

        // Register and map the dispatcher servlet
        ServletRegistration.Dynamic dispatcher =
                container.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");*/


        WebApplicationContext rootContext = createRootContext(servletContext);
        configureJerseyServlet(servletContext);
        configureSpringSecurity(servletContext, rootContext);
        configureOpenEntityManagerInViewFilter(servletContext);
    }

    
    private WebApplicationContext createRootContext(ServletContext servletContext) {
	
    	AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
	    rootContext.register(ServiceConfiguration.class, SecurityConfiguration.class, JPAConfiguration.class, CachingConfiguration.class);
	
	    rootContext.refresh();
	
	    servletContext.addListener(new ContextLoaderListener(rootContext));
	    servletContext.setInitParameter("defaultHtmlEscape", "true");
	    rootContext.scan("ar.com.atsa");
	    
	    return rootContext;
    
    }

    
    private void configureJerseyServlet(ServletContext servletContext) {
    
    	SpringServlet jerseyServlet = new SpringServlet();

        ServletRegistration.Dynamic appServlet = servletContext.addServlet("JerseyServlet", jerseyServlet);
        appServlet.setInitParameter("com.sun.jersey.config.property.packages", "com.fasterxml.jackson.jaxrs.json");
        appServlet.setLoadOnStartup(1);

        appServlet.addMapping("/rest/*");
        
    }

    private void configureSpringSecurity(ServletContext servletContext, WebApplicationContext rootContext) {
    
    	FilterRegistration.Dynamic springSecurity = servletContext.addFilter("springSecurityFilterChain",
	    new DelegatingFilterProxy("springSecurityFilterChain", rootContext));
	    
    	springSecurity.addMappingForUrlPatterns(null, true, "/*");
	}
    
    private void configureOpenEntityManagerInViewFilter(ServletContext servletContext) {
        
    	FilterRegistration.Dynamic openEntityManagerInView = servletContext.addFilter("OpenEntityManagerInViewFilter",
	    new OpenEntityManagerInViewFilter());
	    
    	openEntityManagerInView.setInitParameter("entityManagerFactoryBeanName", "entityManagerFactory");
    	
    	openEntityManagerInView.addMappingForUrlPatterns(null, true, "/*");
	}
    
}
