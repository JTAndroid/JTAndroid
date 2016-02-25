package com.tr.test;

import android.test.AndroidTestCase;

import com.google.gson.Gson;
import com.tr.api.ConferenceReqUtil;
import com.utils.http.IBindData;

public class HyTestAPI extends AndroidTestCase implements IBindData {
	
	public void test() {
		//
		
		ConferenceReqUtil.doGetMeetingDetail(getContext(), this, 633, 14561+"", null);
		int i=0;
	}

	@Override
	public void bindData(int tag, Object object) {
		object.toString();
		int i=0;
	}
	
}
