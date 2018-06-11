package org.eye.parser;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

public class YangGenerator {

	public static final String INDENT = "  ";

	public static final List<String> ADD_LINE = new ArrayList<String>();

	static {
		ADD_LINE.add(YangKeyword.YK_GROUPING);
		ADD_LINE.add(YangKeyword.YK_TYPEDEF);
	}

	public static String generateFile(DefaultMutableTreeNode top) {
		StringBuilder sb = new StringBuilder();
		recursiveGenerate(top, 0, sb);
		return sb.toString();
	}

	private static String getIndent(int level) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < level; i++) {
			sb.append(INDENT);
		}
		return sb.toString();
	}

	private static void recursiveGenerate(DefaultMutableTreeNode node, int level, StringBuilder sb) {
		Statement st = (Statement) node.getUserObject();
		String indent = getIndent(level);
		sb.append(indent);
		if (st.getKeyword().equals(YangKeyword.YK_COMMENT)) {
			sb.append(st.getText()).append("\n");
			return;
		}

		sb.append(st.getKeyword());
		if (st.getValue() != null) {
			sb.append(" ").append(st.getValue());
		}

		if (st.children.size() == 0) {
			sb.append(";\n");
		} else if (st.children.size() == 1 && st.children.get(0).getKeyword().equals(YangKeyword.YK_COMMENT)) {
			String comment = st.children.get(0).getText();
			sb.append(comment).append("\n");
		} else {
			sb.append(" {\n");
			for (int i = 0; i < node.getChildCount(); i++) {
				DefaultMutableTreeNode sub = (DefaultMutableTreeNode) node.getChildAt(i);
				recursiveGenerate(sub, level + 1, sb);
			}
			sb.append(indent).append("}\n");
			if (ADD_LINE.contains(st.getKeyword())) {
				sb.append("\n");
			}
		}
	}

}
