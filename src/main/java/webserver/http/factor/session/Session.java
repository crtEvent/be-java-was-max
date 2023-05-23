package webserver.http.factor.session;

import java.util.UUID;

public class Session {
	private String sessionId;
	private String name;
	private Object value;

	public Session(String name, Object value) {
		this.sessionId = UUID.randomUUID().toString();
		this.name = name;
		this.value = value;
	}

	public String getSessionId() {
		return sessionId;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return value;
	}
}
