package cert;

import java.io.FileInputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class ReadCert {

	public static void main(String[] args) throws Exception {
		 FileInputStream is = new FileInputStream("D:\\MyDocuments\\Data\\workspace\\test\\src\\cert\\crt.crt");
		 
		 CertificateFactory cf = CertificateFactory.getInstance("X.509");
		 Certificate cert = cf.generateCertificate(is);
		 X509Certificate x509cert = (X509Certificate) cert;
		 System.out.println(x509cert.getSigAlgName());
		 byte[] data = x509cert.getExtensionValue("2.5.29.9");
		 String regNo = new String(data, data.length-8, 8);
		 System.out.println(regNo);
	}
	
}
