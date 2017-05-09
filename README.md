# - WebUI Checker -
### An Automated Visual Web Checking tool


What is WebUI Checker?
* It is a library easy to import in projects that gives you the possibility to automate the process <br>
  of identifying changes made to the UI of a Web Application from one version to another.

======================================================================================================

Requirements
* Selenium WebDriver and Java

======================================================================================================

#### Adding WebUI Checker to your test project
##### Option 1
--> use `web-ui-checker` package as a dependency from one of the links below: <br>
Maven Central Repository - https://search.maven.org/ <br>
JCenter - https://bintray.com/bintray/jcenter <br>
    
##### Option 2
--> import `external jar` for WebUI Checker library <br>
--> when using an external jar you also have to add WebUI Checker and OpenCV dependencies to your pom.xml like this:

    <dependency>
        <groupId>ro.altom</groupId>
        <artifactId>web-ui-checker</artifactId>
        <version>1.0</version>
        <scope>system</scope>
        <systemPath>${basedir}/path/to/WebUIChecker/jar</systemPath>
    </dependency>

    <dependency>
        <groupId>org.openpnp</groupId>
        <artifactId>opencv</artifactId>
        <version>2.4.13-0</version>
    </dependency>

======================================================================================================

The default paths for saving baseline images and screenshots are `img/` and `target/reports/screenshots/`. <br/>
If you will want to use other path than default one for baselines you can configure it as follows:

* create `properties` package, same level as src
* create `config.properties` file in `properties` package
* set the path for saving baseline images `path.baseline` in config file for e.g. `path.baseline = img/`

#### Note: Baselines are needed or created during first run. 
#### Tests will first fail, giving the `WARNING` message `Image not found, creating baseline`.

======================================================================================================

### Screenshot could be:
* `ElementScreenshot`
* `ViewPortScreenshot` (user's visible area of a web page)
* `FullPageScreenshot` (multiple view port screenshots, parts of an entire webpage)

### Response could be:
* `binary` (true / false - 10 or more pixels are different) --> returns boolean
* `percentage` (x % different)            --> returns double
* `pixel` (number of different pixels)    --> returns int

Only when using FullPagePartsScreenshot
* `binaryMultiple`                        --> returns list of boolean values
* `percentageMultiple`                    --> returns list of double values
* `pixelMultiple`                         --> returns list of int values


======================================================================================================

#### For reporting new `bugs` and malfunction please use the GitLab Issue Tracker - https://gitlab.com/altom/WebUIChecker/issues

======================================================================================================

## How to use?

    e.g.

    1. First you will need a specific type of screenshot.
       Instantiate a screenshot object using:

* new ElementScreenshot(WebDriver driver, By bySelector, String imageName);
* new ViewPortScreenshot(WebDriver driver, String imageName);
* new FullPageScreenshot(WebDriver driver, String imageName);

    where imageName represents the desired name for your screenshot for e.g. "google"

    2. The second step is to get `responses` from UICheck comparison and make assertions on them.

       2.1. If you choose `FullPageScreenshot` then you'll get a list of responses.
            You can access them as follows:

* List<Boolean> resultList = UICheck.binaryMultiple(screenshot);
* List<Double> resultList = UICheck.percentageMultiple(screenshot);
* List<Integer> resultList = UICheck.pixelMultiple(screenshot);

When using full page capture, the suggested methods for getting responses should contain the word `multiple`.

       2.2. If you choose `ElementScreenshot` or `ViewPortScreenshot` you'll get a single response:
       
* boolean result = UICheck.binary(screenshot);
* double result = UICheck.percentage(screenshot);
* int result = UICheck.pixel(screenshot);

Also you can see your diff image at `target/reports/screenshots/` or attached in Allure report for each test.

Note: A diff image contains `baseline + currentScreenshot + diff`.

======================================================================================================