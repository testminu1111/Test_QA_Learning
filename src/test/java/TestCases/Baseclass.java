package TestCases;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Baseclass {

    public static ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();
    public static WebDriverWait webDriverWait;
    public static ExtentReports extent;
    public static ExtentTest test;
    public static ExtentSparkReporter sparkreport;


    @BeforeSuite
    public static void lunchApplication() {
        // WebDriverManager.chromedriver().setup();
        Map<String, Object> prefs = new HashMap<>();// Allows Camera Permission for chrome
        prefs.put("profile.default_content_setting_values.media_stream_camera", 1);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--disable-notifications");
        options.addArguments("--ignore-ssl-errors=yes");
        options.addArguments("--ignore-certificate-errors");
        webDriver.set(new ChromeDriver(options));

        webDriver.get().manage().window().maximize();
        webDriver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        webDriverWait = new WebDriverWait(webDriver.get(), Duration.ofSeconds(60));
        loadPropertiesFile();

        extent = new ExtentReports();
        sparkreport = new ExtentSparkReporter(
                "sparkReport.html");
        extent.attachReporter(sparkreport);
        extent.flush();
//
//        sparkreport.config().setOfflineMode(true);
//        sparkreport.config().setDocumentTitle("Simple Automation Report");
//        sparkreport.config().setReportName("Test Report");
//        sparkreport.config().setTheme(Theme.STANDARD);
//        sparkreport.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
//        sparkreport.config().setEncoding("UTF-8");
    }

 /*    @Parameters("browser")
     @BeforeTest
    public void launchApplication(String browser) {
        if (browser.equalsIgnoreCase("chrome")) {
            System.out.println("Executing on Chrome");
            //          WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            webDriver.set(new ChromeDriver(options));

        } else if (browser.equalsIgnoreCase("edge")) {
            System.out.println("Executing on Edge");
            //          WebDriverManager.edgedriver().setup();
            EdgeOptions options = new EdgeOptions();

            webDriver.set(new EdgeDriver(options));
        } else {
            throw new IllegalArgumentException("The Browser Type is Undefined");
        }

        webDriver.get().manage().window().maximize();
        webDriver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        webDriverWait = new WebDriverWait(webDriver.get(), Duration.ofSeconds(60));
        loadPropertiesFile();

        sparkreport = new ExtentSparkReporter("Extentreport.html");
        extent = new ExtentReports();
        extent.attachReporter(sparkreport);
    }
*/

    protected static void loadPropertiesFile() {
        // load property file (test.properties)
        Properties props = System.getProperties();
        try {
            FileInputStream fileInputStream = new FileInputStream(new File("src/main/resources/test.properties"));
            props.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @AfterMethod
    public void takeScreenshotForFailingTests(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            test.fail(MarkupHelper.createLabel(result.getName() + " testcase failed", ExtentColor.RED));
            test.fail(result.getThrowable());
            captureScreenshot(result.getName());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.pass(MarkupHelper.createLabel(result.getName() + " testcase passed", ExtentColor.GREEN));
            test.pass(result.getThrowable());
        } else {
            test.skip(MarkupHelper.createLabel(result.getName() + " testcase skipped", ExtentColor.YELLOW));
            test.skip(result.getThrowable());
        }
    }
//
//    private void captureScreenshot(String testName) {
//        try {
//            // Save current time
//            String strDate = LocalDate.now().toString() + " " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH-mm"));
//            // Initialize screenshot class
//            TakesScreenshot screenshot = (TakesScreenshot) webDriver.get();
//            // Select output file
//            File src = screenshot.getScreenshotAs(OutputType.FILE);
//            // Create file in a specified directory
//            String screenshotPath = System.getProperty("path.FailingScreenshots") + "/" + testName + "_" + strDate + ".png";
//            FileUtils.copyFile(src, new File(screenshotPath));
//            System.out.println("Successfully captured a screenshot: " + screenshotPath);
//        } catch (IOException e) {
//            System.out.println("Exception while taking screenshot: " + e.getMessage());
//        }
//    }
    private void captureScreenshot(String testName) {
        try {
            // Save current time
            String strDate = LocalDate.now().toString() + " " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH-mm"));

            // Get the screenshot directory from system property or fallback to default
            String basePath = System.getProperty("path.FailingScreenshots", "screenshots");

            // Create the directory if it does not exist
            File screenshotDir = new File(basePath);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            // Take screenshot
            TakesScreenshot screenshot = (TakesScreenshot) webDriver.get();
            File src = screenshot.getScreenshotAs(OutputType.FILE);

            // Build full path
            String screenshotPath = basePath + "/" + testName + "_" + strDate + ".png";

            // Copy file
            FileUtils.copyFile(src, new File(screenshotPath));

            System.out.println("Successfully captured a screenshot: " + screenshotPath);
        } catch (IOException e) {
            System.out.println("Exception while taking screenshot: " + e.getMessage());
        }
    }


//
//    @AfterSuite
//    public void teardown() {
//        extent.flush();

 //   }
}
