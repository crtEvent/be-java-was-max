package webserver.http.factor.header;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import webserver.http.factor.header.type.GeneralHeaderType;

// HTTP 헤더: https://developer.mozilla.org/ko/docs/Web/HTTP/Headers
public class Header {

	protected final Map<String, String> headers = new LinkedHashMap<>();

	public void put(String fieldName, String fieldValue) {
		headers.put(fieldName, fieldValue);
	}

	public String getFieldValue(String fieldName){
		String value = headers.get(fieldName);
		return value != null? value : "";
	}

	public String getFieldValue(GeneralHeaderType type){
		String value = headers.get(type.getFieldName());
		return value != null? value : "";
	}

	public Set<Map.Entry<String, String>> getEntrySet() {
		return headers.entrySet();
	}

	public String getHeaderMessage() {
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<String, String> entry : headers.entrySet()) {
			sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(System.lineSeparator());
		}

		return sb.toString();
	}

}
