<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<%
	session.invalidate(); //현재 사용자가 클라이언트의 모든 세션정보를 파기 시킴.
%>
<script>
	location.href = 'index.jsp';//로그아웃하면 다시 처음페이지로 돌아오게 함.
</script>