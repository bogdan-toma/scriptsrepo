/**
 * ScriptsRepo - Automatic deploy tool for SAP Sourcing scripts
 * Copyright (C) 2016  Bogdan Toma
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/
package com.tnd.eso.integration.scm.scripts;

public class ScriptsRepo {
	private static String VERSION = "0.4.1";

	public static void main(String[] args) {
		ScriptsRepoApp app = new ScriptsRepoApp();
		System.out.println();

		if (args.length != 1) {
			printArgErr();
			printHelp();
			printCopy();
			return;
		}

		String arg = args[0];
		if ("--import".equals(arg)) {
			app.runImport();
			System.out.println("DONE");
			printCopy();
			return;
		}
		if ("--help".equals(arg)) {
			printHelp();
			printCopy();
			return;
		}
		if ("--deploy".equals(arg)) {
			app.runDeploy();
			System.out.println("DONE");
			printCopy();
			return;
		}
	}

	private static void printHelp() {
		System.out.println();
		System.out.println("Usage: java -jar scriptsrepo-" + VERSION + ".jar [arg]");
		System.out.println("where arg takes one of the below values:");
		System.out.println("    --import\tinitialise or import script configurations from .oma file");
		System.out.println("    \t\toma file should be named allscripts.oma and available in the same directory");
		System.out.println("    --deploy\tdeploy current script versions to SAP Sourcing");
		System.out.println("    --help\tto see this message.");
	}

	private static void printArgErr() {
		System.out.println();
		System.out.println("Invalid argument(s).");
	}

	private static void printCopy() {
		System.out.println();
		System.out.println("ScriptsRepo  Copyright (C) 2016  Bogdan Toma");
		System.out.println("This program comes with ABSOLUTELY NO WARRANTY.");
		System.out.println("This is free software, and you are welcome to redistribute it");
		System.out.println("under certain conditions; please see LICENCE file for details.");
	}
}
