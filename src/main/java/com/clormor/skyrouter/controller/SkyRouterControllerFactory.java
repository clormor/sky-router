package com.clormor.skyrouter.controller;

public class SkyRouterControllerFactory {

	public SkyRouterController makeController(String username, String password, String routerHost) {
		return new SkyRouterController(username, password, routerHost);
	}

}
