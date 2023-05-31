package webserver.http.component.start_line;

import webserver.http.component.HttpMethod;

public class RequestLine implements StartLine {
	private final HttpMethod method;
	private final String requestTarget;
	private final String httpVersion;

	public RequestLine(HttpMethod method, String requestTarget, String httpVersion) {
		this.method = method;
		this.requestTarget = requestTarget;
		this.httpVersion = httpVersion;
	}

	public String getRequestLineMessage() {
		return String.format("%s %s %s%s", method, requestTarget, httpVersion, System.lineSeparator());
	}

	public HttpMethod getMethod() {
		return method;
	}

	public String getRequestTarget() {
		return requestTarget;
	}

	public String getRequestTargetWithoutQueryString() {
		return requestTarget.split("\\?")[0];
	}

	public String getQueryStringFromRequestTarget() {
		try {
			return requestTarget.split("\\?")[1];
		} catch (IndexOutOfBoundsException e) {
			return "";
		}
	}

	public String getHttpVersion() {
		return httpVersion;
	}
}
