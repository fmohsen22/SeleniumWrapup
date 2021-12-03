package com.wraper.app.gui.pages.Suacedemo;

import com.wraper.app.gui.elements.clickable.GuiButtonElement;
import com.wraper.app.gui.elements.datainput.GuiInputElement;
import com.wraper.app.gui.elements.dataoutput.GuiErrorOutput;
import com.wraper.app.gui.elements.dataoutput.GuiSimpleOutput;
import com.wraper.app.gui.pages.base.E2EBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.ByChained;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SauceDemoPage extends E2EBasePage {
    public By byContainer = By.id("root");

    //Page Elements

    public GuiSimpleOutput logo;
    public GuiInputElement userName;
    public GuiInputElement pass;
    public GuiErrorOutput errorMsg;
    public GuiButtonElement logIn;


    public SauceDemoPage(WebDriver driverProvider) {
        super(driverProvider);

        logo = GuiSimpleOutput.of(driverProvider, new ByChained(byContainer,By.className("login_logo")));
        userName = GuiInputElement.of(driverProvider,new ByChained(By.className("form_group"), By.name("user-name")));
        pass = GuiInputElement.of(driverProvider,new ByChained(By.className("form_group"), By.name("password")));
        errorMsg =  GuiErrorOutput.of(driverProvider,By.cssSelector("#login_button_container > div > form > div.error-message-container.error > h3"));
        logIn = GuiButtonElement.of(driverProvider,By.id("login-button"));
    }

    public static SauceDemoPage of(WebDriver driverProvider){return new SauceDemoPage(driverProvider); }


    @Override
    public < T extends E2EBasePage > T waitUntilReady() {
        return null;
    }

    @Override
    public void assertPageIsShown() {
        assertTrue(isVisible());
    }

    @Override
    public void waitUntilVisible() {
        gui().waitForCondition(()-> isVisible());
    }

    @Override
    public boolean isVisible() {
        return userName.isEnabled();
    }
}
