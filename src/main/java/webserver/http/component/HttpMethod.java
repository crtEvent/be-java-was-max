package webserver.http.component;

public enum HttpMethod {
	GET, POST, PUT, DELETE;

	public static HttpMethod from(String method) {
		for (HttpMethod httpMethod : HttpMethod.values()) {
			if (httpMethod.name().equalsIgnoreCase(method)) {
				return httpMethod;
			}
		}
		return GET;
	}
}
