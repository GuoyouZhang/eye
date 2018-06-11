package org.eye.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.eye.parser.FileUtil;
import org.eye.parser.Statement;
import org.eye.parser.YangGenerator;
import org.eye.parser.YangKeyword;
import org.eye.parser.YangRepo;
import org.eye.parser.YangValidator;

class MenuStruct {
	public JMenuItem item;
	public String action;
	public String icon;
	public JMenu menu;

	public MenuStruct(JMenu menu, JMenuItem item, String action, String icon) {
		this.menu = menu;
		this.item = item;
		this.action = action;
		this.icon = icon;
	}
}

public class EyeGui extends JFrame implements ActionListener, MouseListener, DocumentListener {

	private JTabbedPane tabTop;
	private JPopupMenu menuPopup = new JPopupMenu();
	// system menu
	private JMenuItem menuItemLoadYang = new JMenuItem("Load YANG files");
	private JMenuItem menuItemCloseYang = new JMenuItem("Unload All YANG files");
	private JMenuItem menuItemExit = new JMenuItem("Exit");

	// find menu
	private JMenuItem menuItemFindKey = new JMenuItem("Find by YANG keyword");

	// Help Menu
	private JMenuItem menuItemAbout = new JMenuItem("About");

	// popup
	private JMenuItem menuItemGotoTree = new JMenuItem("Go To Tree");
	private JMenuItem menuItemGotoLine = new JMenuItem("Go To Line");
	private JMenuItem menuItemGenYang = new JMenuItem("Save YANG file");
	private JMenuItem menuItemExtendTree = new JMenuItem("Extend Tree");
	private JMenuItem menuItemDelete = new JMenuItem("Delete");
	private JMenuItem menuItemUnloadYang = new JMenuItem("Unload YANG module");

	// tree
	private JTree treeYang = new JTree();
	private JTree treeExtended = new JTree();
	private JTextArea textYang = new JTextArea();
	private boolean textChanged = false;

	private JTextArea textLog = new JTextArea();

	// logic
	private DefaultMutableTreeNode treeRootYang = new DefaultMutableTreeNode("Root");
	private DefaultMutableTreeNode treeRootExtended = new DefaultMutableTreeNode("Root");
	private DefaultMutableTreeNode currentYangTreeNode = null;

	private String currentFileInTextArea = null;
	private Component currentGuiComponent = null;

	private Map<String, Map<Integer, DefaultMutableTreeNode>> file2pos = new HashMap<String, Map<Integer, DefaultMutableTreeNode>>();

	private AddMenuHandler addMenuHandler = null;
	private MoveMenuHandler moveMenuHandler = null;

