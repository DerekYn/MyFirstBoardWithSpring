<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<script type="text/javascript">

	if(${requestScope.n == 1}) {
		alert("글삭제 성공!!");
		location.href="<%= request.getContextPath() %>/list.action";
	}
	else {
		alert("글수정 실패!!");
		javascript:history.back(); // 이전 페이지로 이동
	}

</script>
    