//Auto generated by RfcParser.java. Dont change this file manually

package org.eye.parser;

import java.util.Map;
import java.util.HashMap;

import java.util.List;
import java.util.ArrayList;

public class YangNodeRelation {

	public static final Map<String,List<Relationship>> substatement = new HashMap<String,List<Relationship>>();

	static {
		List<Relationship> list = null;

		list = new ArrayList<Relationship>();
		substatement.put("module", list);
		list.add(new Relationship("anyxml","0..n","7.10"));
		list.add(new Relationship("augment","0..n","7.15"));
		list.add(new Relationship("choice","0..n","7.9"));
		list.add(new Relationship("contact","0..1","7.1.8"));
		list.add(new Relationship("container","0..n","7.5"));
		list.add(new Relationship("description","0..1","7.19.3"));
		list.add(new Relationship("deviation","0..n","7.18.3"));
		list.add(new Relationship("extension","0..n","7.17"));
		list.add(new Relationship("feature","0..n","7.18.1"));
		list.add(new Relationship("grouping","0..n","7.11"));
		list.add(new Relationship("identity","0..n","7.16"));
		list.add(new Relationship("import","0..n","7.1.5"));
		list.add(new Relationship("include","0..n","7.1.6"));
		list.add(new Relationship("leaf","0..n","7.6"));
		list.add(new Relationship("leaf-list","0..n","7.7"));
		list.add(new Relationship("list","0..n","7.8"));
		list.add(new Relationship("namespace","1","7.1.3"));
		list.add(new Relationship("notification","0..n","7.14"));
		list.add(new Relationship("organization","0..1","7.1.7"));
		list.add(new Relationship("prefix","1","7.1.4"));
		list.add(new Relationship("reference","0..1","7.19.4"));
		list.add(new Relationship("revision","0..n","7.1.9"));
		list.add(new Relationship("rpc","0..n","7.13"));
		list.add(new Relationship("typedef","0..n","7.3"));
		list.add(new Relationship("uses","0..n","7.12"));
		list.add(new Relationship("yang-version","0..1","7.1.2"));

		list = new ArrayList<Relationship>();
		substatement.put("import", list);
		list.add(new Relationship("prefix","1","7.1.4"));
		list.add(new Relationship("revision-date","0..1","7.1.5.1"));

		list = new ArrayList<Relationship>();
		substatement.put("includes", list);
		list.add(new Relationship("revision-date","0..1","7.1.5.1"));

		list = new ArrayList<Relationship>();
		substatement.put("revision", list);
		list.add(new Relationship("description","0..1","7.19.3"));
		list.add(new Relationship("reference","0..1","7.19.4"));

		list = new ArrayList<Relationship>();
		substatement.put("submodule", list);
		list.add(new Relationship("anyxml","0..n","7.10"));
		list.add(new Relationship("augment","0..n","7.15"));
		list.add(new Relationship("belongs-to","1","7.2.2"));
		list.add(new Relationship("choice","0..n","7.9"));
		list.add(new Relationship("contact","0..1","7.1.8"));
		list.add(new Relationship("container","0..n","7.5"));
		list.add(new Relationship("description","0..1","7.19.3"));
		list.add(new Relationship("deviation","0..n","7.18.3"));
		list.add(new Relationship("extension","0..n","7.17"));
		list.add(new Relationship("feature","0..n","7.18.1"));
		list.add(new Relationship("grouping","0..n","7.11"));
		list.add(new Relationship("identity","0..n","7.16"));
		list.add(new Relationship("import","0..n","7.1.5"));
		list.add(new Relationship("include","0..n","7.1.6"));
		list.add(new Relationship("leaf","0..n","7.6"));
		list.add(new Relationship("leaf-list","0..n","7.7"));
		list.add(new Relationship("list","0..n","7.8"));
		list.add(new Relationship("notification","0..n","7.14"));
		list.add(new Relationship("organization","0..1","7.1.7"));
		list.add(new Relationship("reference","0..1","7.19.4"));
		list.add(new Relationship("revision","0..n","7.1.9"));
		list.add(new Relationship("rpc","0..n","7.13"));
		list.add(new Relationship("typedef","0..n","7.3"));
		list.add(new Relationship("uses","0..n","7.12"));
		list.add(new Relationship("yang-version","0..1","7.1.2"));

		list = new ArrayList<Relationship>();
		substatement.put("belongs-to", list);
		list.add(new Relationship("prefix","1","7.1.4"));

		list = new ArrayList<Relationship>();
		substatement.put("typedef", list);
		list.add(new Relationship("default","0..1","7.3.4"));
		list.add(new Relationship("description","0..1","7.19.3"));
		list.add(new Relationship("reference","0..1","7.19.4"));
		list.add(new Relationship("status","0..1","7.19.2"));
		list.add(new Relationship("type","1","7.3.2"));
		list.add(new Relationship("units","0..1","7.3.3"));

		list = new ArrayList<Relationship>();
		substatement.put("type", list);
		list.add(new Relationship("bit","0..n","9.7.4"));
		list.add(new Relationship("enum","0..n","9.6.4"));
		list.add(new Relationship("length","0..1","9.4.4"));
		list.add(new Relationship("path","0..1","9.9.2"));
		list.add(new Relationship("pattern","0..n","9.4.6"));
		list.add(new Relationship("range","0..1","9.2.4"));
		list.add(new Relationship("require-instance","0..1","9.13.2"));
		list.add(new Relationship("type","0..n","7.4"));

		list = new ArrayList<Relationship>();
		substatement.put("container", list);
		list.add(new Relationship("anyxml","0..n","7.10"));
		list.add(new Relationship("choice","0..n","7.9"));
		list.add(new Relationship("config","0..1","7.19.1"));
		list.add(new Relationship("container","0..n","7.5"));
		list.add(new Relationship("description","0..1","7.19.3"));
		list.add(new Relationship("grouping","0..n","7.11"));
		list.add(new Relationship("if-feature","0..n","7.18.2"));
		list.add(new Relationship("leaf","0..n","7.6"));
		list.add(new Relationship("leaf-list","0..n","7.7"));
		list.add(new Relationship("list","0..n","7.8"));
		list.add(new Relationship("must","0..n","7.5.3"));
		list.add(new Relationship("presence","0..1","7.5.5"));
		list.add(new Relationship("reference","0..1","7.19.4"));
		list.add(new Relationship("status","0..1","7.19.2"));
		list.add(new Relationship("typedef","0..n","7.3"));
		list.add(new Relationship("uses","0..n","7.12"));
		list.add(new Relationship("when","0..1","7.19.5"));

		list = new ArrayList<Relationship>();
		substatement.put("must", list);
		list.add(new Relationship("description","0..1","7.19.3"));
		list.add(new Relationship("error-app-tag","0..1","7.5.4.2"));
		list.add(new Relationship("error-message","0..1","7.5.4.1"));
		list.add(new Relationship("reference","0..1","7.19.4"));

		list = new ArrayList<Relationship>();
		substatement.put("leaf", list);
		list.add(new Relationship("config","0..1","7.19.1"));
		list.add(new Relationship("default","0..1","7.6.4"));
		list.add(new Relationship("description","0..1","7.19.3"));
		list.add(new Relationship("if-feature","0..n","7.18.2"));
		list.add(new Relationship("mandatory","0..1","7.6.5"));
		list.add(new Relationship("must","0..n","7.5.3"));
		list.add(new Relationship("reference","0..1","7.19.4"));
		list.add(new Relationship("status","0..1","7.19.2"));
		list.add(new Relationship("type","1","7.6.3"));
		list.add(new Relationship("units","0..1","7.3.3"));
		list.add(new Relationship("when","0..1","7.19.5"));

		list = new ArrayList<Relationship>();
		substatement.put("leaf-list", list);
		list.add(new Relationship("config","0..1","7.19.1"));
		list.add(new Relationship("description","0..1","7.19.3"));
		list.add(new Relationship("if-feature","0..n","7.18.2"));
		list.add(new Relationship("max-elements","0..1","7.7.4"));
		list.add(new Relationship("min-elements","0..1","7.7.3"));
		list.add(new Relationship("must","0..n","7.5.3"));
		list.add(new Relationship("ordered-by","0..1","7.7.5"));
		list.add(new Relationship("reference","0..1","7.19.4"));
		list.add(new Relationship("status","0..1","7.19.2"));
		list.add(new Relationship("type","1","7.4"));
		list.add(new Relationship("units","0..1","7.3.3"));
		list.add(new Relationship("when","0..1","7.19.5"));

		list = new ArrayList<Relationship>();
		substatement.put("list", list);
		list.add(new Relationship("anyxml","0..n","7.10"));
		list.add(new Relationship("choice","0..n","7.9"));
		list.add(new Relationship("config","0..1","7.19.1"));
		list.add(new Relationship("container","0..n","7.5"));
		list.add(new Relationship("description","0..1","7.19.3"));
		list.add(new Relationship("grouping","0..n","7.11"));
		list.add(new Relationship("if-feature","0..n","7.18.2"));
		list.add(new Relationship("key","0..1","7.8.2"));
		list.add(new Relationship("leaf","0..n","7.6"));
		list.add(new Relationship("leaf-list","0..n","7.7"));
		list.add(new Relationship("list","0..n","7.8"));
		list.add(new Relationship("max-elements","0..1","7.7.4"));
		list.add(new Relationship("min-elements","0..1","7.7.3"));
		list.add(new Relationship("must","0..n","7.5.3"));
		list.add(new Relationship("ordered-by","0..1","7.7.5"));
		list.add(new Relationship("reference","0..1","7.19.4"));
		list.add(new Relationship("status","0..1","7.19.2"));
		list.add(new Relationship("typedef","0..n","7.3"));
		list.add(new Relationship("unique","0..n","7.8.3"));
		list.add(new Relationship("uses","0..n","7.12"));
		list.add(new Relationship("when","0..1","7.19.5"));

		list = new ArrayList<Relationship>();
		substatement.put("choice", list);
		list.add(new Relationship("anyxml","0..n","7.10"));
		list.add(new Relationship("case","0..n","7.9.2"));
		list.add(new Relationship("config","0..1","7.19.1"));
		list.add(new Relationship("container","0..n","7.5"));
		list.add(new Relationship("default","0..1","7.9.3"));
		list.add(new Relationship("description","0..1","7.19.3"));
		list.add(new Relationship("if-feature","0..n","7.18.2"));
		list.add(new Relationship("leaf","0..n","7.6"));
		list.add(new Relationship("leaf-list","0..n","7.7"));
		list.add(new Relationship("list","0..n","7.8"));
		list.add(new Relationship("mandatory","0..1","7.9.4"));
		list.add(new Relationship("reference","0..1","7.19.4"));
		list.add(new Relationship("status","0..1","7.19.2"));
		list.add(new Relationship("when","0..1","7.19.5"));

		list = new ArrayList<Relationship>();
		substatement.put("case", list);
		list.add(new Relationship("anyxml","0..n","7.10"));
		list.add(new Relationship("choice","0..n","7.9"));
		list.add(new Relationship("container","0..n","7.5"));
		list.add(new Relationship("description","0..1","7.19.3"));
		list.add(new Relationship("if-feature","0..n","7.18.2"));
		list.add(new Relationship("leaf","0..n","7.6"));
		list.add(new Relationship("leaf-list","0..n","7.7"));
		list.add(new Relationship("list","0..n","7.8"));
		list.add(new Relationship("reference","0..1","7.19.4"));
		list.add(new Relationship("status","0..1","7.19.2"));
		list.add(new Relationship("uses","0..n","7.12"));
		list.add(new Relationship("when","0..1","7.19.5"));

		list = new ArrayList<Relationship>();
		substatement.put("anyxml", list);
		list.add(new Relationship("config","0..1","7.19.1"));
		list.add(new Relationship("description","0..1","7.19.3"));
		list.add(new Relationship("if-feature","0..n","7.18.2"));
		list.add(new Relationship("mandatory","0..1","7.6.5"));
		list.add(new Relationship("must","0..n","7.5.3"));
		list.add(new Relationship("reference","0..1","7.19.4"));
		list.add(new Relationship("status","0..1","7.19.2"));
		list.add(new Relationship("when","0..1","7.19.5"));

		list = new ArrayList<Relationship>();
		substatement.put("grouping", list);
		list.add(new Relationship("anyxml","0..n","7.10"));
		list.add(new Relationship("choice","0..n","7.9"));
		list.add(new Relationship("container","0..n","7.5"));
		list.add(new Relationship("description","0..1","7.19.3"));
		list.add(new Relationship("grouping","0..n","7.11"));
		list.add(new Relationship("leaf","0..n","7.6"));
		list.add(new Relationship("leaf-list","0..n","7.7"));
		list.add(new Relationship("list","0..n","7.8"));
		list.add(new Relationship("reference","0..1","7.19.4"));
		list.add(new Relationship("status","0..1","7.19.2"));
		list.add(new Relationship("typedef","0..n","7.3"));
		list.add(new Relationship("uses","0..n","7.12"));

		list = new ArrayList<Relationship>();
		substatement.put("uses", list);
		list.add(new Relationship("augment","0..1","7.15"));
		list.add(new Relationship("description","0..1","7.19.3"));
		list.add(new Relationship("if-feature","0..n","7.18.2"));
		list.add(new Relationship("refine","0..1","7.12.2"));
		list.add(new Relationship("reference","0..1","7.19.4"));
		list.add(new Relationship("status","0..1","7.19.2"));
		list.add(new Relationship("when","0..1","7.19.5"));

		list = new ArrayList<Relationship>();
		substatement.put("rpc", list);
		list.add(new Relationship("description","0..1","7.19.3"));
		list.add(new Relationship("grouping","0..n","7.11"));
		list.add(new Relationship("if-feature","0..n","7.18.2"));
		list.add(new Relationship("input","0..1","7.13.2"));
		list.add(new Relationship("output","0..1","7.13.3"));
		list.add(new Relationship("reference","0..1","7.19.4"));
		list.add(new Relationship("status","0..1","7.19.2"));
		list.add(new Relationship("typedef","0..n","7.3"));

		list = new ArrayList<Relationship>();
		substatement.put("input", list);
		list.add(new Relationship("anyxml","0..n","7.10"));
		list.add(new Relationship("choice","0..n","7.9"));
		list.add(new Relationship("container","0..n","7.5"));
		list.add(new Relationship("grouping","0..n","7.11"));
		list.add(new Relationship("leaf","0..n","7.6"));
		list.add(new Relationship("leaf-list","0..n","7.7"));
		list.add(new Relationship("list","0..n","7.8"));
		list.add(new Relationship("typedef","0..n","7.3"));
		list.add(new Relationship("uses","0..n","7.12"));

		list = new ArrayList<Relationship>();
		substatement.put("output", list);
		list.add(new Relationship("anyxml","0..n","7.10"));
		list.add(new Relationship("choice","0..n","7.9"));
		list.add(new Relationship("container","0..n","7.5"));
		list.add(new Relationship("grouping","0..n","7.11"));
		list.add(new Relationship("leaf","0..n","7.6"));
		list.add(new Relationship("leaf-list","0..n","7.7"));
		list.add(new Relationship("list","0..n","7.8"));
		list.add(new Relationship("typedef","0..n","7.3"));
		list.add(new Relationship("uses","0..n","7.12"));

		list = new ArrayList<Relationship>();
		substatement.put("notification", list);
		list.add(new Relationship("anyxml","0..n","7.10"));
		list.add(new Relationship("choice","0..n","7.9"));
		list.add(new Relationship("container","0..n","7.5"));
		list.add(new Relationship("description","0..1","7.19.3"));
		list.add(new Relationship("grouping","0..n","7.11"));
		list.add(new Relationship("if-feature","0..n","7.18.2"));
		list.add(new Relationship("leaf","0..n","7.6"));
		list.add(new Relationship("leaf-list","0..n","7.7"));
		list.add(new Relationship("list","0..n","7.8"));
		list.add(new Relationship("reference","0..1","7.19.4"));
		list.add(new Relationship("status","0..1","7.19.2"));
		list.add(new Relationship("typedef","0..n","7.3"));
		list.add(new Relationship("uses","0..n","7.12"));

		list = new ArrayList<Relationship>();
		substatement.put("augment", list);
		list.add(new Relationship("anyxml","0..n","7.10"));
		list.add(new Relationship("case","0..n","7.9.2"));
		list.add(new Relationship("choice","0..n","7.9"));
		list.add(new Relationship("container","0..n","7.5"));
		list.add(new Relationship("description","0..1","7.19.3"));
		list.add(new Relationship("if-feature","0..n","7.18.2"));
		list.add(new Relationship("leaf","0..n","7.6"));
		list.add(new Relationship("leaf-list","0..n","7.7"));
		list.add(new Relationship("list","0..n","7.8"));
		list.add(new Relationship("reference","0..1","7.19.4"));
		list.add(new Relationship("status","0..1","7.19.2"));
		list.add(new Relationship("uses","0..n","7.12"));
		list.add(new Relationship("when","0..1","7.19.5"));

		list = new ArrayList<Relationship>();
		substatement.put("identity", list);
		list.add(new Relationship("base","0..1","7.16.2"));
		list.add(new Relationship("description","0..1","7.19.3"));
		list.add(new Relationship("reference","0..1","7.19.4"));
		list.add(new Relationship("status","0..1","7.19.2"));

		list = new ArrayList<Relationship>();
		substatement.put("extension", list);
		list.add(new Relationship("argument","0..1","7.17.2"));
		list.add(new Relationship("description","0..1","7.19.3"));
		list.add(new Relationship("reference","0..1","7.19.4"));
		list.add(new Relationship("status","0..1","7.19.2"));

		list = new ArrayList<Relationship>();
		substatement.put("argument", list);
		list.add(new Relationship("yin-element","0..1","7.17.2.2"));

		list = new ArrayList<Relationship>();
		substatement.put("feature", list);
		list.add(new Relationship("description","0..1","7.19.3"));
		list.add(new Relationship("if-feature","0..n","7.18.2"));
		list.add(new Relationship("status","0..1","7.19.2"));
		list.add(new Relationship("reference","0..1","7.19.4"));

		list = new ArrayList<Relationship>();
		substatement.put("deviation", list);
		list.add(new Relationship("description","0..1","7.19.3"));
		list.add(new Relationship("reference","0..1","7.19.4"));

		list = new ArrayList<Relationship>();
		substatement.put("deviates", list);
		list.add(new Relationship("config","0..1","7.19.1"));
		list.add(new Relationship("default","0..1","7.6.4"));
		list.add(new Relationship("mandatory","0..1","7.6.5"));
		list.add(new Relationship("max-elements","0..1","7.7.4"));
		list.add(new Relationship("min-elements","0..1","7.7.3"));
		list.add(new Relationship("must","0..n","7.5.3"));
		list.add(new Relationship("type","0..1","7.4"));
		list.add(new Relationship("unique","0..n","7.8.3"));
		list.add(new Relationship("units","0..1","7.3.3"));

		list = new ArrayList<Relationship>();
		substatement.put("range", list);
		list.add(new Relationship("description","0..1","7.19.3"));
		list.add(new Relationship("error-app-tag","0..1","7.5.4.2"));
		list.add(new Relationship("error-message","0..1","7.5.4.1"));
		list.add(new Relationship("reference","0..1","7.19.4"));

		list = new ArrayList<Relationship>();
		substatement.put("length", list);
		list.add(new Relationship("description","0..1","7.19.3"));
		list.add(new Relationship("error-app-tag","0..1","7.5.4.2"));
		list.add(new Relationship("error-message","0..1","7.5.4.1"));
		list.add(new Relationship("reference","0..1","7.19.4"));

		list = new ArrayList<Relationship>();
		substatement.put("pattern", list);
		list.add(new Relationship("description","0..1","7.19.3"));
		list.add(new Relationship("error-app-tag","0..1","7.5.4.2"));
		list.add(new Relationship("error-message","0..1","7.5.4.1"));
		list.add(new Relationship("reference","0..1","7.19.4"));

		list = new ArrayList<Relationship>();
		substatement.put("enum", list);
		list.add(new Relationship("description","0..1","7.19.3"));
		list.add(new Relationship("reference","0..1","7.19.4"));
		list.add(new Relationship("status","0..1","7.19.2"));
		list.add(new Relationship("value","0..1","9.6.4.2"));

		list = new ArrayList<Relationship>();
		substatement.put("bit", list);
		list.add(new Relationship("description","0..1","7.19.3"));
		list.add(new Relationship("reference","0..1","7.19.4"));
		list.add(new Relationship("status","0..1","7.19.2"));
		list.add(new Relationship("position","0..1","9.7.4.2"));
	}
}
