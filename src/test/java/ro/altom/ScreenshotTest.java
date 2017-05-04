package ro.altom;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import ro.altom.screenshots.*;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by Altom on 06/10/16.
 */
public class ScreenshotTest extends FileManager {

    RemoteWebDriver driver;
    Screenshot screenshot;
    BrowserSetup browser;

    @Before
    public void setUp() throws MalformedURLException {
        browser = new BrowserSetup();
        browser.start();

        driver = browser.getDriver();
        driver.get("https://www.w3.org/TR/");
        driver.manage().window().maximize();
    }


    @Test
    public void screenshotW3CViewPort() throws IOException {
        screenshot = new ViewPortScreenshot(driver, "w3c");
        Assert.assertTrue(fileCreated(screenshot.capture()));
    }

    @Test
    public void screenshotW3CLogoElement() throws IOException {
        screenshot = new ElementScreenshot(driver, By.cssSelector(".logo>a"), "w3cLogo");
        Assert.assertTrue(fileCreated(screenshot.capture()));
    }

    @Test
    public void screenshotW3CCopyrightElement() throws IOException {
        screenshot = new ElementScreenshot(driver, By.className("copyright"), "w3cCopyright");
        Assert.assertTrue(fileCreated(screenshot.capture()));
    }

    @Test
    public void screenshotW3CThemeElement() throws IOException {
        screenshot = new ElementScreenshot(driver, By.className("theme"), "w3cTheme");
        Assert.assertTrue(fileCreated(screenshot.capture()));
    }

    @Test
    public void screenshotNotTaken() {
        Assert.assertFalse(fileCreated("x"));
    }

    @After
    public void testTearDown() {
        System.out.println("Quitting driver");
        driver.quit();
    }

}
