package mail;

import java.io.File;

import javax.activation.FileDataSource;
import javax.mail.internet.MimeUtility;

import org.apache.commons.mail.HtmlEmail;

public class CommonsHtmlEmail {

	public static void main(String[] args) throws Exception {
		HtmlEmail hm = new HtmlEmail();
			hm.setFrom("awu@uxb2b.com", "allen");
			hm.addTo("awu@uxb2b.com", "awu");
			hm.setSubject("測試信");

			String cid1 = hm.embed(new File("D:\\MyDocuments\\MyPicutres\\2010-10-09-南園\\DSC_8250.jpg"));
			String cid2 = hm.embed(new File("D:\\\\MyDocuments\\\\Data\\\\work\\\\toplogis-tpcs2\\\\WebContent\\\\resources\\\\img\\\\toplogis.png"));
			
			StringBuffer html = new StringBuffer();
			html.append("<h2>這是第一行</h2><br>");
			html.append("<h3>這是第二行，下面會是圖</h3><br>");
			html.append("<img src='cid:" + cid1 + "'/><br>");
			html.append("<h3>這是第三行，下面會是圖2</h3><br>");
			html.append("<img src='cid:" + cid2 + "'/><br>");
			
			hm.setHtmlMsg(html.toString());
			
			//夾檔
			FileDataSource fds2 = new FileDataSource(new File("D:\\MyDocuments\\Data\\work\\toplogis_memo.txt"));
            String fileName = MimeUtility.encodeWord("toplogis_memo.txt", "UTF-8", null);
            hm.attach(fds2, fileName, fileName);
            
			hm.setSmtpPort(25);
			hm.setHostName("mail.uxb2b.com");
			hm.setAuthentication("awu", "m835021");
			hm.setCharset("UTF-8");
			hm.send();
	}
	
}
