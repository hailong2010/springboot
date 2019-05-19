package com.seadragon.registerspringbean.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.seadragon.registerspringbean.factory.EventDelegateFactory;
import com.seadragon.registerspringbean.registrar.JmsEventRegistrar;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({ JmsEventRegistrar.class, EventDelegateFactory.class })
public @interface EnableJmsEvent {
	Class<?>[] scanPath() default {};
}
