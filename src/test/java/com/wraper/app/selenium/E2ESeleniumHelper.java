package com.wraper.app.selenium;import com.wraper.app.base.SWrapUtil;import com.wraper.app.gui.elements.base.GuiBaseElement;import com.wraper.framework.logger.MyLogger;import com.wraper.framework.tool.JsReader;import org.apache.commons.lang3.StringUtils;import org.openqa.selenium.*;import org.openqa.selenium.interactions.Actions;import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;import org.openqa.selenium.support.pagefactory.ByChained;import org.openqa.selenium.support.ui.ExpectedCondition;import org.openqa.selenium.support.ui.ExpectedConditions;import org.openqa.selenium.support.ui.Wait;import org.openqa.selenium.support.ui.WebDriverWait;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import java.text.MessageFormat;import java.util.LinkedList;import java.util.List;import java.util.concurrent.TimeUnit;import java.util.function.BooleanSupplier;import java.util.function.Supplier;import static org.junit.jupiter.api.Assertions.assertEquals;public class E2ESeleniumHelper {    private static final Logger logger                       = LoggerFactory.getLogger(E2ESeleniumHelper.class);    public final static int     TIMEOUT_SHORT                = 1;    public final static int     TIMEOUT_MEDIUM               = 3;    public final static int     TIMEOUT_LONG                 = 10;    public final static int     TIMEOUT_VERYLONG             = 60;    public final static int     TIMEOUT_MAXIMUM              = 300;    public final static int     DEFAULT_IMPLICIT_WAIT_IN_S   = TIMEOUT_MEDIUM;    // private static int zeroTimeout = 0;    private static int          shortTimeout                 = TIMEOUT_SHORT;    private static int          mediumTimeout                = TIMEOUT_MEDIUM;    private static int          longTimeout                  = TIMEOUT_LONG;    private int                 defaultSleepIntervalInMillis = 50;    private Actions             action;    private final By LoadingIndicator             = By.name("loading-point");    private WebDriver  webDriverProvider;    public static E2ESeleniumHelper of(WebDriver driverProvider) {        return new E2ESeleniumHelper(driverProvider);    }    private E2ESeleniumHelper(WebDriver driverProvider) {        this.webDriverProvider = driverProvider;        action = new Actions(webDriverProvider);    }    private WebDriver getDriver() {        return webDriverProvider;    }    private E2ESeleniumHelper withTimeout(int timeout) {        getDriver().manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);        return this;    }    public E2ESeleniumHelper withLongTimeout() {        return withTimeout(longTimeout);    }    public E2ESeleniumHelper withMediumTimeout() {        return withTimeout(mediumTimeout);    }    public E2ESeleniumHelper withShortTimeout() {        return withTimeout(shortTimeout);    }    public E2ESeleniumHelper withMinimumTimeout() {        getDriver().manage().timeouts().implicitlyWait(250, TimeUnit.MILLISECONDS);        return this;    }    public E2ESeleniumHelper withZeroTimeout() {        getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);        return this;    }    public E2ESeleniumHelper withVeryShortTimeout() {        getDriver().manage().timeouts().implicitlyWait(50, TimeUnit.MILLISECONDS);        return this;    }    public E2ESeleniumHelper withMaximumTimeout() {        getDriver().manage().timeouts().implicitlyWait(TIMEOUT_MAXIMUM, TimeUnit.SECONDS);        return this;    }    public E2ESeleniumHelper withDefaultImplicitTimeout() {        getDriver().manage().timeouts().implicitlyWait(DEFAULT_IMPLICIT_WAIT_IN_S, TimeUnit.SECONDS);        return this;    }    /**     * returns locator part of selenium's By-element     *     * @param by     * @return locator as String     */    public static String getLocator(By by) {        String selector = by.toString();        return selector.substring(selector.lastIndexOf(": ") + 2);    }    public String getAttribute(WebElement pElement, String pAttribute) {        return pElement.getAttribute(pAttribute);    }    // // debug helper method for DOM node inspection    // public String getAllAttributesOfElement(WebElement element) {    // JavascriptExecutor executor = (JavascriptExecutor) getDriver();    // Object aa = executor.executeScript(    // "var items = {}; for (index = 0; index < arguments[0].attributes.length; ++index) { items[arguments[0].attributes[index].name] =    // arguments[0].attributes[index].value }; return items;",    // element);    // return aa.toString();    // }    /**     * wait until loading point in the footer is not visible any more     */    public void waitUntilLoadingCompleted() {        waitUntilInvisibleLong(LoadingIndicator);        // currently methods in guiSeleniumHelper do not work        // guiSeleniumHelper.waitUntilLoadingCompleted();        return;        // long start = System.currentTimeMillis();        // // waitUntilAngularIsReady(); -- funktioniert nicht in firefox        // // guiSeleniumHelper.waitForAngularRequestsToFinish();        // guiSeleniumHelper.waitUntilLoadingCompleted();        // long end = System.currentTimeMillis();        // logger        // .info("..." + counter++ + "................................................WaitUntilLoadingCompleted :" + (end - start));        // guiSeleniumHelper.waitUntilAnimationReady();    }    public void waitUntilLoadingCompletedLong() {        waitUntilInvisibleLong(LoadingIndicator);    }    public void waituntilLoadingCompletedVeryLong() {        waitUntilInvisibleVeryLong(LoadingIndicator);    }    public boolean waitUntilInvisibleLong(By by) {        WebDriverWait wait = new WebDriverWait(webDriverProvider, TIMEOUT_VERYLONG, 250);        return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));    }    public boolean waitUntilInvisibleVeryLong(By by) {        WebDriverWait wait = new WebDriverWait(webDriverProvider, TIMEOUT_MAXIMUM, 500);        return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));    }    /**     * same as waitUntilLoadingCompleted, but with additional waiting time before continuation afterwards     *     * @param continuationDelay waiting period in milliseconds     */    public void waitUntilLoadingCompleted(int continuationDelay) {        waitUntilLoadingCompleted();        if (continuationDelay > 0)            SWrapUtil.wait(continuationDelay);    }    public WebElement getParentElement(By byElement) {        WebElement webElement = findElementBy(byElement);        return webElement.findElement(By.xpath("..")); // or ./..    }    public String getValue(By byElement) {        WebElement webElement = findElementByQuick(byElement);        String value = webElement.getAttribute("value");        return value == null ? "" : value;    }    /*     * Warteroutinen     */    public void waitForCondition(BooleanSupplier pSupplier) {        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {            @Override            public Boolean apply(WebDriver driver) {                return pSupplier.getAsBoolean();            }        };        Wait<WebDriver> wait = new WebDriverWait(getDriver(), TIMEOUT_LONG, 250);        try {            wait.until(expectation);        } catch (Throwable error) {            MyLogger.ErrorLog(E2ESeleniumHelper.class,"Erwartete Bedingung nicht eingetreten: " + error.getMessage());            assertEquals(false, true);        }    }    public void waitForConditionNoException(BooleanSupplier supplier) {        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {            @Override            public Boolean apply(WebDriver driver) {                return supplier.getAsBoolean();            }        };        Wait<WebDriver> wait = new WebDriverWait(getDriver(), 5);        try {            wait.until(expectation);        } catch (Throwable error) {            MyLogger.ErrorLog(E2ESeleniumHelper.class,"Erwartete Bedingung nicht eingetreten: " + error.getMessage());        }    }    public void waitForConditionWithParameter(BooleanSupplier pSupplier, long waitPeriod, long interval) {        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {            @Override            public Boolean apply(WebDriver driver) {                return pSupplier.getAsBoolean();            }        };        Wait<WebDriver> wait = new WebDriverWait(getDriver(), waitPeriod, interval);        try {            wait.until(expectation);        } catch (Throwable error) {            assertEquals("Timeout waiting for condition.", true);        }    }    public void waitForConditionLong(BooleanSupplier pSupplier) {        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {            @Override            public Boolean apply(WebDriver driver) {                return pSupplier.getAsBoolean();            }        };        Wait<WebDriver> wait = new WebDriverWait(getDriver(), 20);        long start = System.currentTimeMillis();        try {            wait.until(expectation);            MyLogger.resInfoLog(E2ESeleniumHelper.class,"Waited for " + (System.currentTimeMillis() - start) + " ms");        } catch (Throwable error) {            MyLogger.resInfoLog(E2ESeleniumHelper.class,"Erwartete Bedingung nach " + (System.currentTimeMillis() - start) + " ms Wartezeit nicht eingetreten: " + error.getMessage());            assertEquals(true, true);        }    }    public void waitForConditionVeryLong(BooleanSupplier pSupplier) {        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {            @Override            public Boolean apply(WebDriver driver) {                return pSupplier.getAsBoolean();            }        };        Wait<WebDriver> wait = new WebDriverWait(getDriver(), 40);        try {            wait.until(expectation);        } catch (Throwable error) {            assertEquals("Timeout waiting for condition.", true);        }    }    public void waitUntilClickable(Supplier<WebElement> supplier) {        int nrOfAttempts = 3;        while (nrOfAttempts > 0) {            try {                WebDriverWait wait = new WebDriverWait(getDriver(), TIMEOUT_MEDIUM);                wait.until(ExpectedConditions.and(ExpectedConditions.visibilityOf(supplier.get()), ExpectedConditions.elementToBeClickable(supplier.get())));                return;            } catch (StaleElementReferenceException e) {                nrOfAttempts--;                MyLogger.resInfoLog(E2ESeleniumHelper.class,"Exception: " + e.getMessage() + " bei Warten auf Sichtbarkeit und Klickbarkeit");            }        }    }    public void waitUntilClickable(By element) {        int nrOfAttempts = 3;        WebDriverWait wait = new WebDriverWait(getDriver(), TIMEOUT_MEDIUM);        while (nrOfAttempts > 0) {            try {                wait.until(ExpectedConditions.and(ExpectedConditions.visibilityOf(findElementByQuick(element)),                        ExpectedConditions.elementToBeClickable(findElementByQuick(element))));                return;            } catch (StaleElementReferenceException e) {                nrOfAttempts--;                MyLogger.resInfoLog(E2ESeleniumHelper.class,"Warte auf Klickbarkeit, Element '{}' noch nicht stabil");            }        }    }    public WebElement waitForVisibility(By locator) {        // start time measurement        long startOperation = System.currentTimeMillis();        long endOperation;        WebElement element = null;        try {            element =                    new WebDriverWait(getDriver(), TIMEOUT_MEDIUM, defaultSleepIntervalInMillis).until(ExpectedConditions.visibilityOfElementLocated(locator));            endOperation = System.currentTimeMillis();            MyLogger.resInfoLog(E2ESeleniumHelper.class,String.format("Gesamtzeit der erfolgreichen Sichtbarkeitsprüfung für '{}': {} ms", locator, endOperation - startOperation));        } catch (Exception e) {            endOperation = System.currentTimeMillis();            MyLogger.resInfoLog(E2ESeleniumHelper.class,String.format("Gesamtzeit der NICHT erfolgreichen Sichtbarkeitsprüfung für '{}': {} ms", locator, endOperation - startOperation));            logger.error("Fehler beim Warten auf Element-Sichtbarkeit: " + e.getMessage());            assertEquals(true, element != null);            return element;        }        if (element == null || !element.isEnabled()) {            logger.error("Erwartetes Element '{}' nicht sichtbar!", locator);        }        return element;    }    public E2ESeleniumHelper waitForInvisibility(By locator) {        // start time measurement        long startOperation = System.currentTimeMillis();        long endOperation;        boolean isInvisible;        try {            isInvisible = new WebDriverWait(getDriver(), TIMEOUT_SHORT, 250).until(ExpectedConditions.invisibilityOfElementLocated(locator));            endOperation = System.currentTimeMillis();            MyLogger.resInfoLog(E2ESeleniumHelper.class,String.format("Gesamtzeit der erfolgreichen Unsichtbarkeitsprüfung für '{}': {} ms", locator, endOperation - startOperation));        } catch (Exception e) {            endOperation = System.currentTimeMillis();            MyLogger.resInfoLog(E2ESeleniumHelper.class,String.format("Gesamtzeit der NICHT erfolgreichen Unsichtbarkeitsprüfung für '{}': {} ms", locator, endOperation - startOperation));            logger.error("Fehler beim Warten auf Element-Unsichtbarkeit: " + e.getMessage());            return this;        }        MyLogger.resInfoLog(E2ESeleniumHelper.class,String.format("Status der Unsichtbarkeitsprüfung: {}", isInvisible));        return this;    }    /**     * throws error when still visible after wait period     *     * @param locator     * @param waitInSeconds     * @param sleepInMillis     * @return     */    public E2ESeleniumHelper waitForInvisibility(By locator, int waitInSeconds, int sleepInMillis) {        (new WebDriverWait(getDriver(), waitInSeconds, sleepInMillis)).until(ExpectedConditions.invisibilityOfElementLocated(locator));        return this;    }    public E2ESeleniumHelper waitForInvisibilityShort(By locator) {        return waitForInvisibility(locator, shortTimeout, 20);    }    // public E2ESeleniumHelper waitForInvisibilityMedium(By locator) {    // return waitForInvisibility(locator, mediumTimeout, 100);    // }    // tooltips are saved in a div list, each div having a specific id//    public String getTooltip(String messageId) {//        if (StringUtils.isEmpty(messageId)) {//            return "";//        }//        String jsCode = "return document.getElementById('" + messageId + "').innerHTML";//        MyLogger.resInfoLog(E2ESeleniumHelper.class"retrieve tooltip: " + jsCode);//        return ((E2ETestContext) webDriverProvider).getBrowser().executeJs(jsCode);//    }    // move mouse pointer explicitly to a given element via the com.wraper.app logo    public void movePointerToElementViaLogo(By by) {        // first move it to the corner, might be that otherwise the event is not triggered        movePointerToElement(By.cssSelector("img.header-logo"));        movePointerToElement(by);        waitUntilLoadingCompleted(100);    }    // basic implementation for GuiElements    // assumes it is defined on the main element defined by the byLocator.    // where this is not the case, a different method has to be used    public String getTooltip(By by) {        // zuerst versuchen ob es im cdk-overlay-container ein mat-tooltip gibt        // first move it to the corner, might be that otherwise the event is not triggered        movePointerToElementViaLogo(by);        withVeryShortTimeout();        List<WebElement> wes = findElementsBy(By.cssSelector("div.cdk-overlay-container div.mat-tooltip"));        withDefaultImplicitTimeout();        if (wes.size() == 1) {            // String attributes = getAllAttributesOfElement(wes.get(0));            // MyLogger.resInfoLog(E2ESeleniumHelper.class"Attributes: {}", attributes);            String tooltip = wes.get(0).getText();            if (tooltip != null) {                return tooltip;            }        }        MyLogger.resInfoLog(E2ESeleniumHelper.class,String.format("no tooltip container found " + wes.size()));        String tt = findElementBy(by).getAttribute("aria-label");        if (StringUtils.isEmpty(tt)) {            String id = findElementBy(by).getAttribute("aria-describedby");            if (!StringUtils.isEmpty(id)) {                tt = getTooltip(By.id(id));            }        }        if (StringUtils.isEmpty(tt))            tt = "";        return tt;    }    public String ariaDescribedByRecursiveOne(By by) {        return ariaDescribedByRecursiveOne(findElementBy(by));    }    public String ariaDescribedByRecursiveOne(WebElement we) {        String adb = we.getAttribute("aria-describedBy");        if (StringUtils.isEmpty(adb)) {            List<WebElement> list = we.findElements(By.xpath(".//*"));            for (WebElement w : list) {                adb = w.getAttribute("aria-describedBy");                if (!StringUtils.isEmpty(adb)) {                    return adb;                }            }        } else {            return adb;        }        return "";    }    public String ariaLabelRecursive(WebElement we) {        String al = we.getAttribute("aria-label");        if (StringUtils.isEmpty(al)) {            List<WebElement> list = we.findElements(By.xpath(".//*"));            for (WebElement w : list) {                al = w.getAttribute("aria-label");                if (!StringUtils.isEmpty(al)) {                    return al;                }            }        } else {            return al;        }        return "";    }    public String ariaDescribedByRecursive(By by) {        return ariaDescribedByRecursive(findElementBy(by));    }    /** use with care, can take a long time **/    public String ariaDescribedByRecursive(WebElement we) {        String adb = we.getAttribute("aria-describedBy");        if (StringUtils.isEmpty(adb)) {            List<WebElement> list = we.findElements(By.xpath(".//*"));            for (WebElement w : list) {                adb = w.getAttribute("aria-describedBy");                if (!StringUtils.isEmpty(adb)) {                    return adb;                }                adb = ariaDescribedByRecursive(w);                if (!StringUtils.isEmpty(adb)) {                    return adb;                }            }        } else {            return adb;        }        return "";    }    // public String ariaDescribedBy(By by) {    // return ariaDescribedBy(findElementBy(by));    // }    public String ariaDescribedBy(WebElement we) {        String adb = we.getAttribute("aria-describedBy");        if (StringUtils.isEmpty(adb)) {            return "";        }        return adb;    }    public String ariaLabel(WebElement we) {        String al = we.getAttribute("aria-label");        if (StringUtils.isEmpty(al)) {            return "";        }        return al;    }    public List<String> getSectionHeaders(By byDomArea) {        By area = byDomArea;        if (area == null)            area = By.cssSelector(".content-body");        List<WebElement> wes = findElementsBy(new ByChained(area, By.cssSelector("h2")));        List<String> headers = new LinkedList<String>();        for (WebElement we : wes) {            String s = we.getText();            if (StringUtils.isNotEmpty(s)) {                headers.add(s);            }        }        return headers;    }    public boolean hasClickableElements(GuiBaseElement element) {        withZeroTimeout();        List<WebElement> elements = findElementsBy(new ByChained(element.getByLocator(), By.tagName("button")));        if (elements.size() > 0) {            withDefaultImplicitTimeout();            return true;        }        elements = findElementsBy(new ByChained(element.getByLocator(), By.tagName("a")));        withDefaultImplicitTimeout();        return (elements.size() > 0);    }    public List<WebElement> findElementsBy(By by) {        return getDriver().findElements(by);    }    public List<WebElement> findElementsByQuick(By by) {        withZeroTimeout();        List<WebElement> wes = getDriver().findElements(by);        withDefaultImplicitTimeout();        return wes;    }    public WebElement findElementBy(By by) {        withShortTimeout();        List<WebElement> elements = getDriver().findElements(by);        if (elements.size() == 0)            throw new RuntimeException("Kein Element vorhanden mit Selektor [" + by.toString() + "]");        else if (elements.size() > 1)            throw new RuntimeException("Mehr als 1 Element vorhanden mit Selektor [" + by.toString() + "]");        withDefaultImplicitTimeout();        if (elements.size() > 0) {            return elements.get(0);        } else            return null;    }    public WebElement findElementByQuick(By by) {        withZeroTimeout();        List<WebElement> elements = getDriver().findElements(by);        if (elements.size() == 0)            logger.warn("Kein Element vorhanden mit Selektor [{}]", by);        else if (elements.size() > 1)            logger.warn("{} Elemente vorhanden mit Selektor [{}]", elements.size(), by);        withDefaultImplicitTimeout();        if (elements.size() > 0) {            return elements.get(0);        } else            return null;    }    public boolean isElementDisabled(By element) {        return isElementDisabled(findElementByQuick(element));    }    public boolean isElementDisabled(WebElement element) {        withZeroTimeout();        boolean isDisabled = "true".equals(element.getAttribute("disabled"));        if (!isDisabled)            isDisabled = "true".equals(element.getAttribute("aria-disabled"));        if (!isDisabled)            isDisabled = element.getAttribute("class").contains("ui-state-disabled");        if (!isDisabled)            isDisabled = element.getAttribute("class").contains("disabled");        if (!isDisabled) {            // start time measurement            isDisabled = false;            try {                new WebDriverWait(getDriver(), 1).ignoring(TimeoutException.class).until(ExpectedConditions.elementToBeClickable(element));                logger.warn("Erfolglose Inaktivitätsprüfung für Element [{}]", element);            } catch (Exception e) {                MyLogger.resInfoLog(E2ESeleniumHelper.class,String.format("Erfolgreiche Inaktivitätsprüfung für Element [{}]", element));                isDisabled = true;            }        }        withDefaultImplicitTimeout();        return isDisabled;    }    public boolean isElementVisible(By element) {        withZeroTimeout();        WebElement visibleElement = null;        try {            visibleElement = (new WebDriverWait(getDriver(), TIMEOUT_SHORT, 50)).until(ExpectedConditions.visibilityOfElementLocated(element));        } catch (Exception e) {            MyLogger.resInfoLog(E2ESeleniumHelper.class,String.format("Erfolglose Prüfung auf Sichtbarkeit für Element [{}]", element));        }        withShortTimeout();        return (visibleElement != null);    }    public boolean isElementInvisible(By element) {        withZeroTimeout();        boolean isInvisible = true;        try {            isInvisible =                    (new WebDriverWait(getDriver(), 3 * E2ESeleniumHelper.TIMEOUT_SHORT, 150)).until(ExpectedConditions.invisibilityOfElementLocated(element));        } catch (Exception e) {            MyLogger.resInfoLog(E2ESeleniumHelper.class,String.format("Erfolglose Prüfung auf Unsichtbarkeit für Element [{}]", element));            isInvisible = false;        }        withShortTimeout();        return (isInvisible);    }    public boolean isElementInvisibleQuick(By element) {        return !isElementVisibleQuick(element);    }    /**     * For cases where you expect the element to be already present in the dom     *     * @param element     * @return     */    public boolean isElementVisibleQuick(By element) {        withZeroTimeout();        List<WebElement> elements = getDriver().findElements(element);        withDefaultImplicitTimeout();        return elements.size() == 1 && elements.get(0).isDisplayed();    }    public boolean isElementClickable(By element) {        withMinimumTimeout();        WebElement clickableElement = null;        try {            clickableElement = (new WebDriverWait(getDriver(), TIMEOUT_SHORT, 150)).until(ExpectedConditions.elementToBeClickable(element));        } catch (Exception e) {            MyLogger.resInfoLog(E2ESeleniumHelper.class,String.format("Erfolglose Prüfung auf Anklickbarkeit für Element [{}]", element));        }        withShortTimeout();        return (clickableElement != null);    }    /**     * etwas zweifelhafte methode um zu prüfen ob ein teil der seite "editierbar" ist, durch zu prüfen ob es links oder buttons gibt     */    public boolean elementContainsLinksOrButtons(By elementLocator) {        if (findElementsBy(new ByChained(elementLocator, By.tagName("button"))).size() > 0) {            return true;        }        if (findElementsBy(new ByChained(elementLocator, By.tagName("a"))).size() > 0) {            return true;        }        return false;    }    public void waitForVisibility(By locator, int secondsToWait) {        waitForVisibility(locator, secondsToWait, defaultSleepIntervalInMillis);    }    public void waitForVisibility(By locator, int secondsToWait, int milliSecondsInterval) {        MyLogger.resInfoLog(E2ESeleniumHelper.class,String.format("Warte max. {} Sekunden auf Element [{}]", secondsToWait, locator));        // start time measurement        long startOperation = System.currentTimeMillis();        long endOperation;        WebElement element = null;        try {            element = new WebDriverWait(getDriver(), secondsToWait).until(ExpectedConditions.visibilityOfElementLocated(locator));            endOperation = System.currentTimeMillis();            MyLogger.resInfoLog(E2ESeleniumHelper.class,String.format("Gesamtwartezeit für erfolgreiche Sichtbarkeitsprüfung: {} ms", endOperation - startOperation));        } catch (Exception e) {            endOperation = System.currentTimeMillis();            MyLogger.resInfoLog(E2ESeleniumHelper.class,String.format("Gesamtwartezeit für erfolglose Sichtbarkeitsprüfung: {} ms", endOperation - startOperation));            if (element == null || !element.isDisplayed()) {                logger.error("Erwartetes Element [{}] nicht angezeigt", locator);            }            throw e;        }    }    public void waitForElementVisibility(By locator) {        waitForVisibility(locator, E2ESeleniumHelper.TIMEOUT_MEDIUM);    }    public void clearField(By fieldToClear) {        try {            action.moveToElement(findElementByQuick(fieldToClear)).click().perform(); // setting focus        } catch (MoveTargetOutOfBoundsException e) {            MyLogger.resInfoLog(E2ESeleniumHelper.class,String.format("Element '{}' - Für Löschen vorheriges Scrollen erforderlich", getLocator(fieldToClear)));            // falls Element nicht im sichtbaren Bereich, scrollen und erneut versuchen            scrollIntoView(findElementByQuick(fieldToClear));            action.moveToElement(findElementByQuick(fieldToClear)).click().perform(); // setting focus        }        SWrapUtil.wait(250);        action.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys.DELETE, Keys.BACK_SPACE).build().perform();        SWrapUtil.wait(250);    }//    public void tab(By by) {//        action.moveToElement(findElementByQuick(by)).click().perform(); // setting focus//        SWrapUtil.wait(250);//        action.sendKeys(Keys.TAB).build().perform();//        SWrapUtil.wait(250);//    }    public void scrollIntoView(By elementLocator) {        // if (isElementVisible(elementLocator))        if (isVisible(findElementByQuick(elementLocator)))            return;        scrollIntoView(findElementByQuick(elementLocator));        // while (true) {        // if (!isElementClickable(elementLocator))        // ((JavascriptExecutor) getDriver()).executeScript("scroll(0, 250);");        // else        // break;        // }    }    public void scrollIntoView(WebElement element) {        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView();", element);        if (isVisible(element)) {            return;        }        // nicht sichtbar, neuerlicher Versuch ab Seitenanfang        scrollToTop();        if (!isVisible(element)) {            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView();", element);            // return;        }        // ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView();", element);    }    // public boolean isVisible(By by) {    // List<WebElement> wes = findElementsBy(by);    // if (wes.size() == 0)    // throw new RuntimeException("isVisible - Element with locator " + by.toString() + " not found.");    // if (wes.size() > 1)    // throw new RuntimeException("isVisible - more than one element with locator " + by.toString() + " found.");    // return isVisible(findElementBy(by));    // }    public boolean isVisible(WebElement element) {        try {            String js = JsReader.of().getJs(JsReader.JS.IS_VISIBLE);            Object result = ((JavascriptExecutor) getDriver()).executeScript(js, element);            return Boolean.parseBoolean(result.toString());        } catch (Exception e) {            logger.warn(e.getMessage());            return false;        }    }    public void scrollToTop() {        // ((JavascriptExecutor) getDriver()).executeScript("window.scrollTo(0, document.body.scrollHeight);");        ((JavascriptExecutor) getDriver()).executeScript("window.scrollTo(0, 0);");    }    public void scrollToBottom() {        ((JavascriptExecutor) getDriver()).executeScript("window.scrollTo(0, document.body.scrollHeight);");        // ((JavascriptExecutor) getDriver()).executeScript("window.scrollTo(0, 1600);");    }    public void scrollDown() {        ((JavascriptExecutor) getDriver()).executeScript("window.scrollBy(0, 300);");    }    public void scrollDown(By locator) {        scrollDown(findElementByQuick(locator));    }    public void scrollDown(WebElement we) {        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollBy(0,300);", we);    }    public void scrollUp(By locator) {        scrollUp(findElementByQuick(locator));    }    public void scrollUp(WebElement we) {        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollBy(0,-300);", we);    }    public void enterShortcut(String shortcut) {        action.keyDown(Keys.CONTROL).sendKeys(shortcut).keyUp(Keys.CONTROL).build().perform();        waitUntilLoadingCompleted();    }    /** send tab to currently active element **/    public void tab() {        getDriver().switchTo().activeElement().sendKeys(Keys.TAB);    }    public void enter() {        getDriver().switchTo().activeElement().sendKeys(Keys.RETURN);    }    public String getActiveElementAriaLabel() {        String value = getDriver().switchTo().activeElement().getAttribute("aria-label");        if (value == null) {            String ids = getDriver().switchTo().activeElement().getAttribute("aria-labelledby");            if (ids != null) {                String[] idList = ids.split("\\s");                for (String id : idList) {                    if (id.contains("label")) {                        value = findElementBy(By.id(id)).getText();                    }                }            }        }        return value;    }    public String getActiveElementTagname() {        return getDriver().switchTo().activeElement().getTagName();    }    public String getCurrentElementNameAttribute() {        WebElement currentElement = getDriver().switchTo().activeElement();        return currentElement.getAttribute("name");    }    public void movePointerToElement(By byElement) {        action.moveToElement(findElementBy(byElement)).build().perform();    }    public void clearInput() {        action.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys.DELETE).build().perform();    }    public void ctrlE() {        action.click().keyDown(Keys.CONTROL).sendKeys("e").keyUp(Keys.CONTROL).build().perform();    }    public void escape() {        action.sendKeys(Keys.ESCAPE).build().perform();    }    public void moveByOffset(int x, int y) {        action.moveByOffset(x, y).build().perform();    }    public void keys(WebElement pWebElement, CharSequence... pCharSequences) {        action.moveToElement(pWebElement).perform();        action.sendKeys(pCharSequences).build().perform();    }    public void tab(WebElement pWebElement) {        keys(pWebElement, Keys.TAB);    }    public void tab(By by) {        action.moveToElement(findElementByQuick(by)).click().perform(); // setting focus        SWrapUtil.wait(250);        action.sendKeys(Keys.TAB).build().perform();        SWrapUtil.wait(250);    }    public void click(By by) {        WebElement element = findElementBy(by);        try {            action.moveToElement(element).click().build().perform();        } catch (MoveTargetOutOfBoundsException e) {            // falls Element nicht im sichtbaren Bereich, scrollen und erneut versuchen            MyLogger.resInfoLog(E2ESeleniumHelper.class,String.format("Element '{}' - Für Anklicken vorheriges Scrollen erforderlich", getLocator(by)));            scrollIntoView(element);            action.moveToElement(element).click().build().perform();        }    }    public void click(WebElement element) {        try {            action.moveToElement(element).click().build().perform();        } catch (MoveTargetOutOfBoundsException e) {            // falls Element nicht im sichtbaren Bereich, scrollen und erneut versuchen            MyLogger.resInfoLog(E2ESeleniumHelper.class,String.format("Element '{}' - Für Anklicken vorheriges Scrollen erforderlich", element.toString()));            scrollIntoView(element);            action.moveToElement(element).click().build().perform();        }    }    // temporär umkopiert aus GuiSeleniumHelper//    public void waitUntilAngularIsReadyOnce() {//        String angularReadyScript = "return window.getAllAngularTestabilities().findIndex(x=>!x.isStable()) === -1";//        try {//            String result = guiSeleniumHelper.executeJs(angularReadyScript).toString();//            MyLogger.resInfoLog(E2ESeleniumHelper.class"result.toString(): " + result);//            boolean angularReady = Boolean.valueOf(result);//            MyLogger.resInfoLog(E2ESeleniumHelper.class"angularReady once: {}", angularReady);////            // if (!angularReady) {//            // waitForConditionWithParameter(() -> Boolean.valueOf(guiSeleniumHelper.executeJs(angularReadyScript).toString()), 5, 50);//            // }//        } catch (WebDriverException ignored) {//            MyLogger.resInfoLog(E2ESeleniumHelper.class"ignored error in waitUntilAngularIsReadyOnce: " + ignored.getMessage());//        }//    }    public void clickElement(By locator) {        // withVeryShortTimeout();        List<WebElement> clickElements = null;        boolean done = false;        int repetitionCount = 5;        while (!done && repetitionCount > 0) {            try {                clickElements = findElementsBy(locator);                if (clickElements.size() == 1) {                    if (clickElements.get(0).isEnabled()) {                        clickElements.get(0).click();                    } else {                        waitUntilClickable(locator);                        clickElements.get(0).click();                    }                    MyLogger.resInfoLog(E2ESeleniumHelper.class,MessageFormat.format("Element [{0}] erfolgreich angeklickt", locator));                    done = true;                } else {                    logger.error("{} Elemente mit Locator [{}] vorhanden, Aktion wird abgebrochen", clickElements.size(), locator);                    throw new ElementNotInteractableException(getLocator(locator));                }            } catch (ElementClickInterceptedException e) {                logger.warn("Element [{}] wegen Überlagerung nicht anklickbar, neuer Versuch nach Wartezeit", locator);                --repetitionCount;                scrollIntoView(clickElements.get(0));                SWrapUtil.wait(150);            } catch (ElementNotInteractableException e) {                logger.warn("Element [{}] nicht bearbeitbar, Vorgang wird abgebrochen", locator);                repetitionCount = 0;                throw e;            } catch (StaleElementReferenceException e) {                logger.warn("Element [{}] noch nicht stabil, neuer Versuch nach Wartezeit", locator);                --repetitionCount;                SWrapUtil.wait(150);            } catch (Exception e) {                logger.warn("Element [{}] nicht anklickbar, neuer Versuch nach Repositionierung", locator);                --repetitionCount;                scrollDown();            }        }        // withDefaultImplicitTimeout();        try {            action.moveByOffset(1, 1).build().perform(); // wegen Tooltip??        } catch (Exception e) {            logger.warn("Exception bei Maus-Repositionierung nach Klicken auf [{}]", locator);        }        if (done) {            MyLogger.resInfoLog(E2ESeleniumHelper.class,MessageFormat.format("Warte max. {} sec. auf Abschluß der Aktion nach Klicken auf [{}]", TIMEOUT_VERYLONG, locator));            waitUntilLoadingCompletedLong();        } else {            logger.error("Element [{}] kann nicht angeklickt werden", locator);            throw new ElementNotInteractableException("Element [" + locator + "] kann nicht angeklickt werden");        }    }}