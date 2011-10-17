package com.google.gwt.tok.test.client.widget;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.tok.client.Stream;
import com.google.gwt.tok.test.client.event.NewStreamEvent;
import com.google.gwt.tok.test.client.event.NewStreamHandler;
import com.google.gwt.tok.test.client.event.StreamDeletedEvent;
import com.google.gwt.tok.test.client.event.StreamDeletedHandler;
import com.google.gwt.tok.test.client.event.SubscribeToStreamEvent;
import com.google.gwt.tok.test.client.util.EventBusHelper;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class SubscribersPanel extends Composite {

	private static SubscribersPanelUiBinder uiBinder = GWT.create(SubscribersPanelUiBinder.class);

	interface SubscribersPanelUiBinder extends UiBinder<Widget, SubscribersPanel> {
	}

	private final static EventBus EVENT_BUS = EventBusHelper.getEventBus();

	@UiField
	DivElement subscribers;

	private Map<String, DivElement> panels = new HashMap<String, DivElement>();

	public SubscribersPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		bind();
	}

	private void bind() {
		EVENT_BUS.addHandler(NewStreamEvent.getType(), new NewStreamHandler() {
			@Override
			public void onNewStream(NewStreamEvent event) {
				String panelId = addStreamPanel(event.getStream());
				EVENT_BUS.fireEvent(new SubscribeToStreamEvent(event.getStream(), panelId));
			}
		});

		EVENT_BUS.addHandler(StreamDeletedEvent.getType(), new StreamDeletedHandler() {
			@Override
			public void onStreamDeleted(StreamDeletedEvent event) {
				removeStreamPanel(event.getStream());
			}
		});
	}

	private void removeStreamPanel(Stream stream) {
		String id = this.getPanelId(stream);
		if (this.panels.containsKey(id)) {
			DivElement panel = this.panels.remove(id);
			panel.removeFromParent();
		}
	}

	private String addStreamPanel(Stream stream) {
		DivElement panel = DOM.createDiv().cast();
		String id = this.getPanelId(stream);
		panel.setId(id);
		this.panels.put(id, panel);
		this.subscribers.appendChild(panel);
		return id;
	}

	private String getPanelId(Stream stream) {
		return "stream" + stream.getStreamId();
	}
}
