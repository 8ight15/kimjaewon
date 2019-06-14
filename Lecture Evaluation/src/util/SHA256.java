package util;

import java.security.MessageDigest;

public class SHA256 {
	//기존의 이메일인증방식에 해시값을 적용해서 인증코드로 링크를 타고 들어와서 인증하도록 하는 것.
	//그것을 위해 필요한게  SHA256
	
	public static String getSHA256(String input) {//이메일 값 앞에 해시를 적용한 값을 반환해서 이용. 
		StringBuffer result = new StringBuffer();
		try {
			MessageDigest digest= MessageDigest.getInstance("SHA-256");
			byte[] salt = "Hello! This is Salt.".getBytes();//salt값을 넣어서 악의적인 공격 대비." "안에는 자신이 원하는 문구를 넣어서 Salt값을 지정.
			digest.reset();
			digest.update(salt);
			byte[] chars = digest.digest(input.getBytes("UTF-8"));
			//실제로 해시적용한 값을 캐릭터변수에 담음.
			for(int i=0;i<chars.length;i++) {
				//문자열 형태로 만듦.
				String hex = Integer.toHexString(0xff&chars[i]);//헥스값과 현재 해시값을 적용한 캐릭터의 인덱스 값을 AND연산
				if(hex.length() == 1) result.append("0");//1자리값일 경우 0을 붙여서 총 2자리값의 16진수 형태로 만들어줌.
				result.append(hex);//append로 hex값을 달아서 결과적으로 해시값을 반환할 수 있도록 만들어줌.
			}
			//단순하게 적용하면 해커의 침입이 가능하기 때문에 Salt값을 적용하는 것.
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}
}
