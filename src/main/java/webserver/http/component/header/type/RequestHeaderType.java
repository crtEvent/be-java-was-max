package webserver.http.component.header.type;

public enum RequestHeaderType {
	REFERER("Referer"),
	ACCEPT("Accept"),
	ACCEPT_CHARSET("Accept-Charset"),
	ACCEPT_ENCODING("Accept-Encoding"),
	ACCEPT_LANGUAGE("Accept-Language"),
	COOKIE("Cookie"),
	HOST("Host");

	private final String fieldName;

	RequestHeaderType(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return fieldName;
	}

}
