package org.eye.parser;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eye.parser.Statement;
import org.eye.parser.YangKeyword;
import org.eye.parser.YangParser;
import org.eye.parser.YangRepo;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

class ExtendedNode {
	public ExtendedNode parent;
	public Statement current;
	// prefix for grouping will be changed based on who use it, so should not change
	// original prefix in Statement, which could be shared by multi uses
	public String prefix;
	public List<ExtendedNode> children = new ArrayList<ExtendedNode>();
}

class PrefixNode {
	public String module;
	public String ns;
	public String file;
	public String prefix;
}

public class NamespaceUtil {

	static Map<String, Statement> topStatements = new HashMap<String, Statement>();
	static Map<String, ExtendedNode> extendedTree = new HashMap<String, ExtendedNode>();
	static Map<String, PrefixNode> prefix2ns = new HashMap<String, PrefixNode>();

	public static void init(String yangFileFolder) {
		File dir = new File(yangFileFolder);
		if (!dir.isDirectory()) {
			System.out.println("Wrong yang file path:" + yangFileFolder);
			return;
		}
		// load all yang file and fill topStatements
		for (File file : dir.listFiles()) {
			if (file.getName().endsWith(".yang")) {
				List<String> err = YangParser.go(file.getAbsolutePath());
				if (err.size() > 0) {
					System.out.println(err);
				}
			}
		}
		for (Statement module : YangRepo.findStatementByK(YangKeyword.YK_MODULE)) {
			for (Statement sub : module.children) {
				if (sub.getKeyword().equals(YangKeyword.YK_CONTAINER) || sub.getKeyword().equals(YangKeyword.YK_LEAF)
						|| sub.getKeyword().equals(YangKeyword.YK_LEAF_LIST)) {
					// System.out.println(sub.getValue());
					if (topStatements.get(sub.getValue()) == null) {
						topStatements.put(sub.getValue(), sub);
						// System.out.println("add top level node " + sub.getValue());
					} else {
						Statement s = topStatements.get(sub.getValue());
						System.out.println("Duplicated top level node " + sub.getValue() + " defined in both "
								+ sub.getFile() + " and " + s.getFile());
					}
				}
			}
		}

		initPrefixMap();
	}

	private static void initPrefixMap() {
		List<Statement> modules = YangRepo.findStatementByK(YangKeyword.YK_MODULE);
		Map<String, PrefixNode> module2Prefix = new HashMap<String, PrefixNode>();
		for (Statement module : modules) {
			PrefixNode pn = new PrefixNode();
			pn.file = module.getFile();
			pn.module = module.getValue();
			for (Statement sub : module.children) {
				if (sub.getKeyword().equals(YangKeyword.YK_NAMESPACE)) {
					pn.ns = sub.getValue().replaceAll("\"", "");
				} else if (sub.getKeyword().equals(YangKeyword.YK_PREFIX)) {
					pn.prefix = sub.getValue();
				}
			}
			PrefixNode exist = prefix2ns.get(pn.prefix);
			if (exist == null) {
				prefix2ns.put(pn.prefix, pn);
				module2Prefix.put(pn.module, pn);
			} else {
				System.out.println("Duplicated prefix " + pn.prefix + " in both " + exist.file + " and " + pn.file);
			}
		}
		// the import->prefix could be different from the original defined in
		// module->prefix
		List<Statement> imps = YangRepo.findStatementByK(YangKeyword.YK_IMPORT);
		for (Statement imp : imps) {
			for (Statement sub : imp.children) {
				if (sub.getKeyword().equals(YangKeyword.YK_PREFIX)) {
					if (prefix2ns.get(sub.getValue()) == null) {
						PrefixNode exist = module2Prefix.get(imp.getValue());
						if (exist != null) {
							prefix2ns.put(sub.getValue(), exist);
						} else {
							System.out
									.println("Cannot find import->prefix " + sub.getValue() + " file=" + imp.getFile());
						}
					}
					break;
				}
			}
		}
	}

	private static String trimAugmentPath(String xpath) {
		String s = xpath.replace("\"", "");
		s = s.replaceAll("\\s*\\+\\s*", "");
		return s;
	}

