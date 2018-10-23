package org.eye.gui;

import org.eye.parser.Statement;
import org.eye.parser.YangKeyword;

public class TextLocator {

	public static int locate(Statement st, String txt, int pos) {
		if (st.getLine() < 0)
			return -1;

		int bracket = 0;

		boolean flag = false;
		int i = pos;

		for (; i < txt.length(); i++) {
			char c = txt.charAt(i);
			if (c == '/') {
				// skip comment
				i++;
				c = txt.charAt(i);
				if (c == '/') { // e.g. //xxx
					while (true) {
						i++;
						if (txt.charAt(i) == '\n') {
							if (st.getKeyword().equals(YangKeyword.YK_COMMENT))
								return i + 1;
							break;
						}
					}
				} else if (c == '*') { /* xxx */
					while (true) {
						i++;
						if (txt.charAt(i) == '/' && txt.charAt(i - 1) == '*') {
							if (st.getKeyword().equals(YangKeyword.YK_COMMENT))
								return i + 1;
							break;
						}
					}
				} else { //something like abc/def
					//System.out.println("not a comment, char:" + i);
					//return -1;
				}
			} else if (c == '\'' || c == '\"') {
				// skip quotation
				while (true) { // "xxx" or 'xxx'
					i++;
					if (c == txt.charAt(i) && txt.charAt(i - 1) != '\\') {
						break;
					}
				}
			} else if (c == '{') {
				bracket++;
				flag = true;
			} else if (c == '}') {
				bracket--;
				if (st.children.size() > 0) {
					if (bracket == 0 && flag) {
						break;
					}
				} else {// e.g. container xxx {}
					break;
				}
			} else if (c == ';') {
				if (bracket == 0) { // e.g. type string;
					break;
				}
			}
		}
		return i + 1;
	}

}
