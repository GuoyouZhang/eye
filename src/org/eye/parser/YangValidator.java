package org.eye.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YangValidator {

	private static Map<String, List<Statement>> indexMap = new HashMap<String, List<Statement>>();
	private static final List<String> indexItem = new ArrayList<String>();

	static {
		indexItem.add(YangKeyword.YK_EXTENSION);
		indexItem.add(YangKeyword.YK_GROUPING);
		indexItem.add(YangKeyword.YK_TYPEDEF);
		indexItem.add(YangKeyword.YK_IDENTITY);
		indexItem.add(YangKeyword.YK_FEATURE);
		indexItem.add(YangKeyword.YK_MODULE);
		indexItem.add(YangKeyword.YK_SUBMODULE);
		indexItem.add(YangKeyword.YK_PREFIX);
	}

	public static List<String> validate() {
		List<String> err = new ArrayList<String>();
		indexMap.clear();
		for (String item : indexItem) {
			indexMap.put(item, YangRepo.findStatementByK(item));
		}

		for (String f : YangRepo.getAllFiles()) {
			Statement st = YangRepo.findStatementByF(f);
			validateOneFile(st, err);
		}
		return err;
	}

	private static boolean exist(String key, String value) {
		List<Statement> buffer = indexMap.get(key);
		if (buffer == null)
			return true;

		if (value.contains(":")) {
			String ss[] = value.split(":");
			for (Statement st : buffer) {
				String prefix = st.getPrefix().replace("\"", "");
				if (st.getValue().equals(ss[1]) && prefix.equals(ss[0]))
					return true;
			}
		} else {
			for (Statement st : buffer) {
				if (value.equals(st.getValue()))
					return true;
			}
		}
		return false;
	}

	private static void validateOneFile(Statement st, List<String> err) {
		for (Statement sub : st.children) {
			if (sub.getKeyword().equals(YangKeyword.YK_COMMENT)) {
				continue;
			}
			if (sub.getKeyword().equals(YangKeyword.YK_USES)) {
				if (!exist(YangKeyword.YK_GROUPING, sub.getValue())) {
					err.add("Unknown " + sub.getValue() + " reference at " + sub.getFile() + ":" + sub.getLine());
				}
			} else if (sub.getKeyword().equals(YangKeyword.YK_INCLUDE)) {
				if (!exist(YangKeyword.YK_SUBMODULE, sub.getValue())) {
					err.add("Unknown " + sub.getValue() + " reference at " + sub.getFile() + ":" + sub.getLine());
				}
			} else if (sub.getKeyword().equals(YangKeyword.YK_IMPORT)
					|| sub.getKeyword().equals(YangKeyword.YK_BELONGS_TO)) {
				if (!exist(YangKeyword.YK_MODULE, sub.getValue())) {
					err.add("Unknown " + sub.getValue() + " reference at " + sub.getFile() + ":" + sub.getLine());
				}
			} else if (sub.getKeyword().equals(YangKeyword.YK_IF_FEATURE)) {
				if (!exist(YangKeyword.YK_FEATURE, sub.getValue())) {
					err.add("Unknown " + sub.getValue() + " reference at " + sub.getFile() + ":" + sub.getLine());
				}
			} else if (sub.getKeyword().equals(YangKeyword.YK_BASE)) {
				if (!exist(YangKeyword.YK_IDENTITY, sub.getValue())) {
					err.add("Unknown " + sub.getValue() + " reference at " + sub.getFile() + ":" + sub.getLine());
				}
			} else if (sub.getKeyword().equals(YangKeyword.YK_TYPE)) {
				if (YangBuiltIn.substatement.get(sub.getValue()) == null) {
					if (!exist(YangKeyword.YK_TYPEDEF, sub.getValue())) {
						err.add("Unknown " + sub.getValue() + " reference at " + sub.getFile() + ":" + sub.getLine());
					}
				}
			}

			if (sub.children.size() > 0) {
				validateOneFile(sub, err);
			}
		}
	}
}
