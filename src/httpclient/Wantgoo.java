package httpclient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 玩股網, 某文章按 facebook 讚後, 測試如何偵測使用者有按過 "讚".
 * 結論: 其後端會記住 session id 及按下讚的最後一篇文章 id, 進行比對.
 */
public class Wantgoo {

	public static void main(String[] args) throws Exception {
		URL url = new URL("http://www.wantgoo.com/PersonalPage/MyBlogPost.aspx?MemberNo=119&ArticleID=711");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		connection.addRequestProperty("cookie", "ASP.NET_SessionId=fsqlrc55dcs0mvjh0zvw3myk;Blog_Post119=708");
		
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
		connection.disconnect();
	}
}
