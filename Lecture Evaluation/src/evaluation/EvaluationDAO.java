package evaluation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import util.DatabaseUtil;

public class EvaluationDAO {

	private Connection conn;

	private ResultSet rs;

	public EvaluationDAO() {

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
	public int write(EvaluationDTO evaluationDTO) {//사용자가 한개의 강의 정보를 기록할 수 있도록 해주는 글쓰기 함수.

		String SQL = "INSERT INTO EVALUATION VALUES (NULL,?,?,?,?,?,?,?,?,?,?,?,?,0)";
		//첫번쨰 값이 1씩 증가하는 INCREMENT이므로 미리 NULL값을 넣어주면 차례대로 1씩 증가하면서 알아서 번호를 기입.
		//마지막 likeCount는 기본적으로 좋아요 갯수가 0개부터 시작해야하기 때문에 0. 나머지는 사용자가 직접 입력해야 하는 부분이기 때문에 사용자에게 입력을 받을 수 있도록  ?를 넣는다.
		PreparedStatement pstmt = null;

		try {	
			conn = DatabaseUtil.getConnection();//연결됐음
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1,evaluationDTO.getUserID());
			pstmt.setString(2,evaluationDTO.getLectureName());
			pstmt.setString(3,evaluationDTO.getProfessorName());
			pstmt.setInt(4,evaluationDTO.getLectureYear()); //LectureYear, 즉 강의연도이기 때문에 Int
			pstmt.setString(5,evaluationDTO.getSemesterDivide());
			pstmt.setString(6,evaluationDTO.getLectureDivide());
			pstmt.setString(7,evaluationDTO.getEvaluationTitle());
			pstmt.setString(8,evaluationDTO.getEvaluationContent());
			pstmt.setString(9,evaluationDTO.getTotalScore());
			pstmt.setString(10,evaluationDTO.getCreditScore());
			pstmt.setString(11,evaluationDTO.getComfortableScore());//얼마나 편하게 가르치시는지
			pstmt.setString(12,evaluationDTO.getLectureScore());//강의를 얼마나 잘하시는지
			return pstmt.executeUpdate();//데이터를 검색하는 것 조회할 때 사용.
			//등록이 되었다면 하나가 등록이 되었기 때문에 1을 반환.
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try{if(conn !=null) conn.close();}catch(Exception e) {e.printStackTrace();}
			try{if(pstmt !=null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
			try{if(rs !=null) rs.close();}catch(Exception e) {e.printStackTrace();}
		}//접근한 자원해제함으로써 서버무리하지않게함
		return -1; //데이터베이스 오류
	}
	public ArrayList<EvaluationDTO> getList(String lectureDivide, String searchType, String search, int pageNumber) {

		if(lectureDivide.equals("전체")) {

			lectureDivide = "";

		}

		ArrayList<EvaluationDTO> evaluationList = null;

		PreparedStatement pstmt = null;

		String SQL = "";

		try {

			if(searchType.equals("최신순")) {

				SQL = "SELECT * FROM EVALUATION WHERE lectureDivide LIKE ? AND CONCAT(lectureName, professorName, evaluationTitle, evaluationContent) LIKE ? ORDER BY evaluationID DESC LIMIT " + pageNumber * 5 + ", " + pageNumber * 5 + 6;
//한페이지에 5개씩 평가글이 출력되도록 만
			} else if(searchType.equals("추천순")) {

				SQL = "SELECT * FROM EVALUATION WHERE lectureDivide LIKE ? AND CONCAT(lectureName, professorName, evaluationTitle, evaluationContent) LIKE ? ORDER BY likeCount DESC LIMIT " + pageNumber * 5 + ", " + pageNumber * 5 + 6;

			}

			pstmt = conn.prepareStatement(SQL);

			pstmt.setString(1, "%" + lectureDivide + "%");

			pstmt.setString(2, "%" + search + "%");

			rs = pstmt.executeQuery();

			evaluationList = new ArrayList<EvaluationDTO>();

			while(rs.next()) {

				EvaluationDTO evaluation = new EvaluationDTO(

					rs.getInt(1),

					rs.getString(2),

					rs.getString(3),

					rs.getString(4),

					rs.getInt(5),

					rs.getString(6),

					rs.getString(7),

					rs.getString(8),

					rs.getString(9),

					rs.getString(10),

					rs.getString(11),

					rs.getString(12),

					rs.getString(13),

					rs.getInt(14)

				);

				evaluationList.add(evaluation);

			}

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			try {

				if(rs != null) rs.close();

				if(pstmt != null) pstmt.close();

				if(conn != null) conn.close();

			} catch (Exception e) {

				e.printStackTrace();

			}

		}

		return evaluationList;

	}
	
	public int like(String evaluationID) {
		String SQL = "UPDATE EVALUATION SET likeCount = likeCount+1 WHERE evaluationID = ?";//특정사용자로 하여금 이메일인증이 되도록 처리
		Connection conn  = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {	
			conn = DatabaseUtil.getConnection();//연결됐음
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1,Integer.parseInt(evaluationID));
		    return pstmt.executeUpdate();
			
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try{if(conn !=null) conn.close();}catch(Exception e) {e.printStackTrace();}
			try{if(pstmt !=null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
		}
		return -1; //데이터베이스 오류
		//좋아요를 눌러주는 함수
	}
	public int delete(String evaluationID) {
		String SQL = "DELETE FROM EVALUATION WHERE evaluationID = ?";//특정사용자로 하여금 이메일인증이 되도록 처리
		Connection conn  = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {	
			conn = DatabaseUtil.getConnection();//연결됐음
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1,Integer.parseInt(evaluationID));
		    return pstmt.executeUpdate();
			
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try{if(conn !=null) conn.close();}catch(Exception e) {e.printStackTrace();}
			try{if(pstmt !=null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
		}
		return -1; //데이터베이스 오류
	}
	//추천을 삭제하는 함수
	public String getUserID(String evaluationID) { //특정한 강의평가글을 작성한 사용자의 ID를 구하는 함수
		String SQL = "SELECT userID FROM EVALUATION WHERE evaluationID = ?";
		Connection conn  = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {	
			conn = DatabaseUtil.getConnection();//연결됐음
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1,Integer.parseInt(evaluationID));
			rs=pstmt.executeQuery();
			while(rs.next()) { //존재하는 사용자의 아이디인 경우
				return rs.getString(1);//결과가 존재하면 아이디값 반환
			}
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try{if(conn !=null) conn.close();}catch(Exception e) {e.printStackTrace();}
			try{if(pstmt !=null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
			try{if(rs !=null) rs.close();}catch(Exception e) {e.printStackTrace();}
		}//접근한 자원해제함으로써 서버무리하지않게함
		return null; //아이디값이 없는경우 null값 출력.
	}
}

