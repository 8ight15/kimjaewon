package likey;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import user.UserDTO;
import util.DatabaseUtil;

public class LikeyDAO {
	
	public int like(String userID, String evluationID,String userIP) {
			String SQL = "INSERT INTO LIKEY VALUES(?,?,?)";
			Connection conn  = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {	
				conn = DatabaseUtil.getConnection();//연결됐음
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1,userID);
				pstmt.setString(2,evluationID);
				pstmt.setString(3,userIP);
				return pstmt.executeUpdate();
			}catch(SQLException e) {
				e.printStackTrace();
			} finally {
				try{if(conn !=null) conn.close();}catch(Exception e) {e.printStackTrace();}
				try{if(pstmt !=null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
				try{if(rs !=null) rs.close();}catch(Exception e) {e.printStackTrace();}
			}//접근한 자원해제함으로써 서버무리하지않게함
			return -1; //추천중복오류 -1값반환
		}
	}