	public EyeGui() {
		// setup main window
		this.getContentPane().setLayout(new BorderLayout());
		this.setTitle("Easy Yang Editor");
		// setup menu
		List<MenuStruct> menus = new ArrayList<MenuStruct>();

		JMenu mu_system = new JMenu("System");
		menus.add(new MenuStruct(mu_system, menuItemLoadYang, "AC_LOAD_YANG", "/images/load.png"));
		menus.add(new MenuStruct(mu_system, menuItemCloseYang, "AC_CLOSE_YANG", "/images/delete.png"));
		menus.add(new MenuStruct(mu_system, menuItemExit, "AC_EXIT", "/images/exit.png"));

		JMenu mu_find = new JMenu("Find");
		menus.add(new MenuStruct(mu_find, menuItemFindKey, "AC_FIND_KEY", "/images/find.png"));

		JMenu mu_help = new JMenu("Help");
		menus.add(new MenuStruct(mu_help, menuItemAbout, "AC_ABOUT", "/images/help.png"));

		// for popup menu
		menus.add(new MenuStruct(null, menuItemGotoTree, "AC_GOTO_TREE", "/images/find.png"));
		menus.add(new MenuStruct(null, menuItemGotoLine, "AC_GOTO_LINE", "/images/find.png"));
		menus.add(new MenuStruct(null, menuItemGenYang, "AC_GEN_YANG", "/images/save.png"));
		menus.add(new MenuStruct(null, menuItemDelete, "AC_DELETE_YANG", "/images/delete.png"));
		menus.add(new MenuStruct(null, menuItemUnloadYang, "AC_UNLOAD_YANG", "/images/delete.png"));
		menus.add(new MenuStruct(null, menuItemExtendTree, "AC_EXTEND_TREE", "/images/extend.png"));

		for (MenuStruct ms : menus) {
			if (ms.menu != null)
				ms.menu.add(ms.item);
			ms.item.setActionCommand(ms.action);
			// System.out.println(ms.icon);
			ms.item.setIcon(new ImageIcon(ClassLoader.class.getResource(ms.icon)));
			ms.item.addActionListener(this);
		}

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(mu_system);
		menuBar.add(mu_find);
		menuBar.add(mu_help);

		this.setJMenuBar(menuBar);
		// setup toolbar
		JToolBar toolBar = new JToolBar();
		JButton clearButton = new JButton(new ImageIcon(ClassLoader.class.getResource("/images/clean.png")));
		clearButton.setActionCommand("AC_CLEAN_LOG");
		clearButton.addActionListener(this);
		clearButton.setToolTipText("Clean Log Window");
		JButton validateButton = new JButton(new ImageIcon(ClassLoader.class.getResource("/images/validate.png")));
		validateButton.setActionCommand("AC_VALIDATE_YANG");
		validateButton.addActionListener(this);
		validateButton.setToolTipText("Validate Yang File");
		toolBar.add(clearButton);
		toolBar.add(validateButton);
		toolBar.setBorderPainted(true);
		toolBar.setFloatable(true);

		JPanel north = new JPanel(new BorderLayout());
		north.add(toolBar);
		this.add(north, BorderLayout.NORTH);

		// set main body
		JScrollPane jspYang = new JScrollPane(this.treeYang);
		JScrollPane jspExtend = new JScrollPane(this.treeExtended);

		tabTop = new JTabbedPane();
		tabTop.addTab("YangTree", jspYang);
		tabTop.addTab("ExtendedTree", jspExtend);

		JScrollPane paneYang = new JScrollPane(this.textYang);

		textYang.setBackground(WindowUtil.NICE_COLOR);
		textLog.setBackground(WindowUtil.NICE_COLOR);
		TextLineNumber tln = new TextLineNumber(textYang);
		paneYang.setRowHeaderView(tln);
		textYang.addMouseListener(this);
		textYang.getDocument().addDocumentListener(this);

		JSplitPane splitHorizontal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabTop, paneYang);
		splitHorizontal.setOneTouchExpandable(true);
		splitHorizontal.setDividerLocation(300);

