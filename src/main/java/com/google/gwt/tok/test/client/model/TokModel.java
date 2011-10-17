package com.google.gwt.tok.test.client.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.tok.client.Camera;
import com.google.gwt.tok.client.DeviceManager;
import com.google.gwt.tok.client.Microphone;
import com.google.gwt.tok.client.PublishProperties;
import com.google.gwt.tok.client.Publisher;
import com.google.gwt.tok.client.Session;
import com.google.gwt.tok.client.Stream;
import com.google.gwt.tok.client.Subscriber;
import com.google.gwt.tok.client.TokBox;
import com.google.gwt.tok.client.event.ConnectionCreatedEvent;
import com.google.gwt.tok.client.event.ConnectionDestroyedEvent;
import com.google.gwt.tok.client.event.DeviceStatusEvent;
import com.google.gwt.tok.client.event.ExceptionEvent;
import com.google.gwt.tok.client.event.SessionConnectedEvent;
import com.google.gwt.tok.client.event.SessionDisconnectedEvent;
import com.google.gwt.tok.client.event.StreamCreatedEvent;
import com.google.gwt.tok.client.event.StreamDestroyedEvent;
import com.google.gwt.tok.client.handler.DeviceStatusHandler;
import com.google.gwt.tok.client.handler.ExceptionHandler;
import com.google.gwt.tok.client.handler.SessionHandler;
import com.google.gwt.tok.test.client.event.ConnectEvent;
import com.google.gwt.tok.test.client.event.ConnectHandler;
import com.google.gwt.tok.test.client.event.ConnectedEvent;
import com.google.gwt.tok.test.client.event.DisconnectEvent;
import com.google.gwt.tok.test.client.event.DisconnectHandler;
import com.google.gwt.tok.test.client.event.DisconnectedEvent;
import com.google.gwt.tok.test.client.event.NewStreamEvent;
import com.google.gwt.tok.test.client.event.PublishEvent;
import com.google.gwt.tok.test.client.event.PublishHandler;
import com.google.gwt.tok.test.client.event.StreamDeletedEvent;
import com.google.gwt.tok.test.client.event.SubscribeToStreamEvent;
import com.google.gwt.tok.test.client.event.SubscribeToStreamHandler;
import com.google.gwt.tok.test.client.event.UnpublishEvent;
import com.google.gwt.tok.test.client.event.UnpublishHandler;
import com.google.gwt.tok.test.client.util.EventBusHelper;
import com.google.gwt.user.client.Window;

public class TokModel implements SessionHandler, ExceptionHandler, DeviceStatusHandler {

	private final static String API_KEY = "5577341";
	private final static String DEV_TOKEN = "devtoken";
	private final static String DEV_SESSION_ID = "1_MX4xMjMyMDgxfn4yMDExLTEwLTEwIDEzOjQ1OjA3LjA1MTEyNSswMDowMH4wLjY1ODk0ODUzNzI0OH4";

	private final static EventBus EVENT_BUS = EventBusHelper.getEventBus();

	/**
	 * Tok
	 */
	private boolean connected;

	private Session session;

	private Publisher publisher;

	private Map<Stream, Subscriber> subscribers = new HashMap<Stream, Subscriber>();

	private TokBox tokBox;

	public TokModel() {
		if (TokBox.supportRequirements()) {
			// Init Tok
			tokBox = TokBox.getInstance();
		} else {
			Window.alert("You don't have the minimum requirements to run this application.\n"
					+ "Please upgrade to the latest version of Flash.");
		}
		bind();
	}

	private void bind() {
		EVENT_BUS.addHandler(ConnectEvent.getType(), new ConnectHandler() {
			@Override
			public void onConnect(ConnectEvent event) {
				connect();
			}
		});

		EVENT_BUS.addHandler(DisconnectEvent.getType(), new DisconnectHandler() {
			@Override
			public void onDisconnect(DisconnectEvent event) {
				disconnect();
			}
		});

		EVENT_BUS.addHandler(PublishEvent.getType(), new PublishHandler() {
			@Override
			public void onPublish(PublishEvent event) {
				publish(event.getPanelId(), event.getPublishProperties());
			}
		});

		EVENT_BUS.addHandler(UnpublishEvent.getType(), new UnpublishHandler() {
			@Override
			public void onUnpublish(UnpublishEvent event) {
				unpublish();
			}
		});

		EVENT_BUS.addHandler(SubscribeToStreamEvent.getType(), new SubscribeToStreamHandler() {
			@Override
			public void onSubscribeToStream(SubscribeToStreamEvent event) {
				subscribe(event.getPanelId(), event.getStream());
			}
		});
	}

