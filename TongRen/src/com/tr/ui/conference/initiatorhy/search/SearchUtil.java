package com.tr.ui.conference.initiatorhy.search;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SearchUtil {
	public final static String POUND_SIGN = "#";
	public final static char POUND_SIGN_CHAR = '#';
	// 国标码和区位码转换常量   
	static final int GB_SP_DIFF = 160;
	//存放国标一级汉字不同读音的起始区位码   
	static final int[] secPosValueList = {
		1601, 1637, 1833, 2078, 2274, 2302, 2433, 2594, 2787,
		3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027, 4086,
		4390, 4558, 4684, 4925, 5249, 5600};
	//存放国标一级汉字不同读音的起始区位码对应读音   
	static final char[] firstLetter = {
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j',  
		'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',  
		't', 'w', 'x', 'y', 'z'};
	/**
	 * 获取一个汉字的拼音首字母。 GB码两个字节分别减去160，转换成10进制码组合就可以得到区位码
	 * 例如汉字“你”的GB码是0xC4/0xE3，分别减去0xA0（160）就是0x24/0x43
	 * 0x24转成10进制就是36，0x43是67，那么它的区位码就是3667，在对照表中读音为‘n’
	 */
	private static char convertToLetter(byte[] bytes){
		char result = POUND_SIGN_CHAR;  
		int secPosValue = 0, i = 0;
		if(bytes == null){
			return POUND_SIGN_CHAR;
		}
		if(bytes.length < 2){
			return POUND_SIGN_CHAR;
		}
		for(i = 0; i < bytes.length; i++){  
			bytes[i] -= GB_SP_DIFF;  
		}
		int h = (int)bytes[0], t = (int)bytes[1];
		secPosValue = (h*100 + t);
		for(i = 0; i < firstLetter.length; i++){
			if(secPosValue >= secPosValueList[i] && 
					secPosValue < secPosValueList[i + 1]){  
				result = firstLetter[i];
				break;  
			}  
		}  
		return result;  
	}
	public static String getfirstWordFirstLetter(String str){
		if(isEmpty(str)){
			return POUND_SIGN;
		}
		char ch = str.charAt(0);
		char[] temp = new char[]{ch};
		StringBuffer sb = new StringBuffer();
		try {
			byte[] uniCode = (new String(temp)).getBytes("GBK");
			if(uniCode[0] < 128 && uniCode[0] > 0){
				// 非汉字   
				sb.append(temp);
				return sb.toString();
			}else{
				sb.append(convertToLetter(uniCode));
				return sb.toString();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return POUND_SIGN;
	}
	//获取一个字符串的拼音码   
	public static String getEveryWordFirstLetter(String str){
		StringBuffer sb = new StringBuffer();
		if(isEmpty(str)){
			return sb.toString();
		}
		for(int i = 0; i < str.length(); i++){
			//依次处理str中每个字符   
			char ch = str.charAt(i);
			char[] temp = new char[]{ch};
			try {
				byte[] uniCode = (new String(temp)).getBytes("GBK");
				if(uniCode[0] < 128 && uniCode[0] > 0){
					// 非汉字   
					sb.append(temp);
				}else{  
					sb.append(convertToLetter(uniCode));
				}  
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}  
		return sb.toString();
	}
	public static boolean firstWordIsChinese(String str){
		if(isEmpty(str)){
			return false;
		}
		char ch = str.charAt(0);
		char[] temp = new char[]{ch};
		try {
			byte[] uniCode = (new String(temp)).getBytes("GBK");
			if(uniCode[0] < 128 && uniCode[0] > 0){
				// 非汉字   
				return false;
			}else{
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static boolean firstWordIsEnglish(String str){
		if(isEmpty(str)){
			return false;
		}
		return Character.isLetter(str.charAt(0));
	}
	public static boolean isNumeric(String str){
		if(isEmpty(str)){
			return false;
		}
	     Pattern pattern = Pattern.compile("[0-9]*"); 
	     return pattern.matcher(str).matches();    
	}
	public static boolean isEnglish(String str){
		if(isEmpty(str)){
			return false;
		}
	     Pattern pattern = Pattern.compile("[A-Z]|[a-z]*"); 
	     return pattern.matcher(str).matches();    
	}
	public static boolean isNumeric1(String str){
		if(isEmpty(str)){
			return false;
		}
		for(int i = str.length(); --i>=0;){
			if(!Character.isDigit(str.charAt(i))){
				return false ;
			}
		}
		return true;
	}
	public static boolean isEnglish1(String str){
		if(isEmpty(str)){
			return false;
		}
		for(int i = str.length(); --i>=0;){
			if(!Character.isLetter(str.charAt(i))){
				return false ;
			}
		}
		return true;
	}
	public static boolean isContainCn(String str){
		if(isEmpty(str)){
			return false;
		}
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]+");
		Matcher m = p.matcher(str);
		return m.find();
	}
	public static boolean isChinese(String str){
		if(isEmpty(str)){
			return false;
		}
		Pattern p = Pattern.compile("^[\u4e00-\u9fa5]+$");
		Matcher m = p.matcher(str);
		return m.matches();
	}
	public static boolean isChinese(char c){
		Pattern p = Pattern.compile("^[\u4e00-\u9fa5]+$");
		Matcher m = p.matcher(new String(new char[]{c}));
		return m.matches();
	}
	public static String getFirstLetter(String str) {
		if(isEmpty(str)){
			return POUND_SIGN;
		}
		if(firstWordIsEnglish(str) || isChinese(str.charAt(0))){
			return getfirstWordFirstLetter(str);
		}
		return POUND_SIGN;
	}
	public static String getFirstLetterUpperCace(String str) {
		if(isEmpty(str)){
			return POUND_SIGN;
		}
		if(firstWordIsEnglish(str) || isChinese(str.charAt(0))){
			return getfirstWordFirstLetter(str).toUpperCase();
		}
		return POUND_SIGN;
	}
//	public static String getFirstSpellLetter(String chinese){
//		StringBuffer sb = new StringBuffer();
//		char[] charArr = chinese.toCharArray();
//		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
//		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
//		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//		for(int i = 0; i < charArr.length; i++){
//			if(charArr[i] > 128){
//				try {
//					String[] temp = PinyinHelper.toHanyuPinyinStringArray(charArr[i], defaultFormat);
//					if(temp != null){
//						sb.append(temp[0].charAt(0));
//					}
//				} catch (Exception e) {
//					// TODO: handle exception
//					e.printStackTrace();
//				}
//			}else{
//				sb.append(charArr[i]);
//			}
//		}
//		return sb.toString().replaceAll("\\W", "").trim();
//	}
	
	public static boolean isEmpty(String str){
		return str == null ? true : str.equals("");//str.isEmpty();
	}
	public static <E> boolean isEmpty(List<E> list){
		return list == null ? true : list.isEmpty();
	}
	public static  boolean isEmpty(String[] arry){
		return arry == null ? true : (arry.length == 0 ? true : false);
	}
	public static  boolean isEmpty(Integer[] arry){
		return arry == null ? true : (arry.length == 0 ? true : false);
	}
	public static  boolean isEmpty(Long[] arry){
		return arry == null ? true : (arry.length == 0 ? true : false);
	}
	public static <K, V> boolean isEmpty(Map<K, V> map){
		return map == null ? true : map.isEmpty();
	}
}
