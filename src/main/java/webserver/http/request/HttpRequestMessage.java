package webserver.http.request;

import webserver.http.factor.HttpMethod;
import webserver.http.factor.QueryString;
import webserver.http.factor.body.RequestBody;
import webserver.http.factor.cookie.CookieMap;
import webserver.http.factor.cookie.NewCookie;
import webserver.http.factor.header.RequestHeader;
import webserver.http.factor.header.type.RequestHeaderType;
import webserver.http.factor.start_line.RequestLine;

public class HttpRequestMessage {
	private final RequestLine requestLine;
	private final RequestHeader header;
	private final RequestBody body;
	private final QueryString queryString;
	private NewCookie newCookie;

	public HttpRequestMessage(RequestLine requestLine, RequestHeader header, RequestBody body, QueryString queryString) {
		this.requestLine = requestLine;
		this.header = header;
		this.body = body;
		this.queryString = queryString;
	}

	public String getHttpRequestMessage() {
		StringBuilder sb= new StringBuilder();
		sb.append(requestLine.getRequestLineMessage())
			.append(header.getHeaderMessage())
			.append(System.lineSeparator())
			.append(body.getBody());

		return sb.toString();
	}

	public HttpMethod getMethod() {
		return requestLine.getMethod();
	}

	public String getRequestTarget() {
		return requestLine.getRequestTarget();
	}

	public String getRequestTargetWithoutQueryString() {
		return requestLine.getRequestTargetWithoutQueryString();
	}

	public String getQueryStringFromRequestTarget() {
		return requestLine.getQueryStringFromRequestTarget();
	}

	public String getHttpVersion() {
		return requestLine.getHttpVersion();
	}

	public String getBody() {
		return body.getBody();
	}

	public String getQueryParam(String parameterName) {
		return queryString.getQueryParam(parameterName);
	}

	public String getMimeType() {
		String value = header.getFieldValue(RequestHeaderType.ACCEPT);
		if(value == null) {
			return "";
		}

		return value.split(",")[0];
	}

	public CookieMap getCookies() {
		return new CookieMap(header.getFieldValue(RequestHeaderType.COOKIE));
	}

	public NewCookie getNewCookie() {
		return newCookie;
	}

	public void setNewCookie(NewCookie newCookie) {
		this.newCookie = newCookie;
	}

}
