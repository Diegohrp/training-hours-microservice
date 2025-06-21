package com.diegohrp.traininghoursservice.component;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/component",
        glue = "com.diegohrp.traininghoursservice.component.steps")
public class ComponentTestRunner {
}
