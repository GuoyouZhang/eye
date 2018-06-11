package org.eye.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.eye.parser.Relationship;
import org.eye.parser.Statement;
import org.eye.parser.YangMetaMap;
import org.eye.parser.YangKeyword;
import org.eye.parser.YangNodeRelation;
import org.eye.parser.YangRepo;
import org.eye.parser.YangBuiltIn;

public class AddMenuHandler implements ActionListener {

	public AddMenuHandler(JTree tree) {
		quotaList.add(YangKeyword.YK_ARGUMENT);
		quotaList.add(YangKeyword.YK_AUGMENT);
		quotaList.add(YangKeyword.YK_CONTACT);
		quotaList.add(YangKeyword.YK_DESCRIPTION);
		quotaList.add(YangKeyword.YK_DEFAULT);
		quotaList.add(YangKeyword.YK_ERROR_APP_TAG);
		quotaList.add(YangKeyword.YK_ERROR_MESSAGE);
		quotaList.add(YangKeyword.YK_NAMESPACE);
		quotaList.add(YangKeyword.YK_KEY);
		quotaList.add(YangKeyword.YK_LENGTH);
		quotaList.add(YangKeyword.YK_MUST);
		quotaList.add(YangKeyword.YK_MAX_ELEMENTS);
		quotaList.add(YangKeyword.YK_MIN_ELEMENTS);
		quotaList.add(YangKeyword.YK_ORGANIZATION);
		quotaList.add(YangKeyword.YK_PATH);
		quotaList.add(YangKeyword.YK_PATTERN);
		quotaList.add(YangKeyword.YK_PRESENCE);
		quotaList.add(YangKeyword.YK_RANGE);
		// quotaList.add(YangKeywordAuto.YK_REVISION);
		// quotaList.add(YangKeywordAuto.YK_REVISION_DATE);
		quotaList.add(YangKeyword.YK_UNIQUE);
		quotaList.add(YangKeyword.YK_WHEN);

		this.tree = tree;
		Map<String, List<Relationship>> map = YangNodeRelation.substatement;
		// right click on blank yang tree view, show add module/submodule menus
		List<Relationship> list = new ArrayList<Relationship>();
		map.put("", list);
		list.add(new Relationship("module", "1", "5.1"));
		list.add(new Relationship("submodule", "1", "5.1"));
		addMenus(map);
		map = YangBuiltIn.substatement;
		addMenus(map);

		JMenuItem item = new JMenuItem(YangKeyword.YK_COMMENT);
		item.setActionCommand(YangKeyword.YK_COMMENT);
		item.addActionListener(this);
		menuMap.put(YangKeyword.YK_COMMENT, item);
	}

	List<String> quotaList = new ArrayList<String>();

	Map<String, JMenuItem> menuMap = new HashMap<String, JMenuItem>();
	JMenu addMenu = new JMenu("Add");

	// Statement currentStatement = null;
	JTextField txtInput = new JTextField("");
	JComboBox comboInput = new JComboBox();

	JTree tree;
	DefaultMutableTreeNode parentNode;
	Statement parentStmt;

	private void addMenus(Map<String, List<Relationship>> map) {
		for (List<Relationship> ls : map.values()) {
			for (Relationship r : ls) {
				if (menuMap.get(r.getStatement()) == null) {
					JMenuItem item = new JMenuItem();
					item.setText(r.getStatement());
					item.setToolTipText("See " + r.getSection() + " in RFC6020.");
					item.setActionCommand(r.getStatement());
					item.addActionListener(this);
					menuMap.put(r.getStatement(), item);
				}
			}
		}
	}

	private List<JMenuItem> findExtension(Statement st) {
		List<JMenuItem> ls = new ArrayList<JMenuItem>();
		List<Statement> sts = YangRepo.findStatementByK(YangKeyword.YK_EXTENSION);
		for (Statement ext : sts) {
			String name = this.constructValueWithPrefix(ext);
			JMenuItem item = new JMenuItem(name);
			item.setActionCommand(name);
			item.addActionListener(this);
			ls.add(item);
		}
		return ls;
	}

	// for top level, nodeType = "", see above function
	public JMenu findAddMenus(DefaultMutableTreeNode node) {
		this.parentNode = node;
		Object obj = node.getUserObject();
		this.parentStmt = null;
		if (obj instanceof Statement) {
			parentStmt = (Statement) obj;
		}

		List<Relationship> ls = null;
		this.addMenu.removeAll();

		if (parentStmt == null) {
			ls = YangNodeRelation.substatement.get("");// top level
		} else if (parentStmt.getKeyword().equals(YangKeyword.YK_TYPE)) {
			ls = YangBuiltIn.substatement.get(parentStmt.getValue());
		} else {
			ls = YangNodeRelation.substatement.get(parentStmt.getKeyword());
		}

		if (parentStmt != null && !parentStmt.getKeyword().equals(YangKeyword.YK_COMMENT))
			addMenu.add(menuMap.get(YangKeyword.YK_COMMENT));

		if (ls != null) {
			for (Relationship r : ls) {
				addMenu.add(menuMap.get(r.getStatement()));
			}

			if (this.parentStmt != null) {
				for (JMenuItem item : findExtension(this.parentStmt)) {
					addMenu.add(item);
				}
			}
		}
		return addMenu;
	}

