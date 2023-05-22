package webserver.http.factor.cookie;

import java.util.LinkedHashMap;
import java.util.Map;

public class Cookie {
	private final String name;
	private final String value;
	private final Map<CookieSpecType, String> cookieSpecs = new LinkedHashMap<>();

	public Cookie(String name, String value) {
		this.name = name;
		this.value = value;
		setMaxAge(-1);
		setPath("/");
	}

	public void addSpec(CookieSpecType specType, String value) {
		cookieSpecs.put(specType, value);
	}

	public void setMaxAge(int expiry) {
		cookieSpecs.put(CookieSpecType.MAX_AGE, String.valueOf(expiry));
	}

	public void setPath(String path) {
		cookieSpecs.put(CookieSpecType.PATH, path);
	}

	public String getSetCookieResponseHeaderValue() {
		StringBuilder sb = new StringBuilder();

		sb.append(String.format("%s=%s", name, value));

		for(Map.Entry<CookieSpecType, String> entry : cookieSpecs.entrySet()) {
			sb.append(String.format("; %s=%s", entry.getKey().getFieldName(), entry.getValue()));
		}

		return sb.toString();
	}

}
