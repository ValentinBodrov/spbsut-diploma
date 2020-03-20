package bodrov.valentin.spbsut.utils;

public class NoProcessedImageException extends Exception {

    public static String NO_PROCESSED_IMAGE = "There's no processed image";

    public NoProcessedImageException() {
        super(NO_PROCESSED_IMAGE);
    }
}
