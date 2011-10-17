package com.google.gwt.tok.test.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class DisconnectEvent extends GwtEvent<DisconnectHandler>{

	private static Type<DisconnectHandler> TYPE;
	
	public static Type<DisconnectHandler> getType() {
		return TYPE != null ? TYPE : (TYPE = new Type<DisconnectHandler>());
	}
	

	public DisconnectEvent(){

	}
	

	
	@Override
	protected void dispatch(DisconnectHandler handler) {
		handler.onDisconnect(this);
	}

	@Override
	public GwtEvent.Type<DisconnectHandler> getAssociatedType() {
		return getType();
	}

}
