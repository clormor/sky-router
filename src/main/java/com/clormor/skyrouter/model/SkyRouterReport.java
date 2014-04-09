package com.clormor.skyrouter.model;

public class SkyRouterReport {

	private int down;
	private int up;
	
	public SkyRouterReport(int down, int up) {
		this.up = up;
		this.down = down;
	}

	public int getDown() {
		return down;
	}

	public int getUp() {
		return up;
	}
}
