package des;

import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 自行參考演算法實作 DES
 */
public class DES {

    public static void main(String[] args) throws Exception {
        DES des = new DES();
        des.action();
    }
    
    public void action() throws Exception {
        
        SecretKey secretKey = genKey();
        byte[] key = secretKey.getEncoded();
        byte[] data = "一二三四五六七八".getBytes("big5");
        
        System.out.println("====== DES/ECB/NoPadding encrypt=======");
        byte[] result1 = des_ecb_noPadding(data, key, true);
        System.out.println(toHex(result1));
        
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");  
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] result2 = cipher.doFinal(data);
        System.out.println(toHex(result2));
        System.out.println("解密:" + new String(des_ecb_noPadding(result1, key, false), "big5"));
        
        System.out.println("====== DES/ECB/PKCS5Padding encrypt=======");
        data = "一二三四五六七八九".getBytes("big5");
        result1 = des_pkcs5Padding(null, data, key, true);
        System.out.println(toHex(result1));
        
        cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");  
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        result2 = cipher.doFinal(data);
        System.out.println(toHex(result2));
        System.out.println("解密:" + new String(des_pkcs5Padding(null, result1, key, false), "big5"));
        
        System.out.println("====== DES/CBC/PKCS5Padding encrypt=======");
        byte[] iv = "ABCD1234".getBytes();
        result1 = des_pkcs5Padding(iv, data, key, true);
        System.out.println(toHex(result1));
        
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
        cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
        result2 = cipher.doFinal(data);
        System.out.println(toHex(result2));
        System.out.println("解密:" + new String(des_pkcs5Padding(iv, result1, key, false), "big5"));
    }
    
    public byte[] xor(byte[] b1, byte[] b2) {
        byte[] result = new byte[8];
        for (int i=0; i<8; i++) {
            result[i] = (byte)(b1[i] ^ b2[i]);
        }
        return result;
    }
    
    public byte[] des_ecb_noPadding(byte[] data, byte[] key, boolean encrypt) throws Exception {
        return des(null, data, key, encrypt);
    }
    
    public byte[] des(byte[] iv, byte[] data, byte[] key, boolean encrypt) throws Exception {
        if (data.length % 8 != 0) {
            System.out.println("ERR:資料長度必須為8的倍數");
            return null;
        }
        byte[] block = new byte[8];
        byte[] result = new byte[data.length];
        for (int i=0; i<data.length/8; i++) {
            System.arraycopy(data, i*8, block, 0, 8);
            if (encrypt && iv != null) {
                block = xor(block, iv);
            }
            byte[] r = des(block, key, encrypt);
            if (iv != null) {
                if (encrypt) {
                    iv = r;
                } else {
                    r = xor(r, iv);
                    System.arraycopy(block, 0, iv, 0, 8);
                }
            }
            System.arraycopy(r, 0, result, i*8, 8);
        }
        return result;
    }
    
    /**
     * PKCS5Padding, 當一個 block 未滿 8 個 bytes, 如差 3 個 bytes, 則以 3 填滿.
     * 當剛好為 8 個 bytes, 則多補上一個 block, 以 8 填滿 (意即使用PKCS5Padding, 一定會有 padding)
     * 
     * PKCS7Padding 與 PKCS5Padding 相同, 但 PKCS7Padding 沒規定 block 一定只能 8 個 bytes.
     * 其 block 區塊的 bytes 是可變動的.
     */
    public byte[] des_pkcs5Padding(byte[] iv, byte[] data, byte[] key, boolean encrypt) throws Exception {
        byte[] newData = data;
        if (encrypt) {
            int blockSize = 8;
            int tmp = blockSize - data.length % blockSize;
            newData = new byte[data.length + tmp];
            Arrays.fill(newData, (byte)tmp);
            System.arraycopy(data, 0, newData, 0, data.length);
        }
        byte[] result = des(iv, newData, key, encrypt);
        if (!encrypt) {
            //去掉後面之 padding
            //System.out.println(toHex(result));
            int value = result[result.length-1];
            byte[] newResult = new byte[result.length-value];
            System.arraycopy(result, 0, newResult, 0, newResult.length);
            result = newResult;
        }
        return result;
    }
    
    /**
     * DES 加密區塊為 64 bits
     */
    public byte[] des(byte[] data, byte[] key, boolean encrypt) throws Exception {
        //將 data 與 key 展開為 bit[]
        byte[] tmp = new byte[data.length * 8];
        for (int i=0; i<data.length; i++) {
            tmp[i*8+0] = (byte)((data[i] & 128)>>> 7);
            tmp[i*8+1] = (byte)((data[i] & 64) >>> 6);
            tmp[i*8+2] = (byte)((data[i] & 32) >>> 5);
            tmp[i*8+3] = (byte)((data[i] & 16) >>> 4);
            tmp[i*8+4] = (byte)((data[i] & 8) >>> 3);
            tmp[i*8+5] = (byte)((data[i] & 4) >>> 2);
            tmp[i*8+6] = (byte)((data[i] & 2) >>> 1);
            tmp[i*8+7] = (byte)(data[i] & 1);
        }
        data = tmp;
//      System.out.print("data:");
//      for (int i=0;i<data.length;i++) {
//          System.out.print(data[i]);
//      }
//      System.out.println();
        tmp = new byte[key.length * 8];
        for (int i=0; i<key.length; i++) {
            tmp[i*8+0] = (byte)((key[i] & 128)>>> 7);
            tmp[i*8+1] = (byte)((key[i] & 64) >>> 6);
            tmp[i*8+2] = (byte)((key[i] & 32) >>> 5);
            tmp[i*8+3] = (byte)((key[i] & 16) >>> 4);
            tmp[i*8+4] = (byte)((key[i] & 8) >>> 3);
            tmp[i*8+5] = (byte)((key[i] & 4) >>> 2);
            tmp[i*8+6] = (byte)((key[i] & 2) >>> 1);
            tmp[i*8+7] = (byte)(key[i] & 1);
        }
        key = tmp;
        
        int[] ip = {58, 50, 42, 34, 26, 18, 10, 2,
                    60, 52, 44, 36, 28, 20, 12, 4,
                    62, 54, 46, 38, 30, 22, 14, 6,
                    64, 56, 48, 40, 32, 24, 16, 8,
                    57, 49, 41, 33, 25, 17,  9, 1,
                    59, 51, 43, 35, 27, 19, 11, 3,
                    61, 53, 45, 37, 29, 21, 13, 5,
                    63, 55, 47, 39, 31, 23, 15, 7};
        int[] fp = {40, 8, 48, 16, 56, 24, 64, 32,
                    39, 7, 47, 15, 55, 23, 63, 31,
                    38, 6, 46, 14, 54, 22, 62, 30,
                    37, 5, 45, 13, 53, 21, 61, 29,
                    36, 4, 44, 12, 52, 20, 60, 28,
                    35, 3, 43, 11, 51, 19, 59, 27,
                    34, 2, 42, 10, 50, 18, 58, 26,
                    33, 1, 41,  9, 49, 17, 57, 25};
        
        //1. 初始排列 IP
        byte[] newData = new byte[64];
        for (int i = 0; i < ip.length; i++) {
            newData[i] = data[ip[i]-1];
        }
        byte[] L = new byte[32];
        byte[] R = new byte[32];
        for (int i = 0; i < 32; i++) {
            L[i] = newData[i];
            R[i] = newData[i+32];
        }
        
        //2. 產生子鑰匙
        byte[][] subKeys = genSubKey(key);
        
        //3. 加密處理
        for (int i=0; i<16; i++) {
//          System.out.print(i + ")R==>");
//          for (int j=0;j<32; j++) {
//              System.out.print(R[j]);
//          }
//          System.out.println();
            byte[] subKey;
            if (encrypt) {
                subKey = subKeys[i];
            } else {
                subKey = subKeys[15-i];
            }
            byte[] R1 = new byte[32];
            byte[] f = F(R, subKey);
            for (int j=0; j<32; j++) {
                R1[j] = (byte)(L[j] != f[j] ? 1 : 0);
            }
            L = R;
            R = R1;
        }
        byte[] newData1 = new byte[64];
        for (int i=0; i<32; i++) {
            newData1[i] = R[i];
            newData1[i+32] = L[i];
        }
        
        //4. 終結排列 FP
        byte[] result = new byte[64];
        for (int i = 0; i < fp.length; i++) {
            result[i] = newData1[fp[i]-1];
        }
        
//      System.out.print("FP==>");
//      for (int j=0;j<64; j++) {
//          System.out.print(result[j]);
//      }
//      System.out.println();
        
        //將密文由 bit[] 回到 byte[]
        byte[] result2 = new byte[8];
        for (int i=0; i<8; i++) {
            result2[i] = (byte)(result[i*8+0] * 128 + 
                    result[i*8+1] * 64 + 
                    result[i*8+2] * 32 +
                    result[i*8+3] * 16 +
                    result[i*8+4] * 8 + 
                    result[i*8+5] * 4 + 
                    result[i*8+6] * 2 + 
                    result[i*8+7]);
        }
        
        return result2;
    }
    
    private byte[] F(byte[] data, byte[] key) {
        int[] ex = {32, 1, 2, 3, 4, 5,
                4, 5, 6, 7, 8, 9,
                8, 9,10,11,12,13,
               12,13,14,15,16,17,
               16,17,18,19,20,21,
               20,21,22,23,24,25,
               24,25,26,27,28,29,
               28,29,30,31,32, 1};
        int[][] sbox = {{14,4,13,1,2,15,11,8,3,10,6,12,5,9,0,7, // s1
            0,15,7,4,14,2,13,1,10,6,12,11,9,5,3,8,
            4,1,14,8,13,6,2,11,15,12,9,7,3,10,5,0,
            15,12,8,2,4,9,1,7,5,11,3,14,10,0,6,13},
           {15,1,8,14,6,11,3,4,9,7,2,13,12,0,5,10,  // s2
            3,13,4,7,15,2,8,14,12,0,1,10,6,9,11,5,
            0,14,7,11,10,4,13,1,5,8,12,6,9,3,2,15,
            13,8,10,1,3,15,4,2,11,6,7,12,0,5,14,9},
           {10,0,9,14,6,3,15,5,1,13,12,7,11,4,2,8,  // s3
            13,7,0,9,3,4,6,10,2,8,5,14,12,11,15,1,
            13,6,4,9,8,15,3,0,11,1,2,12,5,10,14,7,
            1,10,13,0,6,9,8,7,4,15,14,3,11,5,2,12},
           {7,13,14,3,0,6,9,10,1,2,8,5,11,12,4,15,  // s4
            13,8,11,5,6,15,0,3,4,7,2,12,1,10,14,9,
            10,6,9,0,12,11,7,13,15,1,3,14,5,2,8,4,
            3,15,0,6,10,1,13,8,9,4,5,11,12,7,2,14},
           {2,12,4,1,7,10,11,6,8,5,3,15,13,0,14,9,   // s5
            14,11,2,12,4,7,13,1,5,0,15,10,3,9,8,6,
            4,2,1,11,10,13,7,8,15,9,12,5,6,3,0,14,
            11,8,12,7,1,14,2,13,6,15,0,9,10,4,5,3},
           {12,1,10,15,9,2,6,8,0,13,3,4,14,7,5,11,   // s6
            10,15,4,2,7,12,9,5,6,1,13,14,0,11,3,8,
            9,14,15,5,2,8,12,3,7,0,4,10,1,13,11,6,
            4,3,2,12,9,5,15,10,11,14,1,7,6,0,8,13},
           {4,11,2,14,15,0,8,13,3,12,9,7,5,10,6,1,   // s7
            13,0,11,7,4,9,1,10,14,3,5,12,2,15,8,6,
            1,4,11,13,12,3,7,14,10,15,6,8,0,5,9,2,
            6,11,13,8,1,4,10,7,9,5,0,15,14,2,3,12},
           {13,2,8,4,6,15,11,1,10,9,3,14,5,0,12,7,   // s8
            1,15,13,8,10,3,7,4,12,5,6,11,0,14,9,2,
            7,11,4,1,9,12,14,2,0,6,10,13,15,3,5,8,
            2,1,14,7,4,10,8,13,15,12,9,0,3,5,6,11}};
        int[] p = {16, 7,20,21,
                   29,12,28,17,
                    1,15,23,26,
                    5,18,31,10,
                    2, 8,24,14,
                    32,27,3,9,
                    19,13,30,6,
                    22,11,4,25 };
        byte[] newData = new byte[48];
        for (int i = 0; i < ex.length; i++) {
            newData[i] = (byte) (data[ex[i]-1] != key[i] ? 1 : 0);
        }
        byte[][] s = new byte[8][6];
        int idx = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 6; j++) {
                s[i][j] = newData[idx++];
            }
        }
        byte[] newData1 = new byte[32];
        idx = 0;
        for (int i=0; i<s.length; i++) {
            int r = s[i][0] * 2 + s[i][5];
            int q = s[i][1] * 8 + s[i][2] * 4 + s[i][3] * 2 + s[i][4];
            int v = sbox[i][r*16+q];
            newData1[idx++] = (byte)((v&8) >> 3);
            newData1[idx++] = (byte)((v&4) >> 2);
            newData1[idx++] = (byte)((v&2) >> 1);
            newData1[idx++] = (byte)(v&1);
        }
        byte[] result = new byte[32];
        for (int i = 0; i < p.length; i++) {
            result[i] = newData1[p[i]-1];
        }
        return result;
    }
    
    private byte[][] genSubKey(byte[] key) {
        
//      System.out.print("主key:");
//      for (int j=0;j<64; j++) {
//          System.out.print(key[j]);
//      }
//      System.out.println();
        
        int[] pc1 = {57,49,41,33,25,17,9,
                  1,58,50,42,34,26,18,
                  10,2,59,51,43,35,27,
                  19,11,3,60,52,44,36,
                  63,55,47,39,31,23,15,
                  7,62,54,46,38,30,22,
                  14,6,61,53,45,37,29,
                  21,13,5,28,20,12,4};
        int[] pc2 = {14,17,11,24,1,5,
                  3,28,15,6,21,10,
                  23,19,12,4,26,8,
                  16,7,27,20,13,2,
                  41,52,31,37,47,55,
                  30,40,51,45,33,48,
                  44,49,39,56,34,53,
                  46,42,50,36,29,32};
        int[] ls = {1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1};
        byte[] C = new byte[28];
        byte[] D = new byte[28];
        for (int i=0; i<28; i++) {
            C[i] = key[pc1[i]-1];
            D[i] = key[pc1[i+28]-1];
        }
        byte[][] subKeys = new byte[16][48];
        for (int i=0; i<16; i++) {
            byte[] C1 = new byte[28];
            byte[] D1 = new byte[28];
            for (int j=0; j<28 - ls[i]; j++) {
                C1[j] = C[j + ls[i]];
                D1[j] = D[j + ls[i]];
            }
            for (int j=0; j<ls[i]; j++) {
                C1[28 - ls[i] + j] = C[j];
                D1[28 - ls[i] + j] = D[j];
            }
            byte[] newData1 = new byte[56];
            for (int j=0; j<28; j++) {
                newData1[j] = C1[j];
                newData1[j+28] = D1[j];
            }
            for (int j=0; j<48; j++) {
                subKeys[i][j] = newData1[pc2[j]-1];
            }
            C = C1;
            D = D1;
        }
        
//      for (int i=0; i<16; i++) {
//          System.out.print("第" + (i<10?"0" :"") + i + "把:");
//          for (int j=0;j<48; j++) {
//              System.out.print(subKeys[i][j]);
//          }
//          System.out.println();
//      }
        return subKeys;
    }
    
    /**
     * DES 的 key 為 8 bytes, 每 byte 最後一碼為同位元檢查碼, 故真正的 key 為 56 bits
     */
    private SecretKey genKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        //SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        //keyGen.init(56, random);
        keyGen.init(56);
        
        //透過 provider gen key
        SecretKey secretKey = keyGen.generateKey();
        
        //自行指定 key
        secretKey = new SecretKey() {
            public String getFormat() {
                return "RAW";
            }
            public byte[] getEncoded() {
                return "12345678".getBytes();
            }
            public String getAlgorithm() {
                return "DES";
            }
        };
        
        //自行指定 key 方式 2
        secretKey = new SecretKeySpec("12345678".getBytes(), "DES");
        
        return secretKey;
    }
    
    private String toHex(byte[] data) {
        String result = "";
        for (int i = 0; i < data.length; i++) {
            int v = data[i];
            if (v < 0) {
                v += 256;
            }
            String v1 = Integer.toHexString(v);
            if (v1.length() == 1) {
                v1 = "0" + v1;
            }
            result += v1;
        }
        return result.toUpperCase();
    }
}
