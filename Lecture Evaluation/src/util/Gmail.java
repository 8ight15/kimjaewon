package util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class Gmail extends Authenticator{
	//실제로 Gmail SMTP를 이용하기 위해 정보를 넣는 것.
	
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication("kjw54482@gmail.com","vkfks9915@");//관리자 자신의 아이디와 비밀번호를 넣어야 함.
	}
	
}
