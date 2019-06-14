<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="user.UserDAO"%>
<%@page import="evaluation.EvaluationDAO"%>
<%@page import="likey.LikeyDAO"%>
<%@page import="java.io.PrintWriter"%>
<%!
	public static String getClientIP(HttpServletRequest request){
	String ip = request.getHeader("X-FORWARDED-FOR"); //프록시서버를 사용한 클라이언트라고 하더라도 그 클라이언트의 IP를 웬만해서는 가져오도록 하는 함수.
	if(ip == null || ip.length() == 0){
		ip = request.getHeader("Proxy-Client-IP");
	}
	if(ip == null || ip.length() == 0){ //처리한 후에도 ip가 null값이거나 ip길이가 0일때는 
		ip = request.getHeader("WL-Proxy-Client-IP"); //ip정보 받아오도록 함.
	}
	if(ip == null || ip.length() == 0){
		ip = request.getRemoteAddr(); // ip주소를 가져올 수 있도록 함.
	}
	return ip;
}//현재 접속한 IP주소를 DB에 저장.
%>
<%

	String userID = null;

	if(session.getAttribute("userID") != null) {

		userID = (String) session.getAttribute("userID");

	}

	if(userID == null) { //사용자가 로그인을 하지 않았다면

		PrintWriter script = response.getWriter();

		script.println("<script>");

		script.println("alert('로그인을 해주세요.');");//특정 게시글을 삭제할 수 없기 때문에 로그인을 해주세요라고 뜸

		script.println("location.href = 'userLogin.jsp'");

		script.println("</script>");

		script.close();

		return;

	}

	request.setCharacterEncoding("UTF-8");
	String evaluationID = null;
	if(request.getParameter("evaluationID") != null) {
		evaluationID = (String) request.getParameter("evaluationID");
	}
	EvaluationDAO evaluationDAO = new EvaluationDAO();
	LikeyDAO likeyDAO = new LikeyDAO();
	int result = likeyDAO.like(userID,evaluationID,getClientIP(request));//사용자의 IP주소를 담아서 함꼐 저장
		if(result == 1){ // 성공적으로 추천버튼이 눌러졌으면
			result = evaluationDAO.like(evaluationID);//실제로 강의평가글의 추천 갯수를 늘려주도록 함
			if(result==1){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('추천이 완료되었습니다.');");
			script.println("location.href = 'index.jsp'");
			script.println("</script>");
			script.close();
			return;
		} else{
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('데이터베이스오류가 발생하였습니다.');");
			script.println("history.back();");
			script.println("</script>");
			script.close();
			return;
		}
	} else {//특정한 사용자가 특정한 강의평가의 추천을 누른 상태라면 
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('이미 추천을 누른 글입니다.');");
		script.println("history.back();");
		script.println("</script>");
		script.close();
		return;
	}


%>