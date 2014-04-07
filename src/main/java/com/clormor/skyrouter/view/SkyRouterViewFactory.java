package com.clormor.skyrouter.view;

public class SkyRouterViewFactory {

	public SkyRouterView createView(String username, String password, String host) {
		return new SkyRouterView(username, password, host);
	}
}
