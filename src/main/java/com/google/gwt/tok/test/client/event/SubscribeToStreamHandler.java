package com.google.gwt.tok.test.client.event;

import com.google.gwt.event.shared.EventHandler;


public interface SubscribeToStreamHandler extends EventHandler {
	

	void onSubscribeToStream(SubscribeToStreamEvent event);
}