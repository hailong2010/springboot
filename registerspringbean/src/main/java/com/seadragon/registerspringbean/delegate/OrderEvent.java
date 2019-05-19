package com.seadragon.registerspringbean.delegate;

import org.springframework.stereotype.Component;

import com.seadragon.registerspringbean.annotation.JmsEvent;
import com.seadragon.registerspringbean.annotation.MsgType;

@Component
@JmsEvent
public interface OrderEvent {
	@MsgType(type = "create")
	void createOrder();

	@MsgType(type = "cancel")
	void cancelOrder();
}
