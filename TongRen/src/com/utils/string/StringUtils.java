package com.utils.string;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

import com.utils.common.Constants;
import com.utils.log.KeelLog;

/**
 * 字符串的一些工具类，如分隔
 * 
 * @author archko
 */
public class StringUtils {

	public static final NaturalStringComparator NSC = new NaturalStringComparator();

	public static final NaturalFileComparator NFC = new NaturalFileComparator();

	private StringUtils() {

	}

	/**
	 * Cleanup title. Remove from title file extension and (...), [...]
	 */
	public static String cleanupTitle(final String in) {
		String out = in;
		try {
			out = in.substring(0, in.lastIndexOf('.'));
			out = out.replaceAll("\\(.*?\\)|\\[.*?\\]", "")
					.replaceAll("_", " ").replaceAll(".fb2$", "").trim();
		} catch (final IndexOutOfBoundsException e) {
		}
		return out;
	}

	public static String md5(final String in) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
			digest.reset();
			digest.update(in.getBytes());
			final byte[] a = digest.digest();
			final int len = a.length;
			final StringBuilder sb = new StringBuilder(len << 1);
			for (int i = 0; i < len; i++) {
				sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
				sb.append(Character.forDigit(a[i] & 0x0f, 16));
			}
			return sb.toString();
		} catch (final NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Set<String> split(final String separator, final String value) {
		final Set<String> list = new LinkedHashSet<String>();

		final String[] split = value.split(separator);
		for (final String string : split) {
			if (LengthUtils.isNotEmpty(string)) {
				list.add(string);
			}
		}
		return list;
	}

	public static String merge(final String separator, final String... items) {
		final StringBuffer result = new StringBuffer();
		for (final String item : items) {
			if (LengthUtils.isNotEmpty(item)) {
				if (result.length() > 0) {
					result.append(separator);
				}
				result.append(item);
			}
		}
		return result.toString();
	}

	public static String merge(final String separator,
			final Collection<String> items) {
		final StringBuffer result = new StringBuffer();
		for (final String item : items) {
			if (LengthUtils.isNotEmpty(item)) {
				if (result.length() > 0) {
					result.append(separator);
				}
				result.append(item);
			}
		}
		return result.toString();
	}

	public static final int compareNatural(String firstString,
			String secondString) {
		int firstIndex = 0;
		int secondIndex = 0;

		Collator collator = Collator.getInstance();

		while (true) {
			if (firstIndex == firstString.length()
					&& secondIndex == secondString.length()) {
				return 0;
			}
			if (firstIndex == firstString.length()) {
				return -1;
			}
			if (secondIndex == secondString.length()) {
				return 1;
			}

			if (Character.isDigit(firstString.charAt(firstIndex))
					&& Character.isDigit(secondString.charAt(secondIndex))) {
				int firstZeroCount = 0;
				while (firstString.charAt(firstIndex) == '0') {
					firstZeroCount++;
					firstIndex++;
					if (firstIndex == firstString.length()) {
						break;
					}
				}
				int secondZeroCount = 0;
				while (secondString.charAt(secondIndex) == '0') {
					secondZeroCount++;
					secondIndex++;
					if (secondIndex == secondString.length()) {
						break;
					}
				}
				if ((firstIndex == firstString.length() || !Character
						.isDigit(firstString.charAt(firstIndex)))
						&& (secondIndex == secondString.length() || !Character
								.isDigit(secondString.charAt(secondIndex)))) {
					continue;
				}
				if ((firstIndex == firstString.length() || !Character
						.isDigit(firstString.charAt(firstIndex)))
						&& !(secondIndex == secondString.length() || !Character
								.isDigit(secondString.charAt(secondIndex)))) {
					return -1;
				}
				if ((secondIndex == secondString.length() || !Character
						.isDigit(secondString.charAt(secondIndex)))) {
					return 1;
				}

				int diff = 0;
				do {
					if (diff == 0) {
						diff = firstString.charAt(firstIndex)
								- secondString.charAt(secondIndex);
					}
					firstIndex++;
					secondIndex++;
					if (firstIndex == firstString.length()
							&& secondIndex == secondString.length()) {
						return diff != 0 ? diff : firstZeroCount
								- secondZeroCount;
					}
					if (firstIndex == firstString.length()) {
						if (diff == 0) {
							return -1;
						}
						return Character.isDigit(secondString
								.charAt(secondIndex)) ? -1 : diff;
					}
					if (secondIndex == secondString.length()) {
						if (diff == 0) {
							return 1;
						}
						return Character
								.isDigit(firstString.charAt(firstIndex)) ? 1
								: diff;
					}
					if (!Character.isDigit(firstString.charAt(firstIndex))
							&& !Character.isDigit(secondString
									.charAt(secondIndex))) {
						if (diff != 0) {
							return diff;
						}
						break;
					}
					if (!Character.isDigit(firstString.charAt(firstIndex))) {
						return -1;
					}
					if (!Character.isDigit(secondString.charAt(secondIndex))) {
						return 1;
					}
				} while (true);
			} else {
				int aw = firstIndex;
				int bw = secondIndex;
				do {
					firstIndex++;
				} while (firstIndex < firstString.length()
						&& !Character.isDigit(firstString.charAt(firstIndex)));
				do {
					secondIndex++;
				} while (secondIndex < secondString.length()
						&& !Character.isDigit(secondString.charAt(secondIndex)));

				String as = firstString.substring(aw, firstIndex);
				String bs = secondString.substring(bw, secondIndex);
				int subwordResult = collator.compare(as, bs);
				if (subwordResult != 0) {
					return subwordResult;
				}
			}
		}
	}

	@Deprecated
	public static Comparator<? super File> getNaturalFileComparator() {
		return NFC;
	}

	public static int split(char[] str, int begin, int len, int[] outStart,
			int[] outLength, boolean lineBreaksOnly) {
		if (str == null) {
			return 0;
		}
		if (len == 0) {
			return 0;
		}
		int i = begin, start = begin;
		int index = 0;
		boolean match = false;
		while (i < begin + len) {
			if ((lineBreaksOnly && (str[i] == 0x0D || str[i] == 0x0A))
					|| (!lineBreaksOnly && (str[i] == 0x20 || str[i] == 0x0D
							|| str[i] == 0x0A || str[i] == 0x09))) {
				if (match) {
					outStart[index] = start;
					outLength[index] = i - start;
					index++;
					match = false;
				}
				start = ++i;
				continue;
			}
			match = true;
			i++;
		}
		if (match) {
			outStart[index] = start;
			outLength[index] = i - start;
			index++;
		}
		return index;
	}

	public static final class NaturalStringComparator implements
			Comparator<String> {

		public int compare(String o1, String o2) {
			return compareNatural(o1, o2);
		}
	}

	public static final class NaturalFileComparator implements Comparator<File> {

		public int compare(File o1, File o2) {
			if (o1 == null) {
				return -1;
			}
			if (o2 == null) {
				return 1;
			}
			return compareNatural(o1.getAbsolutePath(), o2.getAbsolutePath());
		}
	}

	public static int parseInt(char[] string, int start, int length, int radix)
			throws NumberFormatException {
		if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX) {
			throw new NumberFormatException("Invalid radix: " + radix);
		}
		if (string == null) {
			throw new NumberFormatException(new String(string, start, length));
		}
		int i = 0;
		if (length == 0) {
			throw new NumberFormatException(new String(string, start, length));
		}
		boolean negative = string[start + i] == '-';
		if (negative && ++i == length) {
			throw new NumberFormatException(new String(string, start, length));
		}

		return parse(string, start, length, i, radix, negative);
	}

	private static int parse(char[] string, int start, int length, int offset,
			int radix, boolean negative) throws NumberFormatException {
		int max = Integer.MIN_VALUE / radix;
		int result = 0;
		while (offset < length) {
			int digit = Character.digit(string[start + (offset++)], radix);
			if (digit == -1) {
				throw new NumberFormatException(new String(string, start,
						length));
			}
			if (max > result) {
				throw new NumberFormatException(new String(string, start,
						length));
			}
			int next = result * radix - digit;
			if (next > result) {
				throw new NumberFormatException(new String(string, start,
						length));
			}
			result = next;
		}
		if (!negative) {
			result = -result;
			if (result < 0) {
				throw new NumberFormatException(new String(string, start,
						length));
			}
		}
		return result;
	}

	// 专门过滤空格
	public static String trim(String str) {
		if (TextUtils.isEmpty(str) || Constants.NULL.equals(str)
				|| "NULL".equals(str)) {
			return null;
		}
		// 过滤空格
		if (str.equals(" ") || str.trim().equals("")) {

			str.trim();

		}
		return str;
	}

	public static boolean isEquals(String str1, String str2) {
		if (!isEmpty(str1) && !isEmpty(str2) && str1.equals(str2)) {
			return true;
		}
		return false;
	}

	/**
	 * MD5 加密
	 * 
	 * @param str
	 * @return
	 */
	public static String getMD5Str(String str) {
		if (!StringUtils.isEmpty(str)) {
			MessageDigest messageDigest = null;
			try {
				messageDigest = MessageDigest.getInstance("MD5");
				messageDigest.reset();
				messageDigest.update(str.getBytes("UTF-8"));
			} catch (NoSuchAlgorithmException e) {
				if (KeelLog.DEBUG) {
					e.printStackTrace();
				}
				return null;
			} catch (UnsupportedEncodingException e) {
				if (KeelLog.DEBUG) {
					e.printStackTrace();
				}
				return null;
			}

			final byte[] byteArray = messageDigest.digest();

			final StringBuffer md5StrBuff = new StringBuffer();

			for (int i = 0; i < byteArray.length; i++) {
				if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
					md5StrBuff.append("0").append(
							Integer.toHexString(0xFF & byteArray[i]));
				} else {
					md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
				}
			}
			// 16位加密，从第9位到25位
			return md5StrBuff.substring(8, 24).toString().toUpperCase();
		}
		return "";
	}

	/**
	 * 替换字符串中 全部的,为空格.
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceAllComma2Spacing(String str) {
		if (!StringUtils.isEmpty(str)) {
			return str.replaceAll(",", " ");
		}
		return "";
	}

	/**
	 * 计算字符串中的字符长度 汉字长度为2 字母数字长度为1
	 * 
	 * @param value
	 * @return
	 */
	public static int String_length(String value) {
		int valueLength = 0;
		String chinese = "[\u4e00-\u9fa5]";
		for (int i = 0; i < value.length(); i++) {
			String temp = value.substring(i, i + 1);
			if (temp.matches(chinese)) {
				valueLength += 2;
			} else {
				valueLength += 1;
			}
		}
		return valueLength;
	}
	
	/**
	 * list转换成字符串
	 * @param lists
	 * @return
	 */
	public static String List2String(ArrayList<String> lists)
	{
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < lists.size(); i++) {
			if (i < lists.size()-1) {
				builder.append(lists.get(i));
				builder.append(",");
			}else {
				builder.append(lists.get(i));
			}
		}
		return builder.toString();
		
	}
	
	/**
	 * 判断是否为空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return (str == null) || str.trim().equalsIgnoreCase("null")
				|| (str.trim().length() < 1) || str.equals("");
	}

	/**
	 * 验证手机号码的合法性 支持+86,0+，170
	 * 
	 * @param mobile
	 * @return
	 */
	public static boolean checkMobile(String mobile) {

		

//		String regex = "((^(13|15|18)[0-9]{9}$)|(^0[1,2]{1}d{1}-?d{8}$)|"
//				+ "(^0[3-9] {1}d{2}-?d{7,8}$)|"
//				+ "(^0[1,2]{1}d{1}-?d{8}-(d{1,4})$)|"
//				+ "(^0[3-9]{1}d{2}-? d{7,8}-(d{1,4})$))";
		String regex = "^\\d+$";
		
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(mobile);
		return m.matches();
	}

	/**
	 * 验证邮箱地址的合法性
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		String regex = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(email);
		return m.matches();
	}

}
