package webserver.http.component.header.type;

public enum ResponseHeaderType {
	LOCATION("Location"),
	SERVER("Server"),
	WWW_AUTHENTICATE("WWW-Authenticate"),
	ACCEPT_RANGES("Accept-Ranges"),
	SET_COOKIE("Set-Cookie");

	private final String fieldName;

	ResponseHeaderType(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return fieldName;
	}

}
