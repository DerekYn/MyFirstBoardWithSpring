<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    

<style type="text/css">
  .carousel-inner > .item > img,
  .carousel-inner > .item > a > img {
      width: 70%;
      margin: auto;
  }
  
  .myborder {
	border: navy solid 1px;
  }
</style>


<script type="text/javascript">
	
	$(document).ready(function(){
		
		var prodseq =  "${viewProductmap.PRODSEQ}"; // 첨부된 이미지파일의 제품번호 얻어오기
		var thumbnailFileName = "${viewProductImageList.get(0).THUMBNAILFILENAME}"; // 첨부된 이미지의 첫번째 썸네일파일명 얻어오기  
		
		goLargeImgView(prodseq, thumbnailFileName);
		
		$(".my_thumbnail").hover(
				function(){
					$(this).addClass("myborder");
				},
				function(){
					$(this).removeClass("myborder");
				}
		);
				
	});
	
	
	function goLargeImgView(prodseq, thumbnailFileName) {
		
		form_data = {fk_prodseq : prodseq,
				     thumbnailFileName : thumbnailFileName};
		
		$.ajax({ 
			url : "/board/product/getLargeImgFilename.action", 	
			type : "GET",
			data : form_data,  
			dataType : "JSON",  // 응답은 JSON 타입으로 받아라.
			success: function(data) { // 데이터 전송이 성공적으로 이루어진 후 처리해줄 callback 함수
					// data 는 ajax 요청에 의해 url 페이지 getLargeImgFilename.action 으로 부터 리턴받은 데이터
			   			   
				   $("#largeImg").empty();
				   // <div id="largeImg"> 엘리먼트를 모두 비워서 새로운 데이터를 받을수 있게 해라
				   
				   var html = "";
				   html += "<img src=\"/board/resources/files/"+data.IMGFILENAME+"\" "+"width='460' height='345' />";    
			   
		   	   	   $("#largeImg").html(html);   // html을  largeImg 넣어주어라
			
			}, // end of success: function(data) ---------------------
			
			error: function(request, status, error){
		        alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		    } // end of error: function(request,status,error)
			
		}); // end of $.ajax --------------------
	}
	
	
	function goList(){
		location.href= "<%= request.getContextPath() %>/list.action";
	}

</script>


<div align="left" style="width: 80%; padding-left: 5%; margin: 1%; font-size: 14pt; line-height: 160%;">
	 <ol style="list-style-type: decimal;">
	   <li>제품번호 : ${viewProductmap.PRODSEQ}</li>
	   <li>카테고리명 : ${viewProductmap.CATENAME}</li>
	   <li>제품명 : ${viewProductmap.PRODNAME}</li>
	   <li>제조사 : ${viewProductmap.PRODCOMPANY}</li>
	   <li>잔고량 : ${viewProductmap.PRODQTY}</li>
	   <li>제품가 : <fmt:formatNumber value="${viewProductmap.PRICE}" pattern="###,###" /> 원</li>
	   <li>판매가 : <fmt:formatNumber value="${viewProductmap.SALEPRICE}" pattern="###,###" /> 원</li>
	   <li>제품설명 : ${viewProductmap.PRODCONTENT}</li>
	   <li>포인트 : <fmt:formatNumber value="${viewProductmap.PRODPOINT}" pattern="###,###" /> POINT</li>
	   <li>제품스펙 : ${viewProductmap.SPECNAME}</li>
	 </ol>
</div>

<div class="container" align="center">
  <br>
  <div id="myCarousel" class="carousel slide" data-ride="carousel" style="width: 60%;">
    <!-- Indicators -->
    <ol class="carousel-indicators">
      <c:forEach var="viewProductImagemap" items="${viewProductImageList}" varStatus="status">
      	<c:if test="${status.index == 0}">
      		<li data-target="#myCarousel" data-slide-to="${status.index}" class="active"></li>
      	</c:if>
      	<c:if test="${status.index > 0}">
      		<li data-target="#myCarousel" data-slide-to="${status.index}"></li>
      	</c:if>
      </c:forEach>
    </ol>

    <!-- Wrapper for slides -->
    <div class="carousel-inner" role="listbox">
	  <c:forEach var="viewProductImagemap" items="${viewProductImageList}" varStatus="status">
	  	  <c:if test="${status.index == 0}">
	  	  	<div class="item active">
	  	  </c:if>
	  	  <c:if test="${status.index > 0}">
	  	  	<div class="item">
	  	  </c:if>
	        	<img src="<%= request.getContextPath() %>/resources/files/${viewProductImagemap.IMAGEFILENAME}" width="460px" height="345px">
	        	<div class="carousel-caption">
	          		<h3>${viewProductmap.PRODNAME}</h3>
	          		<p><fmt:formatNumber value="${viewProductmap.SALEPRICE}" pattern="###,###" /> 원</p>
	        	</div>
	      	</div>
	  </c:forEach>
    </div>  <%-- end of <div class="carousel-inner" role="listbox"> --%>


    <!-- Left and right controls -->
    <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
      <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
      <span class="sr-only">Previous</span>
    </a>
    <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
      <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
      <span class="sr-only">Next</span>
    </a>
  </div> <%-- end of <div id="myCarousel" class="carousel slide" data-ride="carousel"> --%>
</div> <%-- end of <div class="container"> --%>

<hr style="border: dotted 5px red;">
	
<div id="largeImg" align="center" style="border: green solid 0px; width: 45%; padding: 2%; margin: 2% auto;">
		
</div>  
	
<div align="center" style="border: red solid 0px; width: 80%; margin: auto; padding: 20px;">
	<c:forEach var="viewProductImagemap" items="${viewProductImageList}" varStatus="status">
		<img src="<%= request.getContextPath() %>/resources/files/${viewProductImagemap.THUMBNAILFILENAME}" class="my_thumbnail" style="margin-right: 10px;" onClick="goLargeImgView('${viewProductmap.PRODSEQ}','${viewProductImagemap.THUMBNAILFILENAME}');" />
	</c:forEach>
</div>
	
<div align="center" style="margin-top: 10px; height: 100px; border: solid red 0px;">
	<button type="button" class="btn btn-success" style="width: 80px; height: 40px;" onClick="javascript:location.href='<%= request.getContextPath() %>/product/listProduct.action'">제품목록</button>
</div>

	
	
	