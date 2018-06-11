package org.eye.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.filechooser.FileFilter;

public class FileFilterImpl extends FileFilter {
	ArrayList<String> ls = new ArrayList<String>();

	public FileFilterImpl(String[] postfix) {
		Collections.addAll(ls, postfix);
	}

	@Override
	public boolean accept(File f) {
		if (f.isDirectory())
			return true;
		for (String s : ls) {
			if (f.getName().endsWith(s))
				return true;
		}
		return false;
	}

	@Override
	public String getDescription() {
		return ls.toString();
	}

}
