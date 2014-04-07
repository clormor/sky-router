package com.clormor.skyrouter;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.clormor.skyrouter.model.SkyRouterConstants;
import com.clormor.skyrouter.view.SkyRouterView;
import com.clormor.skyrouter.view.SkyRouterViewFactory;

public class SkyRouterCli implements Runnable {

	final Options options;
	CommandLine command;
	SkyRouterView view;

	@Override
	public void run() {
		try {
			if (command.getOptions().length == 0 || command.hasOption("help")) {
				printHelp();
				return;
			}

			if (command.hasOption("reboot")) {
				view.reboot();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * <p>
	 * Run an instance of the cli with the supplied parameters.
	 * </p>
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SkyRouterCli cli = null;

		try {
			cli = new SkyRouterCli(args);
			cli.run();
		} catch (Exception e) {
			System.exit(1);
		}
	}

	/**
	 * <p>
	 * Constructor - parses command-line options from main program.
	 * </p>
	 * 
	 * @param args
	 * @throws ParseException
	 */
	public SkyRouterCli(String[] args) throws ParseException {
		this(args, new SkyRouterViewFactory());
	}

	/**
	 * <p>
	 * Constructor - parses command-line options from main program.
	 * </p>
	 * 
	 * @param args
	 * @throws ParseException
	 */
	SkyRouterCli(String[] args, SkyRouterViewFactory factory)
			throws ParseException {
		options = new Options();

		Option reboot = new Option("r", "reboot", false, "reboot your router");
		Option help = new Option("h", "help", false, "print this help message");

		Option usernameOption = new Option("u", "username", true,
				"sky router login");
		usernameOption.setArgName("username");

		Option passwordOption = new Option("p", "password", true,
				"sky router password");
		passwordOption.setArgName("password");

		Option addressOption = new Option("i", "ip", true,
				"sky router hostname or IP");
		passwordOption.setArgName("address");
		
		options.addOption(help);
		options.addOption(reboot);
		options.addOption(usernameOption);
		options.addOption(passwordOption);
		options.addOption(addressOption);

		command = processArgs(args);

		String username = command.getOptionValue('u',
				SkyRouterConstants.DEFAULT_USERNAME);
		String password = command.getOptionValue('p',
				SkyRouterConstants.DEFAULT_PASSWORD);
		String routerHost = command.getOptionValue('i',
				SkyRouterConstants.DEFAULT_HOST);
		
		view = factory.createView(username, password, routerHost);
	}

	/**
	 * <p>
	 * Verifies the supplied arguments. Throws a {@link ParseException} if the
	 * arguments are invalid. Must be called before <code>run()</code>.
	 * </p>
	 * 
	 * @param args
	 *            - the supplied command-line arguments
	 * @return the parsed options
	 * @throws ParseException
	 *             if the supplied options are invalid
	 */
	CommandLine processArgs(String[] args) throws ParseException {
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = parser.parse(options, args);

		// if help requested (or no args), ignore all other arguments
		if (cmd.getOptions().length == 0 || cmd.hasOption("help")) {
			return cmd;
		}

		return cmd;
	}

	public void printHelp() {
		new HelpFormatter().printHelp("sky-router", options);
	}

}
