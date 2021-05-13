package com.wraper.app.gui.elements.menu;

import com.wraper.app.gui.elements.clickable.GuiLinkElement;
import com.wraper.app.selenium.E2ESeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class GuiMenuItemElement extends GuiLinkElement {

    private E2ESeleniumHelper gui;
    private WebDriver driver;


    private By mainElement;

    protected GuiMenuItemElement(WebDriver pWebDriverProvider, By mainElement) {
        super(pWebDriverProvider,mainElement);
        this.mainElement = mainElement;
        gui = E2ESeleniumHelper.of(pWebDriverProvider);
        this.driver = pWebDriverProvider;


    }

    public static GuiMenuItemElement of(WebDriver pWebDriverProvider, By byElement) {
        return new GuiMenuItemElement(pWebDriverProvider, byElement);
    }


    public boolean isOpen(By element){
        return gui().findElementBy(element).getAttribute("class").contains("down");
    }

}
