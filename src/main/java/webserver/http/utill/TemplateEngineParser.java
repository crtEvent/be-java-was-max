package webserver.http.utill;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import webserver.model.ModelAndView;

public class TemplateEngineParser {

	private TemplateEngineParser() {

	}

	public static byte[] parseHtmlDynamically(Path resource, ModelAndView modelAndView) {
		try (BufferedReader br = new BufferedReader(new FileReader(resource.toString()))) {
			StringBuilder sb = new StringBuilder();

			String line;

			while ((line = br.readLine()) != null) {
				// HTML 파일의 한 줄씩 처리하는 로직
				//System.out.println(parseHtmlLine(line, modelAndView));
				sb.append(parseHtmlLine(line, modelAndView));
				System.out.println(parseHtmlLine(line, modelAndView));
			}

			return sb.toString().getBytes(StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			return new byte[]{};
		}
	}

	private static String parseHtmlLine(String line, ModelAndView modelAndView) {
		int startPosition = line.indexOf("{{");
		int endPosition = line.indexOf("}}");

		while (startPosition >= 0 && endPosition >= 0) {
			String substringForMatch = line.substring(startPosition + 2, endPosition);
			String substringToReplace = line.substring(startPosition, endPosition + 2);

			if(modelAndView.isContainAttribute(substringForMatch)) {
				line = line.replace(substringToReplace, (String) modelAndView.getAttribute(substringForMatch));
			} else {
				line = line.replace(substringToReplace,"null");
			}

			startPosition = line.indexOf("{{");
			endPosition = line.indexOf("}}");
		}

		return line;
	}
}
