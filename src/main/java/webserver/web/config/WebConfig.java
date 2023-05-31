package webserver.web.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebConfig {
	private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

	private static final Properties properties = new Properties();

	private WebConfig() {

	}

	private static void debugProperties() {
		logger.info(" ╭ ⁀ ⁀ ╮");
		logger.info("( '\uD83D\uDC45'  ) [Read config from \"config.properties\"]");
		logger.info(" ╰ ‿  ‿ ╯");
		properties.forEach((key, value) -> logger.info("▶ Read config : {}={}", key, value));
	}

	public static void readConfig() {
		try(FileInputStream fileInputStream = new FileInputStream("./src/main/server-config/config.properties")) {
			properties.load(fileInputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		debugProperties();
	}

	public static int getDefaultPort() {
		return Integer.parseInt(properties.getProperty("default_port"));
	}

	public static String getTemplatesResourcePath() {
		return properties.getProperty("resource_path_templates");
	}

	public static String getStaticResourcePath() {
		return properties.getProperty("resource_path_static");
	}

	public static String getControllerPackage() {
		return properties.getProperty("controller_package");
	}
}
