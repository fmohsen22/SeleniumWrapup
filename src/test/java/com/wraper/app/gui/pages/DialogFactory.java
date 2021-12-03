package com.wraper.app.gui.pages;


import com.wraper.app.gui.menus.MainMenu;
import com.wraper.app.gui.pages.Suacedemo.SauceDemoPage;
import com.wraper.app.gui.pages.easyseleium.SimpleFormDemoPage;
import com.wraper.app.gui.pages.facebook.FacebookLoginPage;
import com.wraper.framework.logger.MyLogger;
import org.openqa.selenium.WebDriver;


public class DialogFactory implements PageDialogProvider {


  private static DialogFactory                       theDialogFactory  = null;

  private WebDriver                                  webdriverProvider = null;

  //instatiate page objects
  public FacebookLoginPage      facebookLoginPage;
  public SimpleFormDemoPage     simpleFormDemoPage;
  public MainMenu               menu;
  public SauceDemoPage          soucePage;


  // block standard constructor from being used
  private DialogFactory() {}

  private DialogFactory(WebDriver webdriverProvider) {
    this.webdriverProvider = webdriverProvider;
  }

  public static DialogFactory of(WebDriver webdriverProvider) {
    if (theDialogFactory == null) {
      MyLogger.resInfoLog(DialogFactory.class,"Anlage DialogFactory-Singleton");
      theDialogFactory = new DialogFactory(webdriverProvider);
    }
    return theDialogFactory;
  }

  public static void reset() {
    theDialogFactory = null;
  }

  //-------> page objs

  @Override
  public FacebookLoginPage facebookLoginPage(){
    if (facebookLoginPage ==null){
      facebookLoginPage = FacebookLoginPage.of(webdriverProvider);
    }
    return facebookLoginPage;
  }

  @Override
  public SimpleFormDemoPage simplFormPage() {
    if (simpleFormDemoPage ==null){
      simpleFormDemoPage = SimpleFormDemoPage.of(webdriverProvider);
    }
    return simpleFormDemoPage;
  }

  @Override
  public MainMenu mainMenu() {
    if (menu ==null){
      menu = MainMenu.of(webdriverProvider);
    }
    return menu;
  }

  @Override
  public SauceDemoPage soucePage() {
    if (soucePage ==null){
      soucePage = SauceDemoPage.of(webdriverProvider);
    }
    return soucePage;
  }


}
