package com.wraper.app.gui.elements.dataoutput;

import com.wraper.app.selenium.E2ESeleniumHelper;
import com.wraper.framework.guielement.Textual;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.ByChained;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GuiErrorOutput implements Textual{

        private E2ESeleniumHelper gui;
        private WebDriver driver;

        private final By errTxt;


        private By mainElement;

        protected GuiErrorOutput(WebDriver pWebDriverProvider, By mainElement) {
            this.mainElement = mainElement;
            errTxt = new ByChained(mainElement, By.className("error-button"));
            gui = E2ESeleniumHelper.of(pWebDriverProvider);
            this.driver = pWebDriverProvider;
        }

        public static GuiErrorOutput of(WebDriver pWebDriverProvider, By byElement) {
            return new GuiErrorOutput(pWebDriverProvider, byElement);
        }

        @Override
        public String getText() {
            return driver.findElement(mainElement).getText();
        }

        @Override
        public String getValue() {
            return driver.findElement(mainElement).getText();
        }

        @Override
        public void assertText(String text) {
            assertEquals(text, getText());
        }

        @Override
        public void assertTextCaseInsensitive(String text) {
            assertEquals(text.toUpperCase(), getText().toUpperCase());
        }





}
