package webserver.http.utill;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import webserver.model.ModelAndView;

public class TemplateEngineParser {

	private static final String PRINT_STRING_REG_PATTERN = "\\{\\{fn-printString: (.*?)\\}\\}";

	private TemplateEngineParser() {

	}

	public static byte[] parseHtmlDynamically(Path resource, ModelAndView modelAndView) {
		try (BufferedReader br = new BufferedReader(new FileReader(resource.toString()))) {
			StringBuilder sb = new StringBuilder();

			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append(System.lineSeparator());
			}

			return replaceTemplateTag(sb.toString(), modelAndView).getBytes(StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			return new byte[]{};
		}
	}

	private static String replaceTemplateTag(String html, ModelAndView modelAndView) {
		return replacePrintString(html, modelAndView);
	}

	private static String replacePrintString(String html, ModelAndView modelAndView) {

		Pattern regex = Pattern.compile(PRINT_STRING_REG_PATTERN);
		Matcher matcher = regex.matcher(html);

		while (matcher.find()) {
			if(modelAndView.isContainAttribute(matcher.group(1))) {
				html = html.replaceAll("\\{\\{fn-printString: " + matcher.group(1) + "\\}\\}", (String) modelAndView.getAttribute(matcher.group(1)));
			}
		}

		return html;
	}

}
