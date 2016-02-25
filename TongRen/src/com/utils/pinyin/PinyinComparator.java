package com.utils.pinyin;

import java.util.Comparator;

import com.tr.model.obj.Connections;

public class PinyinComparator implements Comparator{

	@Override
	public int compare(Object o1, Object o2) {
		Connections a=(Connections)o1;
		Connections b=(Connections)o2;
		String str1=a.getName();
		String str2=b.getName();
		  str1 = PingYinUtil.getPingYin(str1).toUpperCase();
	      str2 = PingYinUtil.getPingYin(str2).toUpperCase();
	     return str1.compareTo(str2);
	}

}
