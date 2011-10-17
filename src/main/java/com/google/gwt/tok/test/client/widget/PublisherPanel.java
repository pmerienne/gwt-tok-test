package com.google.gwt.tok.test.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.tok.client.PublishProperties;
import com.google.gwt.tok.test.client.event.ConnectedEvent;
import com.google.gwt.tok.test.client.event.ConnectedHandler;
import com.google.gwt.tok.test.client.event.DisconnectedEvent;
import com.google.gwt.tok.test.client.event.DisconnectedHandler;
import com.google.gwt.tok.test.client.event.PublishEvent;
import com.google.gwt.tok.test.client.event.PublishHandler;
import com.google.gwt.tok.test.client.event.UnpublishEvent;
import com.google.gwt.tok.test.client.event.UnpublishHandler;
import com.google.gwt.tok.test.client.util.EventBusHelper;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class PublisherPanel extends Composite {

	private static PublisherPanelUiBinder uiBinder = GWT.create(PublisherPanelUiBinder.class);

	interface PublisherPanelUiBinder extends UiBinder<Widget, PublisherPanel> {
	}

	private final static EventBus EVENT_BUS = EventBusHelper.getEventBus();

	@UiField
	TextBox height;

	@UiField
	TextBox width;

	@UiField
	TextBox microphoneGain;

	@UiField
	CheckBox mirror;

	@UiField
	CheckBox publishAudio;

	@UiField
	CheckBox publishVideo;

	@UiField
	Button publishButton;

	@UiField
	DivElement publisherDiv;

	private boolean publishing = false;

	public PublisherPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		this.publisherDiv.setId("gwt-tok-publisher");
		bind();
	}

	@UiHandler("publishButton")
	protected void onPublishClicked(ClickEvent event) {
		if (publishing) {
			EVENT_BUS.fireEvent(new UnpublishEvent());
		} else {
			PublishProperties publishProperties = PublishProperties.get();
			publishProperties.setHeight(Integer.valueOf(height.getValue()));
			publishProperties.setWidth(Integer.valueOf(width.getValue()));
			publishProperties.setMicrophoneGain(Integer.valueOf(microphoneGain.getValue()));
			publishProperties.setMirror(mirror.getValue());
			publishProperties.setPublishAudio(publishAudio.getValue());
			publishProperties.setPublishVideo(publishVideo.getValue());

			EVENT_BUS.fireEvent(new PublishEvent(publisherDiv.getId(), publishProperties));
		}
	}

	private void bind() {
		EVENT_BUS.addHandler(ConnectedEvent.getType(), new ConnectedHandler() {
			@Override
			public void onConnected(ConnectedEvent event) {
				setEnabled(true);
				publishButton.setEnabled(true);
			}
		});

		EVENT_BUS.addHandler(DisconnectedEvent.getType(), new DisconnectedHandler() {
			@Override
			public void onDisconnected(DisconnectedEvent event) {
				setEnabled(false);
				publishButton.setEnabled(false);
			}
		});

		EVENT_BUS.addHandler(PublishEvent.getType(), new PublishHandler() {
			@Override
			public void onPublish(PublishEvent event) {
				setPublishing(true);
			}
		});

		EVENT_BUS.addHandler(UnpublishEvent.getType(), new UnpublishHandler() {
			@Override
			public void onUnpublish(UnpublishEvent event) {
				setPublishing(false);
			}
		});
	}

	private void setPublishing(boolean publishing) {
		this.publishing = publishing;
		this.setEnabled(!publishing);
		if (publishing) {
			this.publishButton.setText("Unpublish");
		} else {
			this.publishButton.setText("Publish");
		}
	}

	private void setEnabled(boolean enabled) {
		this.height.setEnabled(enabled);
		this.width.setEnabled(enabled);
		this.microphoneGain.setEnabled(enabled);
		this.mirror.setEnabled(enabled);
		this.publishAudio.setEnabled(enabled);
		this.publishVideo.setEnabled(enabled);
	}
}
