package com.tr.ui.work;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.api.WorkReqUtil;
import com.tr.model.joint.AffairNode;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.joint.KnowledgeNode;
import com.tr.model.joint.ResourceNode;
import com.tr.model.obj.Connections;
import com.tr.model.work.BUAffar;
import com.tr.model.work.BUAffarMember;
import com.tr.model.work.BUResponseData;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.JointResourceFragment.ResourceType;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.im.IMRelationSelectActivity;
import com.tr.ui.widgets.CircleImageView;
import com.tr.ui.work.WorkDateTimePickerDialog.OnDayChangeListener;
import com.tr.ui.work.WorkNewAffarDealDialog.OnDealChoseListener;
import com.tr.ui.work.WorkPhoneChoseDialog.OnPhoneChangeListener;
import com.utils.common.EConsts;
import com.utils.common.GlobalVariable;
import com.utils.http.EAPIConsts.WorkReqType;
import com.utils.http.IBindData;
import com.utils.log.ToastUtil;

/**
 * 创建事务--编辑事务--事务详情
 * 
 * 启动方法：	WorkReqType.AFFAIR_DETAIL_GET 事务详情
 * 		  	WorkReqType.AFFAIR_CREATE 创建事务
 * 			WorkReqType.AFFAIR_EDIT 编辑事务
 * 
 * @param OperateType
 * 			c创建  b编辑 s 查看
 * @param jtContact
 * 			创建传null，编辑、详情传 BUAffar类型对象
 * @param requestCode
 *            创建传非0值
 * @author Administrator
 *
 */
