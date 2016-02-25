package com.utils.common;

import android.os.Environment;

/**
 * 全局变量
 */
public class GlobalVariable {
	
    public final static String PRINT_NETWORK_STATUS = "print_network_status";
	
	/*保存Image的目录名 */
    public final static String FOLDER_NAME = "/GinTong/Image/";
    
    public static final String LOG_PATH=Environment.getExternalStorageDirectory().getAbsolutePath()+"/TongRen/TongRen_log.txt";
    
    public static final String SHARED_PREFERENCES_MEETING_NEW_COUNT = "shared_preferences_meeting_new_count";

    public static final String MEETING_NEW_COUNT_KEY = "meeting_new_count_key";
    
    public static final String SHARED_PREFERENCES_NOTICE_NEW_COUNT = "shared_preferences_notice_new_count";
    
    public static final String NOTICE_NEW_COUNT_KEY = "notice_new_count_key";
    
    public static final String SHARED_PREFERENCES_SOCIAL_ISFISTLOAD = "shared_preferences_social_isfistload";
    
    public static final String SOCIAL_ISFISTLOAD = "social_isFirstLoadData";
    
    public static final String ORG_DEFAULT_AVATAR = "organ/avatar/default.jpeg";
    
    public static final String SHARED_PREFERENCES_FIRST_LOGIN_TIME = "shared_preferences_first_login_time";
    
    public static final String MAIN_FIRST_TIME = "main_first_time";
    
    public static final String MAIN_FIRST_FAMOUS_QUOTES_INDEX = "main_first_famous_quotes_index";
    
    //第一次使用
    public static final String SHARED_PREFERENCES_FIRST_USE = "shared_preferences_first_use";
    
    public static final String SHARED_PREFERENCES_INDEX_JSON = "shared_preferences_index_json";
    
    public static final String MAIN_FIRST_USE = "main_first_use";
    public static final String MAIN_INDEX_JSON = "main_index_json";
    
    public static final String MAIN_CREATE_FIRST_USE = "main_create_first_use";
    
    public static final String KNOWLEDGE_FIRST_USE = "knowledge_first_use";
    
    public static final String QRCODE_FIRST_USE = "qrcode_first_use";
    
    public static final String PERSON_DEFAULT_AVATAR = "default.jpeg";
    public static final Boolean ISNETWORK_IMG_URL=true;
    
//    public static final String PERSON_DEFAULT_AVATAR2 = "pic/user/default.jpeg";
    
    public static final String ORGANIZATION_ROLE_KEY = "organization_role_key";
    public static final String CUSTOMER_ROLE_KEY = "customer_role_key";
    public static final String USER_ROLE_KEY = "user_role_key";
    public static final String CONTACTS_ROLE_KEY = "contacts_role_key";
    
    public static final String COMMUNITY_ID="communityId";// 社群id
    public static final String COMMUNITY_TITLE="communitytitle";// 社群名称
    public static final String COMMUNITY_ISNUMIN="community_isnumin";// 是否群号加群
	/**
	 * 分享转发
	 */
	public class ForwardAndShareType{
		public final static int TYPE_REQUIREMENT = 15;//需求
	}
	
	/**
	 * 金桐APP role
	 * Value:根据服务器传参需求拟定（可以随时变更）
	 */
	public enum HomePageInformation {
	    // 利用构造函数传参
		organization(0,ORGANIZATION_ROLE_KEY), Customer(1,CUSTOMER_ROLE_KEY), user(2,USER_ROLE_KEY), Contacts(3,CONTACTS_ROLE_KEY);

	    // 定义私有变量
	    private int value;
	    public String roleStr;
	    
	    // 构造函数，枚举类型只能为私有
	    private HomePageInformation(int _value,String str) {
	        this.value = _value;
	        this.roleStr = str;
	    }

	    @Override
	    public String toString() {
	        return String.valueOf(this.value);// 转换到字符串
	    }

	    public int value() {
	        return this.value;
	    }
	}
}
