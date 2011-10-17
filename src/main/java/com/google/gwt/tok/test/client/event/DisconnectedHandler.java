package com.google.gwt.tok.test.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface DisconnectedHandler extends EventHandler {

	void onDisconnected(DisconnectedEvent event);
}