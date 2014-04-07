package com.clormor.skyrouter;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
	
	@Mock
	SkyRouterView mockView4;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		when(mockView.reboot()).thenReturn("");
		when(mockView2.reboot()).thenReturn("");
		when(mockView3.reboot()).thenReturn("");
		when(mockView4.reboot()).thenReturn("");
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
		when(mockFactory.createView(SkyRouterConstants.DEFAULT_USERNAME, SkyRouterConstants.DEFAULT_PASSWORD, SkyRouterConstants.DEFAULT_HOST)).thenReturn(mockView);
		
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
	public void default_args() throws ParseException {
		String testUsername = "testUser";
		String testPassword = "testPass";
		String testHost = "testHost";
		
		when(mockFactory.createView(SkyRouterConstants.DEFAULT_USERNAME, SkyRouterConstants.DEFAULT_PASSWORD, SkyRouterConstants.DEFAULT_HOST)).thenReturn(mockView);
		when(mockFactory.createView(testUsername, SkyRouterConstants.DEFAULT_PASSWORD, SkyRouterConstants.DEFAULT_HOST)).thenReturn(mockView2);
		when(mockFactory.createView(SkyRouterConstants.DEFAULT_USERNAME, testPassword, SkyRouterConstants.DEFAULT_HOST)).thenReturn(mockView3);
		when(mockFactory.createView(SkyRouterConstants.DEFAULT_USERNAME, SkyRouterConstants.DEFAULT_PASSWORD, testHost)).thenReturn(mockView4);
		
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
		
		String[] args4 = {"-r", "-i", testHost};
		testCli = new SkyRouterCliStub(args4, mockFactory);
		testCli.run();
		
		// method correctly called using default user name and specified password
		verify(mockView4, times(1)).reboot();
	}
	
	@Test
	public void reboot() throws ParseException {
		String testUsername = "testUser";
		
		when(mockFactory.createView(SkyRouterConstants.DEFAULT_USERNAME, SkyRouterConstants.DEFAULT_PASSWORD, SkyRouterConstants.DEFAULT_HOST)).thenReturn(mockView);
		when(mockFactory.createView(testUsername, SkyRouterConstants.DEFAULT_PASSWORD, SkyRouterConstants.DEFAULT_HOST)).thenReturn(mockView2);
		
		String[] args = {"-r"};
		SkyRouterCliStub testCli = new SkyRouterCliStub(args, mockFactory);
		testCli.run();
		
		// method correctly called using -r
		verify(mockView, times(1)).reboot();
		
		String[] args2 = {"-reboot", "-u", testUsername};
		testCli = new SkyRouterCliStub(args2, mockFactory);
		testCli.run();
		
		// method correctly called using -reboot
		verify(mockView2, times(1)).reboot();
	}
}
