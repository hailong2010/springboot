package com.seadragon.registerspringbean.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.seadragon.registerspringbean.annotation.MsgType;

public class JmsEventInvocatioinHandler implements InvocationHandler {

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		MsgType msgType = method.getAnnotation(MsgType.class);
		System.out.println("Msg type is " + msgType.type());
		return null;
	}
}
