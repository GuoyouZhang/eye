package org.eye.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.eye.parser.Statement;
import org.eye.parser.YangKeyword;
import org.eye.parser.YangRepo;

public class AugmentMenuHandler implements ActionListener {

	static final Pattern p = Pattern.compile("\\s*(\\S+)\\s*=\\s*(\\S+)");

	Map<String, List<Statement>> map = new HashMap<String, List<Statement>>();

	String currentXpath = "";

	JTree tree = null;

	DefaultMutableTreeNode augTarget = null;

	EyeGui gui = null;

	public AugmentMenuHandler(JTree tree, EyeGui gui) {
		this.tree = tree;
		this.gui = gui;
	}

	private void addCondition(Statement s) {
		for (Statement child : s.children) {
			if (child.getKeyword().equals(YangKeyword.YK_WHEN)) {
				// System.out.println(child.getValue());
				// System.out.println(convertWhen(child.getValue()));
				String aline = child.getValue();
				String c = convertWhen(aline);
				Matcher m = p.matcher(c);
				while (m.find()) {
					String k = (m.group(1) + "=" + m.group(2));
					List<Statement> ls = map.get(k);
					if (ls == null) {
						ls = new ArrayList<Statement>();
						map.put(k, ls);
					}
					ls.add(s);
				}
			}
		}

	}

	public String trimAugmentPath(String xpath) {
		String s = xpath.replace("\"", "");
		s = s.replaceAll("\\s*\\+\\s*", "");
		return s;
	}

	private String convertWhen(String condition) {
		String s = trimAugmentPath(condition);
		s = s.replace("derived-from-or-self(", "");
		s = s.replace(")", "");
		s = s.replace(",", "=");
		return s;
	}

	public String getXpathWithPrefix(DefaultMutableTreeNode node) {
		String path = "";
		Statement s = (Statement) node.getUserObject();
		if (s.getKeyword().equals(YangKeyword.YK_MODULE) || s.getKeyword().equals(YangKeyword.YK_SUBMODULE)) {
			return path;
		} else {
			path = "/" + s.getPrefix() + ":" + s.getValue();

			return getXpathWithPrefix((DefaultMutableTreeNode) node.getParent()) + path;
		}
	}

	public JMenu findAugmentMenus(DefaultMutableTreeNode node) {
		map.clear();
		augTarget = node;
		findAugment(node);

		JMenu addMenu = new JMenu("Extend Augment");
		for (String s : map.keySet()) {
			JMenuItem item = new JMenuItem();
			item.setText(s);
			item.setActionCommand(s);
			item.addActionListener(this);
			addMenu.add(item);
		}
		return addMenu;
	}

	public void findAugment(DefaultMutableTreeNode node) {
		List<Statement> ls = YangRepo.findStatementByK(YangKeyword.YK_AUGMENT);
		currentXpath = getXpathWithPrefix(node);

		for (Statement s : ls) {
			String trim = trimAugmentPath(s.getValue());
			if (trim.equals(currentXpath)) {
				addCondition(s);
			}
		}

	}

	void addTreeNode(DefaultMutableTreeNode parent, Statement s) {
		DefaultMutableTreeNode child = new DefaultMutableTreeNode(s);
		parent.add(child);
		for (Statement sub : s.children) {
			addTreeNode(child, sub);
		}
	}

	public void actionPerformed(ActionEvent evt) {
		List<Statement> augs = map.get(evt.getActionCommand());
		Statement topS = (Statement) this.augTarget.getUserObject();
		DefaultMutableTreeNode topN = new DefaultMutableTreeNode(topS);
		
		for(Statement att:topS.children) {
			addTreeNode(topN, att);
		}
		
		for (Statement aug : augs) {
			for (Statement att : aug.children) {
				addTreeNode(topN, att);
			}
		}
		
		
		gui.extendAugment(topN);
	}

}
