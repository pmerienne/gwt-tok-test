package com.google.gwt.tok.test.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class UnpublishEvent extends GwtEvent<UnpublishHandler> {

	private static Type<UnpublishHandler> TYPE;

	public static Type<UnpublishHandler> getType() {
		return TYPE != null ? TYPE : (TYPE = new Type<UnpublishHandler>());
	}

	public UnpublishEvent() {

	}

	@Override
	protected void dispatch(UnpublishHandler handler) {
		handler.onUnpublish(this);
	}

	@Override
	public GwtEvent.Type<UnpublishHandler> getAssociatedType() {
		return getType();
	}

}
