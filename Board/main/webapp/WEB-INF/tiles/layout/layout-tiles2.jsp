<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%-- ===== #35. tiles 를 사용하는 레이아웃 페이지 만들기  ===== --%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"  %>    

<%
	String ctxpath = request.getContextPath();
%>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>tiles 를 사용한 게시판 페이지 작성하기</title>

  <meta name="viewport" content="width=device-width, initial-scale=1">
  
  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/resources/bootstrap-3.3.7-dist/css/bootstrap.css">
    
  <%-- 스마트 에디터 구현 시작(no frame 사용시) --%>
   
  <link href="<%=ctxpath%>/resources/smarteditor/css/smart_editor2.css" rel="stylesheet" type="text/css">

  <script type="text/javascript" src="<%=ctxpath%>/resources/smarteditor/js/lib/jindo2.all.js" charset="utf-8"></script>
  <script type="text/javascript" src="<%=ctxpath%>/resources/smarteditor/js/lib/jindo_component.js" charset="utf-8"></script>
  <script type="text/javascript" src="<%=ctxpath%>/resources/smarteditor/js/SE2M_Configuration.js" charset="utf-8"></script>	<!-- 설정 파일 -->
  <script type="text/javascript" src="<%=ctxpath%>/resources/smarteditor/js/SE2BasicCreator.js" charset="utf-8"></script>
  <script type="text/javascript" src="<%=ctxpath%>/resources/smarteditor/js/smarteditor2.min.js" charset="utf-8"></script> 
  

  <!-- 사진첨부샘플  --> 
 
  <script type="text/javascript" src="<%=ctxpath%>/resources/smarteditor/sample/js/plugin/hp_SE2M_AttachQuickPhoto.js" charset="utf-8"></script> 
 
  <%-- 스마트 에디터 구현 끝(no frame 사용시) --%>    
      
  <script type="text/javascript" src="<%=request.getContextPath() %>/resources/js/jquery-2.0.0.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath() %>/resources/bootstrap-3.3.7-dist/js/bootstrap.js"></script>
  
  
  <style type="text/css">
  	#mycontainer	{ width:90%; margin:0 auto; padding:20px; }
	#myheader		{ background-color:#EBEBE0; height:56px; padding: 10px; }
	#mycontent		{ background-color:#F5F5F5; min-height:800px; padding-top: 40px;}
	#myfooter		{ background-color:#555555; height:100px; }
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
		
		<div id="myfooter">
			<tiles:insertAttribute name="footer" />
		</div>
	</div>
</body>
</html>