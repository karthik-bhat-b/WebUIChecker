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
import org.openqa.selenium.*;
import org.openqa.selenium.Point;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import ro.altom.system.Benchmark;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ElementScreenshot extends Screenshot {

    RemoteWebDriver driver;
    String name;
    By element;
    int scale = 1;

    public ElementScreenshot(RemoteWebDriver driver, By element, String name) {
        if ((driver instanceof ChromeDriver || driver instanceof FirefoxDriver) && checkRetinaDisplay()) {
            System.out.println("[INFO] ... Retina display on Mac OS X.");
            System.out.println("[INFO] ... Change the scaling level.");
            this.scale = 2;
        }

        this.driver = driver;
        this.element = element;
        this.name = name + "_" + getDriverName(this.driver);
    }

    @Override
    public String capture() throws IOException {
        if (baselineExists(name)) {
            return screenshotElementAsQuery(name, element);
        } else {
            System.err.println("[INFO] ... Image not found, creating baseline for '" + name + "'.");
            newBaselineImage = true;
            return screenshotElementAsBaseline(name, element);
        }
    }

    private String screenshotElementAsQuery(String screenshotName, By element) throws IOException {
        return screenshotElement(screenshotName, element, false);
    }

    private String screenshotElementAsBaseline(String screenshotName, By element) throws IOException {
        return screenshotElement(screenshotName, element, true);
    }

    private String screenshotElement(String screenshotName, By el, boolean newBaseline) throws IOException {
        Path screenshotPath;

        WebElement element = driver.findElement(el);
        driver.executeScript("arguments[0].scrollIntoView();", element);

        new WebDriverWait(driver, 10).until(d -> d.findElement(el).isDisplayed());

        File urlScreenshot = driver.getScreenshotAs(OutputType.FILE);
        BufferedImage fullImg = ImageIO.read(urlScreenshot);

        Point viewPortLocation = ((Locatable) element).getCoordinates().inViewPort();

        int y = viewPortLocation.getY() * scale;
        int x = viewPortLocation.getX() * scale;

        int elementHeight = element.getSize().getHeight();
        int elementWidth = element.getSize().getWidth();

        BufferedImage elementScreenshot = fullImg.getSubimage(x, y, elementWidth * scale, elementHeight * scale);
        ImageIO.write(elementScreenshot, "png", urlScreenshot);

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
