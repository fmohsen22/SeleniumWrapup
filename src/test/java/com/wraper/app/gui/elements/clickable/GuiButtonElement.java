package com.wraper.app.gui.elements.clickable;


import com.wraper.app.gui.elements.base.GuiBaseElement;
import com.wraper.framework.guielement.Clickable;
import com.wraper.framework.guielement.Textual;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;


/***
 * Represents a simple Button
 * 
 * html: <button id="mainId" disabled info <span for icon <span for Text .../> />
 * 
 * @author cwitteveen
 *
 */
public class GuiButtonElement extends GuiBaseElement implements Clickable, Textual {

  protected GuiButtonElement(WebDriver pProvider, By byMain) {
    super(pProvider, byMain);
  }

  public static GuiButtonElement of(WebDriver pProvider, By byMain) {
    return new GuiButtonElement(pProvider, byMain);
  }

  @Override
  public void click() {
    gui().clickElement(getByLocator());
  }

  public void clickNoWait() {
    waitUntilClickable();
    gui().findElementBy(getByLocator()).click();
  }

  public void clickWithVeryLongWait() {
    gui().findElementBy(getByLocator()).click();
    gui().waituntilLoadingCompletedVeryLong();
  }

  public void waitUntilClickable() {
    waitUntilVisible();
    gui().waitUntilClickable(() -> gui().findElementBy(getByLocator()));
  }

  @Override
  public boolean isClickable() {
    return gui().isElementClickable(getByLocator());
  }

  @Override
  public String getText() {
    return gui().findElementBy(getByLocator()).getText();
  }

  @Override
  public String getValue() {
    return getText();
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
