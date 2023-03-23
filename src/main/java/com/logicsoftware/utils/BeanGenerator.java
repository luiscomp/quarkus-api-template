package com.logicsoftware.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

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

	public <T> Optional<T> getInstance(Class<T> beanClass, String version) {
		Optional<T> bean = Optional.empty();

		String beanClassName = beanClass.getSimpleName();

		try {
			// Busca a pasta correspondente à versão especificada
			Path versionPath = getVersionPath(beanClass, version);

			// Verifica se a implementação da classe de bean especificada existe
			Path implementationPath = getVersionImplementationPath(versionPath, beanClassName, version);

			// Cria uma nova instância da implementação usando o Quarkus Arc
			bean = Optional.ofNullable(getInstance(implementationPath, beanClassName, version));
		} catch (Exception e) {
			logger.error("Error creating bean instance for class " + beanClassName + " in version " + version, e);
		}

		return bean;
	}

	private <T> T getInstance(Path implementationPath, String beanClassName, String version) {
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
			throw new RuntimeException("Error creating bean instance for class " + beanClassName + " in version " + version, e);
		}
	}

	private Path getVersionImplementationPath(Path versionPath, String beanClassName, String version) {
		Path implementationPath = versionPath.resolve(beanClassName + "V" + version + ".class");

		if (!Files.isRegularFile(implementationPath)) {
			logger.error("No implementation found for bean class " + beanClassName + " in version " + version);
			throw new IllegalArgumentException("No implementation found for bean class " + beanClassName + " in version " + version);
		}

		return implementationPath;
	}

	private <T> Path getVersionPath(Class<T> beanClass, String version) {
		Path versionPath = Path.of(beanClass.getResource("").getPath(), VERSION_PREFIX + version);

		// Verifica se a pasta da versão existe
		if (!Files.isDirectory(versionPath)) {
			logger.error("No implementation found for version " + version + " of bean class " + beanClass.getSimpleName());
			throw new IllegalArgumentException("No implementation found for version " + version + " of bean class " + beanClass.getSimpleName());
		}

		return versionPath;
	}

	private String getClassNameFromPath(Path path) {
		Objects.requireNonNull(path, "Path must not be null");
		String packageName = path.getParent().getFileName().toString().replace('/', '.');
		String className = path.getFileName().toString().replace(".class", "");
		return "com.logicsoftware.services." + packageName + "." + className;
	}
}
