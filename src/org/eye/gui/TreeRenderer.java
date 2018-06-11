package org.eye.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

import org.eye.parser.Statement;
import org.eye.parser.YangKeyword;

public class TreeRenderer extends JLabel implements TreeCellRenderer {

	public TreeRenderer() {
	}

	protected boolean selected;

	public Component getTreeCellRendererComponent(JTree tree, Object obj, boolean flag, boolean flag1, boolean flag2,
			int arg5, boolean flag3) {

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;

		Object userObj = node.getUserObject();

		if (userObj instanceof Statement) {

			Statement ytn = (Statement) userObj;
			this.setText(ytn.toString());

			if (ytn.getKeyword().equals(YangKeyword.YK_COMMENT)) {
				this.setIcon(new ImageIcon(ClassLoader.class.getResource("/images/comment.png")));
			} else if (ytn.getKeyword().equals(YangKeyword.YK_PREFIX)
					|| ytn.getKeyword().equals(YangKeyword.YK_NAMESPACE)
					|| ytn.getKeyword().equals(YangKeyword.YK_ORGANIZATION)
					|| ytn.getKeyword().equals(YangKeyword.YK_CONTACT)) {
				this.setIcon(new ImageIcon(ClassLoader.class.getResource("/images/meta.png")));
			} else if (ytn.getKeyword().equals(YangKeyword.YK_INCLUDE) || ytn.getKeyword().equals(YangKeyword.YK_IMPORT)
					|| ytn.getKeyword().equals(YangKeyword.YK_USES) || ytn.getKeyword().equals(YangKeyword.YK_REFERENCE)
					|| ytn.getKeyword().equals(YangKeyword.YK_PATH)) {
				this.setIcon(new ImageIcon(ClassLoader.class.getResource("/images/link.png")));
			} else if (ytn.getKeyword().equals(YangKeyword.YK_LEAF)) {
				this.setIcon(new ImageIcon(ClassLoader.class.getResource("/images/leaf.png")));
			} else if (ytn.getKeyword().equals(YangKeyword.YK_LIST)) {
				this.setIcon(new ImageIcon(ClassLoader.class.getResource("/images/list.png")));
			} else if (ytn.getKeyword().equals(YangKeyword.YK_LEAF_LIST)) {
				this.setIcon(new ImageIcon(ClassLoader.class.getResource("/images/leaflist.png")));
			} else if (ytn.getKeyword().equals(YangKeyword.YK_MODULE)
					|| ytn.getKeyword().equals(YangKeyword.YK_SUBMODULE)) {
				this.setIcon(new ImageIcon(ClassLoader.class.getResource("/images/module.png")));
			} else if (ytn.getKeyword().equals(YangKeyword.YK_ACTION)) {
				this.setIcon(new ImageIcon(ClassLoader.class.getResource("/images/cmd.png")));
			} else if (ytn.getKeyword().equals(YangKeyword.YK_DESCRIPTION)) {
				this.setIcon(new ImageIcon(ClassLoader.class.getResource("/images/help.png")));
			} else if (ytn.getKeyword().equals(YangKeyword.YK_GROUPING)) {
				this.setIcon(new ImageIcon(ClassLoader.class.getResource("/images/group.png")));
			} else if (ytn.getKeyword().equals(YangKeyword.YK_KEY)) {
				this.setIcon(new ImageIcon(ClassLoader.class.getResource("/images/key.png")));
			} else if (ytn.getKeyword().equals(YangKeyword.YK_TYPE)) {
				this.setIcon(new ImageIcon(ClassLoader.class.getResource("/images/type.png")));
			} else if (ytn.getKeyword().equals(YangKeyword.YK_TYPEDEF)) {
				this.setIcon(new ImageIcon(ClassLoader.class.getResource("/images/typedef.png")));
			} else if (ytn.getKeyword().equals(YangKeyword.YK_ERROR_MESSAGE)) {
				this.setIcon(new ImageIcon(ClassLoader.class.getResource("/images/error.png")));
			} else if (ytn.getKeyword().equals(YangKeyword.YK_CONTAINER)) {
				this.setIcon(new ImageIcon(ClassLoader.class.getResource("/images/container.png")));
			} else if (ytn.getKeyword().equals(YangKeyword.YK_CONFIG) || ytn.getKeyword().equals(YangKeyword.YK_DEFAULT)
					|| ytn.getKeyword().equals(YangKeyword.YK_MANDATORY)
					|| ytn.getKeyword().equals(YangKeyword.YK_LENGTH) || ytn.getKeyword().equals(YangKeyword.YK_MAX)
					|| ytn.getKeyword().equals(YangKeyword.YK_MIN)
					|| ytn.getKeyword().equals(YangKeyword.YK_MAX_ELEMENTS)
					|| ytn.getKeyword().equals(YangKeyword.YK_MIN_ELEMENTS)
					|| ytn.getKeyword().equals(YangKeyword.YK_PRESENCE)) {
				this.setIcon(new ImageIcon(ClassLoader.class.getResource("/images/property.png")));
			} else if (ytn.getKeyword().equals(YangKeyword.YK_RANGE)) {
				this.setIcon(new ImageIcon(ClassLoader.class.getResource("/images/range.png")));
			} else if (ytn.getKeyword().equals(YangKeyword.YK_PATTERN)
					|| ytn.getKeyword().equals(YangKeyword.YK_MUST)) {
				this.setIcon(new ImageIcon(ClassLoader.class.getResource("/images/eye.png")));
			} else if (ytn.getKeyword().equals(YangKeyword.YK_INPUT)) {
				this.setIcon(new ImageIcon(ClassLoader.class.getResource("/images/input.png")));
			} else if (ytn.getKeyword().equals(YangKeyword.YK_OUTPUT)) {
				this.setIcon(new ImageIcon(ClassLoader.class.getResource("/images/output.png")));
			} else if (ytn.getKeyword().equals(YangKeyword.YK_ENUM)) {
				this.setIcon(new ImageIcon(ClassLoader.class.getResource("/images/enum.png")));
			} else if (ytn.getKeyword().equals(YangKeyword.YK_AUGMENT)
					|| ytn.getKeyword().equals(YangKeyword.YK_REFINE)) {
				this.setIcon(new ImageIcon(ClassLoader.class.getResource("/images/attach.png")));
			} else if (ytn.getKeyword().equals(YangKeyword.YK_IDENTITY)) {
				this.setIcon(new ImageIcon(ClassLoader.class.getResource("/images/identity.png")));
			} else if (ytn.getKeyword().equals(YangKeyword.YK_FEATURE)) {
				this.setIcon(new ImageIcon(ClassLoader.class.getResource("/images/feature.png")));
			} else if (ytn.getKeyword().equals(YangKeyword.YK_YANG_VERSION)) {
				this.setIcon(new ImageIcon(ClassLoader.class.getResource("/images/version.png")));
			} else if (ytn.getKeyword().equals(YangKeyword.YK_REVISION)
					|| ytn.getKeyword().equals(YangKeyword.YK_REVISION_DATE)) {
				this.setIcon(new ImageIcon(ClassLoader.class.getResource("/images/revision.png")));

			} else {
				this.setIcon(new ImageIcon(ClassLoader.class.getResource("/images/default.png")));
			}

			selected = flag;

			setOpaque(true);

			if (flag) {

				setBackground(Color.GREEN);

				setForeground(Color.black);

			} else {

				setForeground(Color.black);

				setBackground(WindowUtil.NICE_COLOR);
			}

			return this;

			// return ytn.check;
		}

		return this;
	}

}
