package webserver.http.utill;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import webserver.config.WebConfig;
import webserver.model.ModelAndView;

public class TemplateEngineParser {

	private static final String INCLUDE_REG_PATTERN = "\\{\\{fn-include:\\s?(.*?)\\}\\}";
	private static final String PRINT_STRING_REG_PATTERN = "\\{\\{fn-printString:\\s?(.*?)\\}\\}";
	private static final String IF_NOT_NULL_REG_PATTERN = "\\{\\{fn-ifNotNull:\\s?(.*?)\\}\\}(.*?)\\{\\{/fn-ifNotNull\\}\\}";
	private static final String IF_NULL_REG_PATTERN = "\\{\\{fn-ifNull:\\s?(.*?)\\}\\}(.*?)\\{\\{/fn-ifNull\\}\\}";

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
			return new byte[] {};
		}
	}

	private static String replaceTemplateTag(String html, ModelAndView modelAndView) {
		html = includeHtml(html);
		html = replaceIfNotNullTag(html, modelAndView);
		html = replaceIfNullTag(html, modelAndView);
		html = replacePrintStringTag(html, modelAndView);
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
					(String)modelAndView.getAttribute(matcher.group(1)));
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
	private static String replaceMetaToEscape(String str) {
		String pattern = "[{}\\[\\]().*+?^$|\\\\]";
		return str.replaceAll(pattern, "\\\\$0");
	}

}
