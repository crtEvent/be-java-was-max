package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.http.request.HttpRequestMessage;
import webserver.http.response.HttpResponseMessage;
import webserver.http.request.generator.HttpRequestMessageGenerator;
import webserver.http.response.generator.HttpResponseMessageGenerator;

public class RequestHandler implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
	private static final String REQUEST_EMOJI = "\uD83D\uDCE8";
	private static final String RESPONSE_EMOJI = "\uD83D\uDCEC";

	private final Socket connection;

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	public void run() {
		logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
			connection.getPort());

		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			HttpRequestMessage httpRequestMessage = HttpRequestMessageGenerator.generateHttpRequestMessage(in);
			debug(httpRequestMessage);

			HttpResponseMessage httpResponseMessage = HttpResponseMessageGenerator.generateHttpResponseMessage(httpRequestMessage);
			debug(httpResponseMessage);

			response(out, httpResponseMessage);
		} catch (IOException e) {
			logger.error("읽을 수 없음: {}", e.getMessage());
		}
	}

	private void debug(HttpResponseMessage httpResponseMessage) {
		logger.debug("<< HTTP Response Message >> \n{}\n{}", RESPONSE_EMOJI, httpResponseMessage.getStatusLineAndResponseHeaderMessage());
	}

	private void debug(HttpRequestMessage httpRequestMessage) {
		if (httpRequestMessage.getMimeType().equals("text/html")) {
			logger.debug("<< HTTP Request Message >> \n{}\n{}", REQUEST_EMOJI, httpRequestMessage.getHttpRequestMessage());
		} else {
			logger.debug("<< HTTP Request Message >> \n{}\n{} {}", REQUEST_EMOJI, httpRequestMessage.getMethod(),
				httpRequestMessage.getRequestTarget());
		}
	}

	private void response(OutputStream out, HttpResponseMessage httpResponseMessage) {
		DataOutputStream dos = new DataOutputStream(out);
		responseHeader(dos, httpResponseMessage);
		responseBody(dos, httpResponseMessage);
	}

	private void responseHeader(DataOutputStream dos, HttpResponseMessage httpResponse) {
		try {
			dos.writeBytes(httpResponse.getStatusLineAndResponseHeaderMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private void responseBody(DataOutputStream dos, HttpResponseMessage httpResponse) {
		try {
			dos.write(httpResponse.getBody(), 0, httpResponse.getContentLength());
			dos.flush();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

}
