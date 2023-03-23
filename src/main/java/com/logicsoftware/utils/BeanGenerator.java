package com.logicsoftware.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;

import io.quarkus.arc.Arc;
import io.quarkus.arc.InstanceHandle;
import io.quarkus.arc.log.LoggerName;

@ApplicationScoped
public class BeanGenerator {

	public static final String VERSION_PREFIX = "v";

	@LoggerName("bean-generator")
	Logger logger;

	public <T> T getInstance(Class<T> beanClass, String version) {
		String beanClassName = beanClass.getSimpleName();

		// Busca a pasta correspondente à versão especificada
		Path versionPath = Path.of(beanClass.getResource("").getPath(), VERSION_PREFIX + version);

		// Verifica se a pasta da versão existe
		if (!Files.isDirectory(versionPath)) {
			logger.error("No implementation found for version " + version + " of bean class " + beanClassName);
			throw new IllegalArgumentException(
					"No implementation found for version " + version + " of bean class " + beanClassName);
		}

		// Verifica se a implementação da classe de bean especificada existe
		Path implementationPath = versionPath.resolve(beanClassName + "Impl.class");

		if (!Files.isRegularFile(implementationPath)) {
			logger.error("No implementation found for bean class " + beanClassName + " in version " + version);
			throw new IllegalArgumentException(
					"No implementation found for bean class " + beanClassName + " in version " + version);
		}

		// Cria uma nova instância da implementação usando o Quarkus Arc
		try {
			@SuppressWarnings("unchecked")
			Class<T> implementationClass = (Class<T>) Class.forName(getClassNameFromPath(implementationPath));
			InstanceHandle<T> instance = Arc.container().instance(implementationClass);
			if (instance.isAvailable()) {
				return instance.get();
			} else {
				T newInstance = Arc.container().beanManager().createInstance().select(implementationClass).get();
				Arc.container().beanManager().getEvent().select(implementationClass).fire(newInstance);
				return newInstance;
			}
		} catch (Exception e) {
			logger.error("Error creating bean instance for class " + beanClassName + " in version " + version, e);
			throw new RuntimeException(
					"Error creating bean instance for class " + beanClassName + " in version " + version, e);
		}
	}

	private String getClassNameFromPath(Path path) {
		Objects.requireNonNull(path, "Path must not be null");
		String packageName = path.getParent().getFileName().toString().replace('/', '.');
		String className = path.getFileName().toString().replace(".class", "");
		return "com.logicsoftware.services." + packageName + "." + className;
	}
}
