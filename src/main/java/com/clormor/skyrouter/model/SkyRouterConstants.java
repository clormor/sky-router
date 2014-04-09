package com.clormor.skyrouter.model;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class SkyRouterConstants {

	public static final String DEFAULT_USERNAME = "admin";
	public static final String DEFAULT_PASSWORD = "sky";
	public static final String DEFAULT_HOST = "192.168.0.1";
	public static final String DIAGNOSTICS_URL = "/sky_diagnostics.html";
	public static final String STATISTICS_URL = "/sky_router_status.html";
	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("dd/MM/yy HH:mm:ss").withZone(DateTimeZone.forID("Europe/London"));
}
