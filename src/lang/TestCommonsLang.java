package lang;

import org.apache.commons.lang.WordUtils;

public class TestCommonsLang {

    public static void main(String[] args) {
        String str = "This is book!";
        String s = WordUtils.wrap(str, 10, "\r\n", true);
        System.out.println(s);
    }
    
}
