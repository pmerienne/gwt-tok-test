package com.google.gwt.tok.test.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.tok.client.PublishProperties;

public class PublishEvent extends GwtEvent<PublishHandler> {

	private static Type<PublishHandler> TYPE;

	public static Type<PublishHandler> getType() {
		return TYPE != null ? TYPE : (TYPE = new Type<PublishHandler>());
	}

	private String panelId;

	private PublishProperties publishProperties;

	public PublishEvent(String panelId, PublishProperties publishProperties) {
		this.panelId = panelId;
		this.publishProperties = publishProperties;
	}

	@Override
	protected void dispatch(PublishHandler handler) {
		handler.onPublish(this);
	}

	@Override
	public GwtEvent.Type<PublishHandler> getAssociatedType() {
		return getType();
	}

	public PublishProperties getPublishProperties() {
		return publishProperties;
	}

	public String getPanelId() {
		return panelId;
	}

}
