package com.google.gwt.tok.test.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class DisconnectedEvent extends GwtEvent<DisconnectedHandler> {

	private static Type<DisconnectedHandler> TYPE;

	public static Type<DisconnectedHandler> getType() {
		return TYPE != null ? TYPE : (TYPE = new Type<DisconnectedHandler>());
	}

	public DisconnectedEvent() {

	}

	@Override
	protected void dispatch(DisconnectedHandler handler) {
		handler.onDisconnected(this);
	}

	@Override
	public GwtEvent.Type<DisconnectedHandler> getAssociatedType() {
		return getType();
	}

}
