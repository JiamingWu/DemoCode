package mailEncoding;

public class QP {
    private static final char per = '=';
    
    private QP(){}
    
    public static byte[] decode(char[] data){
        int len = 0;
        int index = 0;
        int tmp;
        
        //算出長度
        for(int i=0; i<data.length; i++){
            if(data[i]==per)
                i+=2;
            len++;
        }
        
        //解碼
        byte[] dec = new byte[len];
        for(int i=0; i<len; i++){
            if(data[index]==per){
                dec[i] = (byte) Integer.parseInt(String.valueOf(data[index+1])+String.valueOf(data[index+2]),16);
                index+=3;
            }else{
                dec[i] = (byte) data[index];
                index++;
            }
        }
        
        return dec;
    }
    
    public static char[] encode(byte[] data){
        int len = 0;
        int index = 0;
        int tmp;
        
        //算出長度        
        for(int i=0; i<data.length; i++)
            len += (data[i] & 0x80) == 0x80? 3: 1;
        
        //編碼
        char[] enc = new char[len];
        for(int i=0; i<data.length; i++){
            tmp = data[i]<0? data[i]+256: data[i];
            if((tmp & 0x80) == 0x80){
                enc[index++] = per;
                enc[index++] = Character.toUpperCase(Integer.toHexString(tmp>>4).charAt(0));
                enc[index++] = Character.toUpperCase(Integer.toHexString(0x0F & tmp).charAt(0));
            }else{
                enc[index++] = (char) tmp;
            }
        }
        
        return enc;
    }

    public static void main(String[] args){
        String a = "摘自5/25的商業週刊電子報";
        char[] b = QP.encode(a.getBytes());
        char[] d = "=B3=D5=B0T=BB{=C3=D2=A4=A4=A4=DFEntrust 128bit SSL=B4=A3=A8=D17=A7=E9=AFS=A7O=C0u=B4f".toCharArray();
        b=d;
        byte[] c = QP.decode(b);
        
        try{
            System.out.println (new String(c,"Big5"));
        }catch(Exception e){}
        
    }
}