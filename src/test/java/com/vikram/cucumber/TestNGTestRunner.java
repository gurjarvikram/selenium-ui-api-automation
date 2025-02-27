package com.vikram.cucumber;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/java/com/vikram/cucumber",glue= "com/vikram/stepdefinitions",
monochrome = true, tags= "@Regression", plugin	= {"html:target/cucumber.html"}	)
public class TestNGTestRunner extends AbstractTestNGCucumberTests{

}
