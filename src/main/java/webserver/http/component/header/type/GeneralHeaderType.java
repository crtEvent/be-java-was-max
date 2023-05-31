package webserver.http.component.header.type;

public enum GeneralHeaderType {
	DATE("Date"),
	PRAGMA("Pragma"),
	CACHE_CONTROL("Cache-Control"),
	CONNECTION("Connection"),
	TRANSFER_ENCODING("Transfer-Encoding"),
	VIA("Via");

	private final String fieldName;

	GeneralHeaderType(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return fieldName;
	}

}
