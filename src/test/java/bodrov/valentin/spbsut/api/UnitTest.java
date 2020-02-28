package bodrov.valentin.spbsut.api;

import bodrov.valentin.spbsut.api.steps.TestSteps;
import org.testng.annotations.Test;

import java.awt.image.BufferedImage;

public class UnitTest extends AbstractTest {

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

    @Test
    public void testGreyScale() {
        BufferedImage greyscaledImage = TestSteps.makeImageGreyScaled(TestSteps.openLocalFile(FILENAME));
        boolean isSaved = TestSteps.savePictureAs(greyscaledImage, NEWFILENAME);
        TestSteps.fileShouldBeSaved(isSaved, NEWFILENAME);
    }

    @Test
    public void testSepia() {
        BufferedImage sepiaImage = TestSteps.makeImageSepia(TestSteps.openLocalFile(FILENAME));
        boolean isSaved = TestSteps.savePictureAs(sepiaImage, NEWFILENAME);
        TestSteps.fileShouldBeSaved(isSaved, NEWFILENAME);
    }

    @Test
    public void testNegative() {
        BufferedImage negativeImage = TestSteps.makeImageNegative(TestSteps.openLocalFile(FILENAME));
        boolean isSaved = TestSteps.savePictureAs(negativeImage, NEWFILENAME);
        TestSteps.fileShouldBeSaved(isSaved, NEWFILENAME);
    }

    @Test
    public void testVerticalMirroring() {
        BufferedImage verticallyMirroredImage = TestSteps.makeImageVerticallyMirrored(TestSteps.openLocalFile(FILENAME));
        boolean isSaved = TestSteps.savePictureAs(verticallyMirroredImage, NEWFILENAME);
        TestSteps.fileShouldBeSaved(isSaved, NEWFILENAME);
    }

    @Test
    public void testHorizontalMirroring() {
        BufferedImage horizontallyMirroredImage = TestSteps.makeImageHorizontallyMirrored(TestSteps.openLocalFile(FILENAME));
        boolean isSaved = TestSteps.savePictureAs(horizontallyMirroredImage, NEWFILENAME);
        TestSteps.fileShouldBeSaved(isSaved, NEWFILENAME);
    }

    @Test
    public void testLeftRotating() {
        BufferedImage leftRotatedImage = TestSteps.makeImageLeftRotated(TestSteps.openLocalFile(FILENAME));
        boolean isSaved = TestSteps.savePictureAs(leftRotatedImage, NEWFILENAME);
        TestSteps.fileShouldBeSaved(isSaved, NEWFILENAME);
    }

    @Test
    public void testRightRotating() {
        BufferedImage rightRotatedImage = TestSteps.makeImageRightRotated(TestSteps.openLocalFile(FILENAME));
        boolean isSaved = TestSteps.savePictureAs(rightRotatedImage, NEWFILENAME);
        TestSteps.fileShouldBeSaved(isSaved, NEWFILENAME);
    }

}