	private Statement addTopStatement(String file, String prefix) {
		Statement st = new Statement();
		st.setFile(file);
		st.setPrefix(prefix);
		return st;
	}

	private DefaultMutableTreeNode addTreeNode(String key, String value, String file, String prefix,
			DefaultMutableTreeNode p, Statement pStmt) {
		Statement st = new Statement();
		st.setKeyword(key);
		st.setValue(value);
		st.setLine(-1);
		st.setText(key + " " + value);
		st.setFile(file);
		st.setPrefix(prefix);

		pStmt.children.add(st);
		DefaultMutableTreeNode child = new DefaultMutableTreeNode(st);
		p.add(child);

		YangRepo.addStatement(st);

		((DefaultTreeModel) tree.getModel()).reload(this.parentNode);
		return child;
	}

	private void addModule(String cmd) {
		// define map to dynamically handle text
		JTextField txtFile = new JTextField();
		JTextField txtName = new JTextField();
		JTextField txtPrefix = new JTextField();

		final JComponent[] inputs = new JComponent[] { new JLabel("Name"), txtName, new JLabel("File"), txtFile,
				new JLabel("Prefix"), txtPrefix };

		int yes = JOptionPane.showConfirmDialog(null, inputs, "Add " + cmd, JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (yes != JOptionPane.OK_OPTION)
			return;

		String name = txtName.getText().trim();
		String file = txtFile.getText().trim();
		String prefix = txtPrefix.getText().trim();

		if (file.equals("") || name.equals("") || prefix.equals("")) {
			return;
		}

		Statement top = this.addTopStatement(file, prefix);
		YangRepo.addStatement(top);
		DefaultMutableTreeNode child = addTreeNode(cmd, name, file, prefix, this.parentNode, top);
		addTreeNode(YangKeyword.YK_PREFIX, prefix, file, prefix, child, (Statement) child.getUserObject());
	}

	private void addComment() {

		String input = JOptionPane.showInputDialog("Comment format could be //comment or /* comment */",
				"/* comment */");
		if (input == null || input.trim().equals(""))
			return;

		Statement st = new Statement();
		st.setKeyword(YangKeyword.YK_COMMENT);
		st.setText(input.trim());
		st.setValue(input.trim());
		st.setLine(-1);

		st.setFile(this.parentStmt.getFile());
		st.setPrefix(this.parentStmt.getPrefix());

		parentStmt.children.add(st);
		DefaultMutableTreeNode child = new DefaultMutableTreeNode(st);

		this.parentNode.insert(child, 0);

		YangRepo.addStatement(st);

		((DefaultTreeModel) tree.getModel()).reload(this.parentNode);
	}

	private void addSubModule(String cmd) {
		// define map to dynamically handle text
		JTextField txtFile = new JTextField();
		JTextField txtName = new JTextField();
		comboInput.removeAllItems();

		List<Statement> ls = YangRepo.findStatementByK(YangKeyword.YK_MODULE);

		for (Statement st : ls) {
			comboInput.addItem(st.getValue());
		}

		final JComponent[] inputs = new JComponent[] { new JLabel("Name"), txtName, new JLabel("File"), txtFile,
				new JLabel("Belongs-to"), comboInput };

		int yes = JOptionPane.showConfirmDialog(null, inputs, "Add " + cmd, JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (yes != JOptionPane.OK_OPTION)
			return;

		String name = txtName.getText().trim();
		String file = txtFile.getText().trim();
		if (comboInput.getItemCount() <= 0) {
			return;
		}

		String module = comboInput.getSelectedItem().toString();

		if (file.equals("") || name.equals("") || module.equals("")) {
			return;
		}

		Statement m = YangRepo.findStatementByKV(YangKeyword.YK_MODULE, module);

		Statement top = this.addTopStatement(file, m.getPrefix());
		YangRepo.addStatement(top);

		DefaultMutableTreeNode subm = addTreeNode(cmd, name, file, m.getPrefix(), this.parentNode, top);
		DefaultMutableTreeNode belongsto = addTreeNode(YangKeyword.YK_BELONGS_TO, module, file, m.getPrefix(), subm,
				(Statement) subm.getUserObject());
		addTreeNode(YangKeyword.YK_PREFIX, m.getPrefix(), file, m.getPrefix(), belongsto,
				(Statement) belongsto.getUserObject());
	}

	String constructValueWithPrefix(Statement st) {
		if (st.getPrefix().equals(this.parentStmt.getPrefix()))
			return st.getValue();
		else
			return st.getPrefix() + ":" + st.getValue();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String cmd = e.getActionCommand();

		if (cmd.equals(YangKeyword.YK_MODULE)) {
			this.addModule(cmd);
			return;
		}

		if (cmd.equals(YangKeyword.YK_SUBMODULE)) {
			this.addSubModule(cmd);
			return;
		}

		if (cmd.equals(YangKeyword.YK_COMMENT)) {
			this.addComment();
			return;
		}

		txtInput.setText("");
		comboInput.removeAllItems();

		YangMetaMap.YTYPE t = YangMetaMap.metaMap.get(cmd);

		if (t == YangMetaMap.YTYPE.NONE) {
			// add node to tree
			addTreeNode(cmd, "", this.parentStmt.getFile(), this.parentStmt.getPrefix(), this.parentNode,
					(Statement) parentNode.getUserObject());
			return;
		}
		// if (t == YangGrammer.YTYPE.STRING || t == YangGrammer.YTYPE.XPATH ||
		// t == YangGrammer.YTYPE.NUMBER) {
		JComponent c = this.comboInput;

		if (t == null) {
			// handle extension
			// cmd="prefix:extname"
			String prefix = this.parentStmt.getPrefix();
			String ext = cmd;
			if (cmd.contains(":")) {
				String[] ss = cmd.split(":");
				prefix = ss[0];
				ext = ss[1];
			}

			Statement st = YangRepo.findStatementByKVP(YangKeyword.YK_EXTENSION, ext, prefix);
			boolean hasArgument = false;
			for (Statement sub : st.children) {
				if (sub.getKeyword().equals(YangKeyword.YK_ARGUMENT)) {
					hasArgument = true;
					break;
				}
			}
			if (!hasArgument) {
				addTreeNode(cmd, null, this.parentStmt.getFile(), this.parentStmt.getPrefix(),
						this.parentNode,
						(Statement) parentNode.getUserObject());
				return;
			} else {
				c = this.txtInput;
			}
		} else if (t == YangMetaMap.YTYPE.BOOL) {
			comboInput.addItem("true");
			comboInput.addItem("false");
		} else if (t == YangMetaMap.YTYPE.ENUM) {
			if (cmd.equals(YangKeyword.YK_USES)) {
				for (Statement st : YangRepo.findStatementByK(YangKeyword.YK_GROUPING)) {
					comboInput.addItem(st.getValue());
				}
			} else if (cmd.equals(YangKeyword.YK_BASE)) {
				for (Statement st : YangRepo.findStatementByK(YangKeyword.YK_IDENTITY)) {
					comboInput.addItem(constructValueWithPrefix(st));
				}
			} else if (cmd.equals(YangKeyword.YK_IF_FEATURE)) {
				for (Statement st : YangRepo.findStatementByK(YangKeyword.YK_FEATURE)) {
					comboInput.addItem(st.getValue());
				}
			} else if (cmd.equals(YangKeyword.YK_TYPE)) {
				for (String builtin : YangBuiltIn.substatement.keySet()) {
					comboInput.addItem(builtin);
				}
				for (Statement st : YangRepo.findStatementByK(YangKeyword.YK_TYPEDEF)) {
					comboInput.addItem(constructValueWithPrefix(st));
				}
			} else if (cmd.equals(YangKeyword.YK_ORDERED_BY)) {
				comboInput.addItem("system");
				comboInput.addItem("user");
			} else if (cmd.equals(YangKeyword.YK_STATUS)) {
				comboInput.addItem("current");
				comboInput.addItem("deprecated");
				comboInput.addItem("obsolete");
			}
		} else {
			c = this.txtInput;
		}
		// define map to dynamically handle text or combo
		// final JComponent[] inputs = new JComponent[] { new JLabel("Name"),
		// txtInput };

		int yes = JOptionPane.showConfirmDialog(null, c, "Add " + cmd, JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (yes != JOptionPane.OK_OPTION)
			return;

		String v = null;
		if (c == txtInput) {
			v = txtInput.getText().trim();
			if (quotaList.contains(cmd))
				v = "\"" + v + "\"";
		} else {
			v = this.comboInput.getSelectedItem().toString();
		}
		if (v == null || v.equals("\"\"")) {
			// JOptionPane.showInternalMessageDialog(tree.getRootPane(), cmd + "
			// need a input value");
		} else {
			addTreeNode(cmd, v, this.parentStmt.getFile(), this.parentStmt.getPrefix(), this.parentNode,
					(Statement) parentNode.getUserObject());
		}
	}
}
