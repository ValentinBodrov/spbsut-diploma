package bodrov.valentin.spbsut.api.steps;

import bodrov.valentin.spbsut.api.ImageProcessingApi;
import org.testng.Assert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TestSteps {

    public static BufferedImage openLocalFile(String filename) {
        return ImageProcessingApi.openLocal(filename);
    }

    public static BufferedImage openUrlFile(String website) {
        return ImageProcessingApi.openUrl(website);
    }

    public static boolean savePictureAs(BufferedImage oldImage, String newFilename) {
        return ImageProcessingApi.savePictureAs(oldImage, newFilename);
    }

    public static void localFileShouldBeOpened(BufferedImage openImage, String filename) {
        Assert.assertNotNull(openImage, String.format("The file %s cannot be opened!", filename));
    }

    public static void urlFileShouldBeOpened(BufferedImage openImage, String website) {
        Assert.assertNotNull(openImage, String.format("The link %s cannot provide an image!", website));
    }

    public static void fileShouldBeSaved(boolean isSaved, String newFilename) {
        Assert.assertTrue(isSaved, String.format("The file %s cannot be saved!", newFilename));
    }

    public static void fileShouldExist(String newFilename) {
        boolean exists = new File(newFilename).exists();
        Assert.assertTrue(exists, String.format("The file %s doesn't exist!", newFilename));
    }

    public static void deleteFile(String newFilename) {
        File f = new File(newFilename);
        if (f.delete()) {
            System.out.println("deleted");
        }
    }

    public static BufferedImage makeImageGreyScaled(BufferedImage openImage) {
        return ImageProcessingApi.doGreyScale(
                openImage, 0, 0, openImage.getWidth(), openImage.getHeight());
    }
}
