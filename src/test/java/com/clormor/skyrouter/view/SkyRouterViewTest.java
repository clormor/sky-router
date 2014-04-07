package com.clormor.skyrouter.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.clormor.skyrouter.controller.SkyRouterController;
import com.clormor.skyrouter.controller.SkyRouterControllerFactory;
import com.clormor.skyrouter.view.SkyRouterView;

public class SkyRouterViewTest {

	private String testUser = "user";
	private String testPassword = "testPassword";
	private String testHost = "testHost";
	private SkyRouterView testView;
	
	@Mock
	private SkyRouterController mockController;
	
	@Mock
	private SkyRouterControllerFactory mockFactory;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		when(mockFactory.makeController()).thenReturn(mockController);
		testView = new SkyRouterView(testUser, testPassword, testHost, mockFactory);
	}
	
	@Test
	public void test_reboot_no_exception() throws Exception {
		doNothing().when(mockController).rebootRouter(testUser, testPassword, testHost);
		String result = testView.reboot();
		
		// verify reboot called, and exception printed
		verify(mockController, times(1)).rebootRouter(testUser, testPassword, testHost);
		assertEquals(result, "Router restarted.");
	}
	
	@Test
	public void test_reboot_exception() throws Exception {
		String exceptionMessage = "test failure message";
		Exception testException = new Exception(exceptionMessage);
		
		doThrow(testException).when(mockController).rebootRouter(testUser, testPassword, testHost);
		String result = testView.reboot();
		
		// verify reboot called, and exception printed
		verify(mockController, times(1)).rebootRouter(testUser, testPassword, testHost);
		assertTrue(result.contains(exceptionMessage));
	}
}