	private static ExtendedNode findAugmentedNode(String xpath, ExtendedNode top) {
		// System.out.println(xpath);
		String[] ss = xpath.split("/");
		ExtendedNode n = top;
		for (int i = 0; i < ss.length; i++) {
			if (ss[i].equals(""))
				continue;
			// System.out.println(ss[i]);
			String[] prefixAndName = ss[i].split(":");
			String name = prefixAndName[prefixAndName.length - 1];
			boolean found = false;
			boolean choice = false;
			for (ExtendedNode child : n.children) {
				if (n.current != null && n.current.getKeyword().equals(YangKeyword.YK_CHOICE)) {
					if (!child.current.getKeyword().equals(YangKeyword.YK_CASE)) {
						// skip case level since it is not existed
						if (i < ss.length - 1) {
							i++;
							prefixAndName = ss[i].split(":");
							name = prefixAndName[prefixAndName.length - 1];
						}
					}
				}

				if (child.current.getValue().equals(name)) {
					n = child;
					found = true;
					break;
				}
			}
			if (!found) {
				return null;
			}
			if (i == ss.length - 1) {
				return n;
			}
		}
		return null;
	}

	static void skipChoice(ExtendedNode n) {
		List<ExtendedNode> tobeDeleted = new ArrayList<ExtendedNode>();
		for (ExtendedNode sub : n.children) {
			if (sub.current.getKeyword().equals(YangKeyword.YK_CHOICE)) {
				tobeDeleted.add(sub);
			}
		}
		for (ExtendedNode choiceItem : tobeDeleted) {
			n.children.remove(choiceItem);
			for (ExtendedNode childOfChoice : choiceItem.children) {
				if (childOfChoice.current.getKeyword().equals(YangKeyword.YK_CASE)) {
					for (ExtendedNode childOfCase : childOfChoice.children) {
						childOfCase.parent = n;
						n.children.add(childOfCase);
					}
				} else {
					childOfChoice.parent = n;
					n.children.add(childOfChoice);
				}
			}
		}
		for (ExtendedNode sub : n.children) {
			skipChoice(sub);
		}
	}

	public static ExtendedNode getExtendTree(String name) {
		if (extendedTree.get(name) != null)
			return extendedTree.get(name);

		Statement stmt = topStatements.get(name);
		ExtendedNode parent = new ExtendedNode();
		loadExtendedTree(parent, stmt, null);
		// keep the choice and case in the tree, because augment will use it
		List<Statement> ls = YangRepo.findStatementByK(YangKeyword.YK_AUGMENT);
		List<Statement> rest = new ArrayList<Statement>();

		for (Statement s : ls) {
			String xpath = trimAugmentPath(s.getValue());
			ExtendedNode augmented = findAugmentedNode(xpath, parent);
			if (augmented != null) {
				for (Statement sub : s.children) {
					loadExtendedTree(augmented, sub, null);
				}
			} else {
				if (xpath.startsWith("/" + stmt.getPrefix() + ":" + stmt.getValue())) {
					rest.add(s);
				}
			}
		}
		// some augment are based on other augment
		for (Statement s : rest) {
			String xpath = trimAugmentPath(s.getValue());
			ExtendedNode augmented = findAugmentedNode(xpath, parent);
			if (augmented != null) {
				for (Statement sub : s.children) {
					loadExtendedTree(augmented, sub, null);
				}
			} else {
				System.out.println("cannot find out augmented node for " + xpath);
			}
		}
		// now skip choise and case since it will not show up in runtime xml
		skipChoice(parent);
		extendedTree.put(name, parent);
		return parent;
	}

	private static void loadExtendedTree(ExtendedNode parent, Statement stmt, String usePrefix) {
		// System.out.println("handle " + stmt.getKeyword() + "=" + stmt.getValue());
		if (stmt.getKeyword().equals(YangKeyword.YK_LIST) || stmt.getKeyword().equals(YangKeyword.YK_CONTAINER)
				|| stmt.getKeyword().equals(YangKeyword.YK_LEAF) || stmt.getKeyword().equals(YangKeyword.YK_LEAF_LIST)
				|| stmt.getKeyword().equals(YangKeyword.YK_TYPE) || stmt.getKeyword().equals(YangKeyword.YK_ACTION)
				|| stmt.getKeyword().equals(YangKeyword.YK_INPUT) || stmt.getKeyword().equals(YangKeyword.YK_OUTPUT)
				|| stmt.getKeyword().equals(YangKeyword.YK_CHOICE) || stmt.getKeyword().equals(YangKeyword.YK_CASE)
				|| stmt.getKeyword().equals(YangKeyword.YK_USES)) {
			// System.out.println("handle " + stmt.getKeyword() + "=" + stmt.getValue());

			if (stmt.getKeyword().equals(YangKeyword.YK_USES)) {
				String ss[] = stmt.getValue().split(":");
				String prefix = stmt.getPrefix();
				String gname = stmt.getValue();

				if (ss.length == 2) {
					prefix = ss[0];
					gname = ss[1];
				}
				// the prefix may be different from original one
				PrefixNode pn = prefix2ns.get(prefix);
				Statement group = YangRepo.findStatementByKVP(YangKeyword.YK_GROUPING, gname, pn.prefix);
				if (group == null) {
					System.out.println("cannot find out grouping " + stmt.getValue());
					System.out.println("prefix=" + prefix + ", name=" + gname + ", file=" + stmt.getFile());
					return;
				}
				for (Statement sub : group.children) {
					loadExtendedTree(parent, sub, stmt.getPrefix());
				}
			} else {
				ExtendedNode cnode = new ExtendedNode();
				cnode.current = stmt;
				cnode.prefix = stmt.getPrefix();
				if (usePrefix != null) {
					cnode.prefix = usePrefix;
				}
				cnode.parent = parent;
				parent.children.add(cnode);
				for (Statement sub : stmt.children) {
					loadExtendedTree(cnode, sub, usePrefix);
				}
			}
		}
	}

