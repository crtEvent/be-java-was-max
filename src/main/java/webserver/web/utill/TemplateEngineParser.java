package webserver.web.utill;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.web.config.WebConfig;
import webserver.web.error.ErrorPageGenerator;
import webserver.http.component.StatusCodeType;
import webserver.web.model.ModelAndView;

public class TemplateEngineParser {

	private static final Logger logger = LoggerFactory.getLogger(TemplateEngineParser.class);

	private static final String INCLUDE_REG_PATTERN = "\\{\\{fn-include:\\s?(.*?)\\}\\}";
	private static final String PRINT_STRING_REG_PATTERN = "\\{\\{fn-print:\\s?(.*?)\\}\\}";
	private static final String IF_NOT_NULL_REG_PATTERN = "\\{\\{fn-ifNotNull:\\s?(.*?)\\}\\}([\\s\\S]*?)\\{\\{/fn-ifNotNull\\}\\}";
	private static final String IF_NULL_REG_PATTERN = "\\{\\{fn-ifNull:\\s?(.*?)\\}\\}([\\s\\S]*?)\\{\\{/fn-ifNull\\}\\}";
	private static final String FOREACH_LIST_REG_PATTERN = "\\{\\{fn-forEachList:\\s*(.*?)\\}\\}([\\s\\S]*?)\\{\\{/fn-forEachList\\}\\}";
	private static final String GET_STRING_REG_PATTERN = "\\{\\{fn-get:\\s?(.*?)\\}\\}";

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
			return new ErrorPageGenerator().generate(StatusCodeType.NOT_FOUND_404, e);
		}
	}

	private static String replaceTemplateTag(String html, ModelAndView modelAndView) {
		html = includeHtml(html);
		html = replaceIfNotNullTag(html, modelAndView);
		html = replaceIfNullTag(html, modelAndView);
		html = replacePrintStringTag(html, modelAndView);
		html = replaceForEachList(html, modelAndView);
		return html;
	}

	private static String includeHtml(String html) {
		Pattern regex = Pattern.compile(INCLUDE_REG_PATTERN);
		Matcher matcher = regex.matcher(html);

		while (matcher.find()) {
			try (BufferedReader br = new BufferedReader(new FileReader(WebConfig.getTemplatesResourcePath() + matcher.group(1)))) {
				StringBuilder sb = new StringBuilder();

				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line).append(System.lineSeparator());
				}

				html = html.replaceAll(replaceMetaToEscape(matcher.group(0)), sb.toString());

			} catch (IOException e) {
				html = e.getMessage();
			}
		}

		return html;
	}

	private static String replacePrintStringTag(String html, ModelAndView modelAndView) {
		Pattern regex = Pattern.compile(PRINT_STRING_REG_PATTERN);
		Matcher matcher = regex.matcher(html);

		while (matcher.find()) {
			if (modelAndView.isContainAttribute(matcher.group(1))) {
				html = html.replaceAll(replaceMetaToEscape(matcher.group(0)),
					String.valueOf(modelAndView.getAttribute(matcher.group(1))));
			} else {
				html = html.replaceAll(replaceMetaToEscape(matcher.group(0)), "");
			}
		}

		return html;
	}

	private static String replaceIfNotNullTag(String html, ModelAndView modelAndView) {
		Pattern regex = Pattern.compile(IF_NOT_NULL_REG_PATTERN);
		Matcher matcher = regex.matcher(html);

		while (matcher.find()) {
			if (modelAndView.isContainAttribute(matcher.group(1))) {
				html = html.replaceAll(replaceMetaToEscape(matcher.group(0)), matcher.group(2));
			} else {
				html = html.replaceAll(replaceMetaToEscape(matcher.group(0)), "");
			}
		}

		return html;
	}

	private static String replaceIfNullTag(String html, ModelAndView modelAndView) {
		Pattern regex = Pattern.compile(IF_NULL_REG_PATTERN);
		Matcher matcher = regex.matcher(html);

		while (matcher.find()) {
			if (modelAndView.isContainAttribute(matcher.group(1))) {
				html = html.replaceAll(replaceMetaToEscape(matcher.group(0)), "");
			} else {
				html = html.replaceAll(replaceMetaToEscape(matcher.group(0)), matcher.group(2));
			}
		}

		return html;
	}

	public static String replaceForEachList(String html, ModelAndView modelAndView) {
		Pattern regex = Pattern.compile(FOREACH_LIST_REG_PATTERN);
		Matcher matcher = regex.matcher(html);

		while(matcher.find()) {

			if(modelAndView.isContainAttribute(matcher.group(1))
				&& modelAndView.getAttribute(matcher.group(1)) instanceof List) {

				List<?> list = (List<?>) modelAndView.getAttribute(matcher.group(1));
				html = html.replaceAll(replaceMetaToEscape(matcher.group(0)), forEach(matcher.group(2), list));
			}
		}

		return html;
	}

	public static String forEach(String tagBlock, List<?> list) {
		StringBuilder sb = new StringBuilder();

		for(Object object : list) {
			sb.append(replaceGet(tagBlock, object)).append(System.lineSeparator());
		}
		return sb.toString();
	}

	public static String replaceGet(String tagBlock, Object object) {
		Pattern regex = Pattern.compile(GET_STRING_REG_PATTERN);
		Matcher matcher = regex.matcher(tagBlock);

		while(matcher.find()) {
			tagBlock = tagBlock.replaceAll(replaceMetaToEscape(matcher.group(0)), getMemberVariable(matcher.group(1), object));
		}

		return tagBlock;
	}

	private static String getMemberVariable(String variableName, Object object)  {
		Class<?> clazz = object.getClass();
		Method[] methods = clazz.getDeclaredMethods();

		for(Method method : methods) {
			if(parseVariableNameFromGetterMethodName(method.getName()).equals(variableName)) {
				try {
					return String.valueOf(method.invoke(object));
				} catch (IllegalAccessException | InvocationTargetException e) {
					logger.error("에러: {}", e.getMessage());
					break;
				}
			}
		}
		return "";
	}

	private static String parseVariableNameFromGetterMethodName(String getterMethodName) {
		String substringName = getterMethodName.substring(3);
		return Character.toLowerCase(substringName.charAt(0)) + substringName.substring(1);
	}


	private static String replaceMetaToEscape(String str) {
		String pattern = "[{}\\[\\]().*+?^$|\\\\]";
		return str.replaceAll(pattern, "\\\\$0");
	}

}
