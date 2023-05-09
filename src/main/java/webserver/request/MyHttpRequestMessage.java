package webserver.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import config.WebConfig;

public class MyHttpRequestMessage {
	private static final Logger logger = LoggerFactory.getLogger(MyHttpRequestMessage.class);

	private final String httpRequestMessage;
	private final String uriPath;
	private final String extension;
	private final Map<String, String> queryParams;

	public MyHttpRequestMessage(InputStream in) throws IOException {
		this.httpRequestMessage = getHttpRequestMessageFrom(in);

		String uriWithQueryString = getUriWithQueryStringFrom(this.httpRequestMessage);
		Map<String, String> uriWithQueryStringFactors = parseUrl(uriWithQueryString);
		this.uriPath = uriWithQueryStringFactors.get("uriPath");
		this.extension = uriWithQueryStringFactors.get("extension");
		this.queryParams = getQueryParamsMapFrom(uriWithQueryStringFactors.get("queryParams"));
	}

	private String getHttpRequestMessageFrom(InputStream in) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
		StringBuilder sb = new StringBuilder();

		String line;
		while ((line = br.readLine()) != null && !line.isEmpty()) {
			sb.append(line).append(System.lineSeparator());
		}

		return sb.toString();
	}

	private String getUriWithQueryStringFrom(String httpRequestMessage) {
		try {
			String uriWithQueryString = httpRequestMessage.split(" ")[1];
			if (uriWithQueryString.equals("/")) {
				uriWithQueryString = WebConfig.DEFAULT_URL;
			}

			return uriWithQueryString;
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.error("유효하지 않은 요청 메시지가 입력되었습니다.");
			return "";
		}
	}

	private Map<String, String> parseUrl(String url) {
		Map<String, String> map = new HashMap<>();
		String[] urlWithQueryStringFactors = url.split("\\?");
		String uriPathFactor = urlWithQueryStringFactors[0];

		map.put("uriPath", uriPathFactor);
		int lastDotIndex = uriPathFactor.lastIndexOf(".");
		map.put("extension", uriPathFactor.substring(lastDotIndex + 1));
		map.put("queryParams", urlWithQueryStringFactors.length >= 2? urlWithQueryStringFactors[1] : "");

		return map;
	}

	private Map<String, String> getQueryParamsMapFrom(String queryString) {
		Map<String, String> map = new LinkedHashMap<>();

		if (queryString != null && !queryString.isEmpty()) {
			String[] params = queryString.split("&");
			for (String param : params) {
				String[] keyAndValue = param.split("=");
				String key = keyAndValue[0];
				String value = keyAndValue.length == 2 ? keyAndValue[1] : "";

				map.merge(key, value, (existValue, newValue)
					-> newValue.equals("") ? existValue : existValue + "," + newValue);
			}
		}

		return map;
	}

	public String getHttpRequestMessage() {
		return httpRequestMessage;
	}

	public String getUriPath() {
		return uriPath;
	}

	public String getExtension() {
		return extension;
	}

	public Map<String, String> getQueryParams() {
		return Collections.unmodifiableMap(queryParams);
	}
}
