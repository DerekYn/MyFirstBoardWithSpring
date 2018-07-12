<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<script type="text/javascript">
	alert("${msg}");
    
	location.href="<%= request.getContextPath() %>/view.action?seq=${seq}";           
	// 댓글쓰기를 한 원글 페이지로 이동
</script>    