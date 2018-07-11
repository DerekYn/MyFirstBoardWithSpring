package com.spring.chatting.websocket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.gson.Gson;
import com.spring.chatting.model.MessageVO;
import com.spring.member.model.MemberVO;

// === #171. (웹채팅관련3) === 

public class WebsocketEchoHandler extends TextWebSocketHandler {

	    // === 서버에 연결한 사용자들을 저장하는 리스트 ===
	    private List<WebSocketSession> connectedUsers = new ArrayList<WebSocketSession>();
	 
	    public WebsocketEchoHandler() { }
	 
	    
	    /*
	       afterConnectionEstablished(WebSocketSession wsession) 메소드는 클라이언트 연결 이후에 실행되는 메소드로서
	       WebSocket 연결이 열리고 사용이 준비될 때 호출되어지는(실행되어지는) 메소드이다.
	     */
	   	 // >>> 파라미터 WebSocketSession 은  접속한 사용자임.
	    @Override
	    public void afterConnectionEstablished(WebSocketSession wsession) 
	    	throws Exception {
	        
	    	connectedUsers.add(wsession);
	 
	    	// 클라이언트의 IP Address 얻어오기
	    	/*
	    	   이클립스 메뉴의 
	    	  Run --> Run Configuration 
	    	      --> Arguments 탭
	    	      --> VM arguments 속에 맨 뒤에
	    	      --> 한칸 띄우고 -Djava.net.preferIPv4Stack=true 
	    	                  을 추가한다.  
	    	*/
	       
	        System.out.println("====> 웹채팅확인용 : " + wsession.getId() + "님이 접속했습니다.");
	        System.out.println("====> 웹채팅확인용 : " + "연결 컴퓨터명 : " + wsession.getRemoteAddress().getHostName());
	        System.out.println("====> 웹채팅확인용 : " + "연결 컴퓨터명 : " + wsession.getRemoteAddress().getAddress().getHostName());
	        System.out.println("====> 웹채팅확인용 : " + "연결 IP : " + wsession.getRemoteAddress().getAddress().getHostAddress()); 
	     
	    }
	 
	    
	    /*
          handleTextMessage(WebSocketSession wsession,  TextMessage message) 메소드는 
                 클라이언트가 웹소켓서버로 메시지를 전송했을 때 호출되는(실행되는) 메소드이다.
        */
	    // === 이벤트를 처리 ===
	     // >>> Send : 클라이언트가 서버로 메시지를 보냄
	     
	     // >>> 파라미터  WebSocketSession 은  메시지를 보낸 클라이언트임.
	     // >>> 파라미터  TextMessage 은  메시지의 내용임.
	    @Override
	    protected void handleTextMessage(WebSocketSession wsession, TextMessage message) 
	    	throws Exception {
	    	// ==== 웹소켓(wsession)을 사용하여 HttpSession에 저장된 객체 사용하기 ====
	    	/*
	    	       먼저 /webapp/WEB-INF/spring/config/websocketContext.xml 파일에서
		    	websocket:handlers 태그에 websocket:mapping 아래 websocket:handshake-interceptors에
	            HttpSessionHandshakeInterceptor를 추가하면 WebSocketHandler에 접근하기 전에 
	                     먼저 HttpSession에 접근하여 저장된 값을 읽어 들여 WebSocketHandler에서 사용할 수 있도록 처리해줌. 
            */
	    	Map<String,Object> map = wsession.getAttributes();
	    	MemberVO loginuser = (MemberVO)map.get("loginuser");
	    	System.out.println("====> 웹채팅확인용 : 로그인ID : " + loginuser.getUserid());
	    	// ====> 웹채팅확인용 : 로그인ID : seoyh
	    	
	        MessageVO messageVO = MessageVO.convertMessage(message.getPayload());
	        // message.getPayload() 는  Return the message payload, never be null.
	        // payload(페이로드) ==> 데이터를 나르는 패킷, 메시지 또는 코드의 부분이라는 뜻.
	        // message.getPayload() 은  사용자가 보낸 메시지이다.
	       
	        String hostAddress = "";
	 
	        for (WebSocketSession webSocketSession : connectedUsers) {
	            if (messageVO.getType().equals("all")) { // 채팅할 대상이 "전체" 일 경우 
	                if (!wsession.getId().equals(webSocketSession.getId())) {  // 메시지를 자기자신을 뺀 나머지 모든 사용자들에게 메시지를 보냄.
	                    webSocketSession.sendMessage(
	                            new TextMessage(wsession.getRemoteAddress().getAddress().getHostAddress() +" [" +loginuser.getName()+ "]" + " ▶ " + messageVO.getMessage()));  
	                }
	            } else { // 채팅할 대상이 "전체"가 아닌 특정대상 일 경우 
	            	hostAddress = webSocketSession.getRemoteAddress().getAddress().getHostAddress();
	                if (messageVO.getTo().equals(hostAddress)) {
	                    webSocketSession.sendMessage(
	                            new TextMessage(
	                                    "<span style='color:red; font-weight: bold;' >"
	                                    + wsession.getRemoteAddress().getAddress().getHostAddress() +" [" +loginuser.getName()+ "]" + "▶ " + messageVO.getMessage()
	                                    + "</span>") );
	                    break;
	                }
	            }
	        }
	 
	        // Payload : 사용자가 보낸 메시지
	        System.out.println("====> 웹채팅확인용 : " + wsession.getId() + "님의 메시지 : " + message.getPayload() );
	    }
	 
	    
	    /*
          afterConnectionClosed(WebSocketSession session, CloseStatus status) 메소드는 
                 클라이언트가 연결을 끊었을 때 즉, WebSocket 연결이 닫혔을 때 호출되어지는(실행되어지는) 메소드이다.
        */
	     // 파라미터 WebSocketSession 은 연결을 끊은 클라이언트.
	     // 파라미터 CloseStatus 은 연결 상태.
	    @Override
	    public void afterConnectionClosed(WebSocketSession wsession, CloseStatus status) 
	    	throws Exception {
	    	
	    	Map<String,Object> map = wsession.getAttributes();
	    	MemberVO loginuser = (MemberVO)map.get("loginuser");
	    	
	    	connectedUsers.remove(wsession);
	   	 
	        for (WebSocketSession webSocketSession : connectedUsers) {
	            if (!wsession.getId().equals(webSocketSession.getId())) { // 메시지를 자기자신을 뺀 나머지 모든 사용자들에게 메시지를 보냄.
	                webSocketSession.sendMessage(new TextMessage(wsession.getRemoteAddress().getAddress().getHostAddress() +" [" +loginuser.getName()+ "]" + "님이 퇴장했습니다.")); 
	            }
	        }
	 
	        System.out.println("====> 웹채팅확인용 : " + wsession.getId() + "님이 퇴장했습니다.");
	    }
	    
	    
	    ///////////////////////////////////////////////////////////////////////
	    
	    public void sendMessage (String message){
	           for (WebSocketSession webSocketSession : this.connectedUsers){
	                  if (webSocketSession.isOpen()){
	                         try{
	                        	 webSocketSession.sendMessage(new TextMessage(message));
	                         }catch (Exception e){
	                          // logger.error(">>>> 메시지 보내기 실패!!", e);
	                        	 System.out.println(">>>> 메시지 보내기 실패!!" + e.getMessage());
	                         }
	                  }
	           }
	     }  
	    
		// init-method(@PostConstruct)
		public void init() throws Exception {
			
		}	    
	    

	
}
