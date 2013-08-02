package mail;

import java.security.Security;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SSLMailService  {

	private String host = "smtp.gmail.com";
	private boolean auth = true;
	private String user = "service@cashback.com.tw";
	private String password = "zaq1@WSXcVbN";
	private Integer port = 465;
	
	public SSLMailService() {
		
	}
	
	public boolean sendMail(MailVO mailVO) throws Exception {
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
	        		return new PasswordAuthentication(user, password);
	        	}});
	        
	        MimeMessage msg = new MimeMessage(session);
	        if (mailVO.getFromName() != null) {
	        	msg.setFrom(new InternetAddress(mailVO.getFrom()));
	        } else {
	        	msg.setFrom(new InternetAddress(mailVO.getFrom(), mailVO.getFromName(), "utf-8"));
	        }
	        if (mailVO.getToName() != null) {
	        	msg.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(mailVO.getTo()));
	        } else {
	        	msg.setRecipients(javax.mail.Message.RecipientType.TO, new InternetAddress[] {new InternetAddress(mailVO.getTo(), mailVO.getToName(), "utf-8")});
	        }
	        msg.setSubject(mailVO.getSubject(), "UTF-8");
	        msg.setSentDate(new Date());
	        if (mailVO.isHtml()) {
	        	msg.setContent(mailVO.getMessage(), "text/html;charset=UTF-8");
	        } else {
	        	msg.setText(mailVO.getMessage());
	        }
	        
	        Transport.send(msg);
	        return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void main(String[] args) throws Exception {
		SSLMailService s = new SSLMailService();
		MailVO mailVO = new MailVO();
		mailVO.setFrom("service@cashback.com.tw");
		mailVO.setTo("jiaming0507@gmail.com");
		mailVO.setSubject("test mail");
		mailVO.setMessage("this is a test mail.");
		s.sendMail(mailVO);
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

class MailVO {

	private String to;
	private String toName;
    private String from;
    private String fromName;
    private String cc;
    private String bcc;
    private String subject;
    private String message;
    private boolean isHtml = true;
    
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	public String getBcc() {
		return bcc;
	}
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isHtml() {
		return isHtml;
	}
	public void setHtml(boolean isHtml) {
		this.isHtml = isHtml;
	}
	public String getToName() {
		return toName;
	}
	public void setToName(String toName) {
		this.toName = toName;
	}
	public String getFromName() {
		return fromName;
	}
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
}