package webserver.http.component.start_line;

import webserver.http.component.StatusCodeType;

public class StatusLine implements StartLine {
	private final String httpVersion;
	private final StatusCodeType statusCode;

	public StatusLine(String httpVersion, StatusCodeType statusCode) {
		this.httpVersion = httpVersion;
		this.statusCode = statusCode;
	}

	public String getStatusLineMessage() {
		return String.format("%s %s %s", httpVersion, statusCode.getCodePhrase(), statusCode.getReasonPhrase());
	}

	public String getHttpVersion() {
		return httpVersion;
	}

	public boolean isStatusCodeTypeMatch(StatusCodeType type) {
		return statusCode == type;
	}

	public String getStatusCode() {
		return statusCode.getCodePhrase();
	}

	public String getReasonPhrase() {
		return statusCode.getReasonPhrase();
	}

}
