package com.yaochen.address.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringHelper {
	
	//地址去空格(包括里面的)  a 3
	//全角数字，标点  转半角
	//地址不能为空 留空为 T 的例外
	
	//有特殊字符的，报错
	
	public static boolean isEmpty(String raw){
		if(null == raw || raw.trim().length() ==0 || "null".equals(raw.trim())){
			return true;
		}
		return false;
	}
	
	public static boolean isNotEmpty(String raw){
		return !isEmpty(raw);
	}
	
	public static String replaceAllEmpty(String raw){
		if(isEmpty(raw)){
			return raw;
		}
		raw = raw.trim();
		String [] empties = new String [ ]{" ","\r","\n"};
		for (String string : empties) {
			raw = raw.replaceAll(string, "");
		}
		return raw;
	}
	
	/**
	 * 是否是整数.
	 * @param raw
	 * @return
	 */
	public static boolean isInteger(String raw){
		try {
			Integer.parseInt(raw);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	
	/**
	 * 全角转半角:
	 * @param fullStr
	 * @return
	 */
	public static final String full2Half(String fullStr) {
		if(isEmpty(fullStr)){
			return fullStr;
		}
		char[] c = fullStr.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] >= 65281 && c[i] <= 65374) {
				c[i] = (char) (c[i] - 65248);
			} else if (c[i] == 12288) { // 空格
				c[i] = (char) 32;
			}
		}
		return new String(c);
	}

	/**
	 * 半角转全角
	 * @param halfStr
	 * @return
	 */
	public static final String half2Full(String halfStr) {
		if(isEmpty(halfStr)){
			return halfStr;
		}
		char[] c = halfStr.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 32) {
				c[i] = (char) 12288;
			} else if (c[i] < 127) {
				c[i] = (char) (c[i] + 65248);
			}
		}
		return new String(c);
	}

	/**
	 * 是否是汉字.
	 * @param raw
	 * @return
	 */
	public static boolean isChinese(String raw) {
		Pattern p_str = Pattern.compile("[\\u4e00-\\u9fa5]+");
		Matcher m = p_str.matcher(raw);
		if (m.find() && m.group(0).equals(raw)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 是否包含 特殊字符.
	 * @param str
	 * @return
	 * @throws PatternSyntaxException
	 */
	public static boolean containSpecialCharacter(String str) throws PatternSyntaxException {
		// 只允许字母和数字
		// String regEx = "[^a-zA-Z0-9]";
		// 所有特殊字符
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		if(m.find()){
			return true;
		}
//		m.replaceAll("").trim()
		return false;
	}

	public static void main(String[] args) {
		System.err.println(isChinese("aaa"));
		System.err.println(isChinese("啊"));
		System.err.println(isChinese("a 啊 1"));
		System.err.println("========================================");
		System.err.println(isInteger("123"));
		System.err.println(isInteger("123.11"));
		System.err.println(isInteger("sss"));
		
		System.err.println("====================半角转全角====================");
		System.err.println(half2Full("1234567890"));
		System.err.println(half2Full("~!@#$%^&*() , . < > "));
		System.err.println("====================全角转半角====================");
		System.err.println(full2Half("１２３４５６７８９０"));
		System.err.println(full2Half("1234567890"));
		System.err.println(full2Half("～！＠＃＄％＾＆＊（）　，　．　＜　＞　"));
		
		System.err.println("====================特殊字符====================");
		String str = "*adCVs*34_a _09_b5*[/435^*&城池()^$$&*).{}+.|.)%%*(*.中国}34{45[]12.fd'*&999下面是中文的字符￥……{}【】。，；’“‘”？";
		System.out.println(str);
		System.out.println(containSpecialCharacter(str));
		
	}	
}
