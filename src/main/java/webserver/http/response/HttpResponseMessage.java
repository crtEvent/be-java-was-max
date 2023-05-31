package webserver.http.response;

import webserver.http.component.body.ResponseBody;
import webserver.http.component.header.ResponseHeader;
import webserver.http.component.start_line.StatusLine;

public class HttpResponseMessage {
	private final StatusLine statusLine;
	private final ResponseHeader header;
	private final ResponseBody body;

	public HttpResponseMessage(StatusLine statusLine, ResponseHeader header, ResponseBody body) {
		this.statusLine = statusLine;
		this.header = header;
		this.body = body;
	}

	public String getStatusLineAndResponseHeaderMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append(statusLine.getStatusLineMessage())
			.append(System.lineSeparator())
			.append(header.getHeaderMessage())
			.append(System.lineSeparator());

		return sb.toString();
	}

	public byte[] getBody() {
		return body.getBody();
	}

	public int getContentLength() {
		return body.getContentLength();
	}
}
