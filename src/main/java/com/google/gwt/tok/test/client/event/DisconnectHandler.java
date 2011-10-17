package com.google.gwt.tok.test.client.event;

import com.google.gwt.event.shared.EventHandler;


public interface DisconnectHandler extends EventHandler {
	

	void onDisconnect(DisconnectEvent event);
}