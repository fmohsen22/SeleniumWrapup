package com.wraper.app.gui.elements.clickable;

import com.wraper.app.gui.elements.base.GuiBaseElement;
import com.wraper.framework.guielement.Clickable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Describes a simple Link Element, that has an id (oMainId)
 * 
 * html: <a id="mainId">linkText</a>
 * 
 * @author cwitteveen
 *
 */
public class GuiLinkElement extends GuiBaseElement implements Clickable {

  protected GuiLinkElement(WebDriver pWebDriverProvider, By pBy) {
    super(pWebDriverProvider, pBy);
  }

  public static GuiLinkElement of(WebDriver pWebDriverProvider, By pBy) {
    return new GuiLinkElement(pWebDriverProvider, pBy);
  }

  public GuiLinkElement withWaitOnClick(boolean doWaitOnClick) {
    return this;
  }

  @Override
  public void click() {
    waitUntilClickable();
    click(true);
    gui().withMaximumTimeout();
    gui().waitUntilLoadingCompleted();
    gui().withDefaultImplicitTimeout();
  }

  /**
   * scrollIntoView as used in click() can also result in obscurint the element.
   */
  public void clickNoScroll() {
    click(false);
  }

  private void click(boolean withScroll) {
    gui().clickElement(getByLocator());
  }

  public String getLinkReference() {
    return gui().findElementBy(getByLocator()).getAttribute("href");
  }

  public String getText() {
    return gui().findElementBy(getByLocator()).getText();
  }

  public void assertText(String text) {
    assertEquals(text, getText());
  }

  @Override
  public boolean isClickable() {
    return gui().isElementClickable(getByLocator());
  }

  /**
   * checks element for active state (presumable only valid for menu items) (active means that the element is currently selected, not that it has
   * focus)
   * 
   * @return true - element is active, false - element is not active
   */
  public boolean isActive() {
    return gui().getAttribute(gui().findElementBy(getByLocator()), "class").contains("active");
  }



  private void waitUntilClickable() {
    gui().waitForCondition(() -> isClickable());
  }

}
