package com.fancode.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CucumberHooks {
    private static final Logger logger = LogManager.getLogger(CucumberHooks.class);

    @Before
    public void beforeScenario(Scenario scenario) {
        logger.info("Starting scenario: {}", scenario.getName());
    }

    @After
    public void afterScenario(Scenario scenario) {
        logger.info("Completed scenario: {} - Status: {}", scenario.getName(), scenario.getStatus());
    }
}