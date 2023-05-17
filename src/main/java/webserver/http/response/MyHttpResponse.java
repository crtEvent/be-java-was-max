package webserver.http.response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.config.WebConfig;
import webserver.http.MyCookie;
import webserver.http.request.MyHttpRequest;

public class MyHttpResponse {
	private static final Logger logger = LoggerFactory.getLogger(MyHttpResponse.class);

	//private final List<String> httpResponseFactors;

	// start-line
	private final String httpVersion;
	private final String statusCode;
	private final String reasonPhrase;

	// header
	private final Map<String, String> headers;

	// body
	private final byte[] body;

	// meta-data
	private final int contentLength;
	private final String realTargetPath;
	private final MyCookie cookie;

	public MyHttpResponse(MyHttpRequest myHttpRequest, String realTargetPath) throws IOException {
		this.httpVersion = myHttpRequest.getHttpVersion();

		if(realTargetPath.isEmpty()) {
			this.realTargetPath = myHttpRequest.getRequestTarget();
			this.statusCode = "200";
			this.reasonPhrase = "OK";
		} else {
			this.realTargetPath = realTargetPath;
			this.statusCode = "302";
			this.reasonPhrase = "Redirect";
		}

		this.body = makeBody(this.realTargetPath, myHttpRequest.getMimeType());
		this.contentLength = body.length;
		this.cookie = myHttpRequest.getCookie();
		this.headers = makeHeaders(myHttpRequest.getMimeType(), this.statusCode);
	}

	private String makeStatusLine() {
		return String.format("%s %s %s", httpVersion, statusCode, reasonPhrase);
	}

	private Map<String, String> makeHeaders(String mimeType, String statusCode) {
		Map<String, String> headerMap = new LinkedHashMap<>();

		if(statusCode.equals("200")) {
			headerMap.put("Content-Type", mimeType);
			headerMap.put("Content-Length", String.valueOf(contentLength));
		} else if(statusCode.equals("302")) {
			headerMap.put("Location", realTargetPath);
			if(cookie != null) {
				headerMap.put("Set-Cookie", cookie.makeSetCookieResponseHeaderValue());
			}
		}

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
		sb.append(makeStatusLine()).append(System.lineSeparator());
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
