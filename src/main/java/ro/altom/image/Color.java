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

public class Color {

    public double[] setColor(String color) {
        double[] highlightRGB = {0, 0, 255};
        if (color.equals("magenta")) {
            highlightRGB[0] = 255;
            highlightRGB[1] = 0;
            highlightRGB[2] = 255;
            System.out.println("---> Highlight color was changed into " + color + ".");
        } else {
            System.out.println("---> Wrong color");
            System.out.println("---> Default color: red");
        }
        return highlightRGB;
    }
}