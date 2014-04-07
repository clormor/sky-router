package com.clormor.skyrouter.view;

import com.clormor.skyrouter.controller.SkyRouterController;
import com.clormor.skyrouter.controller.SkyRouterControllerFactory;


public class SkyRouterView {

	private SkyRouterController controller;
	private String username;
	private String password;
	private String routerHost;
	
	SkyRouterView(String username, String password, String host, SkyRouterControllerFactory factory) {
		controller = factory.makeController();
		this.username = username;
		this.password = password;
		this.routerHost = host;
	}
	
	public SkyRouterView(String username, String password, String host) {
		this (username, password, host, new SkyRouterControllerFactory());
	}

	public String reboot() {
		try {
			controller.rebootRouter(username, password, routerHost);
			return "Router restarted.";
		} catch (Exception e) {
			return "Failed to reboot router...\n" +  e.getMessage();
		}
	}

}
