<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%-- ===== #38.  tiles 중 sideinfo 페이지 만들기 ===== --%>

<script type="text/javascript">

	$(document).ready(function() {
		loopshowNowTime();
		
		showWeather(); // 기상청 날씨정보 공공API XML데이터호출하기
		
	}); // end of ready(); ---------------------------------

	function showNowTime() {
		
		var now = new Date();
	
		var strNow = now.getFullYear() + "-" + (now.getMonth() + 1) + "-" + now.getDate();
		
		var hour = "";
		if(now.getHours() > 12) {
			hour = " 오후 " + (now.getHours() - 12);
		} else if(now.getHours() < 12) {
			hour = " 오전 " + now.getHours();
		} else {
			hour = " 정오 " + now.getHours();
		}
		
		var minute = "";
		if(now.getMinutes() < 10) {
			minute = "0"+now.getMinutes();
		} else {
			minute = ""+now.getMinutes();
		}
		
		var second = "";
		if(now.getSeconds() < 10) {
			second = "0"+now.getSeconds();
		} else {
			second = ""+now.getSeconds();
		}
		
		strNow += hour + ":" + minute + ":" + second;
		
		$("#clock").html("<span style='color:green; font-weight:bold;'>"+strNow+"</span>");
	
	}// end of function showNowTime() -----------------------------


	function loopshowNowTime() {
		showNowTime();
		
		var timejugi = 1000;   // 시간을 1초 마다 자동 갱신하려고.
		
		setTimeout(function() {
						loopshowNowTime();	
					}, timejugi);
		
	}// end of loopshowNowTime() --------------------------

	
	function showWeather() {
		$.ajax({
			url: "<%= request.getContextPath() %>/weatherXML.action",
			type: "GET",
			dataType: "XML",
			success: function(xml){
				var rootElement = $(xml).find(":root");
			//	console.log($(rootElement).prop("tagName")); // ==> current
			     
			    var weather = $(rootElement).find("weather");
			//  console.log($(weather).attr("year")+"년 "+$(weather).attr("month")+"월 "+$(weather).attr("day")+"일 "+$(weather).attr("hour")+"시");
			//  2018년 06월 28일 16시
			
			    var updateTime = $(weather).attr("year")+"년 "+$(weather).attr("month")+"월 "+$(weather).attr("day")+"일 "+$(weather).attr("hour")+"시";       
			
				var localArr = $(rootElement).find("local");
			//	console.log(localArr.length); // 95
			
			    var html = "업데이트 : <span style='font-weight:bold;'>"+updateTime+"</span><br/>"; 
			        html += "<table class='table table-hover' align='center'>";
			        html += "<tr>";
			        html += "<th>지역</th>";
			        html += "<th>날씨</th>";
			        html += "<th>기온</th>";
			        html += "</tr>";
			        
			    for(var i=0; i<localArr.length; i++) {
			    	var local = $(localArr).eq(i);
			    	/*
			    	   .eq(index) 는 선택된 요소들을 인덱스 번호로 찾을 수 있는 선택자이다.
			    	      마치 배열의 인덱스(index)로 값(value)을 찾는것과 같은 효과를 낸다.
			    	*/
			     //	console.log( $(local).text() + " desc:" + $(local).attr("desc") + " ta:" + $(local).attr("ta") );
			    	html += "<tr>";
			    	html += "<td>"+$(local).text()+"</td><td>"+$(local).attr("desc")+"</td><td>"+$(local).attr("ta")+"</td>";
			    	html += "</tr>";
			    }     
			    
			    html += "</table>";
			    
			    $("#displayWeather").html(html);
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
		});	
	}
	
</script>


<div style="margin: 0 auto;" align="center">
	현재시각 :&nbsp; 
	<div id="clock" style="display:inline;"></div>
	
	<div id="displayWeather" style="min-width: 90%; margin-top: 20px; padding-left: 10px; padding-right: 10px;"></div> 
</div>

	
	
	
	
	
	