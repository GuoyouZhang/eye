package org.eye.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YangBuiltIn {

	// yang built-in type
	public static final String BI_INT8 = "int8";
	public static final String BI_INT16 = "int16";
	public static final String BI_INT32 = "int32";
	public static final String BI_INT64 = "int64";
	public static final String BI_UINT8 = "uint8";
	public static final String BI_UINT16 = "uint16";
	public static final String BI_UINT32 = "uint32";
	public static final String BI_UINT64 = "uint64";
	public static final String BI_DECIMAL64 = "decimal64";
	public static final String BI_STRING = "string";
	public static final String BI_BOOL = "boolean";
	public static final String BI_ENUM = "enumeration";
	public static final String BI_BITS = "bits";
	public static final String BI_BIN = "binary";
	public static final String BI_LEAFREF = "leafref";
	public static final String BI_IDREF = "identityref";
	public static final String BI_EMPTY = "empty";
	public static final String BI_UNION = "union";
	public static final String BI_INSID = "instance-identifier";

	public static final Map<String,List<Relationship>> substatement = new HashMap<String,List<Relationship>>();

	static {
		List<Relationship> list = null;

		list = new ArrayList<Relationship>();
		list.add(new Relationship("range", "0..1", "9.2.4"));
		substatement.put(BI_INT8, list);
		substatement.put(BI_INT16, list);
		substatement.put(BI_INT32, list);
		substatement.put(BI_INT64, list);
		substatement.put(BI_UINT8, list);
		substatement.put(BI_UINT16, list);
		substatement.put(BI_UINT32, list);
		substatement.put(BI_UINT64, list);

		list = new ArrayList<Relationship>();
		list.add(new Relationship("range", "0..1", "9.2.4"));
		list.add(new Relationship("fraction-digits", "1", "9.3.4"));
		substatement.put(BI_DECIMAL64, list);

		list = new ArrayList<Relationship>();
		list.add(new Relationship("length", "0..1", "9.4.4"));
		list.add(new Relationship("pattern", "0..n", "9.4.6"));
		substatement.put(BI_STRING, list);

		// no restriction to boolean
		list = new ArrayList<Relationship>();
		substatement.put(BI_BOOL, list);

		list = new ArrayList<Relationship>();
		list.add(new Relationship("enum", "0..n", "9.6.4"));
		substatement.put(BI_ENUM, list);

		list = new ArrayList<Relationship>();
		list.add(new Relationship("bit", "0..n", "9.7.4"));
		substatement.put(BI_BITS, list);

		list = new ArrayList<Relationship>();
		list.add(new Relationship("length", "0..1", "9.4.4"));
		substatement.put(BI_BIN, list); // base64 encoding

		list = new ArrayList<Relationship>();
		list.add(new Relationship("path", "0..1", "9.9.2"));
		substatement.put(BI_LEAFREF, list);

		list = new ArrayList<Relationship>();
		list.add(new Relationship("base", "0..1", "9.9.2"));
		substatement.put(BI_IDREF, list);

		// no child for empty
		list = new ArrayList<Relationship>();
		substatement.put(BI_EMPTY, list);

		list = new ArrayList<Relationship>();
		list.add(new Relationship("type", "0..n", "7.4"));
		substatement.put(BI_UNION, list);

		// no child for instance-identifier
		list = new ArrayList<Relationship>();
		list.add(new Relationship("require-instance","0..1","9.13.2"));
		substatement.put(BI_INSID, list);
	}
}
