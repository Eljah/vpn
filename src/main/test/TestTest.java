import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.File;
import java.io.FileFilter;

public class TestTest {
    public static void main(String[] args) {
        File dir = new File("test");

        FileFilter fileFilter = new WildcardFileFilter("*.ovpn");
        File[] files = dir.listFiles(fileFilter);
        for (File file: files) {
            System.out.println(file.getName());
        }
    }
}
