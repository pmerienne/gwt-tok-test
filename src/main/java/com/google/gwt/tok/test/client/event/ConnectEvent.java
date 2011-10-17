package com.google.gwt.tok.test.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class ConnectEvent extends GwtEvent<ConnectHandler> {

	private static Type<ConnectHandler> TYPE;

	public static Type<ConnectHandler> getType() {
		return TYPE != null ? TYPE : (TYPE = new Type<ConnectHandler>());
	}

	private String sessionId;

	public ConnectEvent(String sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	protected void dispatch(ConnectHandler handler) {
		handler.onConnect(this);
	}

	@Override
	public GwtEvent.Type<ConnectHandler> getAssociatedType() {
		return getType();
	}

	public String getSessionId() {
		return sessionId;
	}

}
