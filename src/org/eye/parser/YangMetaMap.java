package org.eye.parser;

import java.util.HashMap;
import java.util.Map;

public class YangMetaMap {

	public static enum YTYPE {
		STRING, ENUM, BOOL, XPATH, NUMBER, NONE
	}

	public static Map<String, YTYPE> metaMap = new HashMap<String, YTYPE>();

	static {

		metaMap.put(YangKeyword.YK_ANYXML, YTYPE.STRING);
		metaMap.put(YangKeyword.YK_CHOICE, YTYPE.STRING);
		metaMap.put(YangKeyword.YK_CONFIG, YTYPE.BOOL);// true,false
		metaMap.put(YangKeyword.YK_CONTAINER, YTYPE.STRING);
		metaMap.put(YangKeyword.YK_DESCRIPTION, YTYPE.STRING);
		metaMap.put(YangKeyword.YK_GROUPING, YTYPE.STRING);// select
		metaMap.put(YangKeyword.YK_IF_FEATURE, YTYPE.ENUM); // find feature
		metaMap.put(YangKeyword.YK_LEAF, YTYPE.STRING);
		metaMap.put(YangKeyword.YK_LEAF_LIST, YTYPE.STRING);
		metaMap.put(YangKeyword.YK_LIST, YTYPE.STRING);
		metaMap.put(YangKeyword.YK_MUST, YTYPE.STRING); // condition
		metaMap.put(YangKeyword.YK_PRESENCE, YTYPE.STRING);
		metaMap.put(YangKeyword.YK_REFERENCE, YTYPE.STRING);
		metaMap.put(YangKeyword.YK_STATUS, YTYPE.ENUM);// "current",
														// "deprecated", or
														// "obsolete"
		metaMap.put(YangKeyword.YK_TYPEDEF, YTYPE.STRING);
		metaMap.put(YangKeyword.YK_USES, YTYPE.ENUM); // find grouping
		metaMap.put(YangKeyword.YK_WHEN, YTYPE.XPATH);// Xpath
		metaMap.put(YangKeyword.YK_MAX_ELEMENTS, YTYPE.NUMBER);
		metaMap.put(YangKeyword.YK_MIN_ELEMENTS, YTYPE.NUMBER);
		metaMap.put(YangKeyword.YK_ORDERED_BY, YTYPE.ENUM);// "system" or
																// "user"
		metaMap.put(YangKeyword.YK_TYPE, YTYPE.ENUM); // built-in or from
														// typedef
		metaMap.put(YangKeyword.YK_UNITS, YTYPE.STRING);
		metaMap.put(YangKeyword.YK_MODULE, YTYPE.STRING);
		metaMap.put(YangKeyword.YK_SUBMODULE, YTYPE.STRING);
		metaMap.put(YangKeyword.YK_ARGUMENT, YTYPE.STRING);
		metaMap.put(YangKeyword.YK_YIN_ELEMENT, YTYPE.BOOL);// true or false
		metaMap.put(YangKeyword.YK_PREFIX, YTYPE.STRING);//
		metaMap.put(YangKeyword.YK_REVISION_DATE, YTYPE.STRING);// YYYY-MM-DD
		metaMap.put(YangKeyword.YK_ERROR_APP_TAG, YTYPE.STRING);
		metaMap.put(YangKeyword.YK_ERROR_MESSAGE, YTYPE.STRING);
		metaMap.put(YangKeyword.YK_DEFAULT, YTYPE.STRING);// cannot co-exist
															// with
															// mandatory=true
		metaMap.put(YangKeyword.YK_MANDATORY, YTYPE.BOOL);
		metaMap.put(YangKeyword.YK_UNIQUE, YTYPE.STRING);// "leaf1 leaf2 leaf3
															// ..."
		metaMap.put(YangKeyword.YK_BIT, YTYPE.STRING);
		metaMap.put(YangKeyword.YK_ENUM, YTYPE.STRING);
		metaMap.put(YangKeyword.YK_LENGTH, YTYPE.STRING); // "11 | 42..max"
		metaMap.put(YangKeyword.YK_PATH, YTYPE.STRING); // Xpath in leaf-ref
		metaMap.put(YangKeyword.YK_PATTERN, YTYPE.STRING);// regular expression
		metaMap.put(YangKeyword.YK_RANGE, YTYPE.STRING);// "1 .. 3.14 | 10 |
														// 20..max";
		metaMap.put(YangKeyword.YK_REQUIRE_INSTANCE, YTYPE.BOOL);
		metaMap.put(YangKeyword.YK_POSITION, YTYPE.NUMBER);
		metaMap.put(YangKeyword.YK_CASE, YTYPE.STRING);
		metaMap.put(YangKeyword.YK_BASE, YTYPE.ENUM);// find other identity
		metaMap.put(YangKeyword.YK_INPUT, YTYPE.NONE);
		metaMap.put(YangKeyword.YK_OUTPUT, YTYPE.NONE);
		metaMap.put(YangKeyword.YK_AUGMENT, YTYPE.STRING);// Xpath
		metaMap.put(YangKeyword.YK_CONTACT, YTYPE.STRING);
		metaMap.put(YangKeyword.YK_DEVIATION, YTYPE.STRING);// Xpath
		metaMap.put(YangKeyword.YK_EXTENSION, YTYPE.STRING);
		metaMap.put(YangKeyword.YK_FEATURE, YTYPE.STRING); // with if-feature
															// together
		metaMap.put(YangKeyword.YK_IDENTITY, YTYPE.STRING);
		metaMap.put(YangKeyword.YK_IMPORT, YTYPE.ENUM);// find module name
		metaMap.put(YangKeyword.YK_INCLUDE, YTYPE.ENUM); // find submodule name
		metaMap.put(YangKeyword.YK_NAMESPACE, YTYPE.STRING); // uri
		metaMap.put(YangKeyword.YK_NOTIFICATION, YTYPE.STRING);
		metaMap.put(YangKeyword.YK_ORGANIZATION, YTYPE.STRING);
		metaMap.put(YangKeyword.YK_REVISION, YTYPE.STRING);// YYYY-MM-DD
		metaMap.put(YangKeyword.YK_RPC, YTYPE.STRING);
		metaMap.put(YangKeyword.YK_YANG_VERSION, YTYPE.STRING);// 1.0 or 1.1
		metaMap.put(YangKeyword.YK_KEY, YTYPE.STRING);// "key1 key2 key3"
		metaMap.put(YangKeyword.YK_VALUE, YTYPE.NUMBER);
		metaMap.put(YangKeyword.YK_BELONGS_TO, YTYPE.ENUM); // find module
		metaMap.put(YangKeyword.YK_REFINE, YTYPE.STRING);// change part of
															// grouping
	}
	
}
