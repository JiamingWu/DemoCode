package httpclient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class TestSession {

	public static void main(String[] args) throws Exception {
	    
//	    System.getProperties().put("http.proxyHost", "10.60.94.21");
//	    System.getProperties().put("http.proxyPort", "3128");
	    
		URL url = new URL("http://10.60.90.151/demo/test2.jsp");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		connection.addRequestProperty("cookie", "JSESSIONID=91E28AB748648ABEC9050AF948AA4B53");
		connection.addRequestProperty("if-modified-since", "Wed, 20 Aug 2014 03:25:00 GMT");
		connection.addRequestProperty("if-none-match", "3561693548");
		
		InputStream is = connection.getInputStream();

		BufferedReader rd = new BufferedReader(new InputStreamReader(is, "utf-8"));
		String line;
		StringBuffer response = new StringBuffer();
		while ((line = rd.readLine()) != null) {
			response.append(line);
			response.append('\r');
		}
		rd.close();
		System.out.println("response:" + response.toString());
		
		System.out.println("~~~~~~~~~ header");
		Map<String, List<String>> header = connection.getHeaderFields();
		for (Map.Entry<String, List<String>> entry : header.entrySet()) {
		    if (entry.getKey() == null) {
		        System.out.println(entry.getValue().get(0));
		    } else {
		        System.out.println(entry.getKey() + "=" + entry.getValue().get(0));
		    }
		}
		
		connection.disconnect();
	}
}
