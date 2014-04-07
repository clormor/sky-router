package com.clormor.skyrouter.view;

public class SkyRouterViewFactory {

	public SkyRouterView createView(String username, String password) {
		return new SkyRouterView(username, password);
	}
}
