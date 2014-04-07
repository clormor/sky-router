package com.clormor.skyrouter;

import org.apache.commons.cli.ParseException;

import com.clormor.skyrouter.view.SkyRouterViewFactory;


public class SkyRouterCliStub extends SkyRouterCli {

	private boolean helpPrinted = false;
	
	public SkyRouterCliStub(String[] args) throws ParseException {
		super(args);
	}

	public SkyRouterCliStub(String[] args, SkyRouterViewFactory factory) throws ParseException {
		super(args, factory);
	}
	
	@Override
	public void printHelp() {
		helpPrinted = true;
	}

	public boolean helpWasPrinted() {
		return helpPrinted;
	}
	
}
