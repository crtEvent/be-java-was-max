package webserver.request;

import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MyHttpRequestMessageTest {

	private String httpRequestMessage = "GET /user/create?userId=user01&password=1234&name=ape&email=&hobby=reading&hobby=swimming&hobby= HTTP/1.1\n"
		+ "Host: localhost:8080\n"
		+ "Connection: keep-alive\n"
		+ "Cache-Control: max-age=0\n"
		+ "sec-ch-ua: \"Google Chrome\";v=\"113\", \"Chromium\";v=\"113\", \"Not-A.Brand\";v=\"24\"\n"
		+ "sec-ch-ua-mobile: ?0\n"
		+ "sec-ch-ua-platform: \"Windows\"\n"
		+ "Upgrade-Insecure-Requests: 1\n"
		+ "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36\n"
		+ "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\n"
		+ "Sec-Fetch-Site: none\n"
		+ "Sec-Fetch-Mode: navigate\n"
		+ "Sec-Fetch-User: ?1\n"
		+ "Sec-Fetch-Dest: document\n"
		+ "Accept-Encoding: gzip, deflate, br\n"
		+ "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7\n"
		+ "Cookie: rememberCheckbox=admin; JSESSIONID=F466F13D43C617D547721036DA103590";

	private InputStream in;

	private MyHttpRequestMessage myHttpRequestMessage;

	@BeforeEach
	void beforeEach() throws IOException {
		in = new ByteArrayInputStream(httpRequestMessage.getBytes(StandardCharsets.UTF_8));
		myHttpRequestMessage = new MyHttpRequestMessage(in);
	}

	@DisplayName("리퀘스트 메시지에서 path만 추출한다.")
	@Test
	void testGetUriPath() {
		String path = myHttpRequestMessage.getUriPath();
		assertThat(path).isEqualTo("/user/create");
	}

	@DisplayName("리퀘스트 메시지에서 query만 추출한다.")
	@Test
	void testGetQueryParams() {
		Map<String, String> map = myHttpRequestMessage.getQueryParams();

		SoftAssertions softAssertions = new SoftAssertions();
		softAssertions.assertThat(map.get("userId")).isEqualTo("user01");
		softAssertions.assertThat(map.get("password")).isEqualTo("1234");
		softAssertions.assertThat(map.get("name")).isEqualTo("ape");
		softAssertions.assertThat(map.get("email")).isEqualTo("");
		softAssertions.assertThat(map.get("hobby")).isEqualTo("reading,swimming");

		softAssertions.assertAll();
	}

	@DisplayName("잘못된 리퀘스트 메시지가 들어가도 에러가 나지 않는다.")
	@Test
	void testFail() {
		httpRequestMessage = "";
		in = new ByteArrayInputStream(httpRequestMessage.getBytes(StandardCharsets.UTF_8));

		SoftAssertions softAssertions = new SoftAssertions();

		softAssertions.assertThatCode(() -> {
			myHttpRequestMessage = new MyHttpRequestMessage(in);
		}).doesNotThrowAnyException();

		httpRequestMessage = "invalid http request message";
		in = new ByteArrayInputStream(httpRequestMessage.getBytes(StandardCharsets.UTF_8));

		softAssertions.assertThatCode(() -> {
			myHttpRequestMessage = new MyHttpRequestMessage(in);
		}).doesNotThrowAnyException();

		softAssertions.assertAll();
	}

}
