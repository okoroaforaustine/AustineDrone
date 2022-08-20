package com.musala.drones.job;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Slf4j
public class ContextListener implements ServletContextListener {
    @Override
    public void contextDestroyed( ServletContextEvent event) {
        log.info("Callback triggered");
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        log.info("Initialized context");

    }
}
