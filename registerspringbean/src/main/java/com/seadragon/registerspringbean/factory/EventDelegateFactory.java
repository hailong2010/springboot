package com.seadragon.registerspringbean.factory;

import java.lang.reflect.Proxy;

public class EventDelegateFactory {
	@SuppressWarnings("unchecked")
	public <T> T newInstance(Class<T> annotated) {
		return (T) Proxy.newProxyInstance(annotated.getClassLoader(), new Class[] { annotated },
				new JmsEventInvocatioinHandler());
	}
}
