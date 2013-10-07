package mailEncoding;

public class Base64 {
    private static final String AlphabetMap = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    private static final char pad = '=';
    
    private Base64(){}
    
    public static byte[] decode(char[] data){
        int len;
        int loops;
        int tmp;
        byte[] dec;
        byte[] three = new byte[3];
        int[] four = new int[4];
        
        //算出解碼後長度
        len = data.length*6/8;
        if(data[data.length-2] == pad)
            len -= 2;
        else if(data[data.length-1] == pad)
            len--;
        dec = new byte[len];
        
        //解碼
        loops = data.length/4;
        for(int i=0; i<loops; i++){
            for(int j=0; j<4; j++){
                four[j] = AlphabetMap.indexOf(String.valueOf(data[i*4+j]));
                if(four[j]==-1)
                    four[j] = 0;
            }
            three[0] = (byte)((four[0]<<2) | (four[1]>>4));
            three[1] = (byte)((four[1]<<4) | (four[2]>>2));
            three[2] = (byte)((four[2]<<6) | (four[3]));
            
            dec[i*3] = three[0];
            if(i<loops-1 || data[data.length-2] != pad)
                dec[i*3+1] = three[1];
            if(i<loops-1 || data[data.length-1] != pad)
                dec[i*3+2] = three[2];
        }
        
        return dec;
    }
    
    public static char[] encode(byte[] data){
        int len;
        int tmp;
        int loops;
        int pad_len;
        char[] enc;
        byte[] three = new byte[3];
        int[] four = new int[4];
        
        //算出編碼後長度(包括 pad)
        tmp = data.length%3;
        pad_len = 0;
        if(tmp == 1)
            pad_len = 2;
        else if(tmp == 2)
            pad_len = 1;
        len = (data.length*8/6) + ((data.length*8)%6>0?1:0) + pad_len;
        enc = new char[len];

        //編碼
        loops = len/4;
        for(int i=0; i<loops; i++){
            three[0] = data[i*3];
            three[1] = 0;
            three[2] = 0;
            if(i<loops-1 || pad_len < 2)
                three[1] = data[i*3+1];
            if(i<loops-1 || pad_len < 1)
                three[2] = data[i*3+2];

            four[0] = 0x3F & (three[0]>>>2);
            four[1] = (0x3  & three[0])<<4 | (0x0F & (three[1]>>>4));
            four[2] = (0x0F & three[1])<<2 | (0x03 & (three[2]>>>6));
            four[3] = 0x3F & three[2];
            
            for(int j=0; j<4; j++)
                enc[i*4+j] = AlphabetMap.charAt(four[j]);
        }
        
        //加上最後面的 padding char
        for(int i=0; i<pad_len; i++)
            enc[len-1-i] = pad;
        
        return enc;
    }

    public static void main(String[] args){
        /*
        try{
            File f = new File("test.doc");
            FileInputStream fis = new FileInputStream(f);
            int len = (int)f.length();
            byte[] data = new byte[len];
            fis.read(data);
            char[] result = Base64.encode(data);
            byte[] result2 = Base64.decode(result);
            result = Base64.encode(result2);
            
            //印出結果
            for (int i=0; i<result.length/76; i++){
                for(int j=0; j<76; j++)
                    System.out.print (result[i*76+j]);
                System.out.println ();
            }
            //印出最後一行
            if(result.length%76>0){
                for(int i=0; i<result.length%76; i++){
                    System.out.print (result[result.length-result.length%76+i]);
                }
            }
        }catch(Exception e){}
        */
        String a = "Fw: 【May】我的妳〔三〕";
        System.out.println (Base64.encode(a.getBytes()));
        char[] b = {'R','n','c','6','I','K','F','p','T','W','F','5','o','W','q','n','2','q','q','6','q','X','C','h','Z','a','R','U','o','W','Y','='};
        char[] d = {'P','z','8','/','P','z','8','/','P','z','8','/','P','z','8','/','P','y','A','y','M','D','A','y','P','z','A','2','P','z','8','/','?','='};
        b=d;
        try{
            System.out.println (new String(Base64.decode(b),"iso-8859-1"));
        }catch(Exception e){}
    }
}