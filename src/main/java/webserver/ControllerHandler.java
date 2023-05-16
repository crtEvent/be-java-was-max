package webserver;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.annotation.MyController;
import webserver.annotation.MyRequestMapping;
import webserver.config.WebConfig;
import webserver.http.request.MyHttpRequest;

public class ControllerHandler {

	private static final Logger logger = LoggerFactory.getLogger(ControllerHandler.class);

	private static final Map<ControllerMapperKey, Method> controllerMapper = new HashMap<>();

	private ControllerHandler() {}

	public static String runRequestMappingMethod(MyHttpRequest myHttpRequest) throws
		InvocationTargetException,
		IllegalAccessException {
		for(Map.Entry<ControllerMapperKey, Method> entry : controllerMapper.entrySet()) {
			ControllerMapperKey key = entry.getKey();
			if(key.getUrl().equals(myHttpRequest.getRequestTargetWithoutQueryString())) {
				Method method = entry.getValue();
				logger.debug("<< Execute Request Mapping Method >> {}.{}()", key.getInstance().getClass().getName(), method.getName());
				return (String) method.invoke(key.getInstance(), myHttpRequest);
			}
		}

		return "";
	}

	public static void initialize() throws NoSuchMethodException, IllegalAccessException,
		InvocationTargetException, InstantiationException, ClassNotFoundException, IOException, URISyntaxException {
		List<Class<?>> classes = getAllClasses(WebConfig.getControllerPackage());

		for (Class<?> clazz : classes) {
			if (!clazz.isAnnotationPresent(MyController.class)) {
				continue;
			}

			List<Method> methods = getAllRequestMappingMethods(clazz);

			Object instance = clazz.getDeclaredConstructor().newInstance();

			for (Method method : methods) {
				MyRequestMapping requestMapping = method.getAnnotation(MyRequestMapping.class);
				controllerMapper.put(new ControllerMapperKey(requestMapping.value(), requestMapping.method(), instance), method);
			}
		}
		debug();
	}

	private static void debug() {
		logger.info("  　А А");
		logger.info("　(*ﾟーﾟ) [Mapping Controller Methods]");
		logger.info("～(_＿_)");
		for(Map.Entry<ControllerMapperKey, Method> entry : controllerMapper.entrySet()) {
			ControllerMapperKey key = entry.getKey();
			Method method = entry.getValue();
			logger.info("▶ Mapping : {}.{}()", key.getInstance().getClass().getName(), method.getName());
		}
	}


	private static List<Class<?>> getAllClasses(String packageName) throws
		URISyntaxException,
		IOException,
		ClassNotFoundException {
		List<Class<?>> allClasses = new ArrayList<>();

		String packageRelativePath = packageName.replace('.', '/');

		URI packageUri = Objects.requireNonNull(
			ControllerHandler.class.getResource("../" + packageRelativePath)).toURI();

		if (packageUri.getScheme().equals("file")) {
			Path packageFullPath = Paths.get(packageUri);
			allClasses.addAll(getAllPackageClasses(packageFullPath, packageName));
		}

		return allClasses;
	}

	private static List<Class<?>> getAllPackageClasses(Path packagePath, String packageName) throws
		IOException,
		ClassNotFoundException {

		if (!Files.exists(packagePath)) {
			return Collections.emptyList();
		}

		List<Path> files = Files.list(packagePath)
			.filter(Files::isRegularFile)
			.collect(Collectors.toList());

		List<Class<?>> classes = new ArrayList<>();

		for (Path filePath : files) {
			String fileName = filePath.getFileName().toString();

			if (fileName.endsWith(".class")) {
				String classFullName = packageName.isBlank() ?
					fileName.replaceFirst("\\.class$", "")
					: packageName + "." + fileName.replaceFirst("\\.class$", "");

				Class<?> clazz = Class.forName(classFullName);
				classes.add(clazz);
			}
		}
		return classes;
	}

	private static List<Method> getAllRequestMappingMethods(Class<?> clazz) {
		List<Method> initializingMethods = new ArrayList<>();
		for (Method method : clazz.getDeclaredMethods()) {
			if (method.isAnnotationPresent(MyRequestMapping.class)) {
				initializingMethods.add(method);
			}
		}
		return initializingMethods;
	}
}
