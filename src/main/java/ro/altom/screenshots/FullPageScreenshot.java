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

import org.openqa.selenium.remote.RemoteWebDriver;
import ro.altom.image.UICheck;
import org.opencv.core.Mat;
import ro.altom.results.ResultException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class FullPageScreenshot extends Screenshot {

    private RemoteWebDriver driver;
    private String name;
    private long viewPortHeight, scrollPosition;
    private int i;
    private Screenshot screenshot;
    private List<Mat> diffList;
    private UICheck checker;
    private Boolean newBaselineImage;

    public FullPageScreenshot(RemoteWebDriver driver, String name) {
        this.driver = driver;
        this.name = name;
        this.viewPortHeight = getViewPortHeight();
        this.diffList = new ArrayList<>();
        this.checker = new UICheck();
    }

    public String getName(){
        return name;
    }

    public boolean checkIfBaseline(){
        return newBaselineImage;
    }

    @Override
    public String capture() throws IOException {
        return null;
    }

    public List<Integer> checkPixels() throws ResultException, IOException, InterruptedException {
        long scrollMaxY = getScrollPositionMaxY();
        i = 0;
        List<Integer> resultList = new ArrayList<Integer>();

        resultList.add(pixelCheck(name, i + 1));
        diffList.add(UICheck.combinedDiff);
        scrollPosition = getScrollPositionOnY();

        while (scrollPosition + 1 < scrollMaxY) {
            scrollOnePage();
            resultList.add(pixelCheck(name, i + 2));
            diffList.add(UICheck.combinedDiff);
            i++;
            scrollPosition = getScrollPositionOnY();
            sleep(2000);
            scrollMaxY = getScrollPositionMaxY();
        }

        checker.attachCombinedDiffList(diffList, name);
        releaseMatrices();

        return resultList;
    }

    private int pixelCheck(String imageName, int part) throws ResultException, IOException {
        screenshot = new ViewPortScreenshot(driver, imageName + part);
        int result = UICheck.pixel(screenshot);
        newBaselineImage = screenshot.newBaselineImage;

        if (!screenshot.newBaselineImage) {
            System.out.println("[INFO] ... " + imageName + String.valueOf(part) + " ---> different pixels: " + result);
            System.out.println("-------------------------------------------------------");
        }

        return result;
    }

    private long getScrollPositionMaxY() {
        long maxScroll = Long.parseLong(driver.executeScript("return document.querySelector(\"html\").scrollHeight - window.innerHeight").toString());

        Boolean vertScrollStatus = (Boolean) driver.executeScript("return document.documentElement.scrollHeight > document.documentElement.clientHeight;");

        return vertScrollStatus.equals(true) ? Math.round(maxScroll) : 0;
    }

    private long getViewPortHeight() {
        return Long.parseLong(driver.executeScript("return window.innerHeight").toString());
    }

    private long getScrollPositionOnY() {
        return Long.parseLong(driver.executeScript("return Math.round(window.scrollY)").toString());
    }

    private void scrollOnePage() throws InterruptedException {
        driver.executeScript("window.scrollBy(0," + viewPortHeight + ")", "");
        sleep(2000);
    }

    private void releaseMatrices() {
        diffList.stream().filter(diff -> diff != null).forEach(Mat::release);
    }
}
