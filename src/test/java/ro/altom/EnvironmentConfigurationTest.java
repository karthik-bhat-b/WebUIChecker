package ro.altom;

import org.junit.Assert;
import org.junit.Test;
import ro.altom.system.EnvironmentConfiguration;

/**
 * Created by Altom on 04/10/16.
 */
public class EnvironmentConfigurationTest {

    EnvironmentConfiguration env = new EnvironmentConfiguration();

    @Test
    public void getScreenshotFolder() {
        Assert.assertTrue(env.screenshotFolder.toString().matches(".*/target/reports.*/screenshots"));
    }

    @Test
    public void getBaselineFolder() {
        Assert.assertTrue("Baseline path isn't the same with the one from config file", env.baselineFolder.toString().matches(".*img"));
    }
}
