package org.eye.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.eye.parser.Statement;

public class MoveMenuHandler implements ActionListener {
	public static final String MOVE_UP = "Up";
	public static final String MOVE_DOWN = "Down";
	public static final String MOVE_FIRST = "To the first";
	public static final String MOVE_LAST = "To the last";


	JMenu moveMenu = new JMenu("Move");

	JMenuItem moveUp = new JMenuItem(MOVE_UP);
	JMenuItem moveDown = new JMenuItem(MOVE_DOWN);
	JMenuItem moveFirst = new JMenuItem(MOVE_FIRST);
	JMenuItem moveLast = new JMenuItem(MOVE_LAST);

	JTree tree;
	DefaultMutableTreeNode currentNode;
	Statement parentStmt;

	public MoveMenuHandler(JTree tree) {
		this.tree = tree;
		moveUp.addActionListener(this);
		moveDown.addActionListener(this);
		moveFirst.addActionListener(this);
		moveLast.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) currentNode.getParent();
		int index = parent.getIndex(currentNode);
		parent.remove(currentNode);
		if (e.getSource() == moveUp) {
			parent.insert(currentNode, index - 1);
		} else if (e.getSource() == moveDown) {
			parent.insert(currentNode, index + 1);
		} else if (e.getSource() == moveFirst) {
			parent.insert(currentNode, 0);
		} else if (e.getSource() == moveLast) {
			parent.add(currentNode);
		}
		((DefaultTreeModel) tree.getModel()).reload(parent);
	}

	public JMenu findMoveMenus(DefaultMutableTreeNode node) {
		currentNode = node;
		moveMenu.removeAll();
		if (node.getParent().getChildCount() > 1) {
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
			if (parent.getFirstChild() == node) {
				moveMenu.add(moveDown);
				moveMenu.add(moveLast);
			} else if (parent.getLastChild() == node) {
				moveMenu.add(moveUp);
				moveMenu.add(moveFirst);
			} else {
				moveMenu.add(moveUp);
				moveMenu.add(moveFirst);
				moveMenu.add(moveDown);
				moveMenu.add(moveLast);
			}
		}
		return moveMenu;
	}
}
