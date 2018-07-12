<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<style type="text/css">

	.mydiv {display: inline-block; 
	        position: relative; 
	        top: 30px; 
	        line-height: 150%; 
	        border: solid 0px green;
	       }
	
	.mydisplay {display: block;}
	       	
	.myfont {font-size: 14pt;}
	
</style>

<script type="text/javascript">
 
    $(document).ready(function(){
    
    	$("#btnLOGIN").click(function(){
    		func_Login(event);
    	});// end of $("#btnLOGIN").click()----------------
    	 
    	$("#pwd").keydown(function(event){
    		if(event.keyCode == 13) { // 엔터를 했을 경우
    			func_Login(event);
    		}
    	});// end of $("#pwd").keydown()--------------------
    	
    	 
    }); // end of $(document).ready()---------------------------	 


    
    function func_Login(event) {
    	
    	if(${sessionScope.loginuser != null}) {
    		alert("이미 로그인 하신 상태입니다!!");
    		$("#userid").val("");
    		$("#pwd").val("");
    		$("#userid").focus();
    		event.preventDefault();
    		return;
    	}
    	
    	var userid = $("#userid").val();
    	var pwd = $("#pwd").val();
    	
    	if(userid.trim() == "") {
    		alert("아이디를 입력하세요!!");
    		$("#userid").val("");
    		$("#userid").focus();
    		event.preventDefault();
    		return;
    	}
    	
    	if(pwd.trim() == "") {
    		alert("암호를 입력하세요!!");
    		$("#pwd").val("");
    		$("#pwd").focus();
    		event.preventDefault();
    		return;
    	}
    	
    	var frm = document.loginFrm;
    	frm.action = "/board/loginEnd.action";
    	frm.method = "post";
    	frm.submit();
    	
    }  // end of function func_Login(event)----------------------
    
</script>

<div style="width:90%; margin: auto; border: solid 0px red;">

	<div style="width:80%; margin-top:10%; margin-left:10%; height:300px; border: solid 0px blue;">
		<h2 class="text-primary">로그인</h2>
		<p class="bg-primary">&nbsp;</p>
		
		<form name="loginFrm">
			<div class="mydiv" style="margin-left: 15%;">
				<span class="mydisplay myfont" >아이디 :</span>
				<span class="mydisplay myfont" style="margin-top: 30px;">암&nbsp;&nbsp;&nbsp;호 :</span> 
			</div>
			
			<div class="mydiv" style="margin-left: 5%;">
				<input class="mydisplay form-control" type="text" name="userid" id="userid" size="20" />
				<input class="mydisplay form-control" style="margin-top: 15px;" type="pwd" name="pwd" id="pwd" size="20" /> 
			</div>
			
			<div class="mydiv" style="margin-left: 10%;">
				<button class="btn btn-success" style="width: 100px; font-size: 14pt;" type="button" id="btnLOGIN" >확인</button> 
			</div>	 
		</form>
	</div>
	
</div>




  