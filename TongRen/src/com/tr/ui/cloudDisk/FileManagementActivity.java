package com.tr.ui.cloudDisk;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.App;
import com.tr.R;
import com.tr.api.CloudDiskReqUtil;
import com.tr.model.obj.JTFile;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.cloudDisk.FileManagementActivity.MakeFileManagementAdapter.ViewHolder;
import com.tr.ui.cloudDisk.model.FileCategoryManager;
import com.tr.ui.cloudDisk.model.FileManagerResponseData;
import com.tr.ui.cloudDisk.model.UserCategory;
import com.tr.ui.cloudDisk.model.UserDocument;
import com.tr.ui.cloudDisk.view.NavigationBarView;
import com.tr.ui.cloudDisk.view.NavigationBarView.OnGetDataClickListener;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.demand.util.FileUploader;
import com.tr.ui.demand.util.FileUploader.OnFileUploadListenerEx;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.organization.utils.CommonUtils;
import com.tr.ui.widgets.CreateCatalogAlertDialog;
import com.tr.ui.widgets.CreateCatalogAlertDialog.OnEditDialogClickListener;
import com.utils.common.EUtil;
import com.utils.common.TaskIDMaker;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.log.ToastUtil;
import com.utils.string.StringUtils;

public class FileManagementActivity extends JBaseFragmentActivity implements
		IBindData, OnClickListener ,OnFileUploadListenerEx{
	
	//文件上传
	
	/* 记录选中的条目数量 */
	private int checkNum = 0;
	private static String typetmp = "";

	private boolean isSelectALL = false;//是否全选
	private boolean isCancleALL = false;//是否全选
	
	//是否是从上传文件保存到目录
	protected boolean isFromUpload =false;
	
	/* 填充数据的list */
	private ArrayList<Object> categoryAndDocument = new ArrayList<Object>();
	private MakeFileManagementAdapter mMakeFileManagementAdapter;
	private boolean isShowCheckBox = false;
	protected boolean isLongClick = false;

	//从畅聊保存文件时文件ID和要保存到的目录ID
	private StringBuilder categoryIds = new StringBuilder();
	private StringBuilder fileIds = new StringBuilder();
	
	private ArrayList<String> DeleteDocumentIdsLists = new ArrayList<String>();//删除时文件ID列表
	private ArrayList<String> DeleteCategoryIdsLists = new ArrayList<String>();//删除时目录ID列表
	private ArrayList<UserDocument> ReNameUserDocumentLists = new ArrayList<UserDocument>();//重命名目录列表
	
	private String saveFileId = "";//从会议和畅聊保存文件时 被保存的文件的ID
	private ArrayList<String> ChatSaveFileToCategoryIdsLists = new ArrayList<String>();//用于保存 “从畅聊和会议保存过来的文件”的ID列表
	
	private ArrayList<String> renameLists = new ArrayList<String>();//重命名名称暂存list
	private static ArrayList<Object> reCreateNameLists = new ArrayList<Object>();//重命名和新建文件夹时考虑是否重名
	
	private enum FileType {
		Category, Document
	}

	private FileType fileType = FileType.Category;
	
	

	/*保存文件目录关系*/
	public final static String SAVE_FILE_ID = "savefileid";
	public static String ISCHATSAVEFILE = "ischatsavefile";
	public static String ISSHOWCHECKBOX = "isshowcheckbox";
	public static String ISFROMUPLOAD = "isFromUpload";
	
	@SuppressWarnings("unused")
	private View view;
	private RelativeLayout showFiflterLl;
	private PopupWindow popupWindow;
	private LinearLayout filter_picture;// 筛选图片
	private LinearLayout filter_document;// 筛选文档文件
	private LinearLayout filter_music;// 筛选音乐文件
	private LinearLayout filter_video;// 筛选视频文件
	private LinearLayout filter_all;// 筛选全部文件
	/* 动态展示XListView */
	private XListView mListView;
	// /* 动态适配器 */
//	 private FileManagementAdapter mFileManagementAdapter;
	private ArrayList<FileCategoryManager> categories;

	private android.app.ActionBar actionbar;

	@SuppressWarnings("unused")
//	private LinearLayout fileManagementInclude;
	private EditText fileSearchEt_navigationbar;// 文件搜索框
	private LinearLayout file_navigation_Ll;

	private TextView delete_file_document;// 删除文件目录按钮
	private TextView delete_file_document_gray;// 删除文件目录按钮、灰色
	private TextView rename_file_document;//重命名文件目录按钮
	private TextView rename_file_document_gray;//重命名文件目录按钮、灰色
	private TextView tipTv; //新建和重命名目录标题
	private TextView mytipTv; //新建和重命名目录标题
	private TextView myCtipTv; //新建和重命名目录标题
	private String UserChangeCategoryOrDocumentParentID = "0";//重命名的文件或者目录的parentID

	private View fileManagementInclude_suspension;

	private ArrayList<Object> allCategoryDocumentLists;
	@SuppressWarnings("unused")
	private ArrayList<UserCategory> fileDocumentLists;// 目录文件集合
	private MenuItem selectAll;

	private MenuItem reverseSelection;
	
	private MenuItem confirmSelection;

	private int UserCategoryLevel = 0;
	private String UserCurrentCategoryID = 0+"";
	private String UserCategorySortID = "";//目录文件中的排序ID
	private ArrayList<UserCategory> UserParentCategoryLists = new ArrayList<UserCategory>();
	private ArrayList<JTFile> fileListSD = new ArrayList<JTFile>();// 本地
	//搜索时返回上一级目录
	private boolean isSearch = false;
	private static String SearchParentID = "0";
	private String searchKey;
	private String showToast = "";//显示“添加成功”和“修改成功”
	/*返回上一级界面的阀值*/
	private final int BACK_THRESHOLD = 0;
	private static boolean isChatSaveFile = false;
	private static boolean doBackKey = true;
	//枚举判断目录类型
	public enum OperateType{
		Create,
		Update
	}
	private OperateType mOperateType = OperateType.Create;
	//创建对话框对象
	private CreateCatalogAlertDialog mCreateCatalogAlertDialog;
	private CreateCatalogAlertDialog deleteCreateCatalogAlertDialog;
	private ImageView file_classification;
	private ImageView file_managemen_upload;
	private Editor editor;
	private static TextView recomand_tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_management);
		
		upLoadParmar = getSharedPreferences("upLoad",Activity.MODE_PRIVATE);
		editor = upLoadParmar.edit();
		
		mCreateCatalogAlertDialog = new CreateCatalogAlertDialog(FileManagementActivity.this);
		deleteCreateCatalogAlertDialog = new CreateCatalogAlertDialog(FileManagementActivity.this);
		allCategoryDocumentLists = new ArrayList<Object>();
		saveFileId = getIntent().getStringExtra(SAVE_FILE_ID);
		isChatSaveFile = getIntent().getBooleanExtra(ISCHATSAVEFILE, false);
		isShowCheckBox = getIntent().getBooleanExtra(ISSHOWCHECKBOX, false);
		isFromUpload = getIntent().getBooleanExtra(ISFROMUPLOAD, false);
		initializeControl();
		initJabActionBar();
		initParmeter();//初始化必要参数

		/* 目录分类 */
