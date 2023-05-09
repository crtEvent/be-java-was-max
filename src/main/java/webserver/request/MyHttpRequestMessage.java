package webserver.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import config.WebConfig;

public class MyHttpRequestMessage {
	private static final Logger logger = LoggerFactory.getLogger(MyHttpRequestMessage.class);

	private final String path;
	private final Map<String, String> queryParams;

	public MyHttpRequestMessage(InputStream in) throws IOException {
		String httpRequestMessage = getHttpRequestMessageFrom(in);

		String url = getUrlFromHttpRequestMessage(httpRequestMessage);
		String[] urlFactors = parseUrl(url);
		this.path = urlFactors[0];
		this.queryParams = getQueryParamsFrom(urlFactors[1]);
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

	private String getUrlFromHttpRequestMessage(String httpRequestMessage) {
		try {
			String url = httpRequestMessage.split(" ")[1];
			if (url.equals("/")) {
				url = WebConfig.DEFAULT_URL;
			}

			return url;
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.error("유효하지 않은 요청 메시지가 입력되었습니다.");
			return "";
		}
	}

	private String[] parseUrl(String url) {
		String[] urlFactors = url.split("\\?");

		if (urlFactors.length == 2) {
			return urlFactors;
		}
		return new String[] {urlFactors[0], ""};
	}

	private Map<String, String> getQueryParamsFrom(String queryString) {
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

	public String getPath() {
		return path;
	}

	public Map<String, String> getQueryParams() {
		return Collections.unmodifiableMap(queryParams);
	}
}
