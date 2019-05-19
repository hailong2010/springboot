package com.seadragon.registerspringbean.registrar;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.seadragon.registerspringbean.annotation.EnableJmsEvent;
import com.seadragon.registerspringbean.annotation.JmsEvent;
import com.seadragon.registerspringbean.factory.EventDelegateFactory;

public class JmsEventRegistrar implements ImportBeanDefinitionRegistrar {
	private static final String SCAN_PATH = "scanPath";
	private static final String NEW_INSTANCE = "newInstance";
	@Override
	public void registerBeanDefinitions(AnnotationMetadata metaData,
			BeanDefinitionRegistry beanDefinitionRegistry) {
		registerHttpRequest(metaData, beanDefinitionRegistry);
	}

	private void registerHttpRequest(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {

		Map<String, Object> enablerAttributes = metadata
				.getAnnotationAttributes(EnableJmsEvent.class.getCanonicalName());
		if (null != enablerAttributes) {
			ClassPathScanningCandidateComponentProvider classScanner = getClassScanner();
			Optional.of(Stream.of((Class<?>[]) enablerAttributes.get(SCAN_PATH))).orElseGet(Stream::empty)
					.map(baseClass -> baseClass.getPackage().getName()).collect(Collectors.toSet()).stream()
					.flatMap(packageName -> classScanner.findCandidateComponents(packageName).stream())
					.forEach(beanDef -> registry.registerBeanDefinition(beanDef.getBeanClassName(),
							this.getDelegateBeanDefinition(beanDef)));
		}

	}

	private ClassPathScanningCandidateComponentProvider getClassScanner() {
		ClassPathScanningCandidateComponentProvider classScanner = new ClassPathScanningCandidateComponentProvider(
				false) {
			@Override
			protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
				return beanDefinition.getMetadata().isInterface();
			}
		};
		AnnotationTypeFilter typeFilter = new AnnotationTypeFilter(
				(Class<? extends Annotation>) JmsEvent.class);
		classScanner.addIncludeFilter(typeFilter);
		return classScanner;
	}

	protected BeanDefinition getDelegateBeanDefinition(BeanDefinition beanDefinition) {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(beanDefinition.getBeanClassName());
		} catch (ClassNotFoundException ignored) {
			// Class scanning started this. Should not happen.
		}
		GenericBeanDefinition proxyBeanDefinition = new GenericBeanDefinition();
		proxyBeanDefinition.setBeanClass(clazz);

		ConstructorArgumentValues args = new ConstructorArgumentValues();
		args.addGenericArgumentValue(clazz);
		proxyBeanDefinition.setConstructorArgumentValues(args);
		proxyBeanDefinition.setFactoryBeanName(EventDelegateFactory.class.getName());
		proxyBeanDefinition.setFactoryMethodName(NEW_INSTANCE);
		return proxyBeanDefinition;
	}
}
