package com.clormor.skyrouter.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;

import com.clormor.skyrouter.model.SkyRouterConstants;
import com.clormor.skyrouter.model.SkyRouterReport;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ConfirmHandler;
import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.common.collect.Iterables;

public class SkyRouterController {

	private WebClient client;
	private final String routerHost;

	final LinkedList<WebWindow> windows = new LinkedList<WebWindow>();

	public SkyRouterController(String username, String password,
			String routerHost) {
		this.routerHost = routerHost;
		client = new WebClient(BrowserVersion.FIREFOX_24);

		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.NoOpLog");

		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit")
				.setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient")
				.setLevel(Level.OFF);

		DefaultCredentialsProvider auth = new DefaultCredentialsProvider();
		auth.addCredentials(username, password);
		client.setCredentialsProvider(auth);

		client.setConfirmHandler(new ConfirmHandler() {
			public boolean handleConfirm(final Page page, final String message) {
				return true;
			}
		});

		client.getOptions().setJavaScriptEnabled(true);
		client.getOptions().setTimeout(10000);
		client.getOptions().setRedirectEnabled(true);
		client.getOptions().setThrowExceptionOnFailingStatusCode(false);
		client.getOptions().setThrowExceptionOnScriptError(false);
		client.getOptions().setCssEnabled(false);
		client.getOptions().setAppletEnabled(false);
		client.getOptions().setActiveXNative(false);
		client.getOptions().setUseInsecureSSL(true);
	}

	/**
	 * <p>
	 * Connect to a sky router and reboot it.
	 * </p>
	 * 
	 * @exception Exception
	 *                if anything goes wrong
	 */
	@SuppressWarnings("unchecked")
	public void rebootRouter() throws Exception {
		HtmlPage currentPage = client.getPage("http://" + routerHost
				+ SkyRouterConstants.DIAGNOSTICS_URL);

		HtmlAnchor rebootButton = Iterables
				.getOnlyElement((List<HtmlAnchor>) currentPage
						.getByXPath("/html/body/div/div/div/div/form[4]/table/tbody/tr/td[5]/a"));

		rebootButton.click();
		waitForJavaScript();
	}

	/**
	 * <p>
	 * Pauses execution until JavaScript processes complete, or throws a
	 * run-time exception if it times out.
	 * </p>
	 */
	private void waitForJavaScript() {
		int maxTries = 10;
		int processesStillExecuting = 1;

		while (processesStillExecuting > 0 & maxTries > 0) {
			maxTries--;
			processesStillExecuting = client.waitForBackgroundJavaScript(1000);
		}

		if (processesStillExecuting != 0) {
			throw new RuntimeException(
					"failed to execute java script, timing out!");
		}
	}

	/**
	 * <p>
	 * Returns the current bandwidth established between the router and the
	 * exchange.
	 * </p>
	 * 
	 * @return a Sting reporting the current bandwidth metrics
	 * @throws Exception
	 *             if something bad happens
	 */
	@SuppressWarnings("unchecked")
	public SkyRouterReport getBandwidthStatistics() throws Exception {
		HtmlPage currentPage = client.getPage("http://" + routerHost
				+ SkyRouterConstants.STATISTICS_URL);

		DomElement downStream = Iterables
				.getOnlyElement((List<DomElement>) currentPage
						.getByXPath("/html/body/div/div/div/form/div/table[2]/tbody/tr[3]/td[2]"));
		int down = Integer.parseInt(downStream.getTextContent());

		DomElement upStream = Iterables
				.getOnlyElement((List<DomElement>) currentPage
						.getByXPath("/html/body/div/div/div/form/div/table[2]/tbody/tr[4]/td[2]"));
		
		int up = Integer.parseInt(upStream.getTextContent());

		return new SkyRouterReport(down, up);
	}

}
