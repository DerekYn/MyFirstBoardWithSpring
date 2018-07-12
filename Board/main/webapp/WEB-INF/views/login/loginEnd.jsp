<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<script type="text/javascript">

	if(${sessionScope.loginuser != null && requestScope.gobackURL == null }) {
		location.href="<%=request.getContextPath()%>/index.action"; 
	}
	else if(${sessionScope.loginuser != null && requestScope.gobackURL != null }) { 
		location.href="${gobackURL}"; 
	}
	else {
		alert("로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요.");
		javascript:history.back();
	}
	

</script>
    