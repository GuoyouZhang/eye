package org.eye.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: check end of file after }
public class YangParser {

	public List<String> errors = new ArrayList<String>();

	int line = 1;
	int brace = 0;
	Pattern pStmt = Pattern.compile("(\\S+)\\s+(.*?)$", Pattern.DOTALL);
	Statement top = null;
	String file = "";

	public void reset(Statement stmt) {
		line = 1;
		brace = 0;
		errors.clear();
		top = stmt;
		file = stmt.getFile();
	}

	private char readChar(BufferedReader reader) throws Exception {
		int i = reader.read();
		if (i == -1) {
			if (errors.size() > 0) {
				throw new Exception("" + errors.size() + " errors found");
			} else if (brace > 0) { // only } missed will be detected here
				errors.add("Unexpected end of file, '}' is missed " + (brace) + " times");
				throw new Exception("Unexpected end of file, '}' is missed " + brace + " times");
			} else {
				throw new Exception("done");
			}
		}
		return (char) i;
	}

	private void skipComment(BufferedReader reader, StringBuilder sb) throws Exception {
		char c = readChar(reader);
		sb.append(c);
		if (c == '/') {
			String aline = reader.readLine();
			sb.append(aline);
			line++;
		} else if (c == '*') {
			while (true) {
				c = readChar(reader);
				if (c == '\n') {
					line++;
				}
				sb.append(c);
				if (c == '/' && sb.charAt(sb.length() - 2) == '*') {
					break;
				}
			}
		} else {
			throw new Exception("Unexpected comment format, at " + this.file + ":" + line);
		}
	}

	private void skipQuotation(char c1, BufferedReader reader, StringBuilder sb) throws Exception {
		while (true) {
			char c2 = readChar(reader);
			if (c2 == '\n') {
				line++;
			}
			sb.append(c2);

			if (c2 == c1 && sb.charAt(sb.length() - 2) != '\\') {
				break;
			}
		}
	}

	public void parseStatement(Statement parent, BufferedReader reader) throws Exception {
		StringBuilder sb = new StringBuilder();
		while (true) {
			char c = readChar(reader);
			if (c == '\n') {
				line++;
			}

			if (c == '/') {
				int startPos = sb.length();
				sb.append(c);
				Statement st = new Statement();
				st.setKeyword(YangKeyword.YK_COMMENT);
				st.setLine(line);
				skipComment(reader, sb);
				st.setText(sb.toString());
				st.setValue(sb.toString());
				sb.delete(startPos, sb.length());
				st.setFile(parent.getFile());
				parent.children.add(st);
				continue;
			} else if (c == '\'' || c == '\"') {
				sb.append(c);
				skipQuotation(c, reader, sb);
				continue;
			}

			if (c == '{') {
				Statement st = new Statement();
				st.setLine(line);
				st.setText(sb.toString());
				st.setFile(parent.getFile());
				sb.delete(0, sb.length());
				parent.children.add(st);
				brace++;
				parseStatement(st, reader);
			} else if (c == '}') {
				brace--;
				if (sb.toString().trim().length() > 0) {
					errors.add("Unexpected content '" + sb.toString().trim() + "' at line:" + (line - 1));
				}
				return;
			} else if (c == ';') {
				Statement st = new Statement();
				st.setLine(line);
				st.setText(sb.toString());
				st.setFile(parent.getFile());
				sb.delete(0, sb.length());
				parent.children.add(st);
			} else {
				sb.append(c);
			}
		}
	}

	public void print(Statement st, int level) {
		for (Statement sub : st.children) {
			System.out.print(sub.getLine());
			for (int i = 0; i < level; i++)
				System.out.print("--");
			System.out.println(sub.getText());
			if (sub.children.size() > 0) {
				print(sub, level + 1);
			}
		}
	}

	public void setPrefix(Statement st, String prefix) {
		for (Statement sub : st.children) {
			sub.setPrefix(prefix);
			if (sub.children.size() > 0) {
				setPrefix(sub, prefix);
			}
		}
	}

	private int countLine(String s) {
		int l = 1;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '\n') {
				l++;
			}
		}
		return l;
	}

	public void postProcessing(Statement st) {
		for (Statement sub : st.children) {
			// System.out.println("----" + sub.getText());
			if (sub.getKeyword() != null)
				continue;
			Matcher m = pStmt.matcher(sub.getText());
			int l = countLine(sub.getText());

			if (l > 1) {
				sub.setLine(sub.getLine() - l + 1);
			}

			if (m.find()) {
				// System.out.println(m.group(1) + "=" + m.group(2));
				sub.setKeyword(m.group(1));
				sub.setValue(m.group(2));
			} else {
				// System.out.println("## " + sub.getText());
				sub.setKeyword(sub.getText());
			}

			if (sub.children.size() > 0) {
				postProcessing(sub);
			}
		}
	}

	private String findPrefix(Statement top) {
		for (Statement module : top.children) {
			if (module.getKeyword().equals(YangKeyword.YK_MODULE)) {
				for (Statement child : module.children) {
					if (child.getKeyword().equals(YangKeyword.YK_PREFIX))
						return child.getValue();
				}
			} else if (module.getKeyword().equals(YangKeyword.YK_SUBMODULE)) {
				for (Statement child : module.children) {
					if (child.getKeyword().equals(YangKeyword.YK_BELONGS_TO)) {
						return child.children.get(0).getValue();
					}
				}
			}
		}
		return null;
	}

	public static List<String> go(String file) {
		FileReader fr;
		YangParser parser = new YangParser();
		Statement statement = new Statement();

		try {
			fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			statement.setFile(file);
			parser.reset(statement);
			parser.parseStatement(statement, br);

			if (parser.errors.size() > 0) {
				System.out.println(parser.errors.toString());
			}
		} catch (Exception e) {
			if (e.getMessage().equals("done")) {
				parser.postProcessing(statement);
				String prefix = parser.findPrefix(statement);
				if (prefix == null) {
					parser.errors.add("No prefix defined in " + file);
				} else {
					parser.setPrefix(statement, prefix);

					YangRepo.add2Repo(file, prefix, statement);
				}
			} else {
				e.printStackTrace();
				if (parser.errors.size() == 0) {
					parser.errors.add(e.getMessage());
				}
				System.out.println(parser.errors.toString());
			}
		} finally {
			return parser.errors;
		}
	}
}