		JScrollPane logpanel = new JScrollPane(this.textLog);
		JSplitPane splitVertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitHorizontal, logpanel);
		splitVertical.setOneTouchExpandable(true);
		splitVertical.setDividerLocation(600);
		this.add(splitVertical, BorderLayout.CENTER);

		initTree();

		addMenuHandler = new AddMenuHandler(this.treeYang);
		moveMenuHandler = new MoveMenuHandler(this.treeYang);

		WindowUtil.setWindowIcon(this, null);
		WindowUtil.setWindowPos(this, true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void cleanTree(JTree tree, DefaultMutableTreeNode treeRoot) {
		treeRoot.removeAllChildren();
		reloadTreeNode(tree, treeRoot);
	}

	private void initTree() {
		treeYang.setModel(new DefaultTreeModel(treeRootYang));
		treeYang.setCellRenderer(new TreeRenderer());
		treeYang.setRootVisible(false);
		treeYang.addMouseListener(this);
		treeYang.setBackground(WindowUtil.NICE_COLOR);
		ToolTipManager.sharedInstance().registerComponent(treeYang);

		treeExtended.setModel(new DefaultTreeModel(treeRootExtended));
		treeExtended.setCellRenderer(new TreeRenderer());
		treeExtended.setRootVisible(false);
		treeExtended.setBackground(WindowUtil.NICE_COLOR);
		treeExtended.addMouseListener(this);
		ToolTipManager.sharedInstance().registerComponent(treeExtended);
	}

	public void reloadTreeNode(JTree tree, DefaultMutableTreeNode node) {
		((DefaultTreeModel) tree.getModel()).reload(node);
	}

	private DefaultMutableTreeNode getTreeNodeByLine(int line) {
		Map<Integer, DefaultMutableTreeNode> posMap = file2pos.get(this.currentFileInTextArea);

		for (int i = 1; i < 100; i++) {
			int pos = line + i;
			if (posMap.get(pos) != null) {
				return posMap.get(pos);
			}
			pos = line + 1 - i;
			if (posMap.get(pos) != null) {
				return posMap.get(pos);
			}
		}
		return null;
	}

	private int getYangTextLine() {
		int offset = textYang.getCaretPosition();
		try {
			return textYang.getLineOfOffset(offset);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return -1;
	}

	private void expandTreeLevel(JTree tree, DefaultMutableTreeNode root, int level) {
		DefaultMutableTreeNode currentNode = root.getNextNode();
		do {
			if (currentNode.getLevel() <= level)
				tree.expandPath(new TreePath(currentNode.getPath()));
			currentNode = currentNode.getNextNode();
		} while (currentNode != null);
	}

	private void findStatement() {
		String key = JOptionPane.showInputDialog("Input a YANG keyword, e.g. must");
		if (key == null || key.equals(""))
			return;
		List<Statement> ls = YangRepo.findStatementByK(key);
		this.textLog.append("------found " + key + ": " + ls.size() + " times------\n");

		for (Statement st : ls) {
			this.textLog
					.append(FileUtil.getNameOfFile(st.getFile()) + ":" + st.getLine() + "  " + st.getValue() + "\n");
		}
	}

	void saveYangFile() {
		String fname, content;

		if (this.currentGuiComponent == textYang) {
			fname = currentFileInTextArea;
			content = textYang.getText();
		} else {
			Statement stmt = (Statement) currentYangTreeNode.getUserObject();
			fname = stmt.getFile();
			content = YangGenerator.generateFile(currentYangTreeNode);
		}

		FileUtil.writeFile(fname, content);
		this.textLog.append("Save yang file " + fname + "\n");
		for (int i = 0; i < this.treeRootYang.getChildCount(); i++) {
			DefaultMutableTreeNode fnode = (DefaultMutableTreeNode) treeRootYang.getChildAt(i);
			Statement st = (Statement) fnode.getUserObject();
			if (st.getFile().equals(fname)) {
				treeRootYang.remove(fnode);
				break;
			}
		}
		// reload tree
		Map<Integer, DefaultMutableTreeNode> posMap = new HashMap<Integer, DefaultMutableTreeNode>();
		List<String> err = TreeBuilder.loadYangFile(fname, this.treeRootYang, posMap);
		this.reloadTreeNode(this.treeYang, treeRootYang);
		for (String info : err)
			textLog.append(info + "\n");
		if (this.currentGuiComponent != textYang) {
			textYang.setText(FileUtil.readFile(fname));
			this.currentFileInTextArea = fname;
			this.textChanged = false;
		}
		this.file2pos.put(fname, posMap);

		this.textLog.append("Tree view refreshed\n");
	}

	/**
	 * Invoked when an action occurs.
	 */
	public void actionPerformed(ActionEvent e) {

		String command = e.getActionCommand();
		if ("AC_EXIT".equals(command)) {
			System.exit(0);
		} else if ("AC_LOAD_YANG".equals(command)) {
			JFileChooser dlg = new JFileChooser(".");

			dlg.setName("Load YANG File");
			dlg.setMultiSelectionEnabled(true);
			String postfix[] = { ".yang" };
			dlg.setFileFilter(new FileFilterImpl(postfix));
			if (dlg.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				File[] files = dlg.getSelectedFiles();
				for (File f : files) {
					// String fname = dlg.getSelectedFile().getPath();
					String fname = f.getAbsolutePath();
					if (fname != null && !fname.trim().equals("")) {
						textLog.append("loading file:" + fname + "\n");
						Map<Integer, DefaultMutableTreeNode> posMap = new HashMap<Integer, DefaultMutableTreeNode>();
						List<String> err = TreeBuilder.loadYangFile(fname, this.treeRootYang, posMap);
						this.reloadTreeNode(this.treeYang, treeRootYang);
						for (String info : err)
							textLog.append(info + "\n");
						textYang.setText(FileUtil.readFile(fname));
						this.currentFileInTextArea = fname;
						this.file2pos.put(fname, posMap);
					}
				}
				this.textChanged = false;
			}
		} else if ("AC_GOTO_TREE".equals(command)) {
			int l = this.getYangTextLine();
			if (l == -1)
				return;

			treeYang.removeSelectionPaths(treeYang.getSelectionPaths());
			DefaultMutableTreeNode n = this.getTreeNodeByLine(l);

			treeYang.addSelectionPath(new TreePath(n.getPath()));
			treeYang.scrollPathToVisible(treeYang.getSelectionPath());
		} else if ("AC_GOTO_LINE".equals(command)) {
			String line = JOptionPane.showInputDialog("Input line number");
			if (line == null || line.equals(""))
				return;
			try {
				int l = Integer.valueOf(line);
				int pos = textYang.getLineStartOffset(l - 1);
				int end = textYang.getLineStartOffset(l);
				textYang.setCaretPosition(pos);
				textYang.requestFocus();
				this.highLight(pos, end);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		} else if ("AC_EXTEND_TREE".equals(command)) {
			this.cleanTree(this.treeExtended, this.treeRootExtended);
			Statement stmt = (Statement) currentYangTreeNode.getUserObject();
			TreeBuilder.loadExtendedTree(this.treeRootExtended, stmt);
			this.reloadTreeNode(this.treeExtended, this.treeRootExtended);
			this.expandTreeLevel(this.treeExtended, this.treeRootExtended, 1);
			tabTop.setSelectedIndex(1);
		} else if ("AC_CLOSE_YANG".equals(command)) {
			this.cleanTree(this.treeYang, this.treeRootYang);
			YangRepo.clean();
			textYang.setText("");
			textChanged = false;
		} else if ("AC_FIND_KEY".equals(command)) {
			this.findStatement();
		} else if ("AC_CLEAN_LOG".equals(command)) {
			this.textLog.setText("");
		} else if ("AC_DELETE_YANG".equals(command)) {
			Statement stmt = (Statement) currentYangTreeNode.getUserObject();
			DefaultMutableTreeNode pnode = (DefaultMutableTreeNode) currentYangTreeNode.getParent();
			pnode.remove(currentYangTreeNode);
			Statement pst = (Statement) pnode.getUserObject();
			if (pst != null) {
				pst.children.remove(stmt);
			}
			YangRepo.delete(stmt);
			this.reloadTreeNode(this.treeYang, pnode);
			currentYangTreeNode = null;
		} else if ("AC_GEN_YANG".equals(command)) {
			saveYangFile();
		} else if ("AC_VALIDATE_YANG".equals(command)) {
			textLog.append("\n-------validate-------\n");
			for (String err : YangValidator.validate()) {
				textLog.append(err);
				textLog.append("\n");
			}
		} else if ("AC_UNLOAD_YANG".equals(command)) {
			Statement st = (Statement) this.currentYangTreeNode.getUserObject();
			treeRootYang.remove(currentYangTreeNode);
			currentYangTreeNode = null;
			YangRepo.delete(st);
			textYang.setText("");
			textChanged = false;
			this.reloadTreeNode(this.treeYang, treeRootYang);
		} else if ("AC_ABOUT".equals(command)) {
			String s = "\t Easy YANG Editor 1.0\n Make your life easier:) \n\n";
			ImageIcon icon = new ImageIcon(ClassLoader.class.getResource("/images/eyelogo.png"));
			JOptionPane.showMessageDialog(this, s, "About Easy YANG Editor", JOptionPane.INFORMATION_MESSAGE, icon);
		}
	}

	private void highLight(int start, int end) {

		Highlighter highLighter = textYang.getHighlighter();
		highLighter.removeAllHighlights();
		DefaultHighlighter.DefaultHighlightPainter p = new DefaultHighlighter.DefaultHighlightPainter(Color.GREEN);

		try {
			highLighter.addHighlight(start, end, p);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	// mouse events
	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {

		menuPopup.removeAll();
		this.currentGuiComponent = e.getComponent();
		if (e.getComponent() == treeYang) {
			TreePath path = treeYang.getPathForLocation(e.getX(), e.getY());
			if (path == null) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					menuPopup.add(addMenuHandler.findAddMenus(this.treeRootYang));
					menuPopup.pack();
					menuPopup.show(treeYang, e.getX(), e.getY());
				}
				return;
			}
			currentYangTreeNode = (DefaultMutableTreeNode) path.getPathComponent(path.getPathCount() - 1);
			Statement ytn = (Statement) currentYangTreeNode.getUserObject();
			locateYangText(ytn);

			if (e.getButton() == MouseEvent.BUTTON3) { // show menu
				if (ytn.getKeyword().equals(YangKeyword.YK_CONTAINER)
						&& currentYangTreeNode.getParent().getParent() == this.treeRootYang) {
					menuPopup.add(menuItemExtendTree);
				}
				if (ytn.getKeyword().equals(YangKeyword.YK_MODULE)
						|| ytn.getKeyword().equals(YangKeyword.YK_SUBMODULE)) {
					menuPopup.add(menuItemGenYang);
					menuPopup.add(menuItemUnloadYang);
				}
				JMenu m = addMenuHandler.findAddMenus(currentYangTreeNode);
				if (m.getItemCount() > 0) {
					menuPopup.add(m);
				}
				if (!ytn.getKeyword().equals(YangKeyword.YK_MODULE)
						&& !ytn.getKeyword().equals(YangKeyword.YK_SUBMODULE)) {
					menuPopup.add(menuItemDelete);
				}
				m = moveMenuHandler.findMoveMenus(currentYangTreeNode);
				if (m.getItemCount() > 0) {
					menuPopup.add(m);
				}
				menuPopup.pack();
				menuPopup.show(treeYang, e.getX(), e.getY());
			}
		} else if (e.getComponent() == treeExtended) {
			TreePath path = treeExtended.getPathForLocation(e.getX(), e.getY());
			if (path == null) {
				return;
			}
			currentYangTreeNode = (DefaultMutableTreeNode) path.getPathComponent(path.getPathCount() - 1);
			Statement ytn = (Statement) currentYangTreeNode.getUserObject();
			locateYangText(ytn);
		} else if (e.getComponent() == textYang) {
			if (e.getButton() == MouseEvent.BUTTON3) { // show menu
				menuPopup.add(menuItemGotoTree);
				menuPopup.add(menuItemGotoLine);
				menuPopup.add(menuItemGenYang);
				menuPopup.pack();
				menuPopup.show(textYang, e.getX(), e.getY());
			}
		}
	}

	private void locateYangText(Statement ytn) {
		try {
			if (ytn.getLine() >= 0) {
				if (!ytn.getFile().equals(this.currentFileInTextArea)) {
					if (this.textChanged) {
						int opt = JOptionPane.showConfirmDialog(this,
								"The YANG file has been changed. Do you want to save it before navigating?");
						if (opt == JOptionPane.CANCEL_OPTION) {
							return;
						} else if (opt == JOptionPane.YES_OPTION) {
							this.currentGuiComponent = textYang;
							this.saveYangFile();
						}
					}
					this.currentFileInTextArea = ytn.getFile();
					this.textYang.setText(FileUtil.readFile(ytn.getFile()));
					this.textChanged = false;
				}
				int pos = textYang.getLineStartOffset(ytn.getLine() - 1);
				textYang.setCaretPosition(pos);
				textYang.requestFocus();
				if (ytn.getKeyword().equals(YangKeyword.YK_COMMENT)) {
					pos = textYang.getLineStartOffset(ytn.getLine() - 1);
					String s = textYang.getText();
					int start = s.indexOf(ytn.getText(), pos);
					int end = start + ytn.getText().length();
					this.highLight(start, end);
				}

				int end = TextLocator.locate(ytn, textYang.getText(), pos);
				if (end > 0) {
					this.highLight(pos, end);
				}
			}
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				EyeGui f = new EyeGui();
				f.setVisible(true);
			}
		});
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		textChanged = true;
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		textChanged = true;
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		textChanged = true;
	}

}
