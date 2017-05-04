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

package ro.altom.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class EnvironmentConfiguration {

    protected static Properties prop;

    public static Path screenshotFolder = new File(System.getProperty("user.dir") + "/target/reports/screenshots/").toPath();
    public static Path baselineFolder = new File(System.getProperty("user.dir") + "/img/").toPath();
    private boolean defaultBaselinePath = false;
    private static boolean pathsAlreadySet = false;

    {
        prop = new Properties();
        nu.pattern.OpenCV.loadShared();

        try {
            prop.load(new FileInputStream(new File("properties/config.properties")));
        } catch (IOException e) {
            defaultBaselinePath = true;
        }
    }

    public EnvironmentConfiguration() {
        if (!pathsAlreadySet) {

            String allureResultsDirectoryPath = System.getProperty("user.dir") + "/target/reports/allure-results";
            System.setProperty("allure.results.directory", allureResultsDirectoryPath);

            if (!defaultBaselinePath) {
                baselineFolder = new File(prop.getProperty("path.baseline")).toPath();
            }

            try {
                createReportFolderForSpecificRun();
            } catch (IOException e) {
                System.out.println("Old reports directory could not be renamed with its creation date");
            }

            pathsAlreadySet = true;
        }
    }

    public void createReportFolderForSpecificRun() throws IOException {
        File reportFile = new File(System.getProperty("user.dir") + "/target/reports");

        if (reportFile.exists() && reportFile.isDirectory()) {
            File reportFileWithCreationTime = new File(System.getProperty("user.dir") + "/target/reports-" + getFileCreationTime(reportFile));
            reportFile.renameTo(reportFileWithCreationTime);
        }
    }

    public String getFileCreationTime(File file) throws IOException {
        Path p = Paths.get(file.getAbsolutePath());
        BasicFileAttributes view = Files.getFileAttributeView(p, BasicFileAttributeView.class).readAttributes();
        FileTime fileTime = view.creationTime();

        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(fileTime.toMillis());
    }

    public String getPlatformName() {
        return System.getProperty("os.name");
    }

}