//		showFiflterLl.setOnClickListener(this);
		categories = new ArrayList<FileCategoryManager>();
		mListView = (XListView) findViewById(R.id.file_listview);
		mListView.showFooterView(false);
		// 设置xlistview可以加载、刷新
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(true);

		mListView.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				/* 访问服务器 */
				startGetData(UserCurrentCategoryID);
			}

			@Override
			public void onLoadMore() {
				startGetData(UserCurrentCategoryID);
			}
		});
		mListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				return false;
			}
		});

		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});
		
	}

	private void initParmeter() {
		startGetData(UserCurrentCategoryID);
	}
	
	/**
	 * 显示提示语句
	 */
	private void showRecomand(boolean isShow) {
		if (isShow) {
			recomand_tv.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
		} else {
			recomand_tv.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);
		}
	}
	

	/** 初始化控件 */
	private void initializeControl() {
		tipTv = (TextView) mCreateCatalogAlertDialog.findViewById(R.id.tipTv);
		recomand_tv = (TextView) findViewById(R.id.recomand_tv);
		showFiflterLl = (RelativeLayout) findViewById(R.id.showFiflterLl);
		file_classification = (ImageView) findViewById(R.id.file_classification);
		file_managemen_upload = (ImageView) findViewById(R.id.file_managemen_upload);
		file_classification.setOnClickListener(this);
		file_managemen_upload.setOnClickListener(this);
		if (isChatSaveFile) {
			showFiflterLl.setVisibility(View.GONE);
			isShowCheckBox = true;
		}
		file_navigation_Ll = (LinearLayout) findViewById(R.id.file_navigation_Ll);
		LinearLayout file_root = (LinearLayout) findViewById(R.id.file_root);
//		fileManagementInclude = (LinearLayout) findViewById(R.id.fileManagementInclude);
		
		NavigationBarView navigationBarView = new NavigationBarView(FileManagementActivity.this);
		fileSearchEt_navigationbar = navigationBarView.getFileSearchEt_navigationbar();
		
		fileManagementInclude_suspension = findViewById(R.id.fileManagementInclude_suspension);
		
		// 底层删除按钮
		delete_file_document = (TextView) findViewById(R.id.delete_file_document);
		delete_file_document_gray = (TextView) findViewById(R.id.delete_file_document_gray);
		delete_file_document.setOnClickListener(this);
		// 底层重命名按钮
		rename_file_document = (TextView) fileManagementInclude_suspension.findViewById(R.id.rename_file_document);
		rename_file_document_gray = (TextView) fileManagementInclude_suspension.findViewById(R.id.rename_file_document_gray);
		rename_file_document.setOnClickListener(this);

		//让删除、重命名按钮不显示
		showBottomControlsUI(false);
		file_navigation_Ll.addView(navigationBarView,file_navigation_Ll.indexOfChild(showFiflterLl) + 1);
		navigationBarView.NoScreen(true);

		// 获取搜索的目录和文件列表
		navigationBarView.setOnGetDataClickListener(new OnGetDataClickListener() {

					@Override
					public void getListData(String keyWord) {
						searchKey = keyWord;
						CloudDiskReqUtil.searchFileDocument(FileManagementActivity.this, FileManagementActivity.this, null, UserCategorySortID, -1+"", keyWord, 0+"", -1+"", UserCurrentCategoryID);
						isSearch = true;
					}

					@Override
					public void showAlertDialog() {
						mOperateType = OperateType.Create;
						if(DeleteCategoryIdsLists.size()!=0)
						{
							typetmp = "category";
						}else {
							typetmp = "document";
						}
						tipTv.setText("新建目录");
						mCreateCatalogAlertDialog.show();
						mCreateCatalogAlertDialog.setIsDelete(false);
						mCreateCatalogAlertDialog.setOnDialogClickListener(editDialogClickListener);
					}
				});
	}

	/** 初始化控件 */
	public void initializePouWindow(View view) {
		filter_picture = (LinearLayout) view.findViewById(R.id.filter_picture);
		filter_picture.setOnClickListener(this);
		filter_document = (LinearLayout) view
				.findViewById(R.id.filter_document);
		filter_document.setOnClickListener(this);
		filter_music = (LinearLayout) view.findViewById(R.id.filter_music);
		filter_music.setOnClickListener(this);
		filter_video = (LinearLayout) view.findViewById(R.id.filter_video);
		filter_video.setOnClickListener(this);
		filter_all = (LinearLayout) view.findViewById(R.id.filter_all);
		filter_all.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rename_file_document:
			mOperateType = OperateType.Update;
			if(DeleteCategoryIdsLists.size()!=0)
			{
				typetmp = "category";
			}else {
				typetmp = "document";
			}
			tipTv.setText("重命名");
			mCreateCatalogAlertDialog.show();
			mCreateCatalogAlertDialog.setIsDelete(false);
			mCreateCatalogAlertDialog.setName("");
			mCreateCatalogAlertDialog.setOnDialogClickListener(editDialogClickListener);
			break;
		case R.id.file_managemen_upload:
			if ( upLoadParmar.getInt("allUpNum", 0) == upLoadParmar.getInt("success", 0) + upLoadParmar.getInt("faile", 0)) {
				ENavigate.startUploadFileAccessoryActivity(FileManagementActivity.this, fileListSD, 255, true);
			}else {
				ToastUtil.showToast(FileManagementActivity.this, "正在上传中,请等待上次上传结束");
				return;
			}
			break;
		case R.id.file_classification:
			showFiflterWindow(v);
			break;
		case R.id.filter_picture:
			showLoadingDialog();
			CloudDiskReqUtil.searchFileDocument(FileManagementActivity.this, FileManagementActivity.this, null, UserCategorySortID, 3+"", "", 0+"", -1+"", UserCurrentCategoryID);
			isSearch = true;
			popupWindow.dismiss();
			break;
		case R.id.filter_document:
			showLoadingDialog();
			CloudDiskReqUtil.searchFileDocument(FileManagementActivity.this, FileManagementActivity.this, null, UserCategorySortID, 2+"", "", 0+"", -1+"", UserCurrentCategoryID);
			isSearch = true;
			popupWindow.dismiss();
			break;
		case R.id.filter_video:
			showLoadingDialog();
			CloudDiskReqUtil.searchFileDocument(FileManagementActivity.this, FileManagementActivity.this, null, UserCategorySortID, 0+"", "", 0+"", -1+"", UserCurrentCategoryID);
			isSearch = true;
			popupWindow.dismiss();
			break;
		case R.id.filter_music:
			showLoadingDialog();
			CloudDiskReqUtil.searchFileDocument(FileManagementActivity.this, FileManagementActivity.this, null, UserCategorySortID, 1+"", "", 0+"", -1+"", UserCurrentCategoryID);
			isSearch = true;
			popupWindow.dismiss();
			break;
		case R.id.filter_all:
			showLoadingDialog();
			startGetData(UserCurrentCategoryID);
			isSearch = true;
			popupWindow.dismiss();
			break;
		case R.id.delete_file_document://删除文件（包含多选）
			
			deleteCreateCatalogAlertDialog.show();
			deleteCreateCatalogAlertDialog.initDeleteDialog();
			deleteCreateCatalogAlertDialog.setOnDialogClickListener(new OnEditDialogClickListener() {
				@Override
				public void onClick(String evaluationValue) {
					if (evaluationValue == null) {
						
					} else {
						showLoadingDialog();
						CloudDiskReqUtil.deleteFileDoucment(FileManagementActivity.this,FileManagementActivity.this, null, StringUtils.List2String(DeleteCategoryIdsLists),StringUtils.List2String(DeleteDocumentIdsLists), UserCurrentCategoryID);
						DeleteCategoryIdsLists.clear();
						DeleteDocumentIdsLists.clear();
						initMyActionBar("已选"+(0)+"项");
					}
				}
			});
			break;
		default:
			break;
		}

	}
	
	
	

	@Override
	public void bindData(int tag, Object object) {
		dismissLoadingDialog();
		//XlistView结束刷新时状态复位
		 mListView.stopLoadMore();
		 mListView.stopRefresh();
		// 如果页面已暂停，丢弃数据
		if (tag == EAPIConsts.HomeReqType.HOME_REQ_GET_DYNAMIC) {
			
		}
		// 删除用户目录或文件
		if (tag == EAPIConsts.CloudDiskType.fileDocumentDelete) {
			dismissLoadingDialog();
			if (object != null) {
				String succeed = (String) object;
				if ("true".equals(succeed)) {
					Toast.makeText(FileManagementActivity.this, "删除成功", 1).show();
//					mMakeFileManagementAdapter.notifyDataSetChanged();
//					checkNum = 0;
//					showBottomRechristenUI(checkNum);
//					renameLists.clear();
					startGetData(UserCurrentCategoryID);
					onBackPressed();
				} else {
					Toast.makeText(FileManagementActivity.this, "删除失败", 1).show();
				}
			}
		}
		//查询所有文件和目录
		if (tag == EAPIConsts.CloudDiskType.queryAllRCategory) {
			FileCategoryManager fileCategoryManager = (FileCategoryManager) object;
			if (fileCategoryManager != null) {
				allCategoryDocumentLists.clear();
				if (fileCategoryManager.categorys != null && !fileCategoryManager.categorys.isEmpty()) {
					for (int i = 0; i < fileCategoryManager.categorys.size(); i++) {
						UserCategory userCategory = fileCategoryManager.categorys.get(i);
						allCategoryDocumentLists.add(userCategory);
					}
				}
				if (fileCategoryManager.files != null && !fileCategoryManager.files.isEmpty()) {
					for (int i = 0; i < fileCategoryManager.files.size(); i++) {
						UserDocument jtFile = fileCategoryManager.files.get(i);
						allCategoryDocumentLists.add(jtFile);
					}
				}
				if (isChatSaveFile) {
					ArrayList<Object> chatCategoryLists = new ArrayList<Object>();//当从畅聊保存文件时的文件列表
					for (Object obj : allCategoryDocumentLists) {
						if (isCategory(obj)) {
							chatCategoryLists.add(obj);
						}
					}
					showMakeFileManagementUI(FileManagementActivity.this, mListView, chatCategoryLists);
				}else {
					showMakeFileManagementUI(FileManagementActivity.this, mListView, allCategoryDocumentLists);
				}
			}
			DeleteCategoryIdsLists.clear();
			DeleteDocumentIdsLists.clear();
		}
		if (tag == EAPIConsts.CloudDiskType.searchFileDocument) {
			if (object != null) {
				HashMap<String, Object> filelist = (HashMap<String, Object>) object;
				ArrayList<UserDocument> files = (ArrayList<UserDocument>) filelist.get("list");
				ArrayList<Object> allFileLists = new ArrayList<Object>();
				if (files != null&& !files.isEmpty()) {
					for (int i = 0; i < files.size(); i++) {
						UserDocument jtFile = files.get(i);
						allFileLists.add(jtFile);
					}
				}
				showMakeFileManagementUI(FileManagementActivity.this, mListView, allFileLists);
			}
		}
		// 保存文件目录关系
		if (tag == EAPIConsts.CloudDiskType.CHAT_SAVE_CATEGORY) {
			if (object != null) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> dataMap = ((HashMap<String, Object>) object);
				Boolean succeed =(Boolean) dataMap.get("succeed");
				if (succeed) {
					if (isFromUpload) {
						if (upLoadParmar.getInt("allUpNum", 0) == upLoadParmar.getInt("success", 0) + upLoadParmar.getInt("faile", 0)) {
							//上传成功刷新当前列表
							startGetData(UserCurrentCategoryID);
							CommonUtils.dismissLoadingDialog();
							if (upLoadParmar.getBoolean("isUpSucess", true)) {
								ToastUtil.showToast(FileManagementActivity.this, "上传成功");
							} else {
								ToastUtil.showToast(FileManagementActivity.this, "有"+upLoadParmar.getInt("faile", 0)+"个文件上传失败");
							}
							editor.putInt("allUpNum", 0);
							editor.putInt("success", 0);
							editor.putInt("faile", 0);
							editor.putString("upFileCategoryId", "");
							editor.putBoolean("isUpSucess", true);
							editor.commit();
						}
					} else {
						Toast.makeText(FileManagementActivity.this, "保存成功", 1).show();
						confirmSelection.setVisible(false);
						onBackPressed();
						//保存成功刷新当前列表
						startGetData(UserCurrentCategoryID);
					}
				} else {
					Toast.makeText(FileManagementActivity.this, "保存失败", 1).show();
				}
			}
		}
		// 重命名文件
		if (tag == EAPIConsts.CloudDiskType.renamefile) {
			if (object != null) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> dataMap = ((HashMap<String, Object>) object);
				Boolean succeed =(Boolean) dataMap.get("succeed");
				if (succeed) {
					onBackPressed();
					Toast.makeText(FileManagementActivity.this, showToast, 0).show();
					mCreateCatalogAlertDialog.setName("");
					startGetData(UserCurrentCategoryID);
//					setShowCheckNum("已选"+(0)+"项");
				} else {
					Toast.makeText(FileManagementActivity.this, "修改失败", 1).show();
				}
			}
		}
		// 添加或修改目录
		if (tag == EAPIConsts.CloudDiskType.categorySaveOrUpdate) {
			FileManagerResponseData succeed_create = (FileManagerResponseData) object;
			if (succeed_create != null) {
				if (Boolean.parseBoolean(succeed_create.succeed)){
					Toast.makeText(FileManagementActivity.this, showToast, 0).show();
					mCreateCatalogAlertDialog.setName("");
					startGetData(UserCurrentCategoryID);
					if ("修改成功".equals(showToast)) {
						onBackPressed();
					}
					if (isShowCheckBox) {
						initMyActionBar("已选"+(0)+"项");
					}
				}else{
					Toast.makeText(FileManagementActivity.this, succeed_create.resultMessage, 0).show();
				}
			}
		}
	}


	@Override
	public void initJabActionBar() {
		actionbar = getActionBar();
		actionbar.setSplitBackgroundDrawable(getResources().getDrawable(
				R.drawable.auth_title_back));
		// 设置actionbar的背景图
		Drawable myDrawable = getResources().getDrawable(
				R.drawable.auth_title_back);
		actionbar.setBackgroundDrawable(myDrawable); // 设置背景图片
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		HomeCommonUtils.initLeftCustomActionBar(this, actionbar, "文件", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}
	
	private void initMyActionBar(String titleName) {
		actionbar.setTitle(titleName);
	}
	
	/**
	 * 展示分类列表
	 * 
	 * @param parent
	 */
	private void showFiflterWindow(View parent) {
		if (popupWindow == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.search_classification, null);
			initializePouWindow(view);
//			float scale = FileManagementActivity.this.getResources()
//					.getDisplayMetrics().density;
//			int fiflterHeight = (int) getResources().getDimension(LayoutParams.WRAP_CONTENT);
			// 创建一个PopuWidow对象
			popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		}
		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		// 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
		int xPos = windowManager.getDefaultDisplay().getWidth() / 2 - popupWindow.getWidth() / 2;
		popupWindow.setAnimationStyle(R.style.popWindow_animation);
		popupWindow.showAsDropDown(parent, xPos, 0);
	}

	@Override
	public void onBackPressed() {
		isLongClick = false;
		if (isSearch) {
			startGetData(UserCurrentCategoryID);
			isSearch = false;
			return ;
		} else {
			boolean normal = true;
			if (UserParentCategoryLists.size() > BACK_THRESHOLD) {
				if (!isChatSaveFile) {
					if (isShowCheckBox) {
						isShowOrGone(false);
						isShowCheckBox = false;
						//隐藏底部按钮
						showBottomControlsUI(false);
						//回到编辑前页面
						startGetData(UserChangeCategoryOrDocumentParentID);
						UserCurrentCategoryID = UserChangeCategoryOrDocumentParentID;
						normal = false;
					}
				}
				if (normal) {
					startGetData(UserParentCategoryLists.get(UserParentCategoryLists.size() - 1).parentId);
					UserCurrentCategoryID = UserParentCategoryLists.get(UserParentCategoryLists.size() - 1).parentId;
					UserParentCategoryLists.remove(UserParentCategoryLists.get(UserParentCategoryLists.size() - 1));
				}
				initMyActionBar((UserParentCategoryLists.size() == 0)?"文件":UserParentCategoryLists.get(UserParentCategoryLists.size() - 1).categoryname);
				checkNum = 0;
				clearAll();
				doBackKey = true;
				return ;
			}else if (UserParentCategoryLists.size() == BACK_THRESHOLD) {
				if (!isChatSaveFile) {
					if (isShowCheckBox) {
						isShowOrGone(false);
						initMyActionBar("文件");
						UserCurrentCategoryID = "0";
						isShowCheckBox = false;
						//隐藏底部按钮
						showBottomControlsUI(false);
						checkNum = 0;
						clearAll();
						return ;
					}
				}
				checkNum = 0;
				clearAll();
				doBackKey = true;
			}
		}
		super.onBackPressed();
	}

	
	/**
	 * 清楚临时存储列表
	 */
	private void clearAll() {
		DeleteCategoryIdsLists.clear();
		DeleteDocumentIdsLists.clear();
		ChatSaveFileToCategoryIdsLists.clear();
	}

	/**
	 * 显示右上角UI
	 * 
	 * @param bool
	 */
	private void showMenuItemUI(boolean bool) {
		selectAll.setVisible(bool);
		reverseSelection.setVisible(!bool);
	}
	/**
	 * 显示右上角UI
	 * 
	 * @param bool
	 */
	private void isShowOrGone(boolean bool) {
		selectAll.setVisible(bool);
		reverseSelection.setVisible(bool);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.filter_select_all: // 全选
			selectALL();
			showMenuItemUI(false);
			break;
		case R.id.filter_reverse_selection: // 反选
			cancleALL();
			showMenuItemUI(true);
			break;
		case R.id.filter_confirm_selection://从畅聊保存目录
			if(!TextUtils.isEmpty(saveFileId)){//保存目录
				String listStrings =  "";
				if (ChatSaveFileToCategoryIdsLists.size() == 0) {
					listStrings = "0";
				} else {
					listStrings = StringUtils.List2String(ChatSaveFileToCategoryIdsLists);
				}
				CloudDiskReqUtil.doChatSaveCategory(FileManagementActivity.this,FileManagementActivity.this, null,saveFileId,listStrings);
			}
			finish();
			break;
		case android.R.id.home:
			isLongClick = false;
			if (isSearch) {
				startGetData(UserCurrentCategoryID);
				isSearch = false;
				checkNum = 0;
				clearAll();
				return false;
			} else {
			boolean normal = true;
			if (UserParentCategoryLists.size() > BACK_THRESHOLD) {
				if (!isChatSaveFile) {
					if (isShowCheckBox) {
						isShowOrGone(false);
						isShowCheckBox = false;
						//隐藏底部按钮
						showBottomControlsUI(false);
						//回到编辑前页面
						startGetData(UserChangeCategoryOrDocumentParentID);
						UserCurrentCategoryID = UserChangeCategoryOrDocumentParentID;
						normal = false;
					}
				}
				if (normal) {
					startGetData(UserParentCategoryLists.get(UserParentCategoryLists.size() - 1).parentId);
					UserCurrentCategoryID = UserParentCategoryLists.get(UserParentCategoryLists.size() - 1).parentId;
					UserParentCategoryLists.remove(UserParentCategoryLists.get(UserParentCategoryLists.size() - 1));
				}
				initMyActionBar((UserParentCategoryLists.size() == 0)?"文件":UserParentCategoryLists.get(UserParentCategoryLists.size() - 1).categoryname);
				checkNum = 0;
				clearAll();
				doBackKey = true;
				return false;
			}else if (UserParentCategoryLists.size() == BACK_THRESHOLD) {
				if (!isChatSaveFile) {
					if (isShowCheckBox) {
						isShowOrGone(false);
						initMyActionBar("文件");
						UserCurrentCategoryID = "0";
						isShowCheckBox = false;
						//隐藏底部按钮
						showBottomControlsUI(false);
						checkNum = 0;
						clearAll();
						return false;
					}
				}
				checkNum = 0;
				clearAll();
				doBackKey = true;
			}
		}
		break;
		}
		return super.onOptionsItemSelected(item);
	}
	

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		getMenuInflater().inflate(R.menu.activity_filter, menu);
		selectAll = menu.findItem(R.id.filter_select_all);
		reverseSelection = menu.findItem(R.id.filter_reverse_selection);
		confirmSelection = menu.findItem(R.id.filter_confirm_selection);
		if(isChatSaveFile){//开启
			showChatSaveFileUI();
		}
		return super.onCreateOptionsMenu(menu);
	}
	
	
	/**
	 * 控制删除、重命名的显示和删除
	 * @param checkNum
	 */
	private void showBottomRechristenUI(int checkNum){
		if (checkNum == this.checkNum && checkNum != 0) {
			showMenuItemUI(false);
		}else{
			showMenuItemUI(true);
		};
		if (checkNum == 0) {
			delete_file_document.setVisibility(View.GONE);
			delete_file_document_gray.setVisibility(View.VISIBLE);
			rename_file_document.setVisibility(View.GONE);
			rename_file_document_gray.setVisibility(View.VISIBLE);
		}else if (checkNum == 1) {
			delete_file_document.setVisibility(View.VISIBLE);
			delete_file_document_gray.setVisibility(View.GONE);
			rename_file_document.setVisibility(View.VISIBLE);
			rename_file_document_gray.setVisibility(View.GONE);
		}else if(checkNum >= 2){
			delete_file_document.setVisibility(View.VISIBLE);
			delete_file_document_gray.setVisibility(View.GONE);
			rename_file_document.setVisibility(View.GONE);
			rename_file_document_gray.setVisibility(View.VISIBLE);
		}
	}
	/**
	 * 控制删除、重命名的显示和隐藏
	 * @param checkNum
	 */
	private void showBottomControlsUI(boolean isShow){
		if (isShow) {
			fileManagementInclude_suspension.setVisibility(View.VISIBLE);
		} else {
			fileManagementInclude_suspension.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 畅聊保存目录界面
	 */
	private void showChatSaveFileUI(){
		actionbar.setTitle("请选择目录");
		selectAll.setVisible(false);
		mListView.setPullRefreshEnable(false);
		reverseSelection.setVisible(false);
		confirmSelection.setVisible(true);
	}
	
	/**
	 * 新建或修改名字时是否包含有重名的文件
	 * @param name
	 * @param type 值为：category 是目录  document 是文件
	 * @return
	 */
	public static boolean isContainName(String name) {
		for (Object obj : reCreateNameLists) {
			if (typetmp.equals("category")) {
				if (obj instanceof UserCategory && ((UserCategory) obj) != null) {
					if (((UserCategory) obj).categoryname.equals(name)) {
						return true;
					}
				}
			} else {
				if (obj instanceof UserDocument && ((UserDocument) obj) != null) {
					if (((UserDocument) obj).fileName.equals(name)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	/**
	 * 显示
	 */
	private void showCommonUI(){
		actionbar.setTitle("文件");
		selectAll.setVisible(false);
		reverseSelection.setVisible(false);
	}
	
	/**
	 * 给创建或更新目录对话框设置监听
	 */
	OnEditDialogClickListener editDialogClickListener = new OnEditDialogClickListener() {
		
		@Override
		public void onClick(String evaluationValue) {
			if (TextUtils.isEmpty(evaluationValue)) {
				return;
			}
			if (evaluationValue != null) {

				switch (mOperateType) {
				case Create://创建目录
					showToast = "添加成功";
					CloudDiskReqUtil.getCategorySaveOrUpdate(FileManagementActivity.this,FileManagementActivity.this, null,mCreateCatalogAlertDialog.getName(),UserCurrentCategoryID, "0");
					DeleteCategoryIdsLists.clear();
					DeleteDocumentIdsLists.clear();
					break;
				case Update://重命名
					if (typetmp.equals("category")) {
						showToast = "修改成功";
						CloudDiskReqUtil.getCategorySaveOrUpdate(FileManagementActivity.this,FileManagementActivity.this, null,mCreateCatalogAlertDialog.getName(),UserChangeCategoryOrDocumentParentID, DeleteCategoryIdsLists.get(0));
						DeleteCategoryIdsLists.clear();
						DeleteDocumentIdsLists.clear();
						showBottomRechristenUI(0);
					} else {
						CloudDiskReqUtil.renameFile(FileManagementActivity.this,FileManagementActivity.this, null, DeleteDocumentIdsLists.get(0), mCreateCatalogAlertDialog.getName(), UserChangeCategoryOrDocumentParentID,ReNameUserDocumentLists.get(0).suffixName);
						showToast = "修改成功";
						DeleteCategoryIdsLists.clear();
						DeleteDocumentIdsLists.clear();
						showBottomRechristenUI(0);
					}
					break;
				default:
					break;
				}
			
			}
		}
	};
	protected String uploadFileName = "";
	//上传文件成功时间
	protected Long uploadFileCreateTime;
	private SharedPreferences upLoadParmar;
	
	
	/**
	 * 判断文件类型是否为目录
	 * @param name
	 * @return
	 */
	public boolean isCategory(Object type) {
		if (type instanceof UserDocument && ((UserDocument) type) != null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 查询用户目录
	 */
	public void startGetData(String id) {
		showLoadingDialog();
		CloudDiskReqUtil.queryAllRCategory(FileManagementActivity.this,FileManagementActivity.this, null, "0", -1+"", id);
	}
	
	/**
	 * 全选
	 */
	private void selectALL() {
		isCancleALL = false;
		isSelectALL = true;
		DeleteDocumentIdsLists.clear();
		DeleteCategoryIdsLists.clear();
		for (Object allCategoryDocument : allCategoryDocumentLists) {
			switch (doDetectionDataType(allCategoryDocument)) {
			case Document:
				DeleteDocumentIdsLists.add(((UserDocument)allCategoryDocument).id);
				ReNameUserDocumentLists.add(((UserDocument)allCategoryDocument));
				break;
			case Category:
				DeleteCategoryIdsLists.add(((UserCategory)allCategoryDocument).id);
			default:
				break;
			}
		}
		// 显示底部按钮
		initMyActionBar("已选"+(DeleteDocumentIdsLists.size()+DeleteCategoryIdsLists.size())+"项");
		showBottomRechristenUI(DeleteDocumentIdsLists.size()+DeleteCategoryIdsLists.size());
		// 刷新listview和TextView的显示
		dataChanged();
	}
	
	
	/**
	 * 取消全选
	 */
	private void cancleALL() {
		isSelectALL = false;
		isCancleALL = true;
		DeleteDocumentIdsLists.clear();
		DeleteCategoryIdsLists.clear();
		// 显示底部按钮
		initMyActionBar("已选"+(DeleteDocumentIdsLists.size()+DeleteCategoryIdsLists.size())+"项");
		showBottomRechristenUI(DeleteDocumentIdsLists.size()+DeleteCategoryIdsLists.size());
		// 刷新listview和TextView的显示
		dataChanged();
	}


	// 刷新listview和TextView的显示
	private void dataChanged() {
		// 通知listView刷新
		mMakeFileManagementAdapter.notifyDataSetChanged();
	};

	/**
	 * 检测数据类
	 * 
	 * @param object
	 * @return
	 */
	private FileType doDetectionDataType(Object object) {
		if (object instanceof UserDocument) {
			fileType = FileType.Document;
		} else if (object instanceof UserCategory) {
			fileType = FileType.Category;
		}
		return fileType;
	}
	
	
	/**
	 * 文件管理的适配器
	 */
	public class MakeFileManagementAdapter extends BaseAdapter {
		// 构造器
		public MakeFileManagementAdapter(ArrayList<Object> allCategoryDocumentLists) {
			categoryAndDocument = allCategoryDocumentLists;
		}

		private ViewHolder holder;

		@Override
		public int getCount() {
			return categoryAndDocument.size();
		}

		@Override
		public int getItemViewType(int position) {
			Object object = getItem(position);
			FileType type = doDetectionDataType(object);
			switch (type) {
			case Category:
				return 0;
			case Document:
				return 1;
			}
			return 0;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public Object getItem(int position) {
			return categoryAndDocument.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(FileManagementActivity.this,R.layout.list_item_filemanagement, null);
				holder.fileManage_RL = (RelativeLayout) convertView .findViewById(R.id.fileManage_RL);
				holder.filemanagement_name_Tv = (TextView) convertView.findViewById(R.id.filemanagement_name_Tv);
				holder.filemanagement_date_Tv = (TextView) convertView.findViewById(R.id.filemanagement_date_Tv);
				holder.filemanagement_Iv = (ImageView) convertView.findViewById(R.id.filemanagement_Iv);
				holder.filemanagement_select_cb = (CheckBox) convertView.findViewById(R.id.filemanagement_select_cb);
				holder.filemanagement_size_Tv = (TextView) convertView.findViewById(R.id.filemanagement_size_Tv);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (isSelectALL) {
				holder.filemanagement_select_cb.setChecked(true);
			}
			if (isCancleALL) {
				holder.filemanagement_select_cb.setChecked(false);
			}
			
			
			holder.filemanagement_select_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChatSaveFile) {
						int num = 0;
						UserCategory category = (UserCategory) allCategoryDocumentLists.get(position);
						if (isChecked) {
							if (!ChatSaveFileToCategoryIdsLists.contains(category.id)) {
								ChatSaveFileToCategoryIdsLists.add(category.id);
							}
						}else {
							ChatSaveFileToCategoryIdsLists.remove(category.id);
						}
						if (ChatSaveFileToCategoryIdsLists.size() >= 1) {
							if(doBackKey){
								num = ChatSaveFileToCategoryIdsLists.size();
							}else{
								num = ChatSaveFileToCategoryIdsLists.size() - 1;
							}
						}else {
							num = 0;
						}
						initMyActionBar("已选"+(num)+"项");
					}else{
						switch (doDetectionDataType(allCategoryDocumentLists.get(position))) {
						case Document:
							UserDocument userDocument = (UserDocument) allCategoryDocumentLists.get(position);
							if (isChecked) {
								if (!DeleteDocumentIdsLists.contains(userDocument.id)) {
									DeleteDocumentIdsLists.add(userDocument.id);
									ReNameUserDocumentLists.add(userDocument);
								}
							}else {
								DeleteDocumentIdsLists.remove(userDocument.id);
								ReNameUserDocumentLists.remove(userDocument);
							}
							break;
						case Category:
							UserCategory category = (UserCategory) allCategoryDocumentLists.get(position);
							if (isChecked) {
								if (!DeleteCategoryIdsLists.contains(category.id)) {
									DeleteCategoryIdsLists.add(category.id);
								}
							}else {
								DeleteCategoryIdsLists.remove(category.id);
							}
							break;
						default:
							break;
						}
						//删除和重命名按钮的显示逻辑
						showBottomRechristenUI(DeleteCategoryIdsLists.size()+DeleteDocumentIdsLists.size());
						if (!mCreateCatalogAlertDialog.isBack() && !deleteCreateCatalogAlertDialog.isBack()) {
							initMyActionBar("已选"+(DeleteDocumentIdsLists.size()+DeleteCategoryIdsLists.size())+"项");
						}
					}
					
				}
			});
			
			/**
			 * 是否显示多选框
			 */
			if (isShowCheckBox) {
				holder.filemanagement_select_cb.setVisibility(View.VISIBLE);
			} else {
				holder.filemanagement_select_cb.setVisibility(View.GONE);
			}

			if (1 == getItemViewType(position)) {//文件
				UserDocument userDocument = (UserDocument) getItem(position);
				holder.filemanagement_name_Tv.setText(userDocument.fileName);
				holder.filemanagement_size_Tv.setText(FormetFileSize(Long.parseLong(userDocument.fileSize)));
				holder.filemanagement_date_Tv.setText(getFormatTime(userDocument.reserved1));
				String suffixName = userDocument.suffixName.toUpperCase();
				int fileSourceId = 0;// 资源文件ID
				if (EUtil.PIC_FILE_STR.contains(suffixName)) {// 图片
					fileSourceId = R.drawable.picture_fang;
				} else if (EUtil.VIDEO_FILE_STR.contains(suffixName)) {// 视频
					fileSourceId = R.drawable.video_fang;
				} else if ("PDF".contains(suffixName)) {//pdf文件
					fileSourceId = R.drawable.pdf_fang;
				} else if ("PPT".contains(suffixName) || "pptx".contains(suffixName)) {//ppt文件
					fileSourceId = R.drawable.ppt_fang;
				} else if ("XLS".contains(suffixName) || "XLSX".equals(suffixName)) {//excel文件
					fileSourceId = R.drawable.file_excel_fang;
				} else if (EUtil.DOC_FILE_STR.contains(suffixName)) {//word文档
					fileSourceId = R.drawable.word_fang;
				} else if (EUtil.AUDIO_FILE_STR.contains(suffixName)) {//音频文件
					fileSourceId = R.drawable.file_audio_fang;
				} else if ("ZIP".contains(suffixName) || "RAR".contains(suffixName)) {//压缩文件
					fileSourceId = R.drawable.file_zip;
				} else {
					fileSourceId = R.drawable.file_other;
				}
				if(DeleteDocumentIdsLists.contains(userDocument.id)){//包含选择id,则选中checkbox
					holder.filemanagement_select_cb.setChecked(true);
				}else{
					holder.filemanagement_select_cb.setChecked(false);
				}
				holder.filemanagement_Iv.setBackgroundResource(fileSourceId);
			} else if (0 == getItemViewType(position)) {// 目录
				UserCategory category = (UserCategory) getItem(position);
				holder.filemanagement_name_Tv.setText(category.categoryname);
				holder.filemanagement_date_Tv
						.setText(MyDateFormat(category.createtime.toString()));
				holder.filemanagement_size_Tv.setVisibility(View.GONE);
				// holder.filemanagement_date_Tv.setText(JTDateUtils.formatDate(category.createtime.toString(),JTDateUtils.DATE_FORMAT_2));
				// 根据isSelected来设置checkbox的选中状况
				
				if(DeleteCategoryIdsLists.contains(category.id)){//包含选择id,则选中checkbox
					holder.filemanagement_select_cb.setChecked(true);
				}else{
					holder.filemanagement_select_cb.setChecked(false);
				}
			}
			return convertView;
		}

		public class ViewHolder {
			public ImageView filemanagement_Iv;
			public TextView filemanagement_name_Tv, filemanagement_date_Tv,
					filemanagement_size_Tv;
			public CheckBox filemanagement_select_cb;
			public RelativeLayout fileManage_RL;
		}
	}
	
	
	/**
	 * 文件管理展示
	 */
	public void showMakeFileManagementUI(FragmentActivity parent,final XListView listview, final ArrayList<Object> allCategoryFileLists) {
		if (allCategoryFileLists.size() == 0) {
			showRecomand(true);
		}else {
			showRecomand(false);
		checkNum = allCategoryFileLists.size();
		}
		
		mMakeFileManagementAdapter = new MakeFileManagementAdapter(allCategoryFileLists);
		listview.setAdapter(mMakeFileManagementAdapter);
		reCreateNameLists = allCategoryFileLists;

		/**
		 * 目录跳转、文件下载
		 */
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				switch (doDetectionDataType(allCategoryFileLists.get(position - 1))) {
					case Document:
						UserDocument mjtFile = (UserDocument) allCategoryFileLists.get(position - 1);
						JTFile jtFile = new JTFile();
						jtFile.mUrl = mjtFile.url;
						jtFile.mFileName = mjtFile.fileName;
						jtFile.mFileSize = Long.parseLong(mjtFile.fileSize);
						jtFile.mSuffixName = mjtFile.suffixName;
						ENavigate.startFilePreviewActivity(FileManagementActivity.this,jtFile,false);
						break;
					case Category:
						if (isLongClick) {
						}else
						{
							//初始化存储文件目录列表
							DeleteCategoryIdsLists.clear();
							DeleteDocumentIdsLists.clear();
							ChatSaveFileToCategoryIdsLists.clear();//清空list
							//获取目录对象
							UserCategory category = (UserCategory) allCategoryFileLists.get(position - 1);
							//记录修改文件或目录的parentID
							UserChangeCategoryOrDocumentParentID = category.id;
							//点击选中的目录的ID
							UserCurrentCategoryID = category.id;
							//将点击目录父ID存储起来，以便于返回时使用
							UserParentCategoryLists.add(category);
							if (isChatSaveFile) {
								ChatSaveFileToCategoryIdsLists.add(category.id);
							}
							//记录目录sortid,以便于排序搜索时使用
							UserCategorySortID = category.sortid;
							//记录目录层级,以便于排序搜索时使用
							UserCategoryLevel++;
							if (isShowCheckBox) {
								if (isChatSaveFile) {
									startGetData(UserCurrentCategoryID);
									initMyActionBar(category.categoryname);
								}
							}else {
								startGetData(UserCurrentCategoryID);
								initMyActionBar(category.categoryname);
							}
							doBackKey = false;
							if (upLoadParmar.getString("upFileCategoryId", "0").equals(category.id)) {
								CommonUtils.showLoadingDialog("正在上传...", FileManagementActivity.this);
							}
						}
						break;
					default:
						break;
	
				}
			}
		});

		/**
		 * 长按批量编辑
		 */
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,int position, long id) {
				ViewHolder holder = (ViewHolder) view.getTag();
				// 初始化
//				DeleteCategoryIdsLists.clear();
//				DeleteDocumentIdsLists.clear();
//				ChatSaveFileToCategoryIdsLists.clear();//清空list
				// 将HashMap集合清空
				isShowCheckBox = true;
				isLongClick = true;
				//选择目录禁止刷新
				listview.setPullLoadEnable(false);
				//显示全选取消按钮
				if (!isChatSaveFile) {
					showMenuItemUI(false);
				}
				// 显示底部(删除、重命名)按钮
				showBottomControlsUI(true);
				Object object = allCategoryFileLists.get(position - 1);
//				switch (doDetectionDataType(object)) {
//				case Document:
//					DeleteDocumentIdsLists.add(((UserDocument)object).id);
//					ReNameUserDocumentLists.add(((UserDocument)object));
//					break;
//				case Category:
//					DeleteCategoryIdsLists.add(((UserCategory)object).id);
//					break;
//				default:
//					break;
//				}
				showBottomRechristenUI(DeleteCategoryIdsLists.size()+DeleteDocumentIdsLists.size());
//				holder.filemanagement_select_cb.setChecked(true);
				return true;
			}
		});
	}
	
	

	/**
	 * 转换文件大小
	 * 
	 * @param fileS
	 * @return
	 */
	public static String FormetFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 * 毫秒转化成日期格式
	 * @param string
	 * @return
	 */
	private String MyDateFormat(String string) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return formatter.format(Long.parseLong(string));
	}
	
	/**
	 * 判断字符串是否包含表情
	 * @param intype
	 * @return
	 */
	public static boolean isContainMoji(String intype)
	{
		boolean isEomji = false;
		boolean YES = true;
		char[] ch = intype.toCharArray();
		for (int i = 0; i < ch.length - 1; i++) {
			if (0xd800 <= ch[i] && ch[i] <= 0xdbff) {
				if (ch.length > 1) {
					int uc = ((ch[i] - 0xd800) * 0x400) + (ch[i+1] - 0xdc00) + 0x10000;
					if (0x1d000 <= uc && uc <= 0x1f77f) {
						isEomji = YES;
						return isEomji;
					}
				}
			} else if (ch.length > 1) {
				if (ch[i+1] == 0x20e3) {
					isEomji = YES;
					return isEomji;
				}
			} else {
				if (0x2100 <= ch[i] && ch[i] <= 0x27ff && ch[i] != 0x263b) {
					isEomji = YES;
					return isEomji;
				} else if (0x2B05 <= ch[i] && ch[i] <= 0x2b07) {
					isEomji = YES;
					return isEomji;
				} else if (0x2934 <= ch[i] && ch[i] <= 0x2935) {
					isEomji = YES;
					return isEomji;
				} else if (0x3297 <= ch[i] && ch[i] <= 0x3299) {
					isEomji = YES;
					return isEomji;
				} else if (ch[i] == 0xa9 || ch[i] == 0xae || ch[i] == 0x303d || ch[i] == 0x3030 || ch[i] == 0x2b55 || ch[i] == 0x2b1c || ch[i] == 0x2b1b || ch[i] == 0x2b50|| ch[i] == 0x231a ) {
					isEomji = YES;
					return isEomji;
				}
			}
			
		}
		return isEomji;
		
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,Intent intent) {
//		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode != RESULT_OK) {
			return;
		}
		
		// 选择附件返回结果
		if (requestCode == 255) {
			HashSet<String> selectList = (HashSet<String>) intent.getSerializableExtra("select_list");
			showAccessory(selectList);
			editor.putInt("success", 0);
			editor.putInt("faile", 0);
			editor.putInt("allUpNum", selectList.size());
			editor.putBoolean("isUpSucess", true);
			editor.putString("upFileCategoryId", StringUtils.isEmpty(UserCurrentCategoryID)?"0":UserCurrentCategoryID);
			editor.commit();
			CommonUtils.showLoadingDialog("正在上传...", FileManagementActivity.this);
		}
		
		
	}

	private void showAccessory(HashSet<String> selectList) {
		List<String> list = new ArrayList(selectList);
		for (String str : list) {
			JTFile jtFile = new JTFile();
			jtFile.mTaskId = StringUtils.isEmpty(jtFile.mTaskId)?TaskIDMaker.getTaskId(App.getUserName()):jtFile.mTaskId;
			jtFile.mModuleType = 15;
			jtFile.mType = 3;
			jtFile.mFileName = (str.split("/"))[str.split("/").length-1];// 文件名
			jtFile.mLocalFilePath = str;// 文件路径
			jtFile.mCreateTime = System.currentTimeMillis(); // 创建时间，作为文件的识别码
			fileListSD.add(jtFile);// 添加到本地集合
		}
		
		for (JTFile jtFile : fileListSD) {// 上传sd卡的文件
			showLoadingDialog();
			FileUploader uploader = new FileUploader(jtFile, this);
			uploader.WEB_URL = EAPIConsts.FILE_DEMAND_URL;
			uploader.start();
		}
		
		fileListSD.clear();
		
	}

	/**
	 * 修改后台传回的时间
	 * @param date
	 * @return
	 */
	private String getFormatTime(String date)
	{
		String[] time = date.split(":");
		return time[0]+":"+time[1];
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPrepared(long uid) {
		
	}

	@Override
	public void onStarted(long uid) {
		
	}

	@Override
	public void onUpdate(long uid, final int progress) {
		
	}

	@Override
	public void onCanceled(long uid) {
		
	}

	@Override
	public void onSuccess(long uid, final String fileUrl) {
		
	}

	@Override
	public void onError(long uid, int code, String message, String namePath) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				int faileNum = upLoadParmar.getInt("faile", 0);
				editor.putInt("faile", faileNum+1);
				editor.putBoolean("isUpSucess", false);
				editor.commit();
				if (upLoadParmar.getInt("allUpNum", 0) == upLoadParmar.getInt("success", 0) + upLoadParmar.getInt("faile", 0)) {
					//上传成功刷新当前列表
					startGetData(UserCurrentCategoryID);
					CommonUtils.dismissLoadingDialog();
					if (upLoadParmar.getBoolean("isUpSucess", true)) {
						ToastUtil.showToast(FileManagementActivity.this, "上传成功");
					} else {
						ToastUtil.showToast(FileManagementActivity.this, "有"+upLoadParmar.getInt("faile", 0)+"个文件上传失败");
					}
					editor.putInt("allUpNum", 0);
					editor.putInt("success", 0);
					editor.putInt("faile", 0);
					editor.putString("upFileCategoryId", "");
					editor.putBoolean("isUpSucess", true);
					editor.commit();
				}
			}
		});
	}

	@Override
	public void onSuccessForJTFile(final JTFile jtFile) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				isFromUpload  = true;
				int successNum = upLoadParmar.getInt("success", 0);
				editor.putInt("success", successNum + 1);
				editor.commit();
				CloudDiskReqUtil.doChatSaveCategory(FileManagementActivity.this,FileManagementActivity.this, null,jtFile.getId(),upLoadParmar.getString("upFileCategoryId", "0"));
			}
		});
	}
}
