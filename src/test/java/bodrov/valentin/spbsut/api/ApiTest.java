package bodrov.valentin.spbsut.api;

import bodrov.valentin.spbsut.api.steps.TestSteps;
import org.testng.annotations.Test;

import java.awt.image.BufferedImage;

/**
 * This class provides some functional tests with
 * test scenarios.
 */
public class ApiTest extends AbstractTest {

    @Test(description = "loading from local drive -> greyscaling -> saving")
    public void testLocalLoadingAndGreyScaling() {
        BufferedImage openImage = TestSteps.openLocalFile(FILENAME);
        TestSteps.localFileShouldBeOpened(openImage, FILENAME);

        BufferedImage greyscaledImage = TestSteps.makeImageGreyScaled(openImage);

        boolean isSaved = TestSteps.savePictureAs(greyscaledImage, NEWFILENAME);
        TestSteps.fileShouldBeSaved(isSaved, NEWFILENAME);
        TestSteps.fileShouldExist(NEWFILENAME);
    }

    @Test(description = "loading from URL -> mirroring -> rotating -> saving")
    public void testUrlLoadingAndMirroring() {
        BufferedImage openImage = TestSteps.openUrlFile(WEBSITE);
        TestSteps.urlFileShouldBeOpened(openImage, WEBSITE);

        BufferedImage mirroredImage = TestSteps.makeImageHorizontallyMirrored(openImage);
        BufferedImage leftRotatedImage = TestSteps.makeImageLeftRotated(mirroredImage);

        boolean isSaved = TestSteps.savePictureAs(leftRotatedImage, NEWFILENAME);
        TestSteps.fileShouldBeSaved(isSaved, NEWFILENAME);
        TestSteps.fileShouldExist(NEWFILENAME);
    }

    @Test(description = "loading from local -> negative -> double rotating -> saving")
    public void testLocalLoadingNegativeAndRotatingTwice() {
        BufferedImage openImage = TestSteps.openLocalFile(FILENAME);
        TestSteps.localFileShouldBeOpened(openImage, FILENAME);

        BufferedImage negativedImage = TestSteps.makeImageNegative(openImage);
        BufferedImage rightRotatedImage = TestSteps.makeImageRightRotated(negativedImage);
        BufferedImage doubleRotatedImage = TestSteps.makeImageRightRotated(rightRotatedImage);

        boolean isSaved = TestSteps.savePictureAs(doubleRotatedImage, NEWFILENAME);
        TestSteps.fileShouldBeSaved(isSaved, NEWFILENAME);
        TestSteps.fileShouldExist(NEWFILENAME);
    }

    @Test(description = "loading from url -> sepia -> negative -> vertical mirroring -> saving")
    public void testUrlLoadingSepiaNegativeAndMirroring() {
        BufferedImage openImage = TestSteps.openUrlFile(WEBSITE);
        TestSteps.urlFileShouldBeOpened(openImage, WEBSITE);

        BufferedImage sepiaImage = TestSteps.makeImageSepia(openImage);
        BufferedImage negativedImage = TestSteps.makeImageNegative(sepiaImage);
        BufferedImage mirroredImage = TestSteps.makeImageVerticallyMirrored(negativedImage);

        boolean isSaved = TestSteps.savePictureAs(mirroredImage, NEWFILENAME);
        TestSteps.fileShouldBeSaved(isSaved, NEWFILENAME);
        TestSteps.fileShouldExist(NEWFILENAME);
    }

}
