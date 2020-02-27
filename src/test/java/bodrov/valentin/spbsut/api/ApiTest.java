package bodrov.valentin.spbsut.api;

import bodrov.valentin.spbsut.api.steps.TestSteps;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.awt.image.BufferedImage;

public class ApiTest {

    private static String FILENAME = "src/test/resources/images/violets.jpg";
    private static String NEWFILENAME = "src/test/resources/images/new_file.png";
    private static String WEBSITE = "https://images-na.ssl-images-amazon.com/images/I/716-9OEYC5L._AC_SY450_.jpg";

    @Test
    public void testOpenLocal() {
        BufferedImage openImage = TestSteps.openLocalFile(FILENAME);
        TestSteps.localFileShouldBeOpened(openImage, FILENAME);
    }

    @Test
    public void testOpenUrl() {
        BufferedImage openImage = TestSteps.openUrlFile(WEBSITE);
        TestSteps.urlFileShouldBeOpened(openImage, WEBSITE);
    }

    @Test
    public void testSavePicturesAs() {
        boolean isSaved = TestSteps.savePictureAs(TestSteps.openLocalFile(FILENAME), NEWFILENAME);
        TestSteps.fileShouldBeSaved(isSaved, NEWFILENAME);
        TestSteps.fileShouldExist(NEWFILENAME);
    }

    @Test(description = "loading -> processing -> saving")
    public void testTheFistScenario() {
        /*
            loading
         */
        BufferedImage openImage = TestSteps.openLocalFile(FILENAME);
        TestSteps.localFileShouldBeOpened(openImage, FILENAME);

        /*
            processing
         */
        BufferedImage greyscaledImage = TestSteps.makeImageGreyScaled(openImage);
        /*
            saving
         */
        boolean isSaved = TestSteps.savePictureAs(greyscaledImage, NEWFILENAME);
        TestSteps.fileShouldBeSaved(isSaved, NEWFILENAME);
        TestSteps.fileShouldExist(NEWFILENAME);
    }

    @AfterClass(enabled = false)
    public void tearDown() {
        TestSteps.deleteFile(NEWFILENAME);
    }

}
