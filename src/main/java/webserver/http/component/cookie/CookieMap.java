package webserver.http.component.cookie;

import java.util.HashMap;
import java.util.Map;

public class CookieMap {
	private final Map<String, String> cookies = new HashMap<>();

	public CookieMap(String cookieHeaderValue) {
		parse(cookieHeaderValue);
	}

	private void parse(String cookieHeaderValue) {
		String[] splitCookies = cookieHeaderValue.split(";\\s*");

		for(String cookie : splitCookies) {
			String[] keyValue = cookie.split("=");
			cookies.put(keyValue[0], keyValue[1]);
		}
	}

	public Cookie getCookieBy(String name) {
		String value = cookies.get(name);
		if(value != null) {
			return new Cookie(name, value);
		}
		return null;
	}
}
