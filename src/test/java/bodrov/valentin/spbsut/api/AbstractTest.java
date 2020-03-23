package bodrov.valentin.spbsut.api;

import bodrov.valentin.spbsut.api.steps.TestSteps;
import org.testng.annotations.AfterClass;

public class AbstractTest {

    static String FILENAME = "src/test/resources/images/violets.jpg";
    static String NEWFILENAME = "src/test/resources/images/new_file.png";
    static String WEBSITE = "https://sun9-3.userapi.com/c635106/v635106468/3850c/co6DwNifebg.jpg";

    @AfterClass()
    public void tearDown() {
        TestSteps.deleteFile(NEWFILENAME);
    }

}
