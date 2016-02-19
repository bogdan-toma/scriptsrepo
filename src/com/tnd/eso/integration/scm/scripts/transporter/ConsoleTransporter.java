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
package com.tnd.eso.integration.scm.scripts.transporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class ConsoleTransporter implements Transporter {

	@Override
	public void transport(File f) {
		if (!"xml".equals(getFileExtension(f)))
			return;
		InputStream stream;
		try {
			stream = new FileInputStream(f);
			int oneByte;
			while ((oneByte = stream.read()) != -1) {
				System.out.write(oneByte);
			}
			System.out.println();
			System.out.flush();
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setWorkingDir(String SFTPWORKINGDIR) {
	}

	@Override
	public void close() {
	}

	private static String getFileExtension(File file) {
		String fileName = file.getName();
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		else
			return "";
	}
}
