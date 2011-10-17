package com.google.gwt.tok.test.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class Application extends Composite {

	private static ApplicationUiBinder uiBinder = GWT.create(ApplicationUiBinder.class);

	interface ApplicationUiBinder extends UiBinder<Widget, Application> {
	}

	public Application() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
