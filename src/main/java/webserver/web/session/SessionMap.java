package webserver.web.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import webserver.http.component.cookie.NewCookie;
import webserver.http.request.HttpRequestMessage;

public class SessionMap {
	private static final Map<String, Session> sessions = new ConcurrentHashMap<>();

	private SessionMap() {}

	public static void addSession(String name, Object value, HttpRequestMessage requestMessage) {
		Session session = new Session(name, value);
		sessions.put(session.getSessionId(), session);

		NewCookie newCookie = new NewCookie(name, session.getSessionId());
		newCookie.setMaxAge(60 * 60 * 24);
		requestMessage.setNewCookie(newCookie);
	}

	public static Session getSession(String sessionId) {
		return sessions.get(sessionId);
	}


}
