package com.google.gwt.tok.test.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.tok.test.client.model.TokModel;
import com.google.gwt.tok.test.client.ui.Application;
import com.google.gwt.user.client.ui.RootPanel;

public class TokTest implements EntryPoint {

	@Override
	public void onModuleLoad() {
		TokModel tokModel = new TokModel();

		Application application = new Application();
		RootPanel.get().add(application);
	}

}
