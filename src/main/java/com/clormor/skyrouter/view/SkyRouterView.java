package com.clormor.skyrouter.view;

import org.joda.time.DateTime;

import com.clormor.skyrouter.controller.SkyRouterController;
import com.clormor.skyrouter.controller.SkyRouterControllerFactory;
import com.clormor.skyrouter.model.SkyRouterConstants;
import com.clormor.skyrouter.model.SkyRouterReport;


public class SkyRouterView {

	private SkyRouterController controller;
	
	SkyRouterView(String username, String password, String host, SkyRouterControllerFactory factory) {
		controller = factory.makeController(username, password, host);
	}
	
	public SkyRouterView(String username, String password, String host) {
		this (username, password, host, new SkyRouterControllerFactory());
	}

	public String reboot() {
		try {
			controller.rebootRouter();
			return "Router restarted.";
		} catch (Exception e) {
			return "Failed to reboot router...\n" +  e.getMessage();
		}
	}

	public String getCurrentBandwidth() {
		try {
			SkyRouterReport bandwidthStatistics = controller.getBandwidthStatistics();
			StringBuilder result = new StringBuilder();
			result.append(SkyRouterConstants.DATE_FORMATTER.print(DateTime.now()));
			result.append(",\t").append(bandwidthStatistics.getDown());
			result.append(",\t").append(bandwidthStatistics.getUp());
			return result.toString();
		} catch (Exception e) {
			return "Failed to get statistics...\n" +  e.getMessage();
		}
	}

}
