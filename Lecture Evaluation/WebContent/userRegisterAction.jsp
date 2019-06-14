<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="user.UserDTO"%>
<%@ page import="user.UserDAO"%>
<%@ page import="util.SHA256"%>
<%@ page import="java.io.PrintWriter"%>

<%
	request.setCharacterEncoding("UTF-8");
	String userID = null;
	if(session.getAttribute("userID")!=null){//사용자가 로그인한 상태여서 로그인한 아이디값이 존재한다면
	userID = (String) session.getAttribute("userID");
	}
	if(userID != null){
	PrintWriter script = response.getWriter();
	script.println("<script>");
	script.println("alert('로그인이 된 상태입니다.')");
	script.println("location.href = 'index.jsp';");
	script.println("</script>");
	script.close();
	return;
	}
	String userPassword = null;
	String userEmail = null;
	if(request.getParameter("userID") != null) {
		userID = (String) request.getParameter("userID");
	}
	if(request.getParameter("userPassword") != null) {
		userPassword = (String) request.getParameter("userPassword");
	}
	if(request.getParameter("userEmail") != null) {
		userEmail = (String) request.getParameter("userEmail");
	}
	if (userID == null || userPassword == null || userEmail == null || userID.equals("") || userEmail.equals("") || userPassword.equals("")) {
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('입력이 안 된 사항이 있습니다.');");
		script.println("history.back();");
		script.println("</script>");
		script.close();
		return;//입력이 안되면 바로 종료.
	}// else {
		UserDAO userDAO = new UserDAO();
		int result = userDAO.join(new UserDTO(userID, userPassword, userEmail, SHA256.getSHA256(userEmail), false));
		if (result == -1) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('이미 존재하는 아이디입니다.');");
			script.println("history.back();");
			script.println("</script>");
			script.close();
			return;
		} else {
			session.setAttribute("userID", userID);
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("location.href = 'emailSendAction.jsp';");
			script.println("</script>");
			script.close();
			return;
		}
	//}
%>