# - WebUI Checker -
### An Automated Visual Web Checking tool


What's WebUI Checker?
* Library easy to import in projects which gives us the possibility to verify the GUI of a Web Application (visual check)

======================================================================================================

Preconditions
* Use Selenium WebDriver and Java

======================================================================================================

Adding WebUI Checker to your test project
* import external jar for WebUI Checker library


The default paths for saving baseline images and screenshots are `img/` and `target/reports/screenshots/`. <br/>
If you will want to use other path than default one for baselines you can configure it as follows:


* create `properties` package, same level as src
* create `config.properties` file in `properties` package
* set the path for saving baseline images `path.baseline` in config file for e.g. `path.baseline = img/`

If you are using Maven don't forget to add WebUI Checker dependency to your pom.xml:

<dependency>

    <groupId>groupId</groupId>
    
    <artifactId>artifactId</artifactId>
    
    <version>0.1</version>
    
    <scope>system</scope>
    
    <systemPath>${basedir}/path/to/WebUIChecker/jar</systemPath>
    
</dependency>

Also because we use OpenCV library when comparing images you'll have to add OpenCV dependency to your pom:

<dependency>

    <groupId>org.openpnp</groupId>

    <artifactId>opencv</artifactId>

    <version>2.4.13-0</version>

</dependency>

### Note: Baselines are needed or created during first run. Tests will first fail, giving the `WARNING` message `Image not found, creating baseline`.
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





