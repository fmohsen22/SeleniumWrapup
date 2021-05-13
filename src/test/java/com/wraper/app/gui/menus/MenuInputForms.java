package com.wraper.app.gui.menus;

import com.wraper.app.gui.elements.clickable.GuiLinkElement;
import com.wraper.app.gui.elements.menu.GuiMenuItemElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MenuInputForms extends GuiMenuItemElement {

    protected By              linkOpen = By.cssSelector("#treemenu > li > ul > li:nth-child(1) > i");

    public GuiLinkElement   lnksOpen;
    public GuiLinkElement   lnkSimpleForm;
    public GuiLinkElement   lnkCheckbox;
    public GuiLinkElement   lnkRadioButton;
    public GuiLinkElement   lnkSelectDropDown;
    public GuiLinkElement   lnkInputFormSubmit;
    public GuiLinkElement   lnkAjaxFormSubmit;
    public GuiLinkElement   lnkJquerySelectDropDown;

    protected MenuInputForms(WebDriver pWebDriverProvider, By mainElement) {
        super(pWebDriverProvider, mainElement);

        lnksOpen = GuiLinkElement.of(pWebDriverProvider, By.cssSelector("#treemenu > li > ul > li:nth-child(1) > i"));

        lnkSimpleForm = GuiLinkElement.of(pWebDriverProvider,
                By.cssSelector("#treemenu > li > ul > li:nth-child(1) > ul > li:nth-child(1) > a") );

        lnkCheckbox =GuiLinkElement.of(pWebDriverProvider,
                By.cssSelector("#treemenu > li > ul > li:nth-child(1) > ul > li:nth-child(2)> a") );

        lnkRadioButton =GuiLinkElement.of(pWebDriverProvider,
                By.cssSelector("#treemenu > li > ul > li:nth-child(1) > ul > li:nth-child(3)") );

        lnkSelectDropDown =GuiLinkElement.of(pWebDriverProvider,
                By.cssSelector("#treemenu > li > ul > li:nth-child(1) > ul > li:nth-child(4)") );

        lnkInputFormSubmit =GuiLinkElement.of(pWebDriverProvider,
                By.cssSelector("#treemenu > li > ul > li:nth-child(1) > ul > li:nth-child(5)") );

        lnkAjaxFormSubmit =GuiLinkElement.of(pWebDriverProvider,
                By.cssSelector("#treemenu > li > ul > li:nth-child(1) > ul > li:nth-child(6)") );

        lnkJquerySelectDropDown =GuiLinkElement.of(pWebDriverProvider,
                By.cssSelector("#treemenu > li > ul > li:nth-child(1) > ul > li:nth-child(7)") );


    }

    public static MenuInputForms of(WebDriver pWebDriverProvider, By mainElement){
        return new MenuInputForms(pWebDriverProvider, mainElement);
    }

    public void openMneu(){
        lnksOpen.click();
    }

    public void clickSimpleFormMenu(){
        if (!isOpen(linkOpen)) openMneu();
        lnkSimpleForm.click();
    }

    public void clickCheckboxMenu(){
        if (!isOpen(linkOpen)) openMneu();
        lnkCheckbox.click();
    }

    public void clickRadioButtonMenu(){
        if (!isOpen(linkOpen)) openMneu();
        lnkRadioButton.click();
    }

    public void clickSelectDropDownMenu(){
        if (!isOpen(linkOpen)) openMneu();
        lnkSelectDropDown.click();
    }

    public void clickInputFormSubmitMenu(){
        if (!isOpen(linkOpen)) openMneu();
        lnkInputFormSubmit.click();
    }

    public void clickAjaxFormSubmitMenu(){
        if (!isOpen(linkOpen)) openMneu();
        lnkAjaxFormSubmit.click();
    }

    public void clickJquerySelectDropDownMenu(){
        if (!isOpen(linkOpen)) openMneu();
        lnkJquerySelectDropDown.click();
    }





}
