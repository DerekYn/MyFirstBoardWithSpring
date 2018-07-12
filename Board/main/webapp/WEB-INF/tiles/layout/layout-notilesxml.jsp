<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%-- ===== #36. tiles 를 사용하지 않는 XML을 사용한 Ajax 를  위한 레이아웃 페이지 만들기  =====
                            조심할것은 xml 형태로 뷰단에서 보여질 .jsp 페이지 파일(앞으로 생성되어질 .jsp 페이지 파일)에서는 
                            상단에 <?xml version="1.0" encoding="UTF-8" ?> 을 넣어주면 안된다는 것이다.
                            왜냐하면 이미 레이아웃 페이지에서 상단부분에 <?xml version="1.0" encoding="UTF-8" ?> 을 넣어주었기 때문이다.                           
 --%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"  %>    

<tiles:insertAttribute name="content" />    