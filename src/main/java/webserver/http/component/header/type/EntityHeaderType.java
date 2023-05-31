package webserver.http.component.header.type;

public enum EntityHeaderType {
	ALLOW("Alow"),
	CONTENT_ENCODING("Content-Encoding"),
	CONTENT_LENGTH("Content-Length"),
	CONTENT_TYPE("Content-Type"),
	CONTENT_LANGUAGE("Content-Language"),
	CONTENT_LOCATION("Content-Location"),
	CONTENT_RANGE("Content-Range"),
	EXPIRES("Expires"),
	LAST_MODIFIED("Last-Modified"),
	ETAG("Etag");

	private final String fieldName;

	EntityHeaderType(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return fieldName;
	}

}
