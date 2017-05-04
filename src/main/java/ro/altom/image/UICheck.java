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

import org.opencv.core.Mat;
import ro.altom.results.BinaryResponse;
import ro.altom.results.PercentageResponse;
import ro.altom.results.PixelResponse;
import ro.altom.results.ResultException;
import ro.altom.screenshots.FullPageScreenshot;
import ro.altom.screenshots.Screenshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UICheck {

    public static Mat combinedDiff;
    private static int totalPixels;

    public static boolean binary(Screenshot screenshot, Boolean magentaColor) throws IOException, ResultException {
        return binaryResponse(screenshot, magentaColor);
    }

    public static boolean binary(Screenshot screenshot) throws IOException, ResultException {
        return binaryResponse(screenshot, false);
    }

    public static List<Boolean> binaryMultiple(FullPageScreenshot screenshot) throws InterruptedException, ResultException, IOException {
        return binaryMultipleResponse(screenshot);
    }

    public static double percentage(Screenshot screenshot, Boolean magentaColor) throws IOException, ResultException {
        return percentageResponse(screenshot, magentaColor);
    }

    public static double percentage(Screenshot screenshot) throws IOException, ResultException {
        return percentageResponse(screenshot, false);
    }

    public static List<Double> percentageMultiple(FullPageScreenshot screenshot) throws InterruptedException, ResultException, IOException {
        return percentageMultipleResponse(screenshot);
    }

    public static int pixel(Screenshot screenshot, Boolean magentaColor) throws IOException, ResultException {
        return pixelResponse(screenshot, magentaColor);
    }

    public static int pixel(Screenshot screenshot) throws IOException, ResultException {
        return pixelResponse(screenshot, false);
    }

    public static List<Integer> pixelMultiple(FullPageScreenshot screenshot) throws InterruptedException, ResultException, IOException {
        return pixelMultipleResponse(screenshot);
    }

    private static boolean binaryResponse(Screenshot screenshot, Boolean magentaColor) throws ResultException, IOException {
        BinaryResponse response = new BinaryResponse();
        PixelComparison pc = new PixelComparison(screenshot, response);
        if (magentaColor) {
            pc.setHighlightColor("magenta");
        }

        pc.compare();
        combinedDiff = pc.getCombinedDiff();
        return response.getResult();
    }

    private static List<Boolean> binaryMultipleResponse(FullPageScreenshot screenshot) throws InterruptedException, ResultException, IOException {
        List<Integer> pixelResponseList = screenshot.checkPixels();
        List<Boolean> binaryResponseList = new ArrayList<>();

        int i = 0;
        for (Integer result : pixelResponseList) {
            BinaryResponse binaryResponse = new BinaryResponse();
            binaryResponse.setResult(result, totalPixels);
            binaryResponseList.add(i, binaryResponse.getResult());
            if (!screenshot.checkIfBaseline()) {
                System.out.println("[INFO] ... " + screenshot.getName() + String.valueOf(i) + " ---> similar baseline: " + binaryResponse.getResult());
                System.out.println("-------------------------------------------------------");
            }
            i++;
        }

        return binaryResponseList;
    }


    private static double percentageResponse(Screenshot screenshot, Boolean magentaColor) throws ResultException, IOException {
        PercentageResponse response = new PercentageResponse();
        PixelComparison pc = new PixelComparison(screenshot, response);
        if (magentaColor) {
            pc.setHighlightColor("magenta");
        }

        pc.compare();
        combinedDiff = pc.getCombinedDiff();
        return response.getResult();
    }

    private static List<Double> percentageMultipleResponse(FullPageScreenshot screenshot) throws InterruptedException, ResultException, IOException {
        List<Integer> pixelResponseList = screenshot.checkPixels();
        List<Double> percentageResponseList = new ArrayList<>();

        int i = 0;
        for (Integer result : pixelResponseList) {
            PercentageResponse percentageResponse = new PercentageResponse();
            percentageResponse.setResult(result, totalPixels);
            percentageResponseList.add(i, percentageResponse.getResult());
            if (!screenshot.checkIfBaseline()) {
                System.out.println("[INFO] ... " + screenshot.getName() + String.valueOf(i) + " ---> percentage difference: " + percentageResponse.getResult() + "%");
                System.out.println("-------------------------------------------------------");
            }
            i++;
        }

        return percentageResponseList;
    }


    private static int pixelResponse(Screenshot screenshot, Boolean magentaColor) throws ResultException, IOException {
        PixelResponse response = new PixelResponse();
        PixelComparison pc = new PixelComparison(screenshot, response);

        if (magentaColor) {
            pc.setHighlightColor("magenta");
        }

        pc.compare();
        totalPixels = pc.getTotalPixels();
        combinedDiff = pc.getCombinedDiff();
        return response.getResult();
    }

    private static List<Integer> pixelMultipleResponse(FullPageScreenshot screenshot) throws InterruptedException, ResultException, IOException {
        return screenshot.checkPixels();
    }

    public void attachCombinedDiffList(List<Mat> list, String name) {
        PixelComparison pixel = new PixelComparison();
        pixel.setDiffList(list, name);
    }
}
