package webserver.annotation;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import webserver.http.utill.ControllerHandler;
import webserver.config.WebConfig;
import webserver.http.request.HttpRequestMessage;
import webserver.http.utill.HttpRequestMessageGenerator;

class AnnotationTest {

	private final String validHttpRequestMessage = "GET / HTTP/1.1\n"
		+ "Host: localhost:8080\n"
		+ "Connection: keep-alive\n"
		+ "Cache-Control: max-age=0\n"
		+ "sec-ch-ua: \"Google Chrome\";v=\"113\", \"Chromium\";v=\"113\", \"Not-A.Brand\";v=\"24\"\n"
		+ "sec-ch-ua-mobile: ?0\n"
		+ "sec-ch-ua-platform: \"Windows\"\n"
		+ "Upgrade-Insecure-Requests: 1\n"
		+ "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36\n"
		+ "diary: 모든 계획들이 산산조각 나고 미래에 대한 두려움과 과거에 대한 후회가 내 마음을 좀먹고 모니터에 비친 내 모습이 너무나도 초라하지만 뭐 어떡해 여기까지 온 거 하던 대로 하는 수밖에\n"
		+ "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\n"
		+ "Sec-Fetch-Site: none\n"
		+ "Sec-Fetch-Mode: navigate\n"
		+ "Sec-Fetch-User: ?1\n"
		+ "Sec-Fetch-Dest: document\n"
		+ "Accept-Encoding: gzip, deflate, br\n"
		+ "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7\n"
		+ "Cookie: rememberCheckbox=admin; JSESSIONID=F466F13D43C617D547721036DA103590";

	private InputStream in;
	private HttpRequestMessage httpRequestMessage;

	@DisplayName("/ 으로 접속하면 /index.html 로 이동한다.")
	@Test
	void controllerScan() throws Exception {
		WebConfig.readConfig();

		in = new ByteArrayInputStream(validHttpRequestMessage.getBytes(StandardCharsets.UTF_8));
		httpRequestMessage = HttpRequestMessageGenerator.generateHttpRequestMessage(in);

		ControllerHandler.initialize();
		String returnValue = ControllerHandler.runRequestMappingMethod(httpRequestMessage);

		Assertions.assertThat(returnValue).isEqualTo("/index.html");
	}
}
