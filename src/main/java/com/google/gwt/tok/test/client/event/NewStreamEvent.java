package com.google.gwt.tok.test.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.tok.client.Stream;

public class NewStreamEvent extends GwtEvent<NewStreamHandler> {

	private static Type<NewStreamHandler> TYPE;

	public static Type<NewStreamHandler> getType() {
		return TYPE != null ? TYPE : (TYPE = new Type<NewStreamHandler>());
	}

	private Stream stream;

	public NewStreamEvent(Stream stream) {
		this.stream = stream;
	}

	@Override
	protected void dispatch(NewStreamHandler handler) {
		handler.onNewStream(this);
	}

	@Override
	public GwtEvent.Type<NewStreamHandler> getAssociatedType() {
		return getType();
	}

	public Stream getStream() {
		return stream;
	}

}
