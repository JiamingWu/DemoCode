package pkcs;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.crypto.Cipher;

import sun.security.pkcs.ContentInfo;
import sun.security.pkcs.PKCS7;
import sun.security.pkcs.SignerInfo;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.x509.AlgorithmId;
import sun.security.x509.X500Name;


public class ShowCardContents {

    /**
     * 需要加入 -Djava.security.debug=sunpkcs11 來執行
     */
    public static void main(String[] args) {

        //Load the implementation of PKCS11
        String pkcs11ConfigFile = "D:\\MyDocuments\\Data\\work\\mycode\\src\\pkcs\\pkcs11.cfg";
        Provider pkcs11Provider = new sun.security.pkcs11.SunPKCS11(pkcs11ConfigFile);
        Security.addProvider(pkcs11Provider);

        //PIN is used to protect the information strored in the card
        String pin = "680507";
        //String pin = "PASSWORD";

        try {

            //Load KeyStore
            KeyStore smartCardKeyStore = KeyStore.getInstance("PKCS11", pkcs11Provider);
            smartCardKeyStore.load(null, pin.toCharArray());

            //Get the enumeration of the entris in the keystore
            Enumeration aliasesEnum = smartCardKeyStore.aliases();
            X509Certificate cert = null;
            PrivateKey privateKey = null;
            while (aliasesEnum.hasMoreElements()) {
                System.out.println("---------------------------");
                //Print alias
                String alias = (String) aliasesEnum.nextElement();
                System.out.println("Alias: " + alias);
                //Print certificate
                cert = (X509Certificate) smartCardKeyStore.getCertificate(alias);
                System.out.println("Certificate: " + cert);
                //Print public key
                PublicKey publicKey = cert.getPublicKey();
                System.out.println("Public key: " + publicKey);
                //Print private key
                privateKey = (PrivateKey) smartCardKeyStore.getKey(alias, null);
                System.out.println("Private key: " + privateKey);
                //Encryption/Decryption Test
                byte[] plainText = new String("Hello World!").getBytes();
                byte[] cipherText = privateEncrypt(plainText, privateKey);
                System.out.println("Cipher Text: " + byte2hex(cipherText));
                byte[] decryptedText = publicDecrypt(cipherText, publicKey);
                System.out.println("Decrypted Text: " + new String(decryptedText));

            }
            if (cert != null) {
                byte[] result = sign(cert, privateKey, "Hello!".getBytes());
                System.out.println("sign finish:" + result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Encrypt the plain text with private key.
     */
    public static byte[] privateEncrypt(byte[] text, PrivateKey priKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, priKey);
        return cipher.doFinal(text);
    }

    /**
     * Encrypt the plain text with public key.
     */
    public static byte[] publicEncrypt(byte[] text, PublicKey pubKey) throws
            Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(text);
    }

    /**
     * Decrypt the cipher text with private key.
     */
    public static byte[] privateDecrypt(byte[] text, PrivateKey priKey) throws
            Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        return cipher.doFinal(text);
    }

    /**
     * Decrypt the cipher text with public key.
     */
    public static byte[] publicDecrypt(byte[] text, PublicKey pubKey) throws
            Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        return cipher.doFinal(text);
    }

    /**
     * Transform a byte array to a hexadecimal string.
     */
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
            if (n < b.length - 1) {
                hs = hs + ":";
            }
        }
        return hs.toUpperCase();
    }
    
    public static byte[] sign(X509Certificate cert, PrivateKey privateKey, byte[] dataToSign) throws Exception {
        //compute signature:
        Signature signature = Signature.getInstance("Sha1WithRSA");
        signature.initSign(privateKey);
        signature.update(dataToSign);
        byte[] signedData = signature.sign();

        //load X500Name
        X500Name xName      = X500Name.asX500Name(cert.getSubjectX500Principal());
        //load serial number
        BigInteger serial   = cert.getSerialNumber();
        //laod digest algorithm
        AlgorithmId digestAlgorithmId = new AlgorithmId(AlgorithmId.SHA_oid);
        //load signing algorithm
        AlgorithmId signAlgorithmId = new AlgorithmId(AlgorithmId.RSAEncryption_oid);

        //Create SignerInfo:
        SignerInfo sInfo = new SignerInfo(xName, serial, digestAlgorithmId, signAlgorithmId, signedData);
        //Create ContentInfo:
        ContentInfo cInfo = new ContentInfo(ContentInfo.DIGESTED_DATA_OID, new DerValue(DerValue.tag_OctetString, dataToSign));
        //Create PKCS7 Signed data
        PKCS7 p7 = new PKCS7(new AlgorithmId[] { digestAlgorithmId }, cInfo,
                new java.security.cert.X509Certificate[] { cert },
                new SignerInfo[] { sInfo });
        //Write PKCS7 to bYteArray
        ByteArrayOutputStream bOut = new DerOutputStream();
        p7.encodeSignedData(bOut);
        byte[] encodedPKCS7 = bOut.toByteArray();
        return encodedPKCS7;
    }
}
