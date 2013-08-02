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
			// ��l�]�w�Ausername �M password �D���n
			Properties props = new Properties();
			props.setProperty("mail.transport.protocol", "smtp");
			props.setProperty("mail.host", "mail.uxb2b.com");
			props.setProperty("mail.user", "awu");
			props.setProperty("mail.password", "m835021");

			Session mailSession = Session.getDefaultInstance(props, null);
			Transport transport = mailSession.getTransport();

			// ���;�� email ���D�� message
			MimeMessage message = new MimeMessage(mailSession);

			// �]�w�D��
			message.setSubject("Javamail with picture attachment and html contents.");

			// ��r�����A�`�N img src �����n�� cid:���U�����ɪ�header
			MimeBodyPart textPart = new MimeBodyPart();
			StringBuffer html = new StringBuffer();
			html.append("<h2>�o�O�Ĥ@��</h2><br>");
			html.append("<h3>�o�O�ĤG��A�U���|�O��</h3><br>");
			html.append("<img src='cid:image'/><br>");
			html.append("<h3>�o�O�ĤT��A�U���|�O��2</h3><br>");
			html.append("<img src='cid:image2'/><br>");
			textPart.setContent(html.toString(), "text/html; charset=UTF-8");

			// ���ɳ����A�`�N html �� cid:image�A�hheader�n�]<image>
			MimeBodyPart picturePart = new MimeBodyPart();
			FileDataSource fds = new FileDataSource("D:\\MyDocuments\\MyPicutres\\2010-10-09-�n��\\DSC_8250.jpg");
			picturePart.setDataHandler(new DataHandler(fds));
			//�U���o��]�w�����ɦW�A�p������ɶȨ���ܤ]�i�����]�w
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
			
			//����
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
