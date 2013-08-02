package encoding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EncodingSolution {

    public static void main(String[] args) {
        
        //若字碼範圍不在 MS950 內, 則瀏覽器會自動轉成 unicode 表示法, 如 "菓" 字, 瀏覽器會自動轉成 &#33747; 
        //但若在 MS950 內就不會自動轉,
        //由於我們系統xslt時都用 BIG5, 使用MS950會有問題, 
        //所以若在 BIG5 之外, MS950 之內的字 (MS950比Big5多出 "碁恒裏粧嫺銹墻" 等七個字, 範圍落在 F9D6~F9DC 之間)
        //必須手動轉成 unicode 表示法
        System.out.println(toUnicode("宏碁不銹鋼公司"));
        
        //在網頁上使用 unicode 表示法, 不會有問題.
        //問題在我們 enid 使用 xslt 的 solution, 會將 & 符跳轉成 &amp; (由於 & 是跳脫符號)
        //所以在xslt轉完之後必須使用以下的方式將跳脫符號轉回來
        System.out.println("宏&amp;#30849;不&amp;#37561;鋼公司".replaceAll("&amp;#", "&#"));
        
        //由於資料存的是 unicode 表示法, 傳到 PDF 會直接顯示該表示法, 
        //所以必須把unicode表示法轉回來成字元
        System.out.println(filterUincode("宏&#30849;不&#37561;鋼公司"));
        
    }
    
    public static String toUnicode(String body) {
        try {
            StringBuffer sb = new StringBuffer(body);
            //找出 big5 碼在 F9D6 ~ F9DC 間的碼轉成 unicode 表示法
            for (int i = body.length() - 1 ; i >= 0; i--) {
                String v = body.substring(i, i+1);
                byte[] data = v.getBytes("MS950");
                if ((0x0FF & data[0]) == 0x0F9 && 
                        ((0x0FF & data[1]) >= 0x0D6 || 
                                (0x0FF & data[1]) <= 0x0DC)) {
                    data = v.getBytes("UTF16");
                    int value = ((0x0FF & data[2]) << 8) + (0x0ff & data[3]);
                    sb.replace(i, i+1, "&#" + value + ";");
                }
            }
            return sb.toString();
        } catch (Exception e) {
            return body;
        }
    }

    public static String filterUincode(String body) {
        try {
            StringBuffer sb = new StringBuffer(body);
            sb.indexOf("&#");
            Pattern p = Pattern.compile("\\d*");
            int startIdx = 0;
            while (true) {
                int sidx = sb.indexOf("&#", startIdx);
                if (sidx == -1) {
                    break;
                }
                int eidx = sb.indexOf(";", sidx);
                if (eidx == -1) {
                    break;
                }
                String value = sb.substring(sidx + 2, eidx);
                Matcher m = p.matcher(value);
                if (m.matches()) {
                    Integer intV = Integer.decode(value);
                    String unicode = Integer.toHexString(intV.intValue()); 
                    byte[] unicodeBytes = convertHexToInt(unicode);
                    String unicodeString = new String(unicodeBytes, "utf-16");
                    sb.replace(sidx, eidx + 1, unicodeString);
                    startIdx = sidx + 1;
                } else {
                    startIdx = eidx + 1;
                }
            }
            return sb.toString();
        } catch (Exception e) {
            return body;
        }
    }
    
    private static byte[] convertHexToInt(String value) {
        byte[] key_value = new byte[value.length()/2];
        byte[] temp = value.getBytes();
        
        for (int i = 0 ; i < temp.length; i++) {
            if ((temp[i] >= 48) && temp[i] <= 57) {        //0 ~ 9
                temp[i] = (byte) (temp[i] - 48);
            } else if (temp[i] >= 65 && temp[i] <= 70) {   //A ~ F
                temp[i] = (byte) (temp[i] - 65 + 10);
            } else if (temp[i] >= 97 && temp[i] <= 102) {  //a ~ f
                temp[i] = (byte) (temp[i] - 97 + 10);
            }
        }
        
        for (int i = 0 ; i < key_value.length; i++) {
            key_value[i] = (byte) (temp[i*2] * 16 + temp[i*2+1]);
        }
        return key_value;
    }
}