public class WorkNewActivity extends JBaseActivity implements
		OnItemClickListener, IBindData, OnPhoneChangeListener,
		OnDealChoseListener, OnDayChangeListener {
	/**创建、编辑、详情的总布局*/
	protected LinearLayout LinearLayoutWork;
	/**底边负责人、成员、关联....布局*/
	protected LinearLayout LinearLayoutMenu;
	/**负责人*/
	protected RelativeLayout RelativeLayoutLead;
	/**成员*/
	protected RelativeLayout RelativeLayoutMember;
	/**关联*/
	protected RelativeLayout RelativeLayoutRelation;
	/**地点*/
//	protected RelativeLayout RelativeLayoutAddr;
	/**颜色*/
//	protected RelativeLayout RelativeLayoutColor;
	protected RelativeLayout RelativeLayoutMore;
	protected Button ButtonTitleConfirm;//已注释
	/**创建事务--名称输入框*/
	protected EditText EditTextTitle;
	
	private ImageView IV_tri;
	private BUAffar mAffar;
	/**事务页面传过来的事务Id*/
	private String mAffarId;
	/**用户Id*/
	private long mUserId;
	/**c 创建 b编辑 s 查看*/
	private String mOperateType = "c"; 
	/**ActionBar的菜单按钮*/
	private Menu mMenu = null;
	/**操作之后，对应的类型："f"事务完成，"r"事务重新开启,"c"事务举报成功,"q"退出事务成功,"d"删除事务成功  */
	private String mAffarOperType = "";
	private String mRelationSelType;//关联的类型
	private String mRelationSelLabel;//关联的标签
	private String mRelationSelEdityType = "a"; // a add e edit
	
	private boolean isCreate = false;
	
	
	private int mButtonClick=0;

	DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_default_avatar)
			.showImageForEmptyUri(R.drawable.ic_default_avatar)
			.showImageOnFail(R.drawable.ic_default_avatar).cacheInMemory(true)
			.cacheOnDisc(true).considerExifParams(false).build();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.work_new_activity);
		mAffarId = getIntent().getStringExtra("AffarId");
		mUserId = Integer.parseInt(App.getUserID());
		mOperateType = getIntent().getStringExtra("OperType");
		initPopupWindow();
		Log.d("xmx", "userid:" + mUserId);

		if (mOperateType == null) {
			mOperateType = "c";
			mAffar = BUAffar.getAffar(mUserId + "", App.getNick());
			//mAffar.addDefaulLeader();
			Intent data = getIntent();

			if (data.hasExtra(EConsts.Key.RELATED_PEOPLE_NODE)) { // 人脉信息
				Log.d("xmx", "RELATED_PEOPLE_NODE");

				ConnectionNode connectionNode = (ConnectionNode) data
						.getSerializableExtra(EConsts.Key.RELATED_PEOPLE_NODE);
				mAffar.addRelationWithConnectionNode(connectionNode, "p");
			}
			if (data.hasExtra(EConsts.Key.RELATED_ORGANIZATION_NODE)) { // 组织信息
				Log.d("xmx", "RELATED_ORGANIZATION_NODE");
				ConnectionNode connectionNode = (ConnectionNode) data
						.getSerializableExtra(EConsts.Key.RELATED_ORGANIZATION_NODE);
				mAffar.addRelationWithConnectionNode(connectionNode, "o");
			}
			if (data.hasExtra(EConsts.Key.RELATED_KNOWLEDGE_NODE)) { // 知识信息
				Log.d("xmx", "RELATED_KNOWLEDGE_NODE");

				KnowledgeNode ResourNode = (KnowledgeNode) data
						.getSerializableExtra(EConsts.Key.RELATED_KNOWLEDGE_NODE);
				mAffar.addRelationWithKnowledgeNode(ResourNode, "k");
			}
			if (data.hasExtra(EConsts.Key.RELATED_AFFAIR_NODE)) {// 事件信息
				Log.d("xmx", "RELATED_AFFAIR_NODE");

				AffairNode affairNode = (AffairNode) data
						.getSerializableExtra(EConsts.Key.RELATED_AFFAIR_NODE);
				mAffar.addRelationWithAffairNode(affairNode, "r");

			}

		}
		if (mAffarId == null)
			mAffarId = "";

		initView();
		initData();

	}

	@Override
	public void onResume() {
		super.onResume();
		mButtonClick=0;
		String location=(String) App.getApp().getParam("location");
		if(!TextUtils.isEmpty(location)){
			mAffar.location = location;
			if (!"不显示位置".equals(location)) {
				resetWorkView();
			}
			App.getApp().reMoveParam("location");
		}
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		mMenu = menu;
		initTopMenu();
		return true;
	}


	public void initView() {
		LinearLayoutWork = (LinearLayout) findViewById(R.id.LinearLayoutWork);
		LinearLayoutMenu = (LinearLayout) findViewById(R.id.LinearLayoutMenu);

		RelativeLayoutLead = (RelativeLayout) findViewById(R.id.RelativeLayoutLead);
		RelativeLayoutMember = (RelativeLayout) findViewById(R.id.RelativeLayoutMember);
		RelativeLayoutRelation = (RelativeLayout) findViewById(R.id.RelativeLayoutRelation);
//		RelativeLayoutAddr = (RelativeLayout) findViewById(R.id.RelativeLayoutAddr);
		RelativeLayoutMore = (RelativeLayout) findViewById(R.id.RelativeLayoutMore);
		IV_tri = (ImageView) findViewById(R.id.IV_tri);

		restButtomMenuView();
	}
	/**初始化数据*/
	public void initData() {

		Log.d("xmx", "initData");

		if (mOperateType.equals("c")) {//创建

			resetWorkView();
		} else if (mOperateType.equals("s")) {//查看
			showLoadingDialog();
			WorkReqUtil.getAffarDetail(WorkNewActivity.this, this,mUserId + "", mAffarId, null);
		} else {//编辑
			resetWorkView();
		}
	}

	public void initTopMenu() {//actionBar menu 完成
		if (mMenu != null) {
			if (mOperateType.equals("c")) {
				Log.d("xmx", "menu init");
				MenuItem item = mMenu.add(0, 101, 0, "完成");
				item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			}
			else if (mOperateType.equals("s")) {
				mMenu.removeItem(101);
				mMenu.removeItem(102);
				mMenu.removeItem(103);
				mMenu.removeItem(104);
				MenuItem item = mMenu.add(0, 102, 0, "更多");
				item.setIcon(R.drawable.ic_action_overflow);
				item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
				
				HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "事务详情", false, null, true, true);
				jabGetActionBar().setDisplayShowHomeEnabled(true);
			} else if (mOperateType.equals("b")) {
				mMenu.removeItem(101);
				mMenu.removeItem(102);
				mMenu.removeItem(103);
				mMenu.removeItem(104);
				MenuItem item = mMenu.add(0, 103, 0, "完成");
				item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
				
				HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "编辑事务", false, null, true, true);
				jabGetActionBar().setDisplayShowHomeEnabled(true);
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {//创建事务
		if (101 == item.getItemId()) {
			if (mButtonClick==0)
			{
				if (EditTextTitle != null && EditTextTitle.getText() != null) {
					Log.d("xmx", "EditTextTitle:"
							+ EditTextTitle.getText().toString());
					String vTitle=EditTextTitle.getText().toString();
					vTitle=vTitle.trim();
					
					if (vTitle.equals("")) {
						Toast.makeText(this, "请输入名称", Toast.LENGTH_SHORT).show();
					} else {
						try {
							if (mBeginTime.equals("")) {
								Toast.makeText(this, "请设置开始时间",
										Toast.LENGTH_SHORT).show();
								return true;
							}

							SimpleDateFormat df = new SimpleDateFormat(
									"yyyyMMdd HH:mm:ss");
							if (!mEndTime.equals("")) {
								Log.d("xmx", "mAffar !=null");
								Date vDateBegin = df.parse(mBeginTime);
								Date vDateEnd = df.parse(mEndTime);
								if (vDateEnd.getTime() < vDateBegin.getTime()) {
									Toast.makeText(this, "设置截止时间要大于开始时间",
											Toast.LENGTH_SHORT).show();
									return true;
								}
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
						mButtonClick=1;
						showLoadingDialog();
						mAffar.title=vTitle;
						WorkReqUtil.createAffar(WorkNewActivity.this, this, mAffar,
								null);
					}
				}
			}

		} else if (102 == item.getItemId()) {
			// 查看
			
			String vType = mAffar.isCreateAffar(mUserId);
			Log.d("xmx", "查看:" + " vtype:" + vType + " finished:"
					+ mAffar.finished);

			WorkNewAffarDealDialog vDialog = new WorkNewAffarDealDialog(
					WorkNewActivity.this, vType, mAffar.finished);
			vDialog.showDialog();
			
			vDialog.setDealSelListener(WorkNewActivity.this);
			
		} else if (103 == item.getItemId()) {
			// 编辑
			if (mButtonClick==0)
			{
				
			if (EditTextTitle != null && EditTextTitle.getText() != null) {
				Log.d("xmx", "EditTextTitle:"
						+ EditTextTitle.getText().toString());
				String vTitle=EditTextTitle.getText().toString();
				vTitle=vTitle.trim();
				
				if (vTitle.equals("")) {
					Toast.makeText(this, "请输入名称", Toast.LENGTH_SHORT).show();
				} else {
					showLoadingDialog();
					mButtonClick=1;
					mAffar.title=vTitle;
					WorkReqUtil.editAffar(WorkNewActivity.this, this, mAffar,mUserId+"",
							null);
				}
			}
			}
		}

		return super.onOptionsItemSelected(item);
	}
	/**重新设置一些布局*/
	public void resetWorkView() {
		View view;
		LinearLayoutWork.removeAllViews();
		LayoutInflater flater = LayoutInflater.from(this);

		if (mOperateType.equals("c") || mOperateType.equals("b")) {
			// 抬头
			view = flater.inflate(R.layout.work_new_title_cell, null);
			resetViewTitle(view);

			// 时间
			view = flater.inflate(R.layout.work_new_text_cell, null);
			resetViewTime(view, "开始时间");
			
			view = flater.inflate(R.layout.work_new_text_cell, null);
			resetViewTime(view, "截至时间");

//			if (!mAffar.getRemindType().equals("o")) {
				String vRemindStr = "不提醒";
				String vRemindType;
				view = flater.inflate(R.layout.work_new_text_cell, null);
				vRemindType = mAffar.getRemindType();
				if (vRemindType.equals("m"))
					vRemindStr = mAffar.getRemindAhead() + "分钟前提醒";
				if (vRemindType.equals("h"))
					vRemindStr = mAffar.getRemindAhead() + "小时前提醒";
				if (vRemindType.equals("d"))
					vRemindStr = mAffar.getRemindAhead() + "天前提醒";
				resetViewText(view, "提醒", vRemindStr);

				view.setOnClickListener(mLineRemandClick);
				if (!mAffar.getRepeatType().equals("o")) {
					view = flater.inflate(R.layout.work_new_text_cell, null);
					resetViewText(view, "重复",
							BUAffar.getRepeatTypeName(mAffar.getRepeatType()));
					view.setOnClickListener(mLineRepeatClick);
				}
//			}

			// 备注
			if(!TextUtils.isEmpty(mAffar.getInfoStr())){
				view = flater.inflate(R.layout.work_new_info_cell, null);
				resetViewInfo(view);
			}

			// 负责人
			if (mAffar.getMemberCount("o") > 0) {
				view = flater.inflate(R.layout.work_new_person_lead_cell, null);
				resetViewPerson(view, "o");
				view.setOnClickListener(mLineLeadClick);
			}
			// 成员
			if (mAffar.getMemberCount("m") > 0) {
				view = flater.inflate(R.layout.work_new_person_cell, null);
				resetViewPerson(view, "m");
				view.setOnClickListener(mLineMemberClick);
			}

			if (mAffar.getColor() > 0) {
				view = flater.inflate(R.layout.work_new_color_cell, null);
				resetViewColor(view, 0, "重要程度", BUAffar.getColorName(mAffar.getColor()));
				view.setOnClickListener(mLineColorClick);
			}
			if (!mAffar.location.equals("")) {
				view = flater.inflate(R.layout.work_new_color_cell, null);
				resetViewColor(view, 1, "地点", mAffar.location);

				view.setOnClickListener(mLineLocationClick);
			}

			if (mAffar.relations.size() > 0) {
				view = flater.inflate(R.layout.work_new_rela_cell, null);
				LinearLayoutWork.addView(view);
				// 人脉

				WrokViewRelationItem("p", "人脉");
				WrokViewRelationItem("o", "组织");
				WrokViewRelationItem("k", "知识");
				WrokViewRelationItem("r", "事件");

			}

			view = flater.inflate(R.layout.work_new_blank_cell, null);
			LinearLayoutWork.addView(view);

		} else {
			// 查看
			// 抬头
			Log.d("xmx", "affid:" + mAffar.id);

			view = flater.inflate(R.layout.work_new_title_show_cell, null);
			resetShowViewTitle(view);

			// 时间
			view = flater.inflate(R.layout.work_new_time_show_cell, null);
			TextView TextViewNameTime = (TextView) view
					.findViewById(R.id.TextViewName);
			
			if (mAffar.getEndTime()!=null)
			{
				if (mAffar.getEndTime().equals(""))
				{
					TextViewNameTime.setText(WorkNewTimeActivity
							.getDateStrByDayNew(mAffar.getStartTime()));
				}
				else
				{
					TextViewNameTime.setText(WorkNewTimeActivity
							.getDateStrByDayNew(mAffar.getStartTime())
							+ " 至 "
							+ WorkNewTimeActivity.getDateStrByDayNew(mAffar
									.getEndTime()));
				}
			}
			else
			{
				TextViewNameTime.setText(WorkNewTimeActivity
						.getDateStrByDayNew(mAffar.getStartTime()));
			}
					
					
			LinearLayoutWork.addView(view);

			// 备注
			view = flater.inflate(R.layout.work_new_info_show_cell, null);
			resetShowViewInfo(view);

			// 负责人
			if (mAffar.getMemberCount("o") > 0) {
				view = flater.inflate(R.layout.work_new_person_show_cell, null);
				resetShowViewPerson(view, "o");
			}
			// 成员
			if (mAffar.getMemberCount("m") > 0) {
				view = flater.inflate(R.layout.work_new_person_show_cell, null);
				resetShowViewPerson(view, "m");
			}
			// 地址
			if (mAffar.getLocation() != null
					&& (!mAffar.getLocation().equals(""))) {
				view = flater
						.inflate(R.layout.work_new_address_show_cell, null);
				TextView TextViewValueAddr = (TextView) view
						.findViewById(R.id.TextViewValue);
				TextViewValueAddr.setText(mAffar.getLocation());
				LinearLayoutWork.addView(view);
			}

			if (mAffar.relations.size() > 0) {
				view = flater.inflate(R.layout.work_new_blank_show_cell, null);
				LinearLayoutWork.addView(view);

				view = flater.inflate(R.layout.work_new_releation_show_cell,
						null);
				
				resetShowViewRela(view, "关联", mAffar.relations.size() + "");
				view.setOnClickListener(mLineRelationShowClick);
			}
			//日志
			if (mAffar.logTotal > 0) {
				view = flater.inflate(R.layout.work_new_blank_show_cell, null);
				LinearLayoutWork.addView(view);

				view = flater.inflate(R.layout.work_new_releation_show_cell,
						null);
				ImageView red_dot = (ImageView) view.findViewById(R.id.red_dot);
//				if (mAffar.logIsNew.equals("0")) {//20160219产品取消日志未读提示
					red_dot.setVisibility(View.GONE);
//				}else {
//					red_dot.setVisibility(View.VISIBLE);
//				}
				resetShowViewRela(view, "日志", mAffar.logTotal + "");
				view.setOnClickListener(mLineLogShowClick);
			}

			//畅聊
			view = flater.inflate(R.layout.work_new_releation_show_cell,
					null);
			TextView tv_im = (TextView) view.findViewById(R.id.TextViewName);
			TextView tv_im1 = (TextView) view.findViewById(R.id.TextViewValue);
			ImageView red_dot1 = (ImageView) view.findViewById(R.id.red_dot1);
			tv_im.setText("畅聊");
			tv_im1.setText("");
			if (mAffar.mucIsNew.equals("0")) {
				red_dot1.setVisibility(View.GONE);
			}else {
				red_dot1.setVisibility(View.VISIBLE);
			}
			view.setOnClickListener(mIMClick);
			LinearLayoutWork.addView(view);

			if (mAffar.finished.equals("0")) {
				view = flater.inflate(R.layout.work_new_blank_show_cell, null);
				LinearLayoutWork.addView(view);

				view = flater.inflate(R.layout.work_new_button_ok_cell, null);
				Button ButtonCommitOk = (Button) view
						.findViewById(R.id.ButtonCommit);
				ButtonCommitOk.setOnClickListener(mButtonCommitClick);
				LinearLayoutWork.addView(view);
			}
		}
		restButtomMenuView();
	}
	/**
	 * 编辑事务--关联关系-人脉布局的显示
	 * @param inType  "关联类型 p:人员，o：组织，k：知识，r:事件"
	 * @param inTypeName “人脉，组织，知识，事件”
	 */
	public void WrokViewRelationItem(String inType, String inTypeName) {
		View view;
		int i;
		LayoutInflater flater = LayoutInflater.from(this);
		if (mAffar.getRelationCount(inType) > 0) {//如果关联的类型大于0
			view = flater.inflate(R.layout.work_new_rela_node_cell, null);
			TextView TextViewName = (TextView) view
					.findViewById(R.id.TextViewName);
			TextViewName.setText(inTypeName);

			LinearLayout vLinearCell = (LinearLayout) view
					.findViewById(R.id.LinearLayoutLabel);

			List<String> vLabelList = mAffar.getRelationLabelListLabel(inType);//存放事务关联标签的集合
			int vSize = vLabelList.size();
			for (i = 0; i < vLabelList.size(); i++) {//遍历标签的集合
				String vStrLabel = vLabelList.get(i);//获取每一个标签
				View vViewItem = flater.inflate(
						R.layout.work_new_rela_item_cell, null);

				TextView TextViewLabel = (TextView) vViewItem
						.findViewById(R.id.TextViewLabel);//名称： 人脉、组织、知识、事件
				//标签为空的时候
				if (vStrLabel.equals(""))
					TextViewLabel.setText(vStrLabel);
				else
					TextViewLabel.setText(vStrLabel + ":");

				TextView TextViewValue = (TextView) vViewItem
						.findViewById(R.id.TextViewValue_work);//关联的 关系和内容
				TextViewValue.setText(mAffar
						.getRelationTitle(inType, vStrLabel));

				LinearLayout LinearLayoutLine = (LinearLayout) vViewItem
						.findViewById(R.id.LinearLayoutLine);
				if (vSize == 1)
					LinearLayoutLine.setBackgroundColor(Color.WHITE);

				LinearLayout LinearLayoutTopLine = (LinearLayout) vViewItem
						.findViewById(R.id.LinearLayoutTopLine);//分割关系之间的线
				if (i == 0)
					LinearLayoutTopLine.setVisibility(View.GONE);

				Button ButtonRelaDel = (Button) vViewItem
						.findViewById(R.id.ButtonRelaDel);//删除的图标
				ButtonRelaDel.setTag(inType + vStrLabel);
				ButtonRelaDel.setOnClickListener(mLineRelationButtonDelClick);

				vViewItem.setTag(inType + vStrLabel);
				vViewItem.setOnClickListener(mLineRelationClick);

				vLinearCell.addView(vViewItem);
			}

			LinearLayoutWork.addView(view);
		}
	}
	/**分情况显示底边菜单栏*/
	public void restButtomMenuView() {
		if(mPopWindow.isShowing()){
			mPopWindow.dismiss();
		}
		if (mOperateType.equals("c") || mOperateType.equals("b")) {
			LinearLayoutMenu.setVisibility(View.VISIBLE);
		} else {
			LinearLayoutMenu.setVisibility(View.GONE);
		}

	}
	/**
	 * 
	 * 设置事务的标题和颜色
	 * @param inView
	 */
	public void resetShowViewTitle(View inView) {
		TextView TextViewTitle = (TextView) inView
				.findViewById(R.id.TextViewTitle);
		TextViewTitle.setText(mAffar.getTitle());

		ImageView ImageViewPic = (ImageView) inView
				.findViewById(R.id.ImageViewPic);

		if (mAffar.getColor() == 0)
			ImageViewPic.setBackground(getResources().getDrawable(
					R.drawable.icon_level1));
		if (mAffar.getColor() == 1 || mAffar.getColor() == 2)
			ImageViewPic.setBackground(getResources().getDrawable(
					R.drawable.icon_level3));
		if (mAffar.getColor() == 3 || mAffar.getColor() == 4)
			ImageViewPic.setBackground(getResources().getDrawable(
					R.drawable.icon_level2));

		LinearLayoutWork.addView(inView);
	}
	/**
	 * 创建事务的名称
	 * @param inView
	 */
	public void resetViewTitle(View inView) {
		EditTextTitle = (EditText) inView.findViewById(R.id.EditTextTitle);
		EditTextTitle.addTextChangedListener(mTextWatcher);
		LinearLayoutWork.addView(inView);
		EditTextTitle.setText(mAffar.title);
		EditTextTitle.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
		        	if(mPopWindow!=null){
		        		mPopWindow.dismiss();
		        	}
				}
			}
		});
	}
	
	TextWatcher mTextWatcher = new TextWatcher() {//监控EditTextTitle

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			mAffar.title = s.toString();
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// mTextView.setText(s);//将输入的内容实时显示
		}

		@Override
		public void afterTextChanged(Editable arg0) {

		}
	};
	/**
	 * 创建事务-时间
	 * @param inView
	 */
	public void resetViewTime(View inView, String text) {
		TextView TextViewName = (TextView) inView
				.findViewById(R.id.TextViewName);
		TextViewName.setText(text);
		TextView TextViewValue = (TextView) inView
				.findViewById(R.id.TextViewValue);

		mBeginTime = mAffar.getStartTime();
		String vStartStr = WorkNewTimeActivity.getDateStrByDayNew(mAffar.getStartTime());
		String vEndStr=WorkNewTimeActivity.getDateStrByDayNew(mAffar.getEndTime());
		
		if(text.equals("开始时间")){
			inView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					initAlertDialog("开始时间选择");
				}
			});
			TextViewValue.setText(vStartStr);
