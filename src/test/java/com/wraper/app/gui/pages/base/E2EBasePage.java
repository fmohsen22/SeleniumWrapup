package com.wraper.app.gui.pages.base;

import com.wraper.app.gui.pages.DialogFactory;
import com.wraper.app.selenium.E2ESeleniumHelper;
import org.openqa.selenium.WebDriver;

public abstract class E2EBasePage  {

  protected WebDriver driverProvider;
  private E2ESeleniumHelper   selenium;
  private DialogFactory       dialogs;

  public E2EBasePage(WebDriver driverProvider) {
    this.driverProvider = driverProvider;
    this.selenium = E2ESeleniumHelper.of(driverProvider);
    this.dialogs = DialogFactory.of(driverProvider);
  }

  public E2ESeleniumHelper gui() {
    return selenium;
  }

  protected DialogFactory dialogs() {
    return dialogs;
  }


  public abstract <T extends E2EBasePage> T waitUntilReady();

  public abstract void assertPageIsShown();

  public abstract void waitUntilVisible();

  public abstract boolean isVisible();
}
