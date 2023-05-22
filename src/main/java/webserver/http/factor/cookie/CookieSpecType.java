package webserver.http.factor.cookie;

// https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Set-Cookie
public enum CookieSpecType {
	EXPIRES("Expires"),
	MAX_AGE("Max-Age"),
	PATH("Path");

	private final String fieldName;

	CookieSpecType(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return fieldName;
	}
}
