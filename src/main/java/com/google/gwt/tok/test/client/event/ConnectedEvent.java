package com.google.gwt.tok.test.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class ConnectedEvent extends GwtEvent<ConnectedHandler> {

	private static Type<ConnectedHandler> TYPE;

	public static Type<ConnectedHandler> getType() {
		return TYPE != null ? TYPE : (TYPE = new Type<ConnectedHandler>());
	}

	public ConnectedEvent() {

	}

	@Override
	protected void dispatch(ConnectedHandler handler) {
		handler.onConnected(this);
	}

	@Override
	public GwtEvent.Type<ConnectedHandler> getAssociatedType() {
		return getType();
	}

}
