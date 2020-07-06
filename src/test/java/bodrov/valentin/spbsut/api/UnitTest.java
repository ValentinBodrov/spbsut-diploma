package bodrov.valentin.spbsut.api;

import bodrov.valentin.spbsut.api.steps.TestSteps;
import org.testng.annotations.Test;

import java.awt.image.BufferedImage;

/**
 * This class provides some unit tests for
 * all methods from source code
 */
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
    public void testRandomPixelation() {
        BufferedImage randomPixelationImage = TestSteps.makeImageRandomPixelated(TestSteps.openLocalFile(FILENAME));
        boolean isSaved = TestSteps.savePictureAs(randomPixelationImage, NEWFILENAME);
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

    @Test
    public void testEnhanceContrast() {
        BufferedImage enhancedImage = TestSteps.makeImageContrastEnhanced(TestSteps.openLocalFile(FILENAME));
        boolean isSaved = TestSteps.savePictureAs(enhancedImage, NEWFILENAME);
        TestSteps.fileShouldBeSaved(isSaved, NEWFILENAME);
    }

    @Test
    public void testEnhanceBrightness() {
        BufferedImage enhancedImage = TestSteps.makeImageBrightnessEnhanced(TestSteps.openLocalFile(FILENAME));
        boolean isSaved = TestSteps.savePictureAs(enhancedImage, NEWFILENAME);
        TestSteps.fileShouldBeSaved(isSaved, NEWFILENAME);
    }

    @Test
    public void testEnhanceSharpness() {
        BufferedImage enhancedImage = TestSteps.makeImageSharpnessEnhanced(TestSteps.openLocalFile(FILENAME));
        boolean isSaved = TestSteps.savePictureAs(enhancedImage, NEWFILENAME);
        TestSteps.fileShouldBeSaved(isSaved, NEWFILENAME);
    }

    @Test
    public void testGaussianBlur() {
        BufferedImage gaussianBlurredImage = TestSteps.makeImageGaussianBlurred(TestSteps.openLocalFile(FILENAME));
        boolean isSaved = TestSteps.savePictureAs(gaussianBlurredImage, NEWFILENAME);
        TestSteps.fileShouldBeSaved(isSaved, NEWFILENAME);
    }

    @Test
    public void testMedianBlur() {
        BufferedImage medianBlurredImage = TestSteps.makeImageMedianBlurred(TestSteps.openLocalFile(FILENAME));
        boolean isSaved = TestSteps.savePictureAs(medianBlurredImage, NEWFILENAME);
        TestSteps.fileShouldBeSaved(isSaved, NEWFILENAME);
    }

    @Test
    public void testBilateralFilter() {
        BufferedImage bilateralFilteredImage = TestSteps.makeImageBilateralFiltered(TestSteps.openLocalFile(FILENAME));
        boolean isSaved = TestSteps.savePictureAs(bilateralFilteredImage, NEWFILENAME);
        TestSteps.fileShouldBeSaved(isSaved, NEWFILENAME);
    }

    @Test
    public void testBoxFilter() {
        BufferedImage boxFilteredImage = TestSteps.makeImageBoxFiltered(TestSteps.openLocalFile(FILENAME));
        boolean isSaved = TestSteps.savePictureAs(boxFilteredImage, NEWFILENAME);
        TestSteps.fileShouldBeSaved(isSaved, NEWFILENAME);
    }

    @Test
    public void testSQRBoxFilter() {
        BufferedImage sqrBoxFilteredImage = TestSteps.makeImageSQRBoxFiltered(TestSteps.openLocalFile(FILENAME));
        boolean isSaved = TestSteps.savePictureAs(sqrBoxFilteredImage, NEWFILENAME);
        TestSteps.fileShouldBeSaved(isSaved, NEWFILENAME);
    }

    @Test
    public void test2DFilter() {
        BufferedImage filtered2dImage = TestSteps.makeImage2DFiltered(TestSteps.openLocalFile(FILENAME));
        boolean isSaved = TestSteps.savePictureAs(filtered2dImage, NEWFILENAME);
        TestSteps.fileShouldBeSaved(isSaved, NEWFILENAME);
    }

    @Test
    public void testPixelation() {
        BufferedImage pixelatedImage = TestSteps.makeImagePixelated(TestSteps.openLocalFile(FILENAME));
        boolean isSaved = TestSteps.savePictureAs(pixelatedImage, NEWFILENAME);
        TestSteps.fileShouldBeSaved(isSaved, NEWFILENAME);
    }

    @Test
    public void testForegroundObjectDetection() {
        BufferedImage imageWithForegroundObject = TestSteps.makeImageWithForegroundDetection(TestSteps.openLocalFile(FILENAME));
        boolean isSaved = TestSteps.savePictureAs(imageWithForegroundObject, NEWFILENAME);
        TestSteps.fileShouldBeSaved(isSaved, NEWFILENAME);
    }
    
}
