package webserver.web.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
	private static final String REDIRECT_DELIMITER = "redirect:";

	private String view;
	private boolean redirect = false;
	private final Map<String, Object> model = new HashMap<>();

	public ModelAndView(String view) {
		this.view = initView(view);
	}

	private String initView(String view) {
		String[] split = view.split(REDIRECT_DELIMITER);

		if(split.length == 2) {
			this.redirect = true;
			return split[1];
		}
		return view;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = initView(view);
	}

	public boolean isRedirect() {
		return redirect;
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
