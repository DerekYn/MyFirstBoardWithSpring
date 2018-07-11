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

// === #171. (��ä�ð���3) === 

public class WebsocketEchoHandler extends TextWebSocketHandler {

	    // === ������ ������ ����ڵ��� �����ϴ� ����Ʈ ===
	    private List<WebSocketSession> connectedUsers = new ArrayList<WebSocketSession>();
	 
	    public WebsocketEchoHandler() { }
	 
	    
	    /*
	       afterConnectionEstablished(WebSocketSession wsession) �޼ҵ�� Ŭ���̾�Ʈ ���� ���Ŀ� ����Ǵ� �޼ҵ�μ�
	       WebSocket ������ ������ ����� �غ�� �� ȣ��Ǿ�����(����Ǿ�����) �޼ҵ��̴�.
	     */
	   	 // >>> �Ķ���� WebSocketSession ��  ������ �������.
	    @Override
	    public void afterConnectionEstablished(WebSocketSession wsession) 
	    	throws Exception {
	        
	    	connectedUsers.add(wsession);
	 
	    	// Ŭ���̾�Ʈ�� IP Address ������
	    	/*
	    	   ��Ŭ���� �޴��� 
	    	  Run --> Run Configuration 
	    	      --> Arguments ��
	    	      --> VM arguments �ӿ� �� �ڿ�
	    	      --> ��ĭ ���� -Djava.net.preferIPv4Stack=true 
	    	                  �� �߰��Ѵ�.  
	    	*/
	       
	        System.out.println("====> ��ä��Ȯ�ο� : " + wsession.getId() + "���� �����߽��ϴ�.");
	        System.out.println("====> ��ä��Ȯ�ο� : " + "���� ��ǻ�͸� : " + wsession.getRemoteAddress().getHostName());
	        System.out.println("====> ��ä��Ȯ�ο� : " + "���� ��ǻ�͸� : " + wsession.getRemoteAddress().getAddress().getHostName());
	        System.out.println("====> ��ä��Ȯ�ο� : " + "���� IP : " + wsession.getRemoteAddress().getAddress().getHostAddress()); 
	     
	    }
	 
	    
	    /*
          handleTextMessage(WebSocketSession wsession,  TextMessage message) �޼ҵ�� 
                 Ŭ���̾�Ʈ�� �����ϼ����� �޽����� �������� �� ȣ��Ǵ�(����Ǵ�) �޼ҵ��̴�.
        */
	    // === �̺�Ʈ�� ó�� ===
	     // >>> Send : Ŭ���̾�Ʈ�� ������ �޽����� ����
	     