//			inView.setOnClickListener(mBeginTimeClick);
		}else if(text.equals("截至时间")){
			inView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(mBeginTime.equals("")){
						Toast.makeText(WorkNewActivity.this, "请设置开始时间",
								Toast.LENGTH_SHORT).show();
					}else{
						initAlertDialog("截至时间选择");
					}
				}
			});
			TextViewValue.setText(vEndStr);
//			inView.setOnClickListener(mEndTimeClick);
		}
//		if (vEndStr.equals(""))
//			TextViewValue.setText(WorkNewTimeActivity.getDateStrByDayNew(mAffar
//				.getStartTime()));
//		else
//			TextViewValue.setText(WorkNewTimeActivity.getDateStrByDayNew(mAffar
//					.getStartTime())
//					+ " 至 "
//					+ WorkNewTimeActivity.getDateStrByDayNew(mAffar.getEndTime()));
		
//		inView.setOnClickListener(mLineTimeClick);
		LinearLayoutWork.addView(inView);
	}

	private String mBeginTime = "";
	private String mEndTime = "";

	private int mSetTimeFlag = 0; // 0 开始 1 结束
	private OnClickListener mBeginTimeClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			startTimeDialog.dismiss();
			mSetTimeFlag = 0;
			String vStr = "";
			Calendar calendar = Calendar.getInstance();
			Date aa = calendar.getTime();
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			vStr = df.format(aa);
			if (!mBeginTime.equals(""))
				vStr = mBeginTime;

			Log.d("xmx", "begintime:" + vStr);
			WorkDateTimePickerDialog datePicKDialog = new WorkDateTimePickerDialog(
					WorkNewActivity.this, vStr);
			datePicKDialog.dateTimePicKDialog(0);

			datePicKDialog.setDayChangeListener(WorkNewActivity.this);

	        InputMethodManager inputmanger = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
	        if (inputmanger.isActive())
	        {
	        	inputmanger.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	        }
			
		}
	};
	
	private OnClickListener mEndTimeClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Log.d("xmx", "onLinearEndTimeClick:" + mBeginTime + " endTime:"
					+ mEndTime);
			endTimeDialog.dismiss();
			if (mBeginTime.equals("")) {
				Toast.makeText(WorkNewActivity.this, "请先输入开始时间", Toast.LENGTH_SHORT).show();
			} else {
				try {
					mSetTimeFlag = 1;
					String vStr = "";
					if (!mEndTime.equals(""))
						vStr = mEndTime;
					else
						vStr = mBeginTime;

					SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
					Date vDateBegin = df.parse(mBeginTime);
					Date vDateEnd = df.parse(vStr);

					if (vDateEnd.getTime() < vDateBegin.getTime()) {
						vStr = mBeginTime;
					}
					Log.d("xmx", "vStr:" + vStr + " vDateBegin.getTime():"+vDateBegin.getTime());
					WorkDateTimePickerDialog datePicKDialog = new WorkDateTimePickerDialog(
							WorkNewActivity.this, vStr);
					datePicKDialog.dateTimePicKDialog(vDateBegin.getTime());

					datePicKDialog.setDayChangeListener(WorkNewActivity.this);
					 InputMethodManager inputmanger = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				        if (inputmanger.isActive())
				        {
				        	inputmanger.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				        }
				} catch (ParseException e) {
					Log.d("xmx", "cash endtime");
					e.printStackTrace();
				}
			}
			
		}
	};

	private AlertDialog startTimeDialog;
	private AlertDialog endTimeDialog;
	private void initAlertDialog(String title) {
		View view = getLayoutInflater().inflate(
				R.layout.work_new_time_pop_window, null);
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		int week = cal.get(Calendar.DAY_OF_WEEK);
		
		TextView titleTv = (TextView) view.findViewById(R.id.titleTv);
		titleTv.setText(title);
		TextView nowTv = (TextView) view.findViewById(R.id.nowTv);
		TextView todayTv = (TextView) view.findViewById(R.id.todayTv);
		TextView tomorrowAMTv = (TextView) view.findViewById(R.id.tomorrowAMTv);
		TextView tomorrowPMTv = (TextView) view.findViewById(R.id.tomorrowPMTv);
		TextView settimeTv = (TextView) view.findViewById(R.id.settimeTv);
		TextView cancelTv = (TextView) view.findViewById(R.id.cancelTv);
		String[] weeksToday = {"日","一","二","三","四","五","六"};
		String[] weeksTomorrow = {"一","二","三","四","五","六","日"};
		todayTv.setText("今天(周"+ weeksToday[week-1] +") 下午14:00");
		tomorrowAMTv.setText("明天(周"+ weeksTomorrow[week-1] +") 上午09:00");
		tomorrowPMTv.setText("明天(周"+ weeksTomorrow[week-1] +") 下午14:00");
		
		final SimpleDateFormat sdf_time = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		final SimpleDateFormat sdf_date = new SimpleDateFormat("yyyyMMdd");
		if(title.equals("开始时间选择")){
			nowTv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mBeginTime = sdf_time.format(new Date());
					mAffar.setStartTime(mBeginTime);
					resetWorkView();
					startTimeDialog.dismiss();
				}
			});
			
			todayTv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mBeginTime = sdf_date.format(new Date())+" 14:00:00";
					mAffar.setStartTime(mBeginTime);
					resetWorkView();
					startTimeDialog.dismiss();
				}
			});
			
			tomorrowAMTv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Calendar cal = Calendar.getInstance();  
		            cal.setTime(new Date());  
		            cal.add(Calendar.DAY_OF_YEAR, +1);  
		            mBeginTime= sdf_date.format(cal.getTime()) + " 09:00:00";  
		            mAffar.setStartTime(mBeginTime);
		            resetWorkView();
		            startTimeDialog.dismiss();
				}
			});
			
			tomorrowPMTv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Calendar cal = Calendar.getInstance();  
		            cal.setTime(new Date());  
		            cal.add(Calendar.DAY_OF_YEAR, +1);  
		            mBeginTime= sdf_date.format(cal.getTime()) + " 14:00:00";  
		            mAffar.setStartTime(mBeginTime);
		            resetWorkView();
		            startTimeDialog.dismiss();
				}
			});
			
			cancelTv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
		            mBeginTime= "";  
		            mAffar.setStartTime(mBeginTime);
		            resetWorkView();
		            startTimeDialog.dismiss();
				}
			});
			
			settimeTv.setOnClickListener(mBeginTimeClick);

			if(startTimeDialog==null){
				startTimeDialog = new  AlertDialog.Builder(this).setView(view).create(); 
			}
			startTimeDialog.setCanceledOnTouchOutside(true);
			startTimeDialog.show();
		}else{
			nowTv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mEndTime = getEndTime(mBeginTime, sdf_time.format(new Date()));
					mAffar.setEndTime(mEndTime);
					resetWorkView();
					endTimeDialog.dismiss();
				}
			});
			
			todayTv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mEndTime = getEndTime(mBeginTime, sdf_date.format(new Date())+" 14:00:00");
					mAffar.setEndTime(mEndTime);
					resetWorkView();
					endTimeDialog.dismiss();
				}
			});
			
			tomorrowAMTv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Calendar cal = Calendar.getInstance();  
		            cal.setTime(new Date());  
		            cal.add(Calendar.DAY_OF_YEAR, +1);  
		            mEndTime= getEndTime(mBeginTime, sdf_date.format(cal.getTime()) + " 09:00:00");  
		            mAffar.setEndTime(mEndTime);
		            resetWorkView();
		            endTimeDialog.dismiss();
				}
			});
			
			tomorrowPMTv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Calendar cal = Calendar.getInstance();  
		            cal.setTime(new Date());  
		            cal.add(Calendar.DAY_OF_YEAR, +1);  
		            mEndTime= getEndTime(mBeginTime, sdf_date.format(cal.getTime()) + " 14:00:00");  
		            mAffar.setEndTime(mEndTime);
		            resetWorkView();
		            endTimeDialog.dismiss();
				}
			});
			
			cancelTv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mEndTime= "";  
		            mAffar.setEndTime(mEndTime);
		            resetWorkView();
		            endTimeDialog.dismiss();
				}
			});
			
			settimeTv.setOnClickListener(mEndTimeClick);

			if(endTimeDialog==null){
				endTimeDialog = new  AlertDialog.Builder(this).setView(view).create(); 
			}
			endTimeDialog.setCanceledOnTouchOutside(true);
			endTimeDialog.show();
		}
	}
	/**
	 * 事务详情的：关联、日志、畅聊
	 * @param inView 填充布局
	 * @param inName 名称(关联、日志、畅聊)
	 * @param inValue 
	 */
	public void resetShowViewRela(View inView, String inName, String inValue) {
		TextView TextViewName = (TextView) inView
				.findViewById(R.id.TextViewName);
		TextViewName.setText(inName);
		TextView TextViewValue = (TextView) inView
				.findViewById(R.id.TextViewValue);
		TextViewValue.setText(inValue);
		LinearLayoutWork.addView(inView);
	}
	/**
	 * 创建、编辑-时间点击后提醒和重复
	 * @param inView填充布局
	 * @param inName 提醒、重复
	 * @param inValue 值
	 */
	public void resetViewText(View inView, String inName, String inValue) {
		TextView TextViewName = (TextView) inView
				.findViewById(R.id.TextViewName);
		TextViewName.setText(inName);
		TextView TextViewValue = (TextView) inView
				.findViewById(R.id.TextViewValue);
		TextViewValue.setText(inValue);
		LinearLayoutWork.addView(inView);
	}
	/**
	 * 备注的的布局填充
	 * @param inView
	 */
	public void resetShowViewInfo(View inView) {
		int i;
		View vSubView = null;
		TextView TextViewValue = (TextView) inView
				.findViewById(R.id.TextViewValue);
		TextViewValue.setText(mAffar.getInfoStr());

		LinearLayout LinearLayoutSounds = (LinearLayout) inView
				.findViewById(R.id.LinearLayoutSounds);
		LayoutInflater flater = LayoutInflater.from(this);
		for (i = 0; i < mAffar.infos.size(); i++) {
			vSubView = flater.inflate(R.layout.work_new_info_item_cell, null);
			LinearLayoutSounds.addView(vSubView);
		}
		LinearLayoutWork.addView(inView);
	}
	/**
	 * 创建编辑-备注布局填充
	 * @param inView
	 */
	public void resetViewInfo(View inView) {
		int i;
		View vSubView = null;
		TextView TextViewName = (TextView) inView
				.findViewById(R.id.TextViewName);
		TextViewName.setText("备注");
		TextView TextViewValue = (TextView) inView
				.findViewById(R.id.TextViewValue);
		TextViewValue.setText(mAffar.getInfoStr());

		LinearLayout LinearLayoutSounds = (LinearLayout) inView
				.findViewById(R.id.LinearLayoutSounds);
		LayoutInflater flater = LayoutInflater.from(this);
		for (i = 0; i < mAffar.infos.size(); i++) {
			vSubView = flater.inflate(R.layout.work_new_info_item_cell, null);
			LinearLayoutSounds.addView(vSubView);
		}
		inView.setOnClickListener(mLineInfoClick);
		LinearLayoutWork.addView(inView);
	}
	/**
	 * 事务详情-负责人、成员的布局填充
	 * @param inView
	 * @param inType  "成员类型，c:创建者，o：负责人，m：成员"
	 */
	public void resetShowViewPerson(View inView, String inType) {
		int i;
		View vSubView;
		TextView TextViewName = (TextView) inView
				.findViewById(R.id.TextViewName);
		
		Button vButtonMsg = (Button) inView.findViewById(R.id.ButtonMsg);
		vButtonMsg.setTag(inType);
		
		
		Button vButtonPhone = (Button) inView.findViewById(R.id.ButtonPhone);
		vButtonPhone.setTag(inType);
		
		if (inType.equals("o")) {
			TextViewName.setText("负责人");
			vButtonMsg.setOnClickListener(mMsgPhoneLeaderClick);
			vButtonPhone.setOnClickListener(mMsgPhoneLeaderClick);
			
		} else {
			TextViewName.setText("成员");
			vButtonMsg.setOnClickListener(mMsgPhoneMemberClick);
			vButtonPhone.setOnClickListener(mMsgPhoneMemberClick);			
		}

		

		LinearLayout LinearLayoutPerson = (LinearLayout) inView
				.findViewById(R.id.LinearLayoutPerson);

		LayoutInflater flater = LayoutInflater.from(this);
		int vCount = 0;
		for (i = 0; i < mAffar.memebers.size(); i++) {
			BUAffarMember vPerson = mAffar.memebers.get(i);
			if (vPerson.getType().equals(inType)) {
				vCount = vCount + 1;
				vSubView = flater.inflate(R.layout.work_person_cell, null);
				TextView vSubTextViewName = (TextView) vSubView
						.findViewById(R.id.TextViewName);
				vSubTextViewName.setText(vPerson.getName());

				String imgName = "";//头像里显示的字
				if(!TextUtils.isEmpty(vPerson.getName())){
					imgName = vPerson.getName().substring(vPerson.getName().length()-1);
				}
				CircleImageView ImageViewPic = (CircleImageView) vSubView
						.findViewById(R.id.ImageViewPic);
				ImageViewPic.setOnClickListener(mPicPersonClick);
				ImageViewPic.setTag(R.id.ImageViewPic, i);
				if (vPerson.deviceType.equals("1")) {
					// 人员
//					Bitmap bm = com.utils.common.Util.createBGBItmap(this, R.drawable.ic_person_default_avatar_gray, R.color.avatar_text_color, R.dimen.avatar_text_size, imgName);
//					ImageViewPic.setImageBitmap(bm);
					ImageViewPic.setImageResource(R.drawable.ic_default_avatar);
				} else {
					// 组织
//					Bitmap bm = Util.createBGBItmap(this, R.drawable.ic_group_default_avatar, R.color.avatar_text_color, R.dimen.avatar_text_size, imgName);
//					ImageViewPic.setImageBitmap(bm);
					ImageViewPic.setImageResource(R.drawable.ic_default_avatar);
				}
				Log.d("xmx","vPerson.getHeadPic():"+vPerson.picUrl);
				/*ImageLoader.getInstance().displayImage(vPerson.picUrl,
							ImageViewPic, options, animateFirstListener);*/
				if (vPerson.picUrl!=null && !(vPerson.picUrl.equals(""))&&!vPerson.picUrl.endsWith(GlobalVariable.PERSON_DEFAULT_AVATAR)&&!vPerson.picUrl.endsWith(GlobalVariable.ORG_DEFAULT_AVATAR))
				{
					ImageLoader.getInstance().displayImage(vPerson.picUrl,
						ImageViewPic, options);
//					ImageLoader.load(ImageViewPic, vPerson.picUrl);
				}
//				ImageLoader.load(ImageViewPic, vPerson.picUrl);
				LinearLayoutPerson.addView(vSubView);
			}
		}

		LinearLayoutWork.addView(inView);
	}
	/**
	 * 跳转用户主页
	 */
	private OnClickListener mPicPersonClick = new OnClickListener() {
		@Override
		public void onClick(View v) {/*vPerson.memeberId+""*/
			 int vId = (Integer) v.getTag(R.id.ImageViewPic);
			BUAffarMember buAffarMember = mAffar.memebers.get(vId);
			
			if (buAffarMember.deviceType.equals("1")) {//好友人脉
				
				ENavigate.startRelationHomeActivity(WorkNewActivity.this,buAffarMember.getMemeberId()+"");
			}else{//组织好友
				ENavigate.startOrgMyHomePageActivityByUseId(WorkNewActivity.this, Long.valueOf(buAffarMember.getMemeberId()+""));
			}
			 
//			ENavigate.startRelationHomeActivity(WorkNewActivity.this, vId);
		}
	};
	/**
	 * 创建编辑 -负责人、成员的布局填充
	 * @param inView
	 * @param inType 成员类型，c:创建者，o：负责人，m：成员",
	 */
	public void resetViewPerson(View inView, String inType)
	{
		int i;
		View vSubView;
		TextView TextViewName = (TextView) inView
				.findViewById(R.id.TextViewName);
	
		if (inType.equals("o")) {
			TextViewName.setText("负责人");
		} else {
			TextViewName.setText("成员");
		}

		
		LinearLayout LinearLayoutPerson = (LinearLayout) inView
				.findViewById(R.id.LinearLayoutPerson);

		LayoutInflater flater = LayoutInflater.from(this);
		int vCount = 0;
		for (i = 0; i < mAffar.memebers.size(); i++) {//遍历事务成员
			BUAffarMember vPerson = mAffar.memebers.get(i);
			Log.d("xmx", " type:" + vPerson.getType());

			if (vPerson.getType().equals(inType)) {
				vCount = vCount + 1;
				vSubView = flater.inflate(R.layout.work_person_cell, null);
				TextView vSubTextViewName = (TextView) vSubView
						.findViewById(R.id.TextViewName);
				vSubTextViewName.setText(vPerson.getName());
				
				String imgName = "";//头像里显示的字
				if(!TextUtils.isEmpty(vPerson.getName())){
					imgName = vPerson.getName().substring(vPerson.getName().length()-1);
				}

				CircleImageView ImageViewPic = (CircleImageView) vSubView
						.findViewById(R.id.ImageViewPic);
				ImageViewPic.setTag(vPerson.getMemeberId() + "");
				if (vPerson.deviceType.equals("1")) {
					// 人员
//					Bitmap bm = com.utils.common.Util.createBGBItmap(this, R.drawable.ic_person_default_avatar_gray, R.color.avatar_text_color, R.dimen.avatar_text_size, imgName);
//					ImageViewPic.setImageBitmap(bm);
					ImageViewPic.setImageResource(R.drawable.ic_default_avatar);
				} else {
					// 组织
//					Bitmap bm = Util.createBGBItmap(this, R.drawable.ic_group_default_avatar, R.color.avatar_text_color, R.dimen.avatar_text_size, imgName);
//					ImageViewPic.setImageBitmap(bm);
					ImageViewPic.setImageResource(R.drawable.ic_default_avatar);
				}
				Log.d("xmx","pic work name:"+vPerson.getName()+",pic:"+vPerson.getHeadPic());
				if (vPerson.getHeadPic()!=null && !(vPerson.getHeadPic().equals(""))&&!vPerson.getHeadPic().endsWith(GlobalVariable.PERSON_DEFAULT_AVATAR)&&!vPerson.getHeadPic().endsWith(GlobalVariable.ORG_DEFAULT_AVATAR))
				{
					ImageLoader.getInstance().displayImage(vPerson.getHeadPic(),
						ImageViewPic, options);
//					ImageLoader.load(ImageViewPic, vPerson.getHeadPic());
				}
				LinearLayoutPerson.addView(vSubView);
			}
		}

		LinearLayoutWork.addView(inView);
	}

	
	/**
	 * 事务，颜色地点的填充
	 * @param inView
	 * @param inType  inType==0 color
	 * @param inName	颜色地点
	 * @param inValue 
	 */
	public void resetViewColor(View inView, int inType, String inName,
			String inValue) {
		TextView TextViewName = (TextView) inView
				.findViewById(R.id.TextViewName);
		TextViewName.setText(inName);
		TextView TextViewValue = (TextView) inView
				.findViewById(R.id.TextViewValue);
		TextViewValue.setText(inValue);
		ImageView ImageViewPic = (ImageView) inView
				.findViewById(R.id.ImageViewPic);

		if (inType == 0) {
			TextViewValue.setTextColor(Color.rgb(53, 53, 53));
			if (mAffar.getColor() == 0)
				ImageViewPic.setImageResource(R.drawable.icon_level1);
			if (mAffar.getColor() == 1 || mAffar.getColor() == 2)
				ImageViewPic.setImageResource(R.drawable.icon_level3);
			if (mAffar.getColor() == 3 || mAffar.getColor() == 4)
				ImageViewPic.setImageResource(R.drawable.icon_level2);
		} else {
			ImageViewPic.setImageResource(R.drawable.work_location_point);
		}

		LinearLayoutWork.addView(inView);
	}
	/**
	 * 跳转时间页面
	 */
	private OnClickListener mLineTimeClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(WorkNewActivity.this,
					WorkNewTimeActivity.class);
			intent.putExtra("RemindValue", mAffar.getRemindAhead());
			intent.putExtra("RemindType", mAffar.getRemindType());
			intent.putExtra("RepeatType", mAffar.getRepeatType());

			intent.putExtra("BeginTime", mAffar.getStartTime());
			intent.putExtra("EndTime", mAffar.getEndTime());

			startActivityForResult(intent, 100);
		}
	};

	// 备注 102a
	private OnClickListener mLineInfoClick = new OnClickListener() {
		@Override
		public void onClick(View v) {

			Log.d("xmx", "mLineInfoClick");
			Intent intent = new Intent(WorkNewActivity.this,
					WorkNewInfoActivity.class);
			intent.putExtra("Info", mAffar.infoStr);
			startActivityForResult(intent, 110);
		}
	};

	// 提醒
	private OnClickListener mLineRemandClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(WorkNewActivity.this,
					WorkNewRemaindActivity.class);
			intent.putExtra("RemindValue", mAffar.getRemindAhead());
			intent.putExtra("RemindType", mAffar.getRemindType());
			startActivityForResult(intent, 102);
		}
	};

	// 重复
	private OnClickListener mLineRepeatClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(WorkNewActivity.this,
					WorkNewRepeatActivity.class);
			intent.putExtra("RepeatType", mAffar.getRepeatType());
			startActivityForResult(intent, 103);
		}
	};

	// 颜色
	private OnClickListener mLineColorClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(WorkNewActivity.this,
					WorkNewColorActivity.class);
			intent.putExtra("Color", mAffar.getColor());
			startActivityForResult(intent, 105);
		}
	};

	// 负责人
	private OnClickListener mLineLeadClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			onButtomMenuLeadClick(v);
		}
	};

	// 成员
	private OnClickListener mLineMemberClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			onButtomMenuMemberClick(v);
		}
	};

	//重要程度
	public void onButtomMenuLevelClick(View v) {
		mPopWindow.dismiss();
		Intent intent = new Intent(WorkNewActivity.this,
				WorkNewColorActivity.class);
		intent.putExtra("Color", mAffar.getColor());
		startActivityForResult(intent, 105);
	}

	// 成员
	private OnClickListener mMsgPhoneMemberClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String vType = v.getTag().toString();
			Log.d("xmx", "msgphoneclikc:" + vType);
			List<BUAffarMember> vMembers = mAffar.getAffarMember(vType);
			if (vMembers.size() > 0) {
				WorkPhoneChoseDialog vDialogPhone = new WorkPhoneChoseDialog(
						WorkNewActivity.this, vMembers, vType);
				vDialogPhone.selectPhoneDialog();

				vDialogPhone.setPhoneChangeListener(WorkNewActivity.this);
			} else if (vMembers.size() == 1) {
				Log.d("xmx", "size=1 onClick");
			}
		}
	};
	
	// 负责人
	private OnClickListener mMsgPhoneLeaderClick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				String vType = v.getTag().toString();
				Log.d("xmx", "msgphoneclikc:" + vType);
				List<BUAffarMember> vMembers = mAffar.getAffarMember(vType);
				if (vMembers.size() > 0) {
					WorkPhoneChoseLeaderDialog vDialogPhone = new WorkPhoneChoseLeaderDialog(
							WorkNewActivity.this, vMembers, vType);
					vDialogPhone.selectPhoneDialog();

					vDialogPhone.setPhoneChangeListener(WorkNewActivity.this);
				} else if (vMembers.size() == 1) {
					Log.d("xmx", "size=1 onClick");
				}
			}
	};

	public void onButtomMenuLeadClick(View v) {
		// gushi
		ConnectionNode vNode = mAffar.genConnectonNodeWithMemberType("o");
		ENavigate.startIMRelationSelectActivity(this, 2002,
				IMRelationSelectActivity.FT_IM, vNode.getListConnections(),
				true, false, true, false);
	}

	// 成员
	public void onButtomMenuMemberClick(View v) {
		ConnectionNode vNode = mAffar.genConnectonNodeWithMemberType("m");
		ENavigate.startIMRelationSelectActivity(this, 2003,
				IMRelationSelectActivity.FT_IM, vNode.getListConnections(),
				false, false, true, false);

	}

	// 关联
	public void onButtomMenuReleationClick(View v) {

		mRelationSelEdityType = "a"; // a add e edit
		ENavigate.startRelatedResourceActivityForResult(this, 2001, "",
				ResourceType.People, null);
		
	}

	// 地点
	public void onButtomMenuLocationClick(View v) {
		mPopWindow.dismiss();
		Log.d("xmx", "addr click");
		Intent intent = new Intent(WorkNewActivity.this,
				WorkNewLocationActivity.class);
		intent.putExtra("Location", mAffar.location);
		intent.putExtra("WorkNewActivity", true);
		startActivityForResult(intent, 111);

	}
	
	//更多
	public void onButtomMenuMoreClick(View v){
		int[] location = new int[2];  
        v.getLocationOnScreen(location); 
		if (mPopWindow.isShowing()) {
			mPopWindow.dismiss();
		} else {
			IV_tri.setVisibility(View.VISIBLE);
			mPopWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1]-mPopWindow.getContentView().getMeasuredHeight());
		}
	}
	
	//重复
	public void onButtomMenuRepeatClick(View v){
		mPopWindow.dismiss();
//		Toast.makeText(getBaseContext(), "正在努力实现...", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(WorkNewActivity.this,
				WorkNewRepeatActivity.class);
		intent.putExtra("RepeatType", mAffar.getRepeatType());
		startActivityForResult(intent, 103);
	}

	//备注
	public void onButtomMenuRemarkClick(View v){
		mPopWindow.dismiss();
//		Toast.makeText(getBaseContext(), "正在努力实现...", Toast.LENGTH_SHORT).show();
		Log.d("xmx", "mLineInfoClick");
		Intent intent = new Intent(WorkNewActivity.this,
				WorkNewInfoActivity.class);
		intent.putExtra("Info", mAffar.infoStr);
		startActivityForResult(intent, 110);
	}
	
	private PopupWindow mPopWindow;
	/**更多弹窗*/
	private void initPopupWindow() {
		View view = getLayoutInflater().inflate(
				R.layout.work_new_pop_window, null);
		view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		mPopWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
		ViewGroup.LayoutParams.WRAP_CONTENT);
