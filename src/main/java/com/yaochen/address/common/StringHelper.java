package com.yaochen.address.common;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.alibaba.fastjson.JSON;
import com.easyooo.framework.common.util.CglibUtil;

public class StringHelper {
	
	public static boolean isEmpty(String raw){
		if(null == raw || raw.trim().length() ==0 || "null".equals(raw.trim())){
			return true;
		}
		return false;
	}
	
	/**
	 * 传入多个字符串,只要有任意一个为空,返回true.否则返回false.
	 * @param arr
	 * @return
	 */
	public static boolean isAnyEmpty(String ...arr){
		for (String str : arr) {
			if(isEmpty(str)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isNotEmpty(String raw){
		return !isEmpty(raw);
	}
	
	public static String replaceAllEmpty(String raw){
		raw = checkAndTrim(raw);
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
//		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		String regEx = "[`~!@#$%^&*+=|{}':;',\\[\\].<>/?~！@#￥%……&*——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		if(m.find()){
			return true;
		}
//		m.replaceAll("").trim()
		return false;
	}

	public static void main1(String[] args) {
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

	/**
     * 比较两个字符串是否都为空或者equals
     * @param str1
     * @param str2
     */
	public static boolean bothEmptyOrEquals(String str1, String str2) {
		if(isEmpty(str1) && isEmpty(str2)){
			return true;
		}
		return (isEmpty(str1)?"":str1).equals(isEmpty(str2)?"":str2);
	}	
	
	/**
	 * 是否數字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	

	/**
	 * 判断char 是不是大写英文字母的.
	 * @param chr
	 * @return
	 */
	public static boolean isUpperEnglishCharacter(char chr) {
		int upperStart = 65;//'A'
		int upperEnd = 90;//'Z'
		int code = (int) chr;
		return code >= upperStart && code <= upperEnd;
	}
	
	/**
	 * 判断char 是不是大写英文字母的.
	 * @param chr
	 * @return
	 */
	public static boolean isLowerEnglishCharacter(char chr) {
		int lowerStart = 97;//'a'
		int lowerEnd = 122;//'z'
		int code = (int) chr;
		return code >= lowerStart && code <= lowerEnd;
	}
	
	/**
	 * 当前字符是否是英文字符.
	 * @param chr
	 * @return
	 */
	public static boolean isAlphabet(char chr){
		return isLowerEnglishCharacter(chr) || isUpperEnglishCharacter(chr);
	}
	
	/**
	 * 判断的当前字符串是否是英文(或者数字)的.
	 * @param str
	 * @return
	 */
	public static boolean isAlphabetOrNumric(String str){
		for (int index = 0; index < str.length(); index++) {
			char chr = str.charAt(index);
			if(!isAlphabet(chr) && !Character.isDigit(chr)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 判断的当前字符串是否是英文字符,不包括数字.
	 * @param str
	 * @return
	 */
	public static boolean isAlphabet(String str){
		for (int index = 0; index < str.length(); index++) {
			char chr = str.charAt(index);
			if(!isAlphabet(chr)){
				return false;
			}
		}
		return true;
	}

	/**
	 * 給定一个字符串,默认按照以 "_" 分割,截成 驼峰型.
	 * @param str
	 * @return
	 */
	public static String camellize(String str) {
		str = checkAndTrim(str);
		return camellize(str,"_");
	}
	
	/**
	 * 給定一个 驼峰型 字符串,默认按照以 "_" 分割,截成 非驼峰型.
	 * @param str
	 * @return
	 */
	public static String deCamellize(String str) {
		str = checkAndTrim(str);
		return deCamellize(str,"_");
	}

	private static String checkAndTrim(String str) {
		if(isEmpty(str)){
			return str;
		}
		str = str.trim();
		return str;
	}

	public static String deCamellize(String str, String spliter) {
		str = checkAndTrim(str);
		StringBuffer sb = new StringBuffer();
		for (int index = 0; index < str.length(); index++) {
			char chr = str.charAt(index);
			if(Character.isUpperCase(chr)){
				sb.append(spliter);
				sb.append(Character.toLowerCase(chr));
			}else{
				sb.append(chr);
			}
		}
		return sb.toString();
	}

	public static String camellize(String columnName, String spliter) {
		if(isEmpty(columnName)){
			return columnName;
		}
		String rst = "";
		String[] split = columnName.split(spliter);
		int index = 0;
		for (String str : split) {
			if(index >0){
				str = upperFirst(str);
			}
			rst += str;
			index++;
		}
		return rst;
	}

	public static void main(String[] args) {
		String str = "AD_TREE".toLowerCase();
		String camellize = camellize(str);
		System.err.println(camellize);
		System.err.println(deCamellize(camellize));
	}

	/**
	 * 首字母转小写
	 * @param rawStr
	 * @return
	 */
	public static String lowerFirst(String rawStr) {
		if (Character.isLowerCase(rawStr.charAt(0))) {
			return rawStr;
		}
		return (new StringBuilder()).append(Character.toLowerCase(rawStr.charAt(0)))
				.append(rawStr.substring(1)).toString();
	}

	/**
	 *  首字母转大写
	 * @param rawStr
	 * @return
	 */
	public static String upperFirst(String rawStr) {
		if (Character.isUpperCase(rawStr.charAt(0))) {
			return rawStr;
		}
		return (new StringBuilder()).append(Character.toUpperCase(rawStr.charAt(0)))
				.append(rawStr.substring(1)).toString();
	}

	public static boolean equalsAsString(Object first, Object second) {
		boolean firstNull = first == null;
		boolean secondNull = second == null;
		//两个都为空,返回true.
		if((firstNull && secondNull)){
			return true;
		}
		// 两个其中有一个为空,返回false.
		if((firstNull || secondNull)){
			return false;
		}
		return first.toString().equals(second.toString());
	}
	
	/**
	 * @param tpl 模版,变量名用 ${} 包裹.如  ${var1}
	 * @param obj 普通的javabean.
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static String formatTpl(String tpl,Object obj) throws Exception{
		obj = null == obj ? new Object() : obj;
		Class<? extends Object> objClass = obj.getClass();
		boolean isCollection = Collection.class.isAssignableFrom(objClass);
		if(objClass.isArray() || isCollection){
			throw new IllegalArgumentException("不接受数据或者集合类型的参数.");
		}
		Map<String, Object> describe = new HashMap<String, Object>(); 
		if(null != obj){
			if(Map.class.isAssignableFrom(objClass)){
				describe = (Map<String, Object>) obj;
			}else{
				describe = CglibUtil.describe(obj);
			}
		}
		return formatTpl(tpl, describe);
	}
	
	public static String formatTpl(String tpl,Map<String, Object> obj) throws Exception {
		Pattern pattern = Pattern.compile("\\$\\{[a-zA-Z_0-9\\.]+\\}");
		Matcher matcher = pattern.matcher(tpl);
		String result = tpl;
		while(matcher.find()){
			String group = matcher.group();
			String varName = group.substring(2, group.length() -1);
			Object varVal = obj.get(varName);
			varVal = varVal == null ? "" : varVal;//保证不抛错
			String replace = "";
			if(Date.class.isAssignableFrom(varVal.getClass())){
				replace = DateHelper.format((Date) varVal, DateHelper.FORMAT_TIME);
			}else if(String.class.isAssignableFrom(varVal.getClass()) || varVal.getClass().isPrimitive() ){
				//数组或者集合
				replace = varVal.toString();
			}else if(varVal.getClass().isArray() || Collection.class.isAssignableFrom(varVal.getClass()) ){
				//数组或者集合
				replace = JSON.toJSONString(varVal);
			}else{
				replace = varVal.toString();
			}
			
			String regex = "\\$\\{" + varName + "\\}";
			try {
				result = result.replaceAll(regex, replace );
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	
}