	     // >>> �Ķ����  WebSocketSession ��  �޽����� ���� Ŭ���̾�Ʈ��.
	     // >>> �Ķ����  TextMessage ��  �޽����� ������.
	    @Override
	    protected void handleTextMessage(WebSocketSession wsession, TextMessage message) 
	    	throws Exception {
	    	// ==== ������(wsession)�� ����Ͽ� HttpSession�� ����� ��ü ����ϱ� ====
	    	/*
	    	       ���� /webapp/WEB-INF/spring/config/websocketContext.xml ���Ͽ���
		    	websocket:handlers �±׿� websocket:mapping �Ʒ� websocket:handshake-interceptors��
	            HttpSessionHandshakeInterceptor�� �߰��ϸ� WebSocketHandler�� �����ϱ� ���� 
	                     ���� HttpSession�� �����Ͽ� ����� ���� �о� �鿩 WebSocketHandler���� ����� �� �ֵ��� ó������. 
            */
	    	Map<String,Object> map = wsession.getAttributes();
	    	MemberVO loginuser = (MemberVO)map.get("loginuser");
	    	System.out.println("====> ��ä��Ȯ�ο� : �α���ID : " + loginuser.getUserid());
	    	// ====> ��ä��Ȯ�ο� : �α���ID : seoyh
	    	
	        MessageVO messageVO = MessageVO.convertMessage(message.getPayload());
	        // message.getPayload() ��  Return the message payload, never be null.
	        // payload(���̷ε�) ==> �����͸� ������ ��Ŷ, �޽��� �Ǵ� �ڵ��� �κ��̶�� ��.
	        // message.getPayload() ��  ����ڰ� ���� �޽����̴�.
	       
	        String hostAddress = "";
	 
	        for (WebSocketSession webSocketSession : connectedUsers) {
	            if (messageVO.getType().equals("all")) { // ä���� ����� "��ü" �� ��� 
	                if (!wsession.getId().equals(webSocketSession.getId())) {  // �޽����� �ڱ��ڽ��� �� ������ ��� ����ڵ鿡�� �޽����� ����.
	                    webSocketSession.sendMessage(
	                            new TextMessage(wsession.getRemoteAddress().getAddress().getHostAddress() +" [" +loginuser.getName()+ "]" + " �� " + messageVO.getMessage()));  
	                }
	            } else { // ä���� ����� "��ü"�� �ƴ� Ư����� �� ��� 
	            	hostAddress = webSocketSession.getRemoteAddress().getAddress().getHostAddress();
	                if (messageVO.getTo().equals(hostAddress)) {
	                    webSocketSession.sendMessage(
	                            new TextMessage(
	                                    "<span style='color:red; font-weight: bold;' >"
	                                    + wsession.getRemoteAddress().getAddress().getHostAddress() +" [" +loginuser.getName()+ "]" + "�� " + messageVO.getMessage()
	                                    + "</span>") );
	                    break;
	                }
	            }
	        }
	 
	        // Payload : ����ڰ� ���� �޽���
	        System.out.println("====> ��ä��Ȯ�ο� : " + wsession.getId() + "���� �޽��� : " + message.getPayload() );
	    }
	 
	    
	    /*
          afterConnectionClosed(WebSocketSession session, CloseStatus status) �޼ҵ�� 
                 Ŭ���̾�Ʈ�� ������ ������ �� ��, WebSocket ������ ������ �� ȣ��Ǿ�����(����Ǿ�����) �޼ҵ��̴�.
        */
	     // �Ķ���� WebSocketSession �� ������ ���� Ŭ���̾�Ʈ.
	     // �Ķ���� CloseStatus �� ���� ����.
	    @Override
	    public void afterConnectionClosed(WebSocketSession wsession, CloseStatus status) 
	    	throws Exception {
	    	
	    	Map<String,Object> map = wsession.getAttributes();
	    	MemberVO loginuser = (MemberVO)map.get("loginuser");
	    	
	    	connectedUsers.remove(wsession);
	   	 
	        for (WebSocketSession webSocketSession : connectedUsers) {
	            if (!wsession.getId().equals(webSocketSession.getId())) { // �޽����� �ڱ��ڽ��� �� ������ ��� ����ڵ鿡�� �޽����� ����.
	                webSocketSession.sendMessage(new TextMessage(wsession.getRemoteAddress().getAddress().getHostAddress() +" [" +loginuser.getName()+ "]" + "���� �����߽��ϴ�.")); 
	            }
	        }
	 
	        System.out.println("====> ��ä��Ȯ�ο� : " + wsession.getId() + "���� �����߽��ϴ�.");
	    }
	    
	    
	    ///////////////////////////////////////////////////////////////////////
	    
	    public void sendMessage (String message){
	           for (WebSocketSession webSocketSession : this.connectedUsers){
	                  if (webSocketSession.isOpen()){
	                         try{
	                        	 webSocketSession.sendMessage(new TextMessage(message));
	                         }catch (Exception e){
	                          // logger.error(">>>> �޽��� ������ ����!!", e);
	                        	 System.out.println(">>>> �޽��� ������ ����!!" + e.getMessage());
	                         }
	                  }
	           }
	     }  
	    
		// init-method(@PostConstruct)
		public void init() throws Exception {
			
		}	    
	    

	
}
