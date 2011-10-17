package com.google.gwt.tok.test.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.tok.test.client.event.ConnectEvent;
import com.google.gwt.tok.test.client.event.ConnectedEvent;
import com.google.gwt.tok.test.client.event.ConnectedHandler;
import com.google.gwt.tok.test.client.event.DisconnectEvent;
import com.google.gwt.tok.test.client.event.DisconnectedEvent;
import com.google.gwt.tok.test.client.event.DisconnectedHandler;
import com.google.gwt.tok.test.client.util.EventBusHelper;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ConnectionPanel extends Composite {

	private static ConnectionPanelUiBinder uiBinder = GWT.create(ConnectionPanelUiBinder.class);

	interface ConnectionPanelUiBinder extends UiBinder<Widget, ConnectionPanel> {
	}

	private final static EventBus EVENT_BUS = EventBusHelper.getEventBus();

	@UiField
	Button connectButton;

	@UiField
	TextBox sessionId;

	@UiField
	Label connectionStatus;

	private boolean connected = false;

	public ConnectionPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		bind();
	}

	private void bind() {
		EVENT_BUS.addHandler(ConnectedEvent.getType(), new ConnectedHandler() {
			@Override
			public void onConnected(ConnectedEvent event) {
				setConnected(true);
				setPending(false);
			}
		});

		EVENT_BUS.addHandler(DisconnectedEvent.getType(), new DisconnectedHandler() {
			@Override
			public void onDisconnected(DisconnectedEvent event) {
				setConnected(false);
				setPending(false);
			}
		});
	}

	@UiHandler("connectButton")
	protected void onConnectClicked(ClickEvent event) {
		if (connected) {
			EVENT_BUS.fireEvent(new DisconnectEvent());
			this.setPending(true);
		} else {
			EVENT_BUS.fireEvent(new ConnectEvent(sessionId.getValue()));
			this.setPending(true);
		}
	}

	private void setPending(boolean pending) {
		this.connectButton.setEnabled(!pending);
		this.sessionId.setEnabled(!pending);
		if (pending) {
			this.connectionStatus.setText("Pending ...");
		}
	}

	private void setConnected(boolean connected) {
		if (connected) {
			this.connectionStatus.setText("Ready");
			this.connectButton.setText("Disconnect");
		} else {
			this.connectionStatus.setText("Disconnected");
			this.connectButton.setText("Connect to : ");
		}
		this.sessionId.setEnabled(!connected);
		this.connected = connected;
	}
}
