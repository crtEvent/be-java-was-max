package webserver.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView {

	private String view;
	private final Map<String, Object> model = new HashMap<>();

	public ModelAndView(String view) {
		this.view = view;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public void addAttribute(String name, Object value) {
		model.put(name, value);
	}

	public Object getAttribute(String attributeName) {
		return model.get(attributeName);
	}


	public Map<String, Object> getAttributes() {
		return Collections.unmodifiableMap(model);
	}

	public boolean isContainAttribute(String attributeName) {
		return model.containsKey(attributeName);
	}

}
