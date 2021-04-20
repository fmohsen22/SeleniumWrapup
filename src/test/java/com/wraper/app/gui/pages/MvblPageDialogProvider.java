package com.wraper.app.gui.pages;

import com.wraper.app.gui.pages.easyseleium.SimpleFormDemoPage;
import com.wraper.app.gui.pages.facebook.FacebookLoginPage;
import com.wraper.framework.pageobject.PageObjectProvider;

/*
 * Interface für Bereitstellungsroutinen von PageObjects
 */
public interface MvblPageDialogProvider extends PageObjectProvider {

    FacebookLoginPage facebookLoginPage();

    SimpleFormDemoPage simplFormPage();

}
