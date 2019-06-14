<%@ page language="java" contentType="text/html;charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "java.io.PrintWriter" %>
<%@ page import = "user.UserDAO" %>
<!DOCTYPE html>
<!-- DOCTYPE html은 자동으로 html5라고 사용한다고 설정해주는 것 -->
<html>
<head>
	<meta charset="UTF-8">
	<meta name = "viewport" content ="width=device-width,initial-scale=1,shrink-to-fit=no">
	<!--  디자인을 알아서 처리해주는 반응형 웹사이트이기 때문에 view포트와 관련된 것들을 적어야 한다 -->
	
	<title>강의평가 웹 사이트</title>
	<!--  부트스트랩 CSS 추가하기 -->
	<link rel = "stylesheet" href = "./css/bootstrap.min.css">
	<!--  커스텀CSS 추가하기 -->
	<link rel = "stylesheet" href = "./css/custom.css">
	
</head>
<body>
<%
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
%>
	<nav class = "navbar navbar-expand-lg navbar-light bg-light">
	<!-- nav태그는 HTML5부터 적용됨 -->
		<a class="navbar-brand" href = "index.jsp">강의평가 웹 사이트 </a>
		<button class = "navbar-toggler" type = "button" data-toggle="collapse" data-target="#navbar">
		<!-- navbar라는 아이디가 보였다가 안보이는 것을 나타내는 것 -->
		<span class = "navbar-toggler-icon"></span>
		<!-- 작대기가 3개 그어져있는 아이콘 -->
		</button>
		<div id = "navbar" class = "collapse navbar-collapse">
			<ul class="navbar-nav mr-auto">
				<li class = "nav-item active"> 
					<a class = "nav-link" href="index.jsp">메인</a>
				</li>
				<li class = "nav-item dropdown">
					<a class = "nav-link dropdown-toggle" id = "dropdown" data-toggle="dropdown">
						회원 관리
					</a>
					<div class = "dropdown-menu" aria-labelleby="dropdown">
<%
	if(userID == null){
%>
					
						<a class = "dropdown-item" href="userLogin.jsp">로그인</a>
						<a class = "dropdown-item" href="userJoin.jsp">회원가입</a>
<%
	}else{
%>
						<a class = "dropdown-item" href="userLogout.jsp">로그아웃</a>
<% 
	}
%>						
					</div>
				</li>
			</ul>
		<form action="./index.jsp" method="get" class="form-inline my-2 my-lg-0">
          <input type="text" name="search" class="form-control mr-sm-2" placeholder="내용을 입력하세요.">
          <button class="btn btn-outline-success my-2 my-sm-0" type="submit">검색</button>
			</form>
		</div>
		
	<!--  navbar-brand는 부트스트랩안에서 로고같은걸 출력해주는 것 -->
	</nav>
		
	<section class="container mt-3" style ="max-width:560px;">
		<form method="post" action = "./userLoginAction.jsp">
			<div class= "form-group">
				<label>아이디</label>
				<input type="text" name = "userID" class = "form-control">
				
			</div>
			<div class = "form-group">
				<label>비밀번호</label>
				<input type = "password" name = "userPassword" class = "form-control">
			</div>
			<button type = "submit" class = "btn btn-primary">로그인</button>
		</form>
		
	</section>
	<footer class = "bg-dark mt-4 p-5 text-center" style = "color: #FFFFFF;">
		Copyright &copy; 2019 김재원 All Rights Reserved.
	</footer> 
	<!-- 제이쿼리 자바스크립트 추가하기 -->
	<script src = "./js/jquery.min.js"></script>
	
	<!-- 파퍼 자바스크립트 추가하기 -->
	<script src = "./js/popper.js"></script>
	
	<!-- 부트스트랩 자바스크립트 추가하기 -->
	<script src = "./js/bootstrap.min.js"></script>
</body>
</html>