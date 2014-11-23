
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/echo")
public class EchoServerEndPoint {

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("[open] " + session);
	}

	@OnMessage
	public String onMessage(String message, Session session) {
		System.out.println("[" + message + "] " + session);
		return message;
	}

	@OnClose
	public void onClose(Session session) {
		System.out.println("[close] " + session);
	}
}
