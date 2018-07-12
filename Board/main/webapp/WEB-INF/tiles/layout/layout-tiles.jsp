<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%-- ===== #35. tiles 를 사용하는 레이아웃 페이지 만들기  ===== --%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"  %>    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>tiles 를 사용한 메인(시작) 페이지 작성하기</title>

  <meta name="viewport" content="width=device-width, initial-scale=1">
  
  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/resources/bootstrap-3.3.7-dist/css/bootstrap.css">
      
  <script type="text/javascript" src="<%=request.getContextPath() %>/resources/js/jquery-2.0.0.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath() %>/resources/bootstrap-3.3.7-dist/js/bootstrap.js"></script>
    
  <style type="text/css">
  	#mycontainer	{ width:90%; margin:0 auto; padding:20px; }
	#myheader		{ background-color:#EBEBE0; height:56px; padding: 10px; }
	#mycontent		{ background-color:#F5F5F5; float:left; width:75%; min-height:800px; padding-top: 40px;}
	#mysideinfo		{ background-color:#F0FFFF; float:left; width:25%; min-height:800px; padding-top: 20px;}
	#myfooter		{ background-color:#555555; clear:both; height:100px; }
	/* #displayRank 	{ margin:20px; height:200px;} */
	
	#myheader a {text-decoration:none;}
	
	/* mouse over link */
	#myheader a:hover {color: navy; font-weight: bolder;}
	/* 
	  unvisited link 
	a:link {color: #FF0000;}
		
	  visited link 
	a:visited {color: #00FF00;}
		
	  selected link 
	a:active {color: #0000FF;}
	*/
	
	#myheader .mynav {font-size: 13pt;}
	.myrank {font-weight:bold; color:red; font-size:13pt;}
	.mynumber {text-align:center;}
	
	</style>
</head>

<body>
	<div id="mycontainer">
		<div id="myheader">
			<tiles:insertAttribute name="header" />
		</div>
	
		<div id="mycontent">
			<tiles:insertAttribute name="content" />
		</div>
		
		<div id="mysideinfo">
			<tiles:insertAttribute name="sideinfo" />
		</div>
	
		<div id="myfooter">
			<tiles:insertAttribute name="footer" />
		</div>
	</div>
</body>
</html>