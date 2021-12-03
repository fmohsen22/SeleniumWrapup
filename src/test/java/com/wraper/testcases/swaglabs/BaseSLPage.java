package com.wraper.testcases.swaglabs;

import com.wraper.app.gui.pages.Suacedemo.SauceDemoPage;
import com.wraper.testcases.BaseTest;

public class BaseSLPage  extends BaseTest {

    public SauceDemoPage sauceDemoPage;

    public BaseSLPage() {
        driver.get("https://www.saucedemo.com/");

    }

    @Override
    public void initPages(){
        super.initPages();
        sauceDemoPage = dialogFactory.soucePage();

    }

}
