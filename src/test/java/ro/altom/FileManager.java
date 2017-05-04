package ro.altom;

import ro.altom.system.EnvironmentConfiguration;

import java.io.File;

/**
 * Created by Altom on 20/10/16.
 */
public class FileManager {

    EnvironmentConfiguration env = new EnvironmentConfiguration();

    public boolean fileCreated(String name) {
        File baselineFile = new File(String.valueOf(env.baselineFolder.resolve(name + ".png")));
        File screenshotFile = new File(String.valueOf(env.screenshotFolder.resolve(name + ".png")));
        if ((baselineFile.exists() && !baselineFile.isDirectory()) || (screenshotFile.exists() && !screenshotFile.isDirectory())) {
            if (baselineFile.length() > 0 || screenshotFile.length() > 0)
                return true;
        }
        return false;
    }
}
