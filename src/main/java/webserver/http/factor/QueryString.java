package webserver.http.factor;

import java.util.LinkedHashMap;
import java.util.Map;

public class QueryString {

	private final Map<String, String> queryParams = new LinkedHashMap<>();

	public QueryString(String queryString) {
		parseQueryString(queryString);
	}

	private void parseQueryString(String queryString) {
		if (queryString != null && !queryString.isEmpty()) {
			String[] params = queryString.split("&");
			for (String param : params) {
				String[] keyAndValue = param.split("=");
				String key = keyAndValue[0];
				String value = keyAndValue.length == 2 ? keyAndValue[1] : "";

				queryParams.merge(key, value, (existValue, newValue)
					-> newValue.equals("") ? existValue : existValue + "," + newValue);
			}
		}
	}

	public String getQueryParam(String parameterName) {
		String paramValue = queryParams.get(parameterName);
		return paramValue == null? "" : paramValue;
	}

}
