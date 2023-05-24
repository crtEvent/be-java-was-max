package webserver.http.factor.session;

import webserver.http.factor.cookie.Cookie;
import webserver.http.request.HttpRequestMessage;

public class SessionController {

	private SessionController() {}

	public static Object getSessionValue(HttpRequestMessage requestMessage, String sessionName) {
		Cookie cookie = requestMessage.getCookies().getCookieBy(sessionName);
		String uuid = cookie.getValue();

		Session session = SessionMap.getSession(uuid);

		return session != null? session.getValue() : null;
	}
}
