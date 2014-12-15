package files;

import java.io.File;
import java.io.FileInputStream;

public class FindNewLineInDir {

    public static void main(String[] args) throws Exception {
        File dir = new File("C:\\Users\\allenwu\\git\\kangaroo\\src");
        listDir("", dir);
    }
    
    public static void listDir(String base, File dir) throws Exception {
        //System.out.println("process " + base);
        File[] files = dir.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                listDir(base + "/" + f.getName(), f);
                continue;
            }
            if (f.getName().endsWith(".jar")) {
                continue;
            }
            try (FileInputStream fis = new FileInputStream(f)){
                byte[] buf = new byte[2048];
                fis.read(buf);
                if (new String(buf).contains("\r\n")) {
                    System.out.println(base + "/" + f.getName() + " is not windows new line!");
                }
            }
        }
    }
}
