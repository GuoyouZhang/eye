package org.eye.parser;

import java.util.ArrayList;
import java.util.List;

public class Statement {
	String text;
	String value;
	int line;
	String file;
	String keyword;
	String prefix;

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public List<Statement> children = new ArrayList<Statement>();

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text.trim();
	}

	public int getLine() {
		return line;
	}

	public String getFile() {
		return file;
	}

	public String getKeyword() {
		return keyword;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String toString() {
		if (value != null)
			return keyword + "=" + value;
		else
			return this.keyword;
	}

}
