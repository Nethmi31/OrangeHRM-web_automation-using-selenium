package com.orangehrm.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentReportListener implements ITestListener {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        if (extent == null) {
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter("reports/TestReport.html");
            sparkReporter.config().setDocumentTitle("OrangeHRM Test Report");
            sparkReporter.config().setReportName("OrangeHRM Automation Results");
            sparkReporter.config().setTheme(Theme.STANDARD);

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Application", "OrangeHRM");
            extent.setSystemInfo("Browser", "Chrome");
            extent.setSystemInfo("OS", System.getProperty("os.name"));
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getDescription();
        if (testName == null || testName.isEmpty()) {
            testName = result.getMethod().getMethodName();
        }

        ExtentTest extentTest = extent.createTest(testName);

        String[] groups = result.getMethod().getGroups();
        if (groups.length > 0) {
            extentTest.assignCategory(groups);
        }

        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.get().log(Status.FAIL, "Test failed");
        test.get().fail(result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().log(Status.SKIP, "Test skipped");
        if (result.getThrowable() != null) {
            test.get().skip(result.getThrowable());
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
        }
    }
}
