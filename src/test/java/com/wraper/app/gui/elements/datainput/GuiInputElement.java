package com.wraper.app.gui.elements.datainput;


import com.wraper.app.selenium.E2ESeleniumHelper;
import com.wraper.framework.guielement.Textual;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.ByChained;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GuiInputElement implements  Textual {
  
  private E2ESeleniumHelper gui;
  private WebDriver driver;

  private final By byInput;
  private final By byLable;

  private By mainElement;

  protected GuiInputElement(WebDriver pWebDriverProvider, By mainElement) {
    this.mainElement = mainElement;
//    byInput = new ByChained( By.tagName("input"),mainElement);
    byInput = new ByChained(mainElement);
    byLable = new ByChained( By.tagName("label"),mainElement);
    gui = E2ESeleniumHelper.of(pWebDriverProvider);
    this.driver = pWebDriverProvider;
  }

  public static GuiInputElement of(WebDriver pWebDriverProvider, By byElement) {
    return new GuiInputElement(pWebDriverProvider, byElement);
  }

  @Override
  public String getText() {
    return gui.getValue(byInput);
  }

  @Override
  public String getValue() {
    return gui.getValue(byInput);
  }

  public By getLable() {
    return byLable;
  }

  public String getLableText(){
    if (getLable()==null){
    return driver.findElement(mainElement).getText();}
    else return driver.findElement(getLable()).getText();
  }

  public void assertLable(String txt){
    assertEquals(txt, getLableText());
  }

  public String getTooltip() {
    return gui.getTooltip(byInput);
  }

//  public void clearText() {
//    gui.clearField(byInput);
//    gui.findElementBy(byInput).clear();
//    gui.waitUntilLoadingCompleted();
//  }

  public void clearText(){
    driver.findElement(byInput).clear();
  }

//  public void enterText(String pText) {
//    clearText();
//    if (pText == null) {
//      gui.tab();
//      gui.waitUntilLoadingCompleted();
//      return;
//    }
//    gui.findElementBy(byInput).sendKeys(pText);
//    SWrapUtil.wait(100);
//    gui.findElementBy(byInput).sendKeys(Keys.TAB);
//    gui.waitUntilLoadingCompleted(250);
//  }

  public void enterText(String pText) {
    clearText();
    driver.findElement(byInput).sendKeys(pText);
  }


  public boolean isEnabled() {
    return !"true".equals(driver.findElement(byInput).getAttribute("disabled"));
  }

  public String getPlaceholder() {
    return driver.findElement(byInput).getAttribute("placeholder");
  }

  public void assertPlaceholder(String placeholder) {
    assertEquals(placeholder, getPlaceholder());
  }

  public void waitForText(String text) {
    gui.waitForCondition(() -> text.equals(getText()));
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
