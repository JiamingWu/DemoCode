package other;

import java.io.InputStream;

public class ReadFile {

    public static void main(String[] args) throws Exception {
        InputStream is = ReadFile.class.getResourceAsStream("test.txt");
        byte[] buf = new byte[1024];
        int len = is.read(buf, 0, buf.length);
        String str = new String(buf, 0, len);
        System.out.println(len + "," + str);
        
        byte[] big5bytes = str.getBytes("big5");
        String col1 = new String(big5bytes, 0, 20, "big5");
        System.out.println(col1);
        
    }
}
