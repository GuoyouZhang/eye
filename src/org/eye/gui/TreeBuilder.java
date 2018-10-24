package org.eye.gui;

import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

import org.eye.parser.Statement;
import org.eye.parser.YangKeyword;
import org.eye.parser.YangParser;
import org.eye.parser.YangRepo;

public class TreeBuilder {

	public static List<String> loadYangFile(String file, DefaultMutableTreeNode root,
			Map<Integer, DefaultMutableTreeNode> posMap) {

		List<String> err = YangParser.go(file);

		if (err.size() == 0) {
			loadTreeNode(YangRepo.findStatementByF(file), root, posMap);
		}
		return err;
	}

	private static void loadTreeNode(Statement pstmt, DefaultMutableTreeNode pnode,
			Map<Integer, DefaultMutableTreeNode> posMap) {
		for (Statement sub : pstmt.children) {
			DefaultMutableTreeNode cnode = new DefaultMutableTreeNode(sub);
			pnode.add(cnode);


			if (sub.children.size() > 0) {
				loadTreeNode(sub, cnode, posMap);
			}

			// comment will not overwrite normal yang element
			if (sub.getKeyword().equals(YangKeyword.YK_COMMENT) && posMap.get(sub.getLine()) != null)
				continue;

			posMap.put(sub.getLine(), cnode);
		}
	}

	public static void loadExtendedTree(DefaultMutableTreeNode parent,
			Statement stmt) {

		if (!stmt.getKeyword().equals(YangKeyword.YK_COMMENT) && stmt.getKeyword().equals(YangKeyword.YK_USES)) {
			String ss[] = stmt.getValue().split(":");
			String prefix = stmt.getPrefix();
			String gname = stmt.getValue();
			
			if(ss.length==2)
			{
				prefix = ss[0];
				gname = ss[1];
			}
			Statement group = YangRepo.findStatementByKVP(YangKeyword.YK_GROUPING, gname,
					prefix);
			if (group == null) {
				System.out.println("cannot find out grouping:" + stmt.getValue());
				return;
			}
			for (Statement sub : group.children) {
				loadExtendedTree(parent, sub);
			}
		} else {
			DefaultMutableTreeNode cnode = new DefaultMutableTreeNode(stmt);
			parent.add(cnode);
			for (Statement sub : stmt.children) {
				loadExtendedTree(cnode, sub);
			}
		}
	}
}
