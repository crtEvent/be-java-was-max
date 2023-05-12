package webserver.response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.config.WebConfig;
import webserver.request.MyHttpRequest;

public class MyHttpResponse {
	private static final Logger logger = LoggerFactory.getLogger(MyHttpResponse.class);

	//private final List<String> httpResponseFactors;

	// start-line
	private final String httpVersion;
	private final String stausCode;
	private final String reasonPhrase;

	// header
	private final Map<String, String> headers;

	// body
	private final byte[] body;

	// meta-data
	private final int contentLength;

	public MyHttpResponse(MyHttpRequest myHttpRequest) throws IOException {
		this.httpVersion = myHttpRequest.getHttpVersion();
		this.stausCode = "200";
		this.reasonPhrase = "OK";

		this.body = makeBody(myHttpRequest.getRequestTarget(), myHttpRequest.getMimeType());

		this.contentLength = body.length;

		this.headers = makeHeaders(myHttpRequest.getMimeType(), this.contentLength);
	}

	private String makeStatusLine() {
		return "HTTP/1.1 200 OK";
	}

	private Map<String, String> makeHeaders(String mimeType, int contentLength) {
		Map<String, String> headerMap = new LinkedHashMap<>();
		headerMap.put("Content-Type", mimeType);
		headerMap.put("Content-Length", String.valueOf(contentLength));

		return headerMap;
	}

	private byte[] makeBody(String requestTarget, String mimeType) throws IOException {
		String resourcePath;
		if (mimeType.equals("text/html")) {
			resourcePath = WebConfig.getTemplatesResourcePath();
		} else {
			resourcePath = WebConfig.getStaticResourcePath();
		}

		return Files.readAllBytes(new File(resourcePath + requestTarget).toPath());
	}

	public String responseHeader() {
		StringBuilder sb = new StringBuilder();
		sb.append(httpVersion).append(" ").append(stausCode).append(" ").append(reasonPhrase).append(System.lineSeparator());
		for(Map.Entry<String, String> entry : headers.entrySet()) {
			sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(System.lineSeparator());
		}
		sb.append(System.lineSeparator());

		return sb.toString();
	}

	public byte[] getBody() {
		return body;
	}

	public int getContentLength() {
		return contentLength;
	}
}
