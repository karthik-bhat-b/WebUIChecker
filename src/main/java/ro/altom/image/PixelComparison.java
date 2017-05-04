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

package ro.altom.image;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.CvType;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import ro.altom.results.*;
import ru.yandex.qatools.allure.annotations.Attachment;
import ru.yandex.qatools.allure.annotations.Step;
import ro.altom.screenshots.Screenshot;
import ro.altom.system.Benchmark;
import ro.altom.system.EnvironmentConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class PixelComparison {

    private String screenshotName, baselineName;
    private IResponse response;
    EnvironmentConfiguration env = new EnvironmentConfiguration();

    private double[] highlightRGB = {0, 0, 255};
    private int diffPixels, matchingCount;
    private int heightBaselineImg, widthBaselineImg, heightComparedImg, widthComparedImg;
    private Mat highlightDiffImgMat, combinedDiffMat, baselineImgMat, comparedImgMat;
    private int maxPixelColorDifference = 20;
    private int totalPixels = 0;

    public PixelComparison() {
    }

    public PixelComparison(String screenshotName, IResponse response) throws ResultException {
        this.screenshotName = screenshotName;
        this.response = response;
    }

    public PixelComparison(Screenshot screenshot, IResponse response) throws IOException, ResultException {
        this.screenshotName = screenshot.capture();
        this.response = response;
    }

    public int getTotalPixels() {
        return totalPixels;
    }

    public void compare() throws ResultException {
        if (!screenshotName.contains("_screenshot_")) {
            System.out.println("[INFO] ... Baseline image was created");
        } else {
            baselineName = getBaseline(screenshotName);
            compareImages(baselineName, screenshotName);
            totalPixels = heightBaselineImg * widthBaselineImg;
            response.setResult(diffPixels, totalPixels);
        }
    }

    private void compareImages(String baselineImage, String comparedImage) {
        pixelByPixelComparison(baselineImage, comparedImage, maxPixelColorDifference);
    }

    @Step("Pixel by pixel comparison")
    private void pixelByPixelComparison(String baselineName, String comparedName, int maxPixelColorDifference) {
        matchingCount = 0;
        diffPixels = 0;
        baselineImgMat = new Mat();
        String baselinePath = String.valueOf(env.baselineFolder.resolve(baselineName + ".png"));
        baselineImgMat = Highgui.imread(baselinePath, Highgui.CV_LOAD_IMAGE_COLOR);
        comparedImgMat = new Mat();
        String screenshotPath = String.valueOf(env.screenshotFolder.resolve(comparedName + ".png"));
        comparedImgMat = Highgui.imread(screenshotPath, Highgui.CV_LOAD_IMAGE_COLOR);

        System.out.println("[INFO] ... Baseline image size: " + baselineImgMat.size());
        System.out.println("[INFO] ... Compared image size: " + comparedImgMat.size());

        heightBaselineImg = baselineImgMat.height();
        widthBaselineImg = baselineImgMat.width();
        heightComparedImg = comparedImgMat.height();
        widthComparedImg = comparedImgMat.width();

        resolveImageSizeDifference();
        prepareHighlightedDiffImage();
        compareImagesAlgorithm(maxPixelColorDifference);
        makeCombinedDiff();

        String highlightedDiffPath = String.valueOf(env.screenshotFolder.resolve(baselineName + "_diff_" + Benchmark.getTimestamp() + ".png"));
        Highgui.imwrite(highlightedDiffPath, combinedDiffMat);
        attachCombinedDiffInReport(highlightedDiffPath);
    }

    public Mat getCombinedDiff() {
        return combinedDiffMat;
    }

    @Step("Resolve images size difference")
    private void resolveImageSizeDifference() {
        ImageProperties img = new ImageProperties();
        System.out.println("[INFO] ... Resolving image size difference");
        if (widthBaselineImg > widthComparedImg) {
            baselineImgMat = img.resize(baselineImgMat, widthComparedImg);
            widthBaselineImg = baselineImgMat.width();
            heightBaselineImg = baselineImgMat.height();
        } else {
            comparedImgMat = img.resize(comparedImgMat, widthBaselineImg);
            widthComparedImg = comparedImgMat.width();
            heightComparedImg = comparedImgMat.height();
        }
    }

    @Step("Prepare highlighted diff image")
    private void prepareHighlightedDiffImage() {
        int maxHeight, maxWidth;
        maxHeight = Math.max(baselineImgMat.height(), comparedImgMat.height());
        maxWidth = Math.max(baselineImgMat.width(), comparedImgMat.width());
        highlightDiffImgMat = new Mat(maxHeight, maxWidth, 16);
        baselineImgMat.copyTo(highlightDiffImgMat);
        if (highlightDiffImgMat.height() < maxHeight) {
            Mat outM = new Mat(maxHeight - highlightDiffImgMat.height(), maxWidth, 16);
            highlightDiffImgMat.push_back(outM);
            for (int i = baselineImgMat.height(); i < highlightDiffImgMat.height(); i++) {
                for (int j = 0; j < maxWidth; j++) {
                    diffPixels++;
                    double[] dataDiff = highlightRGB;
                    highlightDiffImgMat.put(i, j, dataDiff);
                }
            }
        } else if (comparedImgMat.height() < maxHeight) {
            for (int i = comparedImgMat.height(); i < highlightDiffImgMat.height(); i++) {
                for (int j = 0; j < maxWidth; j++) {
                    diffPixels++;
                    double[] dataDiff = highlightRGB;
                    highlightDiffImgMat.put(i, j, dataDiff);
                }
            }
        }
    }

    @Step("Compare images")
    private void compareImagesAlgorithm(int maxPixelColorDifference) {
        int minWidth, minHeight;
        minHeight = Math.min(baselineImgMat.height(), comparedImgMat.height());
        minWidth = Math.min(baselineImgMat.width(), comparedImgMat.width());
        for (int i = 0; i < minHeight; i++) {
            for (int j = 0; j < minWidth; j++) {
                double[] data1 = baselineImgMat.get(i, j);
                double[] data2 = comparedImgMat.get(i, j);
                if (Math.abs(data1[0] - data2[0]) <= maxPixelColorDifference && Math.abs(data1[1] - data2[1]) <= maxPixelColorDifference && Math.abs(data1[2] - data2[2]) <= maxPixelColorDifference) {
                    matchingCount++;
                } else {
                    diffPixels++;
                    double[] dataDiff = highlightRGB;
                    highlightDiffImgMat.put(i, j, dataDiff);
                }
            }
        }
    }

    @Step("Make combined diff")
    private void makeCombinedDiff() {
        Mat baselineImgMatFramed = new Mat(heightBaselineImg, widthBaselineImg, 16),
                comparedImgMatFramed = new Mat(heightComparedImg, widthComparedImg, 16),
                highlightDiffImgMatFramed = new Mat(highlightDiffImgMat.height(), highlightDiffImgMat.width(), 16);

        Imgproc.copyMakeBorder(baselineImgMat, baselineImgMatFramed, 10, 10, 10, 5, Imgproc.BORDER_CONSTANT);
        Imgproc.copyMakeBorder(comparedImgMat, comparedImgMatFramed, 10, 10, 5, 5, Imgproc.BORDER_CONSTANT);
        Imgproc.copyMakeBorder(highlightDiffImgMat, highlightDiffImgMatFramed, 10, 10, 5, 10, Imgproc.BORDER_CONSTANT);

        int widthCombinedDiff = baselineImgMatFramed.width() + comparedImgMatFramed.width() + highlightDiffImgMatFramed.width();
        int heightCombinedDiff = highlightDiffImgMatFramed.height();
        combinedDiffMat = new Mat(heightCombinedDiff, widthCombinedDiff, CvType.CV_64FC(3));

        /**
         * adding frame process
         */

        for (int i = 0; i < baselineImgMatFramed.height(); i++) {
            for (int j = 0; j < baselineImgMatFramed.width(); j++) {
                double[] baselinePixel = baselineImgMatFramed.get(i, j);
                combinedDiffMat.put(i, j, baselinePixel);
            }
        }

        int k = 0, l;
        for (int i = 0; i < comparedImgMatFramed.height(); i++) {
            l = 0;
            for (int j = baselineImgMatFramed.width(); j < baselineImgMatFramed.width() + comparedImgMatFramed.width(); j++) {
                double[] comparedPixel = comparedImgMatFramed.get(k, l);
                l++;
                combinedDiffMat.put(i, j, comparedPixel);
            }
            k++;
        }

        k = 0;
        for (int i = 0; i < highlightDiffImgMatFramed.height(); i++) {
            l = 0;
            for (int j = baselineImgMatFramed.width() + comparedImgMatFramed.width(); j < combinedDiffMat.width(); j++) {
                double[] highlightPixel = highlightDiffImgMatFramed.get(k, l);
                l++;
                combinedDiffMat.put(i, j, highlightPixel);
            }
            k++;
        }

        baselineImgMat.release();
        comparedImgMat.release();
        highlightDiffImgMat.release();

        baselineImgMatFramed.release();
        comparedImgMatFramed.release();
        highlightDiffImgMatFramed.release();
    }


    @Step("Changed highlight color to {0}")
    public void setHighlightColor(String color) {
        Color c = new Color();
        highlightRGB = c.setColor(color.toLowerCase());
    }

    @Step("Save CombinedDiff in report: {0}")
    @Attachment(value = "{0}", type = "image/png")
    private byte[] attachCombinedDiffInReport(String fileNamePath) {
        Path path = Paths.get(fileNamePath);
        byte[] data = new byte[0];

        try {
            data = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    public String getBaseline(String screenshotName) {
        String[] parts = screenshotName.split("_screenshot_");
        return parts[0];
    }

    void setDiffList(List<Mat> list, String name) {
        try {
            Mat finalDiffMat = new Mat();
            Core.vconcat(list, finalDiffMat);

            String fullpageDiffPath = String.valueOf(env.screenshotFolder.resolve(name + "_FULL_PAGE_DIFF_" + Benchmark.getTimestamp() + ".png"));
            Highgui.imwrite(fullpageDiffPath, finalDiffMat);
            attachCombinedDiffInReport(fullpageDiffPath);
            finalDiffMat.release();
        } catch (NullPointerException ex) {
            System.err.println("[INFO] ... There are no images for creating combined diff");
        }
    }
}
