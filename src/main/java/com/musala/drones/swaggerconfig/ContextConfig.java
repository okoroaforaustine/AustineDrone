package com.musala.drones.swaggerconfig;

import com.musala.drones.job.ContextListener;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContextListener;

@Configuration
public class ContextConfig {
    @Bean
    ServletListenerRegistrationBean<ServletContextListener> servletListener() {
        ServletListenerRegistrationBean<ServletContextListener> srb
                = new ServletListenerRegistrationBean<>();
        srb.setListener(new ContextListener());
        return srb;
    }
}
