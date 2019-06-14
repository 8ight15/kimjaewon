package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import util.DatabaseUtil;

public class UserDAO {
	private Connection conn;
	private ResultSet rs;
	public UserDAO() {
		Connection conn=null;
		ResultSet rs=null;
		try {
			String dbURL = "jdbc:mysql://localhost/LectureEvaluation?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
			String dbID = "root";
			String dbPassword = "root";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//아이디와 비밀번호 받아서 로그인을 시도하는 함수, 반환은 정수형
	public int login(String userID,String userPassword) {
		Connection conn  = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT userPassword FROM USER WHERE userID= ?";
		
		try {	
			conn = DatabaseUtil.getConnection();//연결됐음
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1,userID);
			rs = pstmt.executeQuery();//데이터를 검색하는 것 조회할 때 사용.
			if(rs.next()) { //resultset을 통해서 결과를 확인
				if(rs.getString(1).equals(userPassword)) {
					return 1;//로그인 성공
				}
				else {
					return 0;//비밀번호 틀림
				}
			}
			return -1;//아이디 없음
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try{if(conn !=null) conn.close();}catch(Exception e) {e.printStackTrace();}
			try{if(pstmt !=null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
			try{if(rs !=null) rs.close();}catch(Exception e) {e.printStackTrace();}
		}//접근한 자원해제함으로써 서버무리하지않게함
		return -2; //데이터베이스 오류
	}
	//사용자 정보를 입력받아 사용자의 회원가입을 진행해주는 함수, 결과 또한 정수
	public int join(UserDTO user) {
		String SQL = "INSERT INTO USER VALUES(?,?,?,?,false)";
		Connection conn  = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {	
			conn = DatabaseUtil.getConnection();//연결됐음
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1,user.getUserID());
			pstmt.setString(2,user.getUserPassword());
			pstmt.setString(3,user.getUserEmail());
			pstmt.setString(4,user.getUserEmailHash());
			return pstmt.executeUpdate();//insert나 delete,update같은 경우는 executeUpdate를 사용.
			//실제로 영향을 받은 데이터의 갯수를 반환한다. 성공적으로 회원가입을 성공하였다면 1명이 추가가 됐기 때문에 1이 반환.
			//1이 반환->성공 -1->실패
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try{if(conn !=null) conn.close();}catch(Exception e) {e.printStackTrace();}
			try{if(pstmt !=null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
			try{if(rs !=null) rs.close();}catch(Exception e) {e.printStackTrace();}
		}//접근한 자원해제함으로써 서버무리하지않게함
		return -1; //회원가입 실패 /회원의 아이디가 겹칠 떄는 PrimaryKey가 UserID에 들어있기 때문에 -1값이 반환.
	}
	//사용자의 아이디값을 받아서 특정회원이메일주소를 반환하는 함수, 때문에 결과값은 문자열 
	public String getUserEmail(String userID) {
		String SQL = "SELECT userEmail FROM USER WHERE userID = ?";
		Connection conn  = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {	
			conn = DatabaseUtil.getConnection();//연결됐음
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1,userID);
			rs=pstmt.executeQuery();
			while(rs.next()) { //존재하는 사용자의 아이디인 경우
				return rs.getString(1);//첫번쨰 속성 즉 userEmailChecked의 속성을 반환해줌.
			}
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try{if(conn !=null) conn.close();}catch(Exception e) {e.printStackTrace();}
			try{if(pstmt !=null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
			try{if(rs !=null) rs.close();}catch(Exception e) {e.printStackTrace();}
		}//접근한 자원해제함으로써 서버무리하지않게함
		return null; //데이터베이스 오류
	}
	//현재 사용자의 이메일 인증이 완료되었는지 확인해주는 함수, 결과는 참 혹은 거짓,boolean값
	public boolean getUserEmailChecked(String userID) {
		String SQL = "SELECT userEmailChecked FROM USER WHERE userID = ?";
		Connection conn  = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {	
			conn = DatabaseUtil.getConnection();//연결됐음
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1,userID);
			rs=pstmt.executeQuery();
			while(rs.next()) { //존재하는 사용자의 아이디인 경우
				return rs.getBoolean(1);//첫번쨰 속성 즉 userEmailChecked의 속성을 반환해줌.
			}
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try{if(conn !=null) conn.close();}catch(Exception e) {e.printStackTrace();}
			try{if(pstmt !=null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
			try{if(rs !=null) rs.close();}catch(Exception e) {e.printStackTrace();}
		}//접근한 자원해제함으로써 서버무리하지않게함
		return false; //데이터베이스 오류
	}
	//특정한 사용자의 이메일 인증을 수행해주는 함수.
	public boolean setUserEmailChecked(String userID) {//특정사용자가 이메일검증을 통해서 이메일인증완료되도록 해주는 함수
		String SQL = "UPDATE USER SET userEmailChecked = true WHERE userID = ?";//특정사용자로 하여금 이메일인증이 되도록 처리
		Connection conn  = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {	
			conn = DatabaseUtil.getConnection();//연결됐음
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1,userID);
		    pstmt.executeUpdate();
			rs=pstmt.executeQuery();//업데이트 한 다음 resultset에 있는 suec의 값을 검색.
		    return true;//한번 인증하였더라도 추가적으로 인증을 할 수 있도록 반환.
		    //이제 특정한 이메일링크를 누르면 사용자에 대한 이메일인증이 되는 것이지만 인증이 되더라도 다시 추가적으로 인증할 수 있게끔 만든 것임.
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try{if(conn !=null) conn.close();}catch(Exception e) {e.printStackTrace();}
			try{if(pstmt !=null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
			//try{if(rs !=null) rs.close();}catch(Exception e) {e.printStackTrace();}
		}//접근한 자원해제함으로써 서버무리하지않게함
		return false; //데이터베이스 오류
	}
}
