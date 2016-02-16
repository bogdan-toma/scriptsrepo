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
