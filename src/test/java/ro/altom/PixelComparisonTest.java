package ro.altom;

import org.openqa.selenium.remote.RemoteWebDriver;
import ro.altom.image.UICheck;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import ro.altom.results.*;
import ro.altom.screenshots.ElementScreenshot;
import ro.altom.screenshots.ViewPortScreenshot;
import ro.altom.screenshots.Screenshot;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by Altom on 07/10/16.
 */
public class PixelComparisonTest {

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
    public void compareW3CViewPort() throws IOException, ResultException {
        screenshot = new ViewPortScreenshot(driver, "w3c");
        double result = UICheck.percentage(screenshot, true);

        Assert.assertTrue("Image different from baseline ", result < 10);
    }

    @Test
    public void compareW3CLogoElement() throws IOException, ResultException, InterruptedException {
        screenshot = new ElementScreenshot(driver, By.cssSelector(".logo>a"), "w3cLogo");
        int result = UICheck.pixel(screenshot);

        Assert.assertTrue("Image different from baseline ", result < 100);
    }

    @Test
    public void compareW3CThemeElement() throws IOException, ResultException {
        screenshot = new ElementScreenshot(driver, By.className("theme"), "w3cTheme");

        Assert.assertTrue("Image different from baseline ", UICheck.binary(screenshot));
    }

    @Test
    public void compareW3CCopyrightElement() throws IOException, ResultException {
        screenshot = new ElementScreenshot(driver, By.className("copyright"), "w3cCopyright");
        double result = UICheck.percentage(screenshot);

        Assert.assertTrue("Image different from baseline ", result < 10);
    }

    @After
    public void testTearDown() {
        System.out.println("Quitting driver");
        driver.quit();
    }
}
