package ro.altom;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by melinda on 03/05/17.
 */
public class BrowserSetup {

    private static final String remoteDriverURL = System.getProperty("remoteHub", null);
    private static String browser = System.getProperty("browser");
    private RemoteWebDriver driver;

    protected void start() throws MalformedURLException {
        String browserType = remoteDriverURL == null ? "Local" : "Remote";
        switch (browserType) {
            case "Local":
                initializeDriver();
                break;
            case "Remote":
                DesiredCapabilities capability = new DesiredCapabilities();
                capability.setCapability("browserName", browser);
                capability.setCapability("javascriptEnabled", "true");
                assert remoteDriverURL != null;
                driver = new RemoteWebDriver(new URL(remoteDriverURL), capability);
                break;
        }
    }

    private void initializeDriver() {
        if (System.getProperty("webdriver.chrome.driver") != null) {
            driver = new ChromeDriver();
        }

        if (System.getProperty("webdriver.gecko.driver") != null) {
            driver = new FirefoxDriver();
        }
    }

    protected RemoteWebDriver getDriver() {
        return this.driver;
    }
}
