package ebcdic;

import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class ReadEBCDIC {

    public static void main(String[] args) throws Exception {
        InputStream is = ReadEBCDIC.class.getResourceAsStream("NEGOSETL.20130531171355");
        byte[] data = IOUtils.toByteArray(is);
        System.out.println(new String(data, "cp937"));
        //System.out.println(baos.toString("cp937"));
        
        FileOutputStream fos = new FileOutputStream("c:/test.txt");
        fos.write(new String(data, "cp937").getBytes("big5"));
        fos.flush();
        fos.close();
        
//        BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("cp937")));
//        String line = null;
//        while((line = br.readLine()) != null) {
//            System.out.println(line);
//        }
    }
    
}
