package com.google.gwt.tok.test.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.tok.client.Stream;

public class SubscribeToStreamEvent extends GwtEvent<SubscribeToStreamHandler> {

	private static Type<SubscribeToStreamHandler> TYPE;

	public static Type<SubscribeToStreamHandler> getType() {
		return TYPE != null ? TYPE : (TYPE = new Type<SubscribeToStreamHandler>());
	}

	private Stream stream;

	private String panelId;

	public SubscribeToStreamEvent(Stream stream, String panelId) {
		this.stream = stream;
		this.panelId = panelId;
	}

	@Override
	protected void dispatch(SubscribeToStreamHandler handler) {
		handler.onSubscribeToStream(this);
	}

	@Override
	public GwtEvent.Type<SubscribeToStreamHandler> getAssociatedType() {
		return getType();
	}

	public Stream getStream() {
		return stream;
	}

	public String getPanelId() {
		return panelId;
	}

}
