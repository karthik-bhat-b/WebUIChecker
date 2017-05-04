package ro.altom;

import org.openqa.selenium.remote.RemoteWebDriver;
import ro.altom.image.UICheck;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ro.altom.results.ResultException;
import ro.altom.screenshots.FullPageScreenshot;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

/**
 * Created by Altom on 09/12/16.
 */
public class FullPageComparisonTest {

    RemoteWebDriver driver;
    BrowserSetup browser;

    @Before
    public void setUp() throws MalformedURLException {
        browser = new BrowserSetup();
        browser.start();
        driver = browser.getDriver();
    }


    @Test
    public void comparePixelW3CStandards() throws ResultException, IOException, InterruptedException {
        driver.get("https://www.w3.org/TR/");
        driver.manage().window().maximize();

        FullPageScreenshot screenshot = new FullPageScreenshot(driver, "w3cStandards");
        List<Integer> resultList = UICheck.pixelMultiple(screenshot);

        for (int i = 0; i < resultList.size(); i++) {
            Assert.assertTrue("Image different from baseline ", resultList.get(i) < 3600);
        }
    }

    @Test
    public void compareBinaryW3CMediaCapture() throws ResultException, IOException, InterruptedException {
        driver.get("https://www.w3.org/TR/2014/CR-html-media-capture-20140909/");
        driver.manage().window().maximize();

        FullPageScreenshot screenshot = new FullPageScreenshot(driver, "w3cCapture");
        List<Boolean> resultList = UICheck.binaryMultiple(screenshot);

        for (int i = 0; i < resultList.size(); i++) {
            Assert.assertTrue("Image different from baseline ", resultList.get(i));
        }
    }

    @Test
    public void comparePercentageW3CStandards() throws InterruptedException, ResultException, IOException {
        driver.get("https://www.w3.org/TR/");
        driver.manage().window().maximize();

        FullPageScreenshot screenshot = new FullPageScreenshot(driver, "w3cStandards");
        List<Double> resultList = UICheck.percentageMultiple(screenshot);

        for (int i = 0; i < resultList.size(); i++) {
            Assert.assertTrue("Image different from baseline ", resultList.get(i) < 10);
        }
    }

    @After
    public void testTearDown() {
        System.out.println("Quitting driver");
        driver.quit();
    }
}
