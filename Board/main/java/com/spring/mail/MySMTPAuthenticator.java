package com.spring.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MySMTPAuthenticator extends Authenticator {
	
	@Override
	public PasswordAuthentication getPasswordAuthentication() { 
	
		// Gmail �� ��� @gmail.com �� ������ ���̵� �Է��Ѵ�.
		return new PasswordAuthentication("cksdud7873","!@Kari0830");
	}
}
