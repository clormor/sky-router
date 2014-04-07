package com.clormor.skyrouter;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.doNothing;

import com.clormor.skyrouter.model.SkyRouterConstants;
import com.clormor.skyrouter.view.SkyRouterView;
import com.clormor.skyrouter.view.SkyRouterViewFactory;

public class SkyRouterCliTest {

	@Mock
	SkyRouterViewFactory mockFactory;

	@Mock
	SkyRouterView mockView;
	
	@Mock
	SkyRouterView mockView2;
	
	@Mock
	SkyRouterView mockView3;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void no_options_specified() throws ParseException {
		String[] args = { };
		SkyRouterCliStub testCli = new SkyRouterCliStub(args);
		testCli.run();
		
		// no exceptions, help message printed
		assertTrue(testCli.helpWasPrinted());
	}
	
	@Test
	public void help_specified() throws ParseException {
		doNothing().when(mockView).reboot();
		when(mockFactory.createView(SkyRouterConstants.DEFAULT_USERNAME, SkyRouterConstants.DEFAULT_PASSWORD)).thenReturn(mockView);
		
		SkyRouterCliStub testCli;
		
		String[] args1 = {"-help"};
		
		// no exceptions, help message printed
		testCli = new SkyRouterCliStub(args1);
		testCli.run();
		assertTrue(testCli.helpWasPrinted());
		
		String[] args2 = {"-h"};

		// no exceptions, help message printed
		testCli = new SkyRouterCliStub(args2);
		testCli.run();
		assertTrue(testCli.helpWasPrinted());
		
		String[] args3 = {"-h", "-r"};
		
		// help printed, reboot not run
		testCli = new SkyRouterCliStub(args3);
		testCli.run();
		assertTrue(testCli.helpWasPrinted());
		verify(mockView, never()).reboot();
	}
	
	@Test
	public void default_credentials() throws ParseException {
		String testUsername = "testUser";
		String testPassword = "testPass";
		
		doNothing().when(mockView).reboot();
		doNothing().when(mockView2).reboot();
		doNothing().when(mockView3).reboot();
		
		when(mockFactory.createView(SkyRouterConstants.DEFAULT_USERNAME, SkyRouterConstants.DEFAULT_PASSWORD)).thenReturn(mockView);
		when(mockFactory.createView(testUsername, SkyRouterConstants.DEFAULT_PASSWORD)).thenReturn(mockView2);
		when(mockFactory.createView(SkyRouterConstants.DEFAULT_USERNAME, testPassword)).thenReturn(mockView3);
		
		String[] args = {"-r"};
		SkyRouterCliStub testCli = new SkyRouterCliStub(args, mockFactory);
		testCli.run();
		
		// method correctly called using default credentials
		verify(mockView, times(1)).reboot();
		
		String[] args2 = {"-r", "-u", testUsername};
		testCli = new SkyRouterCliStub(args2, mockFactory);
		testCli.run();
		
		// method correctly called using default password and specified user name
		verify(mockView2, times(1)).reboot();
		
		String[] args3 = {"-r", "-p", testPassword};
		testCli = new SkyRouterCliStub(args3, mockFactory);
		testCli.run();
		
		// method correctly called using default user name and specified password
		verify(mockView3, times(1)).reboot();
	}
}
