package org.eye.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class Record {
	public String file;
	public String prefix;
	public Statement topStmt;
}

public class YangRepo {

	// file -> record
	private static Map<String, Record> repo = new HashMap<String, Record>();


	public static Statement findStatementByKVP(String key, String value, String prefix) {
		for (Record r : repo.values()) {
			if (r.prefix.equals(prefix)) {
				return findStatementByKV(key, value);
			}
		}
		return null;
	}

	public static Statement findStatementByKV(String key, String value) {
		List<Statement> ls = findStatementByK(key);
		for (Statement st : ls) {
			if (st.getValue().equals(value))
				return st;
		}
		return null;
	}

	public static List<Statement> findStatementByK(String keyword) {
		List<Statement> ls = new ArrayList<Statement>();
		for (Record r : repo.values()) {
			loopSearch(ls, r.topStmt, keyword);
		}
		return ls;
	}

	private static void loopSearch(List<Statement> ls, Statement st, String key) {

		for (Statement sub : st.children) {
			if (sub.getKeyword().equals(key)) {
				ls.add(sub);
			}
			if (sub.children.size() > 0)
				loopSearch(ls, sub, key);
		}
	}

	public static void add2Repo(String file, String prefix, Statement top) {
		Record r = new Record();
		r.file = file;
		r.prefix = prefix;
		r.topStmt = top;
		repo.put(file, r);
	}

	public static void addStatement(Statement stmt) {
		Record r = repo.get(stmt.getFile());
		if (r == null) {
			r = new Record();
			r.file = stmt.getFile();
			r.prefix = stmt.getPrefix();
			r.topStmt = stmt;
			repo.put(stmt.getFile(), r);
		}
	}

	public static void delete(Statement stmt) {
		if (stmt.getKeyword().equals(YangKeyword.YK_SUBMODULE)
				|| stmt.getKeyword().equals(YangKeyword.YK_MODULE))
			repo.remove(stmt.getFile());
	}

	public static void clean() {
		repo.clear();
	}

	public static Statement findStatementByF(String file) {
		Record r = repo.get(file);
		if (r != null) {
			return r.topStmt;
		}
		return null;
	}

	public static Set<String> getAllFiles() {
		return repo.keySet();
	}
}
