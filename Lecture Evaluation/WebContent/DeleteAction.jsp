<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="user.UserDAO"%>
<%@page import="evaluation.EvaluationDAO"%>
<%@page import="likey.LikeyDTO"%>
<%@page import="java.io.PrintWriter"%>
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
	if(request.getParameter("evaluationID") != null) { //어떠한 글을 삭제할 건지 사용자로부터 입력을 받았으면
		evaluationID = (String) request.getParameter("evaluationID");
	}
	EvaluationDAO evaluationDAO = new EvaluationDAO();
	if(userID.equals(evaluationDAO.getUserID(evaluationID))){
		//현재 게시글을 삭제하려고 하는 사용자가 해당 게시글을 작성한 사용자의 아이디 값이 맞다면,일치한다면
		int result = new EvaluationDAO().delete(evaluationID);
		//그럴 떄만 삭제가 가능하도록 액션.
		if(result == 1){ //성공적으로 삭제가 완료되었다면
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('삭제가 완료되었습니다.');");
			script.println("location.href = 'index.jsp'"); //삭제이후에는 index로 이동
			script.println("</script>");
			script.close();
			return;
		} else{ //삭제가 수행이 안됐다면 
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('데이터베이스오류가 발생하였습니다.');");
			script.println("history.back();");
			script.println("</script>");
			script.close();
			return;
		}
	} else { //그렇지 않다면 자신이 쓴 글이 아니기 때문에 
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('자신이 쓴 글만 삭제 가능합니다.');");
		script.println("history.back();");
		script.println("</script>");
		script.close();
		return;
	}


%>