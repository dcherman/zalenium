package de.zalando.ep.zalenium.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DashboardTest {

    @Before
    public void initDashboard() throws IOException {
        Dashboard.setExecutedTests(0, 0);
        removeTestingVideosFolder();
        ensureRequiredInputFilesExist();
    }

    private void ensureRequiredInputFilesExist() throws IOException {
        createEmptyFileIfMissing("list_template.html");
        createEmptyFileIfMissing("dashboard_template.html");
        createEmptyFileIfMissing("zalando.ico");
        createEmptyDirIfMissing("css");
        createEmptyDirIfMissing("js");
    }

    private void createEmptyDirIfMissing(String dirname) {
        File toBeChecked = fileWithLocalPath(dirname);
        if (!toBeChecked.exists()) {
            toBeChecked.mkdir();
        }
    }

    private File fileWithLocalPath(String name) {
        return new File(new CommonProxyUtilities().currentLocalPath() + "/" + name);
    }

    private void createEmptyFileIfMissing(String filename) throws IOException {
        File toBeChecked = fileWithLocalPath(filename);
        if (!toBeChecked.exists()) {
            toBeChecked.createNewFile();
        }
    }

    @After
    public void removeTestingVideosFolder() {
        String videosPath = new CommonProxyUtilities().currentLocalPath() + "/" + Dashboard.VIDEOS_FOLDER_NAME;
        File videos = new File(videosPath);
        FileUtils.deleteQuietly(videos);
    }

    @Test
    public void testCountOne() throws IOException {
        TestInformation ti = createDefaultTestInformation();

        Dashboard.updateDashboard(ti);

        Assert.assertEquals(1, Dashboard.getExecutedTests());
        Assert.assertEquals(1, Dashboard.getExecutedTestsWithVideo());
    }

    @Test
    public void testCountTwo() throws IOException {
        TestInformation ti = createDefaultTestInformation();
        Dashboard.updateDashboard(ti);

        Dashboard.updateDashboard(ti);

        Assert.assertEquals(2, Dashboard.getExecutedTests());
        Assert.assertEquals(2, Dashboard.getExecutedTestsWithVideo());
    }

    @Test
    public void missingExecutedTestsFile() throws IOException {
        TestInformation ti = createDefaultTestInformation();
        Dashboard.updateDashboard(ti);
        removeTestingVideosFolder();

        Dashboard.updateDashboard(ti);

        Assert.assertEquals(1, Dashboard.getExecutedTests());
        Assert.assertEquals(1, Dashboard.getExecutedTestsWithVideo());
    }

    private TestInformation createDefaultTestInformation() {
        return new TestInformation("seleniumSessionId", "testName", "proxyName", "browser", "browserVersion",
                "platform");
    }
}
