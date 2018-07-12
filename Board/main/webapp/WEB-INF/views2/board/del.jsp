<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<style type="text/css">
	table, th, td {border: solid gray 1px;}
	
	#table {width: 300px; border-collapse: collapse;}
	
	#table th, #table td {padding: 5px;}
	#table th {width: 80px; background-color: #DDDDDD;}
	#table td {width: 200px;}
</style>

<script type="text/javascript">
	function goDelete() {
		var frm = document.delFrm;
		
		var pwval = frm.pw.value.trim();
		
		if(pwval == "") {
			alert("암호를 입력하세요!!");
			return;
		}
		
		frm.action = "/board/delEnd.action";
		frm.method = "post";
		frm.submit();
	}
</script>

<div style="padding-left: 10%;">
	<h1 style="margin-bottom: 50px;">글삭제</h1>
	
	<form name="delFrm">
		<table id="table">
			<tr>
				<th>암호</th>
				<td>
					<input type="password" name="pw" />
					<input type="hidden" name="seq" value="${seq}" />
					<!-- 삭제해야할 글번호와 함께 암호를 전송하도록 한다. --> 
				</td>
			</tr>
		</table>
		<br/>
		<button type="button" onClick="goDelete();">삭제</button>
		<button type="button" onClick="history.back();">취소</button>
	</form>
	
</div>
    