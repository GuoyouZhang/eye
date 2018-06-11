package org.eye.parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileUtil {
	public static void writeFile(String name, String txt) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
					name)));
			writer.write(txt);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String readFile(String filename) {

		String ret = "";
		File f = new File(filename);
		if (!f.exists()) {
			return null;
		}

		FileChannel channel = null;
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(f);
			channel = fs.getChannel();
			ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
			while ((channel.read(byteBuffer)) > 0) {
			}
			ret = new String(byteBuffer.array());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	public static String getNameOfFile(String fullPath) {
		int j = fullPath.length();
		for (int i = 0; i < j; i++) {
			if (fullPath.charAt(j - i - 1) == File.separatorChar) {
				String s = fullPath.substring(j - i, j);
				return s;
			}

		}
		return null;
	}
}
