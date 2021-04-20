package com.wraper.app.gui.elements.dataoutput;

import com.wraper.app.gui.elements.base.GuiBaseElement;
import com.wraper.app.selenium.E2ESeleniumHelper;
import com.wraper.framework.guielement.Textual;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GuiSimpleElement extends GuiBaseElement implements Textual {
  private E2ESeleniumHelper gui;
  private WebDriver driver;
  protected GuiSimpleElement(WebDriver pWebDriverProvider, By element) {
    super(pWebDriverProvider,element);
    gui = E2ESeleniumHelper.of(pWebDriverProvider);
    this.driver = pWebDriverProvider;
  }

  public static GuiSimpleElement of(WebDriver pWebDriverProvider, By element) {
    return new GuiSimpleElement(pWebDriverProvider, element);
  }

  @Override
  public String getText() {
    return driver.findElement(getByLocator()).getText();
  }

  @Override
  public String getValue() {
    return getText();
  }

  public void assertText(String text) {
    assertEquals(text, getText());
  }

  public void assertTextCaseInsensitive(String text) {
    assertEquals(text.toLowerCase(), getText().toLowerCase());
  }


}
