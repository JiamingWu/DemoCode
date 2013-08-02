package mail;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class MimeMail {

	public static void main(String[] args) {
		try {
			// 初始設定，username 和 password 非必要
			Properties props = new Properties();
			props.setProperty("mail.transport.protocol", "smtp");
			props.setProperty("mail.host", "mail.uxb2b.com");
			props.setProperty("mail.user", "awu");
			props.setProperty("mail.password", "m835021");

			Session mailSession = Session.getDefaultInstance(props, null);
			Transport transport = mailSession.getTransport();

			// 產生整封 email 的主體 message
			MimeMessage message = new MimeMessage(mailSession);

			// 設定主旨
			message.setSubject("Javamail with picture attachment and html contents.");

			// 文字部份，注意 img src 部份要用 cid:接下面附檔的header
			MimeBodyPart textPart = new MimeBodyPart();
			StringBuffer html = new StringBuffer();
			html.append("<h2>這是第一行</h2><br>");
			html.append("<h3>這是第二行，下面會是圖</h3><br>");
			html.append("<img src='cid:image'/><br>");
			html.append("<h3>這是第三行，下面會是圖2</h3><br>");
			html.append("<img src='cid:image2'/><br>");
			textPart.setContent(html.toString(), "text/html; charset=UTF-8");

			// 圖檔部份，注意 html 用 cid:image，則header要設<image>
			MimeBodyPart picturePart = new MimeBodyPart();
			FileDataSource fds = new FileDataSource("D:\\MyDocuments\\MyPicutres\\2010-10-09-南園\\DSC_8250.jpg");
			picturePart.setDataHandler(new DataHandler(fds));
			//下面這行設定附件檔名，如附件圖檔僅供顯示也可不必設定
			//picturePart.setFileName(MimeUtility.encodeText(fds.getName(), "UTF-8", "B"));
			picturePart.setHeader("Content-ID", "<image>");

			MimeBodyPart picturePart2 = new MimeBodyPart();
			FileDataSource fds3 = new FileDataSource("D:\\MyDocuments\\Data\\work\\toplogis-tpcs2\\WebContent\\resources\\img\\toplogis.png");
			picturePart2.setDataHandler(new DataHandler(fds3));
			picturePart2.setHeader("Content-ID", "<image2>");

			
			Multipart email = new MimeMultipart();
			email.addBodyPart(textPart);
			email.addBodyPart(picturePart);
			email.addBodyPart(picturePart2);
			
			//夾檔
			File file = new File("D:\\MyDocuments\\Data\\work\\toplogis_memo.txt");
            FileDataSource fds2 = new FileDataSource(file);
            MimeBodyPart attachment = new MimeBodyPart();
            attachment.setDataHandler(new DataHandler(fds2));
            attachment.setFileName(MimeUtility.encodeWord(file.getName(), "UTF-8", null));
            email.addBodyPart(attachment);

			message.setContent(email);
			message.setFrom(new InternetAddress("awu@uxb2b.com"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					"awu@uxb2b.com"));
			transport.connect();
			transport.sendMessage(message,
					message.getRecipients(Message.RecipientType.TO));
			transport.close();
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
