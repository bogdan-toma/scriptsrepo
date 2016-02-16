package com.tnd.eso.integration.scm.scripts;

public class ScriptsRepo {

	public static void main(String[] args) throws Exception {
		ScriptsRepoApp app = new ScriptsRepoApp();
		if (args.length != 1) {
			printArgErr();
			printHelp();
			return;
		}

		String arg = args[0];
		if ("--import".equals(arg)) {
			app.generateConfig();
			return;
		}
		if ("--help".equals(arg)) {
			printHelp();
			return;
		}
		if ("--deploy".equals(arg)) {
			app.loadProps();
			app.process();
		}
	}

	private static void printHelp() {
		System.out.println();
		System.out.println("Usage: java -jar scriptsrepo-[version].jar [arg]");
		System.out.println("where arg takes one of the below values:");
		System.out.println("    --import\tinitialise or import script configurations from .oma file");
		System.out.println("    --deploy\tdeploy current script versions to SAP Sourcing");
		System.out.println("    --help\tto see this message.");
	}

	private static void printArgErr() {
		System.out.println();
		System.out.println("Invalid argument(s).");
	}
}
