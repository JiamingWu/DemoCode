package commons;

import org.apache.commons.lang.StringUtils;

public class Lang {

	public static void main(String[] args) {
		String a = StringUtils.substring("abcdefg", 1, 10);
		System.out.println(a);
		
		String b = StringUtils.substring(null, 1, 10);
		System.out.println(b);
		
		String c = StringUtils.rightPad(a, 10);
		System.out.println(c + ".");
		
		String[] d = StringUtils.split("abc\r\ndef\r\nddd\r\n\r\n", "\r\n");
		System.out.println(d.length);
		for (String l : d) {
			System.out.println(l);
		}
		
		String content = "abc\t de\rf:\ng\r\nt";
		boolean vaild = StringUtils.containsOnly(content, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789/-?:().,'+{} \r\n");
		System.out.println(vaild);
	}
	
}