	private void connect() {
		// Check devices
		DeviceManager deviceManager = tokBox.initDeviceManager(API_KEY);
		deviceManager.addDeviceStatusHandler(this);
		deviceManager.detectDevice();

		// Init session
		session = tokBox.initSession(DEV_SESSION_ID);
		session.addSessionHandler(this);
		session.connect(API_KEY, DEV_TOKEN);
	}

	private void disconnect() {
		if (this.session != null) {
			this.session.disconnect();
			this.session = null;
		}
	}

	private void unpublish() {
		if (this.publisher != null) {
			this.session.unpublish(this.publisher);
			this.publisher = null;
		}
	}

	private void publish(String panelId, PublishProperties publishProperties) {
		this.publisher = session.publish(panelId, publishProperties);
	}

	private void subscribe(String panelId, Stream stream) {
		Subscriber subscriber = this.session.subscribe(stream, panelId);
		this.subscribers.put(stream, subscriber);
	}

	private void unsubscribe(Stream stream) {
		Subscriber subscriber = this.subscribers.remove(stream);
		if (subscriber != null) {
			this.session.unsubscribe(subscriber);
		}
		EVENT_BUS.fireEvent(new StreamDeletedEvent(stream));
	}

	private void createStreamPanel(List<Stream> streams) {
		for (Stream stream : streams) {
			if (stream.getConnection().getConnectionId().equals(this.session.getConnection().getConnectionId())) {
				// Make sure we don't subscribe to ourself
				continue;
			}
			EVENT_BUS.fireEvent(new NewStreamEvent(stream));
		}
	}

	private void deleteStreamPanel(List<Stream> streams) {
		for (Stream stream : streams) {
			if (stream.getConnection().getConnectionId().equals(this.session.getConnection().getConnectionId())) {
				// Make sure we don't subscribe to ourself
				continue;
			}
			EVENT_BUS.fireEvent(new StreamDeletedEvent(stream));
		}
	}

	@Override
	public void onException(ExceptionEvent event) {
		this.log(event.getCode() + " : " + event.getMesage());
	}

	@Override
	public void onSessionConnected(SessionConnectedEvent event) {
		this.log("Session connected");
		createStreamPanel(event.getStreams());
		EVENT_BUS.fireEvent(new ConnectedEvent());
	}

	@Override
	public void onSessionDisconnected(SessionDisconnectedEvent event) {
		this.log("Session disconnected");
		EVENT_BUS.fireEvent(new DisconnectedEvent());
	}

	@Override
	public void onConnectionCreated(ConnectionCreatedEvent event) {
		this.log("Connection created");
	}

	@Override
	public void onConnectionDestroyed(ConnectionDestroyedEvent event) {
		this.log("Connection destroyed");
	}

	@Override
	public void onStreamCreated(StreamCreatedEvent event) {
		this.log("Stream created");
		createStreamPanel(event.getStreams());
	}

	@Override
	public void onStreamDestroyed(StreamDestroyedEvent event) {
		this.log("Stream destroyed");
		this.deleteStreamPanel(event.getStreams());
	}

	@Override
	public void onDeviceStatus(DeviceStatusEvent event) {
		for (Camera camera : event.getCameras()) {
			this.log("Camera detected : " + camera.getName() + "(" + camera.getStatus() + ")");
		}
		for (Microphone microphone : event.getMicrophones()) {
			this.log("Microphone detected : " + microphone.getName() + "(status:" + microphone.getStatus() + ")");
		}
		Camera selectedCamera = event.getSelectedCamera();
		Microphone selectedMicrophone = event.getSelectedMicrophone();
		this.log("Microphone selected : " + selectedMicrophone.getName() + "(status:" + selectedMicrophone.getStatus()
				+ ")");
		this.log("Camera selected : " + selectedCamera.getName() + "(status:" + selectedCamera.getStatus() + ")");
	}

	private native void log(String message) /*-{
		console.log(message);
	}-*/;
}
