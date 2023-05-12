package webserver.model;

import java.util.Map;
import java.util.Set;

public class MyModel {
	private final Map<String, String> parameters;

	public MyModel(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public String get(String key) {
		return parameters.get(key);
	}

	public Set<String> keySet() {
		return parameters.keySet();
	}
}
