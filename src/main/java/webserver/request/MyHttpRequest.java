package webserver.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.config.WebConfig;

public class MyHttpRequest {
	private static final Logger logger = LoggerFactory.getLogger(MyHttpRequest.class);

	private static final String FIELD_NAME_HTTP_METHOD = "method";
	private static final String FIELD_NAME_REQUEST_TARGET = "request-target";
	private static final String FIELD_NAME_HTTP_VERSION = "http-version";

	private final List<String> httpRequestFactors;

	// start-line
	private final String method;
	private final String requestTarget;
	private final String httpVersion;

	// header
	private final Map<String, String> headers;

	// meta-data
	private final Map<String, String> queryParams;
	private final String mimeType;


	public MyHttpRequest(InputStream in) throws IOException {
		this.httpRequestFactors = getHttpRequestFactorsFrom(in);

		Map<String, String> startLineFactors = extractStartLine(this.httpRequestFactors);
		this.method = startLineFactors.get(FIELD_NAME_HTTP_METHOD);
		this.requestTarget = startLineFactors.get(FIELD_NAME_REQUEST_TARGET);
		this.httpVersion = startLineFactors.get(FIELD_NAME_HTTP_VERSION);

		this.headers = extractHeader(this.httpRequestFactors);

		this.queryParams = extractQueryParams(this.requestTarget);
		this.mimeType = headers.get("Accept").split(",")[0];
	}

	private List<String> getHttpRequestFactorsFrom(InputStream in) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
		List<String> requestFactors = new ArrayList<>();

		String line;
		while ((line = br.readLine()) != null && !line.isEmpty()) {
			requestFactors.add(line);
		}

		return requestFactors;
	}

	private Map<String, String> extractStartLine(List<String> httpRequestMessageFactors) {
		Map<String, String> startLineFactors = new HashMap<>();
		String[] splitStartLine;
		try {
			splitStartLine = httpRequestMessageFactors.get(0).split(" ");
		} catch (IndexOutOfBoundsException e) {
			logger.error("잘못된 요청 메시지가 입력되었습니다. {}", e.getMessage());
			return startLineFactors;
		}

		if(splitStartLine.length == 3) {
			startLineFactors.put(FIELD_NAME_HTTP_METHOD, splitStartLine[0]);
			startLineFactors.put(FIELD_NAME_REQUEST_TARGET
				, splitStartLine[1].equals("/") ? WebConfig.getWelcomePage() : splitStartLine[1]);
			startLineFactors.put(FIELD_NAME_HTTP_VERSION, splitStartLine[2]);
		} else {
			startLineFactors.put(FIELD_NAME_HTTP_METHOD, "");
			startLineFactors.put(FIELD_NAME_REQUEST_TARGET, "");
			startLineFactors.put(FIELD_NAME_HTTP_VERSION, "");
		}

		return startLineFactors;
	}

	private Map<String, String> extractQueryParams(String requestTarget) {
		Map<String, String> paramMap = new LinkedHashMap<>();
		String queryString;
		try {
			queryString = requestTarget.split("\\?")[1];
		} catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
			return paramMap;
		}

		if (queryString != null && !queryString.isEmpty()) {
			String[] params = queryString.split("&");
			for (String param : params) {
				String[] keyAndValue = param.split("=");
				String key = keyAndValue[0];
				String value = keyAndValue.length == 2 ? keyAndValue[1] : "";

				paramMap.merge(key, value, (existValue, newValue)
					-> newValue.equals("") ? existValue : existValue + "," + newValue);
			}
		}

		return paramMap;
	}

	private Map<String, String> extractHeader(List<String> httpRequestMessageFactors) {
		Map<String, String> headerMap = new LinkedHashMap<>();
		for(int i = 1; i < httpRequestMessageFactors.size(); i++) {
			if(httpRequestMessageFactors.get(i).isEmpty()) {
				break;
			}
			String[] pair = httpRequestMessageFactors.get(i).split(":\\s*");
			headerMap.put(pair[0], pair[1]);
		}

		return headerMap;
	}

	public List<String> getHttpRequestFactors() {
		return httpRequestFactors;
	}

	public String getMethod() {
		return method;
	}

	public String getRequestTarget() {
		return requestTarget;
	}

	public String getHttpVersion() {
		return httpVersion;
	}

	public String getQueryParam(String parameterName) {
		String paramValue = queryParams.get(parameterName);
		return paramValue == null? "" : paramValue;
	}

	public Map<String, String> getQueryParams() {
		return Collections.unmodifiableMap(queryParams);
	}

	public String getHeaderFieldValue(String fieldName) {
		String fieldValue = headers.get(fieldName);
		return fieldValue == null? "" : fieldValue;
	}

	public Map<String, String> getHeaders() {
		return Collections.unmodifiableMap(headers);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(String line : httpRequestFactors) {
			sb.append(line).append(System.lineSeparator());
		}

		return sb.toString();
	}

	public String getMimeType() {
		return mimeType;
	}
}
