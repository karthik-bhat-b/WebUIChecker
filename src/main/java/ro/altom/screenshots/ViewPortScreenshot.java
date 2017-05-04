/**
 * Automated Visual Web Checking (AVWC) - Library easy to import in projects which gives you
 * the possibility to verify the GUI of a Web Application (visual check).
 * <p>
 * Copyright (C) 2017  Altom Consulting.
 * <p>
 * This file is part of Automated Visual Web Checking.
 * <p>
 * Automated Visual Web Checking is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * Automated Visual Web Checking is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ro.altom.screenshots;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.RemoteWebDriver;
import ro.altom.system.Benchmark;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ViewPortScreenshot extends Screenshot {

    RemoteWebDriver driver;
    String name;

    public ViewPortScreenshot(RemoteWebDriver driver, String name) {
        this.driver = driver;
        this.name = name + "_" + getDriverName(this.driver);
    }

    @Override
    public String capture() throws IOException {
        if (baselineExists(name)) {
            return screenshotAsQuery(name);
        } else {
            System.err.println("[INFO] ... Image not found, creating baseline for '" + name + "'.");
            newBaselineImage = true;
            return screenshotAsBaseline(name);
        }
    }

    private String screenshotAsQuery(String screenshotName) throws IOException {
        return screenshot(screenshotName, false);
    }

    private String screenshotAsBaseline(String screenshotName) throws IOException {
        return screenshot(screenshotName, true);
    }

    private String screenshot(String screenshotName, boolean newBaseline) throws IOException {
        Path screenshotPath;

        File urlScreenshot = driver.getScreenshotAs(OutputType.FILE);

        if (newBaseline) {
            screenshotPath = env.baselineFolder.resolve(screenshotName + ".png");
        } else {
            screenshotName = screenshotName + "_screenshot_" + Benchmark.getTimestamp();
            screenshotPath = env.screenshotFolder.resolve(screenshotName + ".png");
        }

        FileUtils.copyFile(urlScreenshot, screenshotPath.toFile());
        return screenshotName;
    }

}
