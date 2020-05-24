package bodrov.valentin.spbsut.utils;

/**
 * This class provides an exception for the situation when there's no
 * processed image.
 */
public class NoProcessedImageException extends Exception {

    public static final String NO_PROCESSED_IMAGE = "There's no processed image";

    public NoProcessedImageException() {
        super(NO_PROCESSED_IMAGE);
    }
}
