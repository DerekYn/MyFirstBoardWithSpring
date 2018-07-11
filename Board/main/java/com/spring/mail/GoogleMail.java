package com.spring.mail;

import javax.mail.Transport;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.Address;
import javax.mail.internet.MimeMessage;
import javax.mail.Session;
import javax.mail.Authenticator;
import java.util.Properties;
import com.spring.mail.MySMTPAuthenticator;

 
public class GoogleMail {
	
    public void sendmail(String recipient, String certificationCode)  
    		throws Exception{
        
    	// 1. ������ ��� ���� ��ü
    	Properties prop = new Properties(); 
    	
    	// 2. SMTP ������ ���� ����
   	    //    Google Gmail �� ������ ��� Gmail �� email �ּҸ� ���� 
    	prop.put("mail.smtp.user", "cksdud7873@gmail.com");
        	
    	
    	// 3. SMTP ���� ���� ����
    	//    Google Gmail �� ���  smtp.gmail.com
    	prop.put("mail.smtp.host", "smtp.gmail.com");
         	
    	
    	prop.put("mail.smtp.port", "465");
    	prop.put("mail.smtp.starttls.enable", "true");
    	prop.put("mail.smtp.auth", "true");
    	prop.put("mail.smtp.debug", "true");
    	prop.put("mail.smtp.socketFactory.port", "465");
    	prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    	prop.put("mail.smtp.socketFactory.fallback", "false");
    	
    	prop.put("mail.smtp.ssl.enable", "true");
    	prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
      	
    	
    	Authenticator smtpAuth = new MySMTPAuthenticator();
    	Session ses = Session.getInstance(prop, smtpAuth);
    		
    	// ������ ������ �� ���� ��Ȳ�� �ֿܼ� ����Ѵ�.
    	ses.setDebug(true);
    	        
    	// ������ ������ ��� ���� ��ü����
    	MimeMessage msg = new MimeMessage(ses);

    	// ���� ����
    	String subject = "localhost:9090/MyMVC/mall.do ȸ������ ��й�ȣ�� ã������ �����ڵ� �߼�";
    	msg.setSubject(subject);
    	        
    	// ������ ����� �����ּ�
    	String sender = "cksdud7873@gmail.com";
    	Address fromAddr = new InternetAddress(sender);
    	msg.setFrom(fromAddr);
    	        
    	// �޴� ����� �����ּ�
    	Address toAddr = new InternetAddress(recipient);
    	msg.addRecipient(Message.RecipientType.TO, toAddr);
    	        
    	// �޽��� ������ ����� ����, ĳ���� �� ����
    	msg.setContent("�߼۵� �����ڵ� : <span style='font-size:14pt; color:red;'>"+certificationCode+"</span>", "text/html;charset=UTF-8");
    	        
    	// ���� �߼��ϱ�
    	Transport.send(msg);
    	
    }// end of sendmail(String recipient, String certificationCode)-----------------
    
    
    public void sendmail_OrderFinish(String recipient, String emailContents)  
    		throws Exception{
        
    	// 1. ������ ��� ���� ��ü
    	Properties prop = new Properties(); 
    	
    	// 2. SMTP ������ ���� ����
   	    //    Google Gmail �� ������ ��� Gmail �� email �ּҸ� ���� 
    	prop.put("mail.smtp.user", "cksdud7873@gmail.com");
        	
    	
    	// 3. SMTP ���� ���� ����
    	//    Google Gmail �� ���  smtp.gmail.com
    	prop.put("mail.smtp.host", "smtp.gmail.com");
         	
    	
    	prop.put("mail.smtp.port", "465");
    	prop.put("mail.smtp.starttls.enable", "true");
    	prop.put("mail.smtp.auth", "true");
    	prop.put("mail.smtp.debug", "true");
    	prop.put("mail.smtp.socketFactory.port", "465");
    	prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    	prop.put("mail.smtp.socketFactory.fallback", "false");
    	
    	prop.put("mail.smtp.ssl.enable", "true");
    	prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
      	
    	
    	Authenticator smtpAuth = new MySMTPAuthenticator();
    	Session ses = Session.getInstance(prop, smtpAuth);
    		
    	// ������ ������ �� ���� ��Ȳ�� �ֿܼ� ����Ѵ�.
    	ses.setDebug(true);
    	        
    	// ������ ������ ��� ���� ��ü����
    	MimeMessage msg = new MimeMessage(ses);

    	// ���� ����
    	String subject = "localhost:9090/MyMVC/mall.do ȸ������ �ֹ��� �����߽��ϴ�. ";
    	msg.setSubject(subject);
    	        
    	// ������ ����� �����ּ�
    	String sender = "cksdud7873@gmail.com";
    	Address fromAddr = new InternetAddress(sender);
    	msg.setFrom(fromAddr);
    	        
    	// �޴� ����� �����ּ�
    	Address toAddr = new InternetAddress(recipient);
    	msg.addRecipient(Message.RecipientType.TO, toAddr);
    	        
    	// �޽��� ������ ����� ����, ĳ���� �� ����
    	msg.setContent("<span style='font-size:14pt; color:red;'>"+emailContents+"</span>", "text/html;charset=UTF-8");
    	        
    	// ���� �߼��ϱ�
    	Transport.send(msg);
    	
    }// end of sendmail_OrderFinish(String recipient, String emailContents)-----------------------
    
    
    public void sendmail_IbgoFinish(String recipient, String emailContents)  
    		throws Exception{
        
    	// 1. ������ ��� ���� ��ü
    	Properties prop = new Properties(); 
    	
    	// 2. SMTP ������ ���� ����
   	    //    Google Gmail �� ������ ��� Gmail �� email �ּҸ� ���� 
    	prop.put("mail.smtp.user", "cksdud7873@gmail.com");
        	
    	
    	// 3. SMTP ���� ���� ����
    	//    Google Gmail �� ���  smtp.gmail.com
    	prop.put("mail.smtp.host", "smtp.gmail.com");
         	
    	
    	prop.put("mail.smtp.port", "465");
    	prop.put("mail.smtp.starttls.enable", "true");
    	prop.put("mail.smtp.auth", "true");
    	prop.put("mail.smtp.debug", "true");
    	prop.put("mail.smtp.socketFactory.port", "465");
    	prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    	prop.put("mail.smtp.socketFactory.fallback", "false");
    	
    	prop.put("mail.smtp.ssl.enable", "true");
    	prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
      	
    	
    	Authenticator smtpAuth = new MySMTPAuthenticator();
    	Session ses = Session.getInstance(prop, smtpAuth);
    		
    	// ������ ������ �� ���� ��Ȳ�� �ֿܼ� ����Ѵ�.
    	ses.setDebug(true);
    	        
    	// ������ ������ ��� ���� ��ü����
    	MimeMessage msg = new MimeMessage(ses);

    	// ���� ����
    	String subject = "localhost:9090/board/ �԰� �����߽��ϴ�. ";
    	msg.setSubject(subject);
    	        
    	// ������ ����� �����ּ�
    	String sender = "cksdud7873@gmail.com";
    	Address fromAddr = new InternetAddress(sender);
    	msg.setFrom(fromAddr);
    	        
    	// �޴� ����� �����ּ�
    	Address toAddr = new InternetAddress(recipient);
    	msg.addRecipient(Message.RecipientType.TO, toAddr);
    	        
    	// �޽��� ������ ����� ����, ĳ���� �� ����
    	msg.setContent("<span style='font-size:14pt; color:red;'>"+emailContents+"</span>", "text/html;charset=UTF-8");
    	        
    	// ���� �߼��ϱ�
    	Transport.send(msg);
    	
    }// end of sendmail_IbgoFinish(String recipient, String emailContents)-----------------------
    
    
}