	public static void printTree(ExtendedNode n, int level) {
		for (ExtendedNode child : n.children) {
			if (child.current.getKeyword().equals(YangKeyword.YK_TYPE))
				continue;
			for (int i = 0; i < level; i++)
				System.out.print("- ");
			String prefix = "";
			if (!child.prefix.equals(child.parent.prefix))
				prefix = child.prefix + ":";
			System.out.println(child.current.getKeyword() + "=" + prefix + child.current.getValue());
			printTree(child, level + 1);
		}
	}

	private static boolean isIdentifyref(Statement s) {
		for (Statement t : s.children) {
			if (t.getKeyword().equals(YangKeyword.YK_TYPE)) {
				return t.getValue().equals("identityref");
			}
		}
		return false;
	}

	private static void addNsToElement(Element ele, ExtendedNode n) {
		// System.out.println(ele.getTagName());
		NodeList ls = ele.getChildNodes();
		ExtendedNode next = null;
		for (ExtendedNode node : n.children) {
			if (node.current.getValue().equals(ele.getTagName())) {
				if (!node.prefix.equals(node.parent.prefix)) {
					PrefixNode pre = prefix2ns.get(node.prefix);
					ele.setAttribute("xmlns", pre.ns);
				}
				if (isIdentifyref(node.current)) {
					String value = ele.getTextContent();
					Statement id = YangRepo.findStatementByKV(YangKeyword.YK_IDENTITY, value);
					if (id == null) {
						System.out.println("fail to find ns for " + value);
					}
					if (!id.getPrefix().equals(node.prefix)) {
						PrefixNode pre = prefix2ns.get(id.getPrefix());
						String trim = id.getPrefix().replaceAll("\"", "");
						ele.setAttribute("xmlns:" + trim, pre.ns);
						ele.setTextContent(trim + ":" + value);
					}
				}
				next = node;
				break;
			}
		}
		if (next == null) {
			System.out.println("Fail to find ns for " + ele.getTagName());
			return;
		}
		for (int i = 0; i < ls.getLength(); i++) {
			if (ls.item(i) instanceof Element) {
				addNsToElement((Element) ls.item(i), next);
			}
		}
	}

	public static String addNsToXml(String input) throws Exception {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader("<root>" + input + "</root>"));
		Document doc = builder.parse(is);
		NodeList ls = doc.getDocumentElement().getChildNodes();

		for (int i = 0; i < ls.getLength(); i++) {
			if (ls.item(i) instanceof Element) {
				Element top = (Element) ls.item(i);
				ExtendedNode n = NamespaceUtil.getExtendTree(top.getTagName());
				addNsToElement(top, n);
			}
		}

		TransformerFactory transFactory = TransformerFactory.newInstance();
		Transformer transformer = transFactory.newTransformer();
		StringWriter buffer = new StringWriter();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		transformer.transform(new DOMSource(doc.getDocumentElement()), new StreamResult(buffer));
		return buffer.toString().replace("<root>", "").replaceAll("</root>", "");
	}

	public static void main(String[] args) {
		NamespaceUtil.init("yang file folder");

		//ExtendedNode n = NamespaceUtil.getExtendTree("interfaces");
		//ExtendedNode n = NamespaceUtil.getExtendTree("hardware");
		//NamespaceUtil.printTree(n, 0);

		StringBuilder sb = new StringBuilder();
		
		sb.append("<interfaces>");
		sb.append("<interface>");
		sb.append("</interface>");
		sb.append("</interfaces>");

		try {
			System.out.println(sb.toString());
			System.out.println(addNsToXml(sb.toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
