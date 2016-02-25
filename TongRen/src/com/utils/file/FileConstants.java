package com.utils.file;

import android.os.Environment;

public class FileConstants
{
    // 通用资源目录，用来存放图片、debug log文件、流量统计文件
    public static final String RESOURCE_DIRECTORY = "/cjRes/";
    
    // 文件保存路径
	public static final String SAVE_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	
    // IM文件保存路径
    public static final String SAVE_FILE_PATH_DIRECTORY_IM = "imcache/";
   
    
    // 1.9以前的缓存文件夹
    public static final String[] oldCacheVersion = { "/imgfiles/", "/dxFiles/",
            "/rallyPoint/", "/imgfiles1.9/", "/dxFiles1.9/", "/rallyPoint1.9/",
            "/addRecord1.9/" };

    // 缓存图片的目录
    public static final String CACHE_IMG_DIR_PATH = "/imgfiles1.95/";
    //缓存裁剪图片
    public static final String CACHE_CLIP_PICTURE_PATH = "/imgfiles1.95/clipPicture";
    // 文件缓存
    public static final String CACHE_FILE_PATH = "/dxFiles1.95/";
    // 聚点发言缓存的目录
    public static final String CACHE_THREADLIST_PATH = "/rallyPoint1.95/";
    // 添加记录缓存
    public static final String CACHE_ADD_RECORD_PATH = "/addRecord1.95/";
    // 完整城市列表缓存
    public static final String CACHE_DX_CITY_LIST_FILEPATH = "dxCityList.dat";
    // 省份列表缓存
    public static final String CACHE_DX_PROVINCE_LIST_FILEPATH = "dxProvinceList.dat";
    // 简化后的城市列表缓存
    public static final String CACHE_DX_CITY_LIST_SIMPLIFY_FILEPATH = "dxCityListSimplify.dat";
    
    public static final String CACHE_DX_MY_FAVORITE_HOTEL = "myFavoriteHotel.dat";
    public static final String CACHE_DX_MY_HISTORY_HOTEL = "myHistoryHotel.dat";
    // 最近浏览缓存
    public static final String CACHE_DX_MY_RECENTLY_VIEWED = "myDXRecentlyViewed.dat";
    // 初见订单缓存
    public static final String CACHE_DX_MY_RESERVATION_DX = "myReservationDX.dat";
    // 7天订单缓存
    public static final String CACHE_DX_MY_RESERVATION_7DAY = "myReservation7day.dat";
    // 7天城市列表 TODO 1.9 已作废
    public static final String CACHE_7DAY_CITY_LIST_FILEPATH = "7DayCityList.dat";
    // 周边分类缓存
    public static final String CACHE_CATEGORY_LIST_FILEPATH = "categoryList.dat";
    // 获取首页分组图缓存
    public static final String CACHE_GETHOMEINDEX_FILEPATH = "homeGrid.dat";
    // 广告 缓存
    public static final String CACHE_NOTICE_FILEPATH = "notice.dat";
    // 优惠缓存
    public static final String CACHE_DXACTIVITYITEM = "dxActivityItem.dat";
    // 聚点首页发言列表缓存目录
    public static final String CACHE_THREAD_LIST_RALLYPOINT = "ThreadListByRallyPoint.dat";
    // 我的发言缓存
    public static final String CACHE_MY_THREAD_LIST = "MyThreadList.dat";
    // 评论我的缓存
    public static final String CACHE_COMMENT_ME_LIST = "CommentMeList.dat";
    // @我的缓存
    public static final String CACHE_ATME_THREAD_LIST = "AtMeThreadList.dat";
    // 我的私信缓存
    public static final String CACHE_MY_PRIVATEMESSAGE_LIST = "MyPrivateMessageList.dat";
    // 聚点点友缓存
    public static final String CACHE_USER_ROUND_POINT = "UserRoundPoint.dat";
    // 用户输入缓存
    public static final String CACHE_USER_ASSOCIATELIB = "userAssociateLib.dat";
    // IM图片url缓存路径
//    public static final String CACHE_IM_URL_MAP = SAVE_FILE_PATH_DIRECTORY + "imcache/" + "urlmap";// TODO 2.8屏蔽
    
    // 流量统计文件路径
    public static final String apiTrafficStatsPath = SAVE_FILE_PATH + RESOURCE_DIRECTORY + "ApiTrafficStats.txt";
    public static final String imageTrafficStatsPath = SAVE_FILE_PATH + RESOURCE_DIRECTORY + "DownloadImageTrafficStats.txt";
    //热词缓存
    public static final String CACHE_HOTWORDS = "hotWords.dat";
    //朋友圈的缓存
    public static final String FRIENDSCIRCLEFILE = "friendsCircleFile.dat";
    //优惠缓存
    public static final String CACHE_DXACTIVITY = "dxactivity";
}
