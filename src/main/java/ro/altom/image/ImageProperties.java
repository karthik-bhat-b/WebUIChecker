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
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class ImageProperties {

    public Mat resize(Mat imageToResize, int newWidth) {
        Mat resizedImage = new Mat();
        Imgproc.resize(imageToResize, resizedImage, calculateNewSizeAspectRatio(imageToResize.width(), imageToResize.height(), newWidth), 0, 0, Imgproc.INTER_NEAREST);
        return resizedImage;
    }

    private Size calculateNewSizeAspectRatio(int originalWidth, int originalHeight, int newWidth) {
        double div = (double) originalHeight / originalWidth;
        int newHeight = (int) (div * newWidth);
        Size sizeForResize = new Size(newWidth, newHeight);
        return sizeForResize;
    }

}
