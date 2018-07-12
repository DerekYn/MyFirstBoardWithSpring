<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<script type="text/javascript">

	if(${requestScope.n == 1}) {
		alert("글수정 성공!!");
		location.href="<%= request.getContextPath() %>/view.action?seq=${seq}";
		// 수정된 자신의 글을 보여주는 페이지로 이동
	}
	else {
		alert("글수정 실패!!");
		location.href="<%= request.getContextPath() %>/list.action";
	}

</script>
    