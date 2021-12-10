package com.wraper.testcases.swaglabs;

import com.wraper.framework.logger.MyLogger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogInTest extends BaseSLPage{
    String user=null;
    String pass = null;

    @Test
    public void  testSoucePgDemo(){

        testActivity("Check the Log In Page");
        testSteps(1);
        sauceDemoPage.waitUntilVisible();
        user="xxx";
        pass = "123";
        sauceDemoPage.userName.enterText(user);
        sauceDemoPage.pass.enterText(pass);
        sauceDemoPage.logIn.click();
        sauceDemoPage.errorMsg.assertText("Epic sadface: Username and password do not match any user in this service");

        testActivity("correct data as user and pass");
        testSteps(2);
        user="standard_user";
        pass = "secret_sauce";
        sauceDemoPage.userName.enterText(user);
        sauceDemoPage.pass.enterText(pass);
        sauceDemoPage.logIn.click();

        assertEquals("https://www.saucedemo.com/inventory.html",driver.getCurrentUrl());
        MyLogger.InfoLog(LogInTest.class,"user : '"+user+"' and pass : '"+pass+"' are successfully accepted and page: 'https://www.saucedemo.com/inventory.html' openned.");

    }
}
