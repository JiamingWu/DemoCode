package httpclient;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetData {

	public static void main(String[] args) throws Exception {
	    URL url = new URL("http://gdata.youtube.com/feeds/api/videos?author=goodtv");
        HttpURLConnection URLConn = (HttpURLConnection) url.openConnection();
		InputStream is = URLConn.getInputStream();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[512];
		int cnt = 0;
		while ((cnt = is.read(buf)) != -1) {
			baos.write(buf, 0, cnt);
		}
		System.out.println(new String(baos.toByteArray()));
	}
}