//		mPopWindow.setOutsideTouchable(true);
//		mPopWindow.setBackgroundDrawable(new ColorDrawable(0));
		mPopWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				IV_tri.setVisibility(View.GONE);
			}
		});
	}

	private OnClickListener mLineLocationClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.d("xmx", "addr click");
			Intent intent = new Intent(WorkNewActivity.this,
					WorkNewLocationActivity.class);
			intent.putExtra("Location", mAffar.location);
			intent.putExtra("WorkNewActivity", true);
			startActivityForResult(intent, 111);
		}
	};

	// 菜单
	private OnClickListener mButtonCommitClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.d("xmx", "mLineRelationClick:" + v.getTag());
			dealConfirAffar();
		}
	};
	
	private OnClickListener mIMClick = new OnClickListener(){
		@Override
		public void onClick(View v) {
			isCreate = false;
			WorkReqUtil.getCharId(WorkNewActivity.this, WorkNewActivity.this, mAffar,mUserId, null);
			mButtonClick=104;
		}
	};

	public void dealConfirAffar() {
		mAffarOperType="f";
		showLoadingDialog();
		WorkReqUtil.modifyAffarStatus(WorkNewActivity.this, this, mAffar.id
				+ "", mUserId + "", "f", null);
		
	}

	// 日志
	private OnClickListener mLineLogShowClick = new OnClickListener() {
		@Override
		public void onClick(View v) {

			Log.d("xmx", "mLineRelationClick:" + mAffar.id);
			Intent intent = new Intent(WorkNewActivity.this,
					WorkLogActivity.class);
			intent.putExtra("AffarId", mAffar.id);
			startActivity(intent);

		}
	};

	// 关联
	private OnClickListener mLineRelationButtonDelClick = new OnClickListener() {
		@Override
		public void onClick(View v) {

			String vTagStr = v.getTag().toString();
			String vType = vTagStr.substring(0, 1);
			String vLabel = vTagStr.substring(1, vTagStr.length());

			mAffar.deleteRelationTitle(vType, vLabel);
			Log.d("xmx", "mLineRelation del:" + vType + ",label:" + vLabel);
			resetWorkView();
		}
	};
	/**跳转 ：事务详情----点击关联----关联*/
	private OnClickListener mLineRelationShowClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.d("xmx", "mLineRelationClick:" + v.getTag());
			Intent intent = new Intent(WorkNewActivity.this,
					WorkAffarRelationActivity.class);
			intent.putExtra("AffarId", mAffar.id);
			startActivity(intent);
		}
	};

	// 关联
	private OnClickListener mLineRelationClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.d("xmx", "mLineRelationClick:" + v.getTag());
			ResourceNode vNode = null;

			String vTagStr = v.getTag().toString();
			String vType = vTagStr.substring(0, 1);
			String vLabel = vTagStr.substring(1, vTagStr.length());

			mRelationSelType = vType;
			mRelationSelLabel = vLabel;
			mRelationSelEdityType = "e";

			ResourceType vResourceType = ResourceType.People;
			if (vType.equals("p")) {//人脉
				vResourceType = ResourceType.People;
				vNode = mAffar.genConnectonNodeByTypeLabel("p", vLabel);
			}
			if (vType.equals("o")) {//组织
				vResourceType = ResourceType.Organization;
				vNode = mAffar.genConnectonNodeByTypeLabel("o", vLabel);
			}
			if (vType.equals("k")) {//知识
				vResourceType = ResourceType.Knowledge;
				vNode = mAffar.genKnowledgeNodeByTypeLabel("k", vLabel);
			}
			if (vType.equals("r")) {//事件
				vResourceType = ResourceType.Affair;
				vNode = mAffar.genAffairNodeByTypeLabel("r", vLabel);

			}
			ENavigate.startRelatedResourceActivityForResult(
					WorkNewActivity.this, 2001, "", vResourceType, vNode);
		}
	};

	@Override
	public void bindData(int tag, Object object) {
		dismissLoadingDialog();
		Log.d("xmx", "bindData:" + tag);
		if (object != null) {
			Log.d("xmx", "bindData:" + object.toString());

		} else {
//			ToastUtil.showToast(this, "网络错误");
			return;
		}
		switch (tag) {
		case WorkReqType.AFFAIR_DETAIL_GET://事务详情
			mAffar = (BUAffar) object;
			resetWorkView();
			break;
		case WorkReqType.AFFAIR_CREATE:
			mButtonClick=0;
			BUResponseData vResponseData = (BUResponseData) object;
			if (vResponseData.succeed) {
				isCreate = true;
				// 成功
				ToastUtil.showToast(this, "创建事务成功");
				mOperateType = "s";
				mAffarId = vResponseData.id + "";
				mAffar.id = vResponseData.id;
				Log.d("xmx", "mAffarId:" + mAffarId);
				setClock();
				initData();
				initTopMenu();
				WorkReqUtil.getCharId(WorkNewActivity.this, WorkNewActivity.this, mAffar,mUserId, null);
			} else {
				ToastUtil.showToast(this, "创建事务失败");
			}
			break;
		case WorkReqType.AFFAIR_EDIT:
			mButtonClick=0;
			BUResponseData vResponseDataEdit = (BUResponseData) object;
			if (vResponseDataEdit.succeed) {
				// 成功
				ToastUtil.showToast(this, "编辑事务成功");
				mOperateType = "s";
				mAffarId = vResponseDataEdit.id + "";

				setClock();
				initData();
				initTopMenu();
			} else {
				ToastUtil.showToast(this, "编辑事务失败");
			}
			break;
		case WorkReqType.AFFAIR_MODIFY_STATUS: // 改状态
			mButtonClick=0;
			BUResponseData vResponseDataModify = (BUResponseData) object;
			if (vResponseDataModify.succeed) {
				// 成功
				String vMsg = "修改成功";
				if (mAffarOperType.equals("f"))
					vMsg = "事务已完成";
				if (mAffarOperType.equals("r"))
					vMsg = "事务重新开启";
				if (mAffarOperType.equals("c"))
					vMsg = "事务举报成功";
				if (mAffarOperType.equals("q"))
					vMsg = "退出事务成功";
				if (mAffarOperType.equals("d"))
					vMsg = "事务删除成功";

				Toast.makeText(this, vMsg, Toast.LENGTH_SHORT).show();
				if (mAffarOperType.equals("d") || mAffarOperType.equals("q"))
					finish();
				else {
					mOperateType = "s";
					mAffarId = vResponseDataModify.id + "";
					initData();
					initTopMenu();
				}
			} else {
				Toast.makeText(this, "修改失败", Toast.LENGTH_SHORT).show();
			}
			break;
			
		case WorkReqType.AFFAIR_CHART: // 获取聊天id
			mButtonClick=0;
			BUResponseData vResponseDataChar = (BUResponseData) object;
			if (vResponseDataChar.succeed && !isCreate) 
			{
				ENavigate.startIMGroupActivity(WorkNewActivity.this, vResponseDataChar.id+"",1, isMemberO(App.getUserID()));
			}
			else if(!vResponseDataChar.succeed)
			{
				Toast.makeText(this, "获取聊天id失败", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
	
	public boolean isMemberO(String memberId){
		boolean IsMemberO = false;
		for(BUAffarMember member:mAffar.memebers){
			if((member.memeberId+"").equals(memberId)){
				if(member.type.equals("o")){
					IsMemberO = true;
					break;
				}
			}
		}
		return IsMemberO;
	}
	//设置时钟的样式
	public void setClock() {
		long vRemindTime=System.currentTimeMillis();
		
		if (!mAffar.remindType.equals("o")) {
			try {
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
				Date vDate;

				vDate = df.parse(mAffar.startTime);
				long vStartTime = vDate.getTime();
				long vEndTime = vStartTime;
				if (vRemindTime<vStartTime)
				{
				
				Intent intent = new Intent(WorkNewActivity.this,
						WorkRemindClockReceiver.class);
				intent.setAction("JT_WORK");
				intent.setType("REMIND");
				intent.setData(Uri.EMPTY);
				intent.addCategory("CATEGORY_ACTIVITY");

				

				
				Log.d("xmx", "startTime:" + vStartTime);
				long vAdd = 0;
				if (mAffar.remindType.equals("m"))
					vAdd = 60 * 1000 * mAffar.remindAhead;
				if (mAffar.remindType.equals("h"))
					vAdd = 60 * 60 * 1000 * mAffar.remindAhead;
				if (mAffar.remindType.equals("d"))
					vAdd = 24 * 60 * 60 * 1000 * mAffar.remindAhead;

				vStartTime = vStartTime - vAdd;
				Log.d("xmx", "vStartTime end:" + vStartTime + ",vAdd:" + vAdd);
				Log.d("xmx", "sys :" + System.currentTimeMillis());

				intent.putExtra("Title", mAffar.title);
				intent.putExtra("AffarId", mAffar.id);
				intent.putExtra("UserId", mUserId);
				intent.putExtra("RemindEndTime", vEndTime);

				long vRepeatTime = 0;
				if (mAffar.repeatType.equals("d"))
					vRepeatTime = 24 * 60 * 60 * 1000;
				if (mAffar.repeatType.equals("w"))
					vRepeatTime = 7 * 24 * 60 * 60 * 1000;
				if (mAffar.repeatType.equals("m"))
					vRepeatTime = 30 * 24 * 60 * 60 * 1000;
				if (mAffar.repeatType.equals("y"))
					vRepeatTime = 365 * 24 * 60 * 60 * 1000;

				intent.putExtra("RemindSpace", vRepeatTime);

				int vAffid = (int) mAffar.id;
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						WorkNewActivity.this, vAffid, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				// alarmCount是你需要记录的闹钟数量，必须保证你所发的alarmCount不能相同，最后一个参数填0就可以。
				AlarmManager am = (AlarmManager) getSystemService(WorkNewActivity.this.ALARM_SERVICE);
				am.cancel(pendingIntent);
				am.set(AlarmManager.RTC_WAKEUP, vStartTime, pendingIntent);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}
	
	@Override
	public void initJabActionBar() {//ActionBar的设置
		if (mOperateType.equals("c")){
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "创建事务", false, null, true, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
		}else
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "事务详情", false, null, true, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		int i;
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 100 && resultCode == 1000) {
			// 时间
			int vValue = data.getIntExtra("RemindValue", 0);
			String vRemindType = data.getStringExtra("RemindType");
			mAffar.setRepeatType(data.getStringExtra("RepeatType"));
			mAffar.setRemindAhead(vValue);
			mAffar.setRemindType(vRemindType);

			String vBeginTime = data.getStringExtra("BeginTime");
			String vEndTime = data.getStringExtra("EndTime");
			if (vBeginTime != null)
				mAffar.setStartTime(vBeginTime);
			if (vEndTime != null)
				mAffar.setEndTime(vEndTime);
			Log.d("xmx", "mAffar endTime:" + mAffar.endTime);

			resetWorkView();
		}
		if (requestCode == 102 && resultCode == 1000) {
			// 提醒
			int vValue = data.getIntExtra("RemindValue", 0);
			String vRemindType = data.getStringExtra("RemindType");
			mAffar.setRemindAhead(vValue);
			mAffar.setRemindType(vRemindType);
			resetWorkView();
		}
		if (requestCode == 103 && resultCode == 1000) {
			// 重复
			String vType = data.getStringExtra("RepeatType");
			mAffar.setRepeatType(vType);
			resetWorkView();
		}
		if (requestCode == 105 && resultCode == 1000) {
			// 颜色
			int vValue = data.getIntExtra("Color", 0);
			mAffar.setColor(vValue);
			resetWorkView();

		}
		if (requestCode == 110 && resultCode == 1000) {
			// 备注
			String vStr = data.getStringExtra("Info");
			mAffar.infoStr = vStr;
			resetWorkView();
		}
		if (requestCode == 111 && resultCode == 1000) {
			// 地址
			String vStr = data.getStringExtra("Location");
			mAffar.location = vStr;
			if (!"不显示位置".equals(vStr)) {
				resetWorkView();
			}
		}
		if (requestCode == 2002 && resultCode == RESULT_OK) {
			// 负责人
			Log.d("xmx", "2002 back");
			if (data.hasExtra(ENavConsts.redatas)) {
				Log.d("xmx", "2002 back 1");
				ArrayList<Connections> vList = (ArrayList<Connections>) data
						.getSerializableExtra(ENavConsts.redatas);

				mAffar.deleteMemberByType("o");
				mAffar.addMemberByList("o", vList);
				mAffar.deleteMemberByLeader();
				resetWorkView();
			}
		}
		if (requestCode == 2003 && resultCode == RESULT_OK) {
			// 成员
			Log.d("xmx", "2003 back");
			if (data.hasExtra(ENavConsts.redatas)) {
				Log.d("xmx", "2003 back 1");
				ArrayList<Connections> vList = (ArrayList<Connections>) data
						.getSerializableExtra(ENavConsts.redatas);

				mAffar.deleteMemberByType("m");
				mAffar.addMemberByList("m", vList);
				mAffar.deleteLeaderByMember();
				resetWorkView();
			}
		}

		if (requestCode == 2001 && resultCode == RESULT_OK &&data !=null) {

			// 关联
			if (mRelationSelEdityType.equals("e")) {
				mAffar.deleteRelationTitle(mRelationSelType, mRelationSelLabel);
			}

			if (data.hasExtra(EConsts.Key.RELATED_PEOPLE_NODE)) { // 人脉信息
				Log.d("xmx", "RELATED_PEOPLE_NODE");

				ConnectionNode connectionNode = (ConnectionNode) data
						.getSerializableExtra(EConsts.Key.RELATED_PEOPLE_NODE);
				mAffar.addRelationWithConnectionNode(connectionNode, "p");
			}
			if (data.hasExtra(EConsts.Key.RELATED_ORGANIZATION_NODE)) { // 组织信息
				Log.d("xmx", "RELATED_ORGANIZATION_NODE");
				ConnectionNode connectionNode = (ConnectionNode) data
						.getSerializableExtra(EConsts.Key.RELATED_ORGANIZATION_NODE);
				mAffar.addRelationWithConnectionNode(connectionNode, "o");
			}
			if (data.hasExtra(EConsts.Key.RELATED_KNOWLEDGE_NODE)) { // 知识信息
				Log.d("xmx", "RELATED_KNOWLEDGE_NODE");

				KnowledgeNode ResourNode = (KnowledgeNode) data
						.getSerializableExtra(EConsts.Key.RELATED_KNOWLEDGE_NODE);
				mAffar.addRelationWithKnowledgeNode(ResourNode, "k");
			}
			if (data.hasExtra(EConsts.Key.RELATED_AFFAIR_NODE)) {// 事件信息
				Log.d("xmx", "RELATED_AFFAIR_NODE");

				AffairNode affairNode = (AffairNode) data
						.getSerializableExtra(EConsts.Key.RELATED_AFFAIR_NODE);
				mAffar.addRelationWithAffairNode(affairNode, "r");

			}

			resetWorkView();
			Log.d("xmx", "people return");
		}
	}
	
	@Override
	public void onPhoneChagne(String ouType, String outPhone) {//跳转系统电话界面
		Log.d("xmx", "outType:" + ouType + " click phone:" + outPhone);
		if (ouType.equals("m")) {
			Uri smsToUri = Uri.parse("smsto:" + outPhone);
			Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
			intent.putExtra("sms_body", "");
			startActivity(intent);
		} else {
			Uri telUri = Uri.parse("tel:" + outPhone);
			Intent intent = new Intent(Intent.ACTION_DIAL, telUri);
			startActivity(intent);
		}
	}

	@Override
	public void onDealChose(String outOperate) {
		if (mAffar != null) {
			mAffarOperType = outOperate;
			Log.d("xmx", "mAffar:" + outOperate);
			if (outOperate.equals("e")) {//编辑事务
				mOperateType = "b";
				mAffarId = mAffar.id + "";
				initData();
				initTopMenu();

			} else {
				if (mButtonClick==0)
				{
				mButtonClick=1;
				showLoadingDialog();
				WorkReqUtil.modifyAffarStatus(WorkNewActivity.this, this,
						mAffar.id + "", mUserId + "", outOperate, null);
				}
			}
		}
	}

	@Override
	public void onDayChagne(String outDay) {
		Log.d("xmx", "outDay:" + outDay);
		if (mSetTimeFlag == 0){
			mBeginTime = outDay;
			mAffar.setStartTime(mBeginTime);
		}else{
			mEndTime = getEndTime(mBeginTime, outDay);
			mAffar.setEndTime(mEndTime);
		}

		resetWorkView();
	}
	
	public String getEndTime(String startTime, String endTime){
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			if (!endTime.equals("")) {
				Log.d("xmx", "mAffar !=null");
				Date vDateBegin;
				vDateBegin = df.parse(startTime);
				Date vDateEnd = df.parse(endTime);
				if (vDateEnd.getTime() < vDateBegin.getTime()) {
					Toast.makeText(this, "截至时间不能早于开始时间",
							Toast.LENGTH_SHORT).show();
					endTime = "";
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return endTime;
	}
	
	@Override  
    public boolean dispatchTouchEvent(MotionEvent ev) {  
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {  
        	if(EditTextTitle!=null){
        		EditTextTitle.clearFocus();
        	}
        }  
        return super.dispatchTouchEvent(ev);  
    }
	
}
