package httpclient;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class PostXml {

	public static void main(String[] args) throws Exception {
		
		URL httpServletUrl = new URL("https://210.242.138.194/HttpServiceReceive");
		HttpURLConnection httpURLConnection = (HttpURLConnection) httpServletUrl.openConnection();
		HttpsURLConnection conn = (HttpsURLConnection)httpURLConnection;
		conn.setHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				System.out.println("the hostname:" + hostname);
				return true;
			}
		});
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setDoOutput(true);
		httpURLConnection.connect();

		OutputStream httpOutputStream = httpURLConnection.getOutputStream();
		httpOutputStream.write("<?xml version=\"1.0\"?><Root>This is a test...</Root>".getBytes());

		httpOutputStream.flush();
		httpOutputStream.close();

		InputStream httpInputStream = httpURLConnection.getInputStream();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[4096];
		int totalRead;
		while ((totalRead = httpInputStream.read(buf, 0, 4096)) > 0) {
			baos.write(buf, 0, totalRead);
		}

		byte[] result = baos.toByteArray();

		boolean isutf8 = false;
		String tmp = new String(result);
		if (tmp.indexOf("utf-8") > 0) {
			tmp = new String(result, "utf-8");
			isutf8 = true;
		}
		System.out.println("receiver data (" + (isutf8 ? "utf-8" : "big5") + ")==> "+ tmp);
	}
	
}
