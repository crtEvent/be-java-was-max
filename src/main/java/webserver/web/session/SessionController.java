package webserver.web.session;

import webserver.http.component.cookie.Cookie;
import webserver.http.request.HttpRequestMessage;

public class SessionController {

	private SessionController() {}

	public static Object getSessionValue(HttpRequestMessage requestMessage, String sessionName) {
		Cookie cookie = requestMessage.getCookies().getCookieBy(sessionName);

		if(cookie != null) {
			String uuid = cookie.getValue();
			Session session = SessionMap.getSession(uuid);

			return session != null? session.getValue() : null;
		}

		return null;
	}
}
