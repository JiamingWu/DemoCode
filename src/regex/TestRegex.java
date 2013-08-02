package regex;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRegex {

	// \$\{([^\}]*)\}
//	private final static Pattern varPattern = Pattern.compile("\\$\\{([^\\}]*)\\}");
	private final static Pattern varPattern = Pattern.compile("\\$\\{(.*?)\\}");

	public static String convert(String subject, Map<String, String> vars) {
		StringBuffer buffer = new StringBuffer();
		Matcher m = varPattern.matcher(subject);
		while (m.find()) {
			System.out.println(m.group(1).toLowerCase());
			System.out.println("~~"+buffer);
			if (vars.containsKey(m.group(1).toLowerCase())) {
				m.appendReplacement(buffer, vars.get(m.group(1).toLowerCase()).toString());
			} else {
				m.appendReplacement(buffer, "");
			}
			System.out.println("=="+buffer);
		}
		m.appendTail(buffer);
		return buffer.toString();
	}
	
	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "Allen");
		String result = convert("Hi ${name}! ${name2}!", map);
		System.out.println("result:" + result);
	}
}
