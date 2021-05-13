package com.wraper.app.gui.menus;

import com.wraper.app.gui.elements.menu.GuiMenuItemElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ByChained;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends GuiMenuItemElement {

    public static By        mainElement = By.cssSelector("#treemenu > li");
    public MenuInputForms   mnInputForms;


    protected MainMenu(WebDriver pWebDriverProvider, By mainElement) {
        super(pWebDriverProvider,mainElement);
        mnInputForms = MenuInputForms.of(pWebDriverProvider, By.cssSelector("#treemenu > li > ul > li:nth-child(1) > a"));
    }

    public static MainMenu of(WebDriver pWebDriverProvider){
        return new MainMenu(pWebDriverProvider,mainElement);
    }

    /**
     * return the menu items as WebElements and also as List of Strings
     * @return
     */
    public List<WebElement> getElementsList(){
        List<WebElement> allElements = gui().findElementsBy(new ByChained(getByLocator(),By.cssSelector("#treemenu > li > ul > li")));
        return allElements;
    }

    public List<String> getMenuList(){
        List<String> stringList = new ArrayList<>();
        for(WebElement element:getElementsList()){
            stringList.add(element.getText());
        }
        return stringList;
    }

    public void clickInputFormMenu(){
        if (!isOpen(mnInputForms.linkOpen))
        mnInputForms.click();
    }






}
