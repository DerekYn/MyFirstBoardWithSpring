<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<style type="text/css">
	table, th, td, input, textarea {border: solid gray 1px;}
	
	#table {border-collapse: collapse;
	 		width: 1000px;
	 		}
	#table th, #table td{padding: 5px;}
	#table th{width: 120px; background-color: #DDDDDD;}
	#table td{width: 860px;}
	.long {width: 470px;}
	.short {width: 120px;} 		

</style>

<script type="text/javascript">
	
	$(document).ready(function(){
		
		
	}); // end of ready()-------------------------------------------
		
</script>

<div style="padding-left: 10%; margin-bottom: 0.2%; border: solid 0px red;">
	<h1>글쓰기</h1>
		
	<form name="writeFrm"> 
		<table id="table">
			<tr>
				<th>성명</th>
				<td>
				    <input type="hidden" name="userid" value="" readonly />
					<input type="text" name="name" value="" class="short" readonly />
				</td>
			</tr>
			<tr>
				<th>제목</th>
				<td><input type="text" name="subject" id="subject" class="long" /></td>
			</tr>
			<tr>
            	<th>내용</th>
            	<td><textarea name="content" id="content" rows="10" cols="100" style="width:95%; height:412px;"></textarea></td>
         	</tr>
         	
			<tr>
				<th>암호</th>
				<td><input type="password" name="pw" id="pw" class="short" /></td>
			</tr>
		</table>
		<br/>
		
		<button type="button" id="btnWrite">쓰기</button>
		<button type="button" onClick="javascript:history.back();">취소</button>
	</form>

</div>	