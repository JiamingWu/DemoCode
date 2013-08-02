package httpclient;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 羅東鎮公所舉辦活動之灌票程式.
 */
public class UseCookie {

	public static void main(String[] args) throws Exception {

		String pre = "mail";
		if (args.length > 0) {
			pre = args[0];
			if (pre.trim().length() == 0) {
				pre = "mail";
			}
		}
		String mid = "1";
		if (args.length > 1) {
			mid = args[1];
		}
		
		for (int i=0; i<500; i++) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String id = pre + sdf.format(new Date()) + "@mail.to";
			go(i+1, id, mid);
		}
		
	}
	
	public static void go(int idx, String id, String mid) throws Exception {
		URL url = new URL("http://www.lotong.gov.tw/gift/join.asp?sp=3&voteid="
						+ id
						+ "&votepw1=test777&votepw2=test777&votename=test777&votetel=0987654321&voteadd=");
		HttpURLConnection URLConn = (HttpURLConnection) url.openConnection();
		InputStream is = URLConn.getInputStream();   
		String cookieval = URLConn.getHeaderField("set-cookie");   
		String sessionId = null;   
		if(cookieval != null)   
		{   
			sessionId = cookieval.substring(0, cookieval.indexOf(";"));   
		}
		System.out.println("idx:" + idx + ", id:" + id);
		
		url = new URL("http://www.lotong.gov.tw/gift/login.asp?voteid=" + id + "&votepw=test777&sp=2");
		URLConn = (HttpURLConnection) url.openConnection();
		URLConn.setRequestProperty("cookie", sessionId);   
		is = URLConn.getInputStream();
		
		url = new URL("http://www.lotong.gov.tw/gift/vote.asp?sp=2&id=" + mid);
		URLConn = (HttpURLConnection) url.openConnection();
		URLConn.setRequestProperty("cookie", sessionId);   
		is = URLConn.getInputStream();
		
		url = new URL("http://www.lotong.gov.tw/gift/logout.asp");
		URLConn = (HttpURLConnection) url.openConnection();
		URLConn.setRequestProperty("cookie", sessionId);   
		is = URLConn.getInputStream();
		
		/*URLConn.setRequestProperty("User-agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; zh-TW; rv:1.9.1.2) "
									+ "Gecko/20090729 Firefox/3.5.2 GTB5 (.NET CLR 3.5.30729)");
		URLConn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,* /*;q=0.8");
		URLConn.setRequestProperty("Accept-Language", "zh-tw,en-us;q=0.7,en;q=0.3");
		URLConn.setRequestProperty("Accept-Charse", "Big5,utf-8;q=0.7,*;q=0.7");
		if (cookie != null) {
			URLConn.setRequestProperty("Cookie", cookie);
		}*/
		Thread.sleep(1000);
		
	}

}
