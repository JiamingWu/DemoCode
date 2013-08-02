package mail;
import java.security.Security;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class Gmail {
	private String host = "smtp.gmail.com";
	private boolean auth = true;
	private String user = "service";
	private String domain = "cashback.com.tw";
	private String password = "zaq1@WSXcVbN";
	private Integer port = 465;
	private String fileName = "";
	

	public static void main(String[] args) throws Exception {
		Gmail m = new Gmail();
		m.fileName = args[0];
		if (args.length > 1) {
			m.password = args[1];
		}
		m.sendMail();
	}
	
	public Gmail() {
		
	}
	
	public boolean sendMail() throws Exception {
		try {
			Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
			final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
			
			Properties props = System.getProperties();
			props.setProperty("mail.transport.protocol", "smtp");
	        props.setProperty("mail.smtp.host", host);
	        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
			props.setProperty("mail.smtp.socketFactory.fallback", "false");
			props.setProperty("mail.smtp.port", port.toString());
			props.setProperty("mail.smtp.socketFactory.port", port.toString());
			props.setProperty("mail.smtp.auth", String.valueOf(auth));
	        
	        Session session = Session.getDefaultInstance(props, new Authenticator(){
	        	protected PasswordAuthentication getPasswordAuthentication() {
	        		return new PasswordAuthentication(user + "@" + domain, password);
	        	}});
	        
	        MimeMessage msg = new MimeMessage(session);
        	msg.setFrom(new InternetAddress(user + "@" + domain));
        	msg.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(user + "@" + domain));
	        msg.setSubject("backup");
	        msg.setSentDate(new Date());
	        
	        FileDataSource fileSource = new FileDataSource(fileName);
	        MimeMultipart multiPart = new MimeMultipart();
	        MimeBodyPart bodyPart = new MimeBodyPart();
	        bodyPart.setDataHandler(new DataHandler(fileSource));
	        bodyPart.setFileName(fileName);
	        multiPart.addBodyPart(bodyPart);
	        
	        msg.setContent(multiPart);
	        msg.saveChanges();
	        
	        Transport.send(msg);
	        return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public boolean isAuth() {
		return auth;
	}

	public void setAuth(boolean auth) {
		this.auth = auth;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}
	
}
