package com.wraper.app.gui.elements.base;

import com.wraper.app.gui.pages.base.E2EBasePage;
import com.wraper.app.selenium.E2ESeleniumHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public abstract class GuiBaseElement {


  // Hilfskomponenten
  private E2ESeleniumHelper   seleniumHelper;
  private WebDriver           webDriverProvider;
  private E2EBasePage         ownerPage;

  // Hauptidentifikationselement
  private By                locator;

  protected GuiBaseElement(WebDriver webDriverProvider, By locator, E2EBasePage ownerPage) {
    this.webDriverProvider = webDriverProvider;
    this.seleniumHelper = E2ESeleniumHelper.of(webDriverProvider);
    setLocator(locator);
    setOwnerPage(ownerPage);
    initInstance();
  }

  protected GuiBaseElement(WebDriver webDriverProvider, By locator) {
    this(webDriverProvider, locator, null);
  }

  // es ist manchmal nicht möglich direkt im Constructor den locator zu setzen
  protected GuiBaseElement setLocator(By locator) {
    this.locator = locator;
    return this;
  }

  public GuiBaseElement setOwnerPage(E2EBasePage ownerPage) {
    this.ownerPage = ownerPage;
    return this;
  }

  // @formatter:off
  /**
   * 
   * overwrite for specific initialization operations 
   * Note: 
   * Never call this routine directly, it is called automatically during object instantiation
   * 
   */
  // @formatter:on
  protected void initInstance() {}

  protected E2ESeleniumHelper gui() {
    return seleniumHelper;
  }


  protected E2EBasePage ownerPage() {
    return ownerPage;
  }

  public final By getByLocator() {
    return locator;
  }

  public boolean exists() {
    return (gui().findElementByQuick(getByLocator()) != null);
  }

  public boolean isEnabled() {
    return !gui().isElementDisabled(getByLocator());
  }

  public void waitUntilVisible() {
    gui().waitForVisibility(getByLocator(), E2ESeleniumHelper.TIMEOUT_MEDIUM);
  }

  public void waitUntilEnabled() {
    gui().waitForCondition(() -> isEnabled());
  }

  public void waitUntilReady() {
    // initial implementation, might be necessary to adapt
    waitUntilVisible();
  }

  public void waitUntilInvisible() {
    gui().waitForInvisibility(getByLocator(), E2ESeleniumHelper.TIMEOUT_MEDIUM, 100);
  }

  /**
   * checks element for visibility
   * 
   * @return
   */
  public boolean isVisible() {
    if (!exists()) {
      return false;
    }
    gui().withZeroTimeout();
    boolean visible = gui().isElementVisible(getByLocator());
    gui().withDefaultImplicitTimeout();
    return visible;
  }

  // public void movePointerToElement() {
  // // first move it to the corner, might be that otherwise the event is not triggered
  // gui().movePointerToElement(By.cssSelector("img.header-logo"));
  // gui().movePointerToElement(getByLocator());
  // gui().waitUntilLoadingCompleted(100);
  // }

  /**
   * explicit check for element invisibility
   * 
   * @return
   */
  public boolean isInvisible() {
    if (!exists()) {
      return true;
    }
    gui().withZeroTimeout();
    boolean invisible = gui().isElementInvisible(getByLocator());
    gui().withDefaultImplicitTimeout();
    return invisible;
  }

  public void scrollIntoView() {
    gui().scrollIntoView(getByLocator());
  }

  // basic implementation, assuming it is defined on the main element defined by the byLocator.
  // if this is not the case, override the method
  public String getTooltip() {
    return gui().getTooltip(getByLocator());
  }

  /*
   * basic assertions for all kinds of gui elements
   */
  public void assertTooltip(String tooltip) {
    assertEquals(tooltip, getTooltip());
  }

  public void assertVisible() {
    assertTrue(isVisible());
  }

  public void assertInvisible() {
    assertTrue(isInvisible());
  }

  public void assertEnabled(boolean enabled) {
    if (enabled)
      assertEquals(true, isEnabled());
    else
      assertEquals(false, isEnabled());
  }

  public void clickOnField() {
    gui().clickElement(getByLocator());
  }

}
