package webserver.http;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class MyCookie {
	private final Map<String, String> cookies = new LinkedHashMap<>();

	public void putCookieValue(String key, String value) {
		cookies.put(key, value);
	}

	public Set<Map.Entry<String, String>> getCookieSet() {
		return cookies.entrySet();
	}

	public String get(String key) {
		return cookies.get(key);
	}

	public String makeSetCookieResponseHeaderValue() {
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<String, String> entry : cookies.entrySet()) {
			sb.append(String.format("%s=%s; ", entry.getKey(), entry.getValue()));
		}
		sb.delete(sb.length() - 2, sb.length());

		return sb.toString();
	}
}
