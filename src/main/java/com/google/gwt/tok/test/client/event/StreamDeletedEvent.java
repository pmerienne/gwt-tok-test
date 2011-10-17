package com.google.gwt.tok.test.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.tok.client.Stream;

public class StreamDeletedEvent extends GwtEvent<StreamDeletedHandler> {

	private static Type<StreamDeletedHandler> TYPE;

	public static Type<StreamDeletedHandler> getType() {
		return TYPE != null ? TYPE : (TYPE = new Type<StreamDeletedHandler>());
	}

	private Stream stream;

	public StreamDeletedEvent(Stream stream) {
		this.stream = stream;
	}

	@Override
	protected void dispatch(StreamDeletedHandler handler) {
		handler.onStreamDeleted(this);
	}

	@Override
	public GwtEvent.Type<StreamDeletedHandler> getAssociatedType() {
		return getType();
	}

	public Stream getStream() {
		return stream;
	}

}
