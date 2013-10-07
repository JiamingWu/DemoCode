package unicode;

/*
將字串轉成unicode的編碼程式

ex:
中(A4A4) --> 4e2d
文(A4E5) --> 6587   base 10  (164, 229) --> (101, 135)

為了給wml用,所以 "中" --> &#x4e2d;
*/
public class ToUnicode {

    public static void main(String[] args) {
        try{
            String temp = "中文自己的WAP手機";
            String a = unicode(temp);
            System.out.println(a);
        }catch(Exception e){System.out.println(e);}
    }
    
    public static String unicode(String temp){  //傳入的字串為預設編碼
        String uni="";
        try{
            char[] temp_char = new char[temp.length()];
            temp.getChars(0,temp.length(),temp_char,0);
            
            for(int i=0; i<temp.length(); i++){
                String tmp = "";
                if(temp_char[i]>>8 != 0)
                    tmp = Integer.toHexString(temp_char[i]>>8);
                tmp += Integer.toHexString(temp_char[i] & 0x00ff);
                uni += "&#x" + tmp + ";";
            }
        }catch(Exception e){System.out.println(e);}
        return uni;
    }
    
}
