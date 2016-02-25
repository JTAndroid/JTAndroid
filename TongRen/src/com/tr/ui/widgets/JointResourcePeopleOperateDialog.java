package com.tr.ui.widgets;

import com.tr.R;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.obj.AffairsMini;
import com.tr.model.obj.Connections;
import com.tr.model.obj.KnowledgeMini;
import com.tr.model.obj.ResourceBase;
import com.tr.model.obj.ResourceBase.ResourceType;
import com.tr.ui.widgets.KnoCategoryAlertDialog.OperType;
import com.utils.common.EUtil;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 资源对接人脉操作弹出框
 * @author leon
 */
public class JointResourcePeopleOperateDialog extends Dialog implements View.OnClickListener{

	private final String TAG = getClass().getSimpleName();
	
	private LinearLayout parentLl;
	private TextView smsTv; // 短信
	private TextView telTv; // 电话
	private LinearLayout userParentLl; // 用户操作
	private TextView forwardTv; // 转发
	private TextView shareTv; // 分享
	private LinearLayout peoParentLl; // 人脉操作
	private TextView inviteTv; // 邀请
	
	
	private Context mContext;
	private OnOperateSelectListener mListener;
	private Connections mAttachConnections; // 关联对象
	private String mTitle; // 资源标题 
	
	private ResourceBase mRes; // 资源对象
	
	public JointResourcePeopleOperateDialog(Context context) {
		super(context, R.style.dialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.widget_joint_res_peo_oper_dialog);
		initVars(context);
		initControls();
	}
	
	private void initVars(Context context){
		mContext = context;
//		mAttachConnections = connections;
	}
	
	private void initControls(){
		parentLl = (LinearLayout) findViewById(R.id.parentLl);
		smsTv = (TextView) findViewById(R.id.smsTv);
		smsTv.setOnClickListener(this);
		telTv = (TextView) findViewById(R.id.telTv); 
		telTv.setOnClickListener(this);
		userParentLl = (LinearLayout) findViewById(R.id.userParentLl);
		forwardTv = (TextView) findViewById(R.id.forwardTv);
		forwardTv.setOnClickListener(this);
		shareTv = (TextView) findViewById(R.id.shareTv);
		shareTv.setOnClickListener(this);
		peoParentLl = (LinearLayout) findViewById(R.id.peoParentLl);
//		inviteTv = (TextView) findViewById(R.id.inviteTv);添加去掉
//		inviteTv.setOnClickListener(this);
	}
	
	/**
	 * 显示对话框
	 * @param attachView
	 * @param affair
	 */
	public void show(View attachView, Object attachObject) {

		if (attachView == null || attachObject == null
				|| (!(attachObject instanceof AffairsMini) && !(attachObject instanceof KnowledgeMini2))) {
			Toast.makeText(mContext, "参数不完整!", Toast.LENGTH_SHORT).show();
			return;
		}
		if (attachObject instanceof AffairsMini) {
			if (((AffairsMini) attachObject).connections.type == Connections.type_org) {
				Toast.makeText(mContext, "此操作不支持机构用户", Toast.LENGTH_SHORT).show();
				return;
			}
			mRes = (ResourceBase) attachObject;
			mRes.setResourceType(ResourceType.Requirement);
			mTitle = ((AffairsMini) attachObject).title;
			mAttachConnections = ((AffairsMini) attachObject).connections;
		}
		else if(attachObject instanceof KnowledgeMini2){
			if (((KnowledgeMini2) attachObject).connections.type == Connections.type_org) {
				Toast.makeText(mContext, "此操作不支持机构用户", Toast.LENGTH_SHORT).show();
				return;
			}
			mRes = (ResourceBase) attachObject;
			mRes.setResourceType(ResourceType.Knowledge);
			mTitle = ((KnowledgeMini2) attachObject).title;
			mAttachConnections = ((KnowledgeMini2) attachObject).connections;
		}
		
		setDialogLocation(attachView);
		customDialog(mAttachConnections);
		show();
	}
	
	// 定制对话框样样式，用户还是人脉
	private void customDialog(Connections connections){
		
		if (!connections.isOnline()) { // 人脉
//			peoParentLl.setVisibility(View.GONE);
			userParentLl.setVisibility(View.GONE);
		} 
//		else { // 人脉
//			userParentLl.setVisibility(View.GONE);
//		}
	}
	
	// 设置对话框显示位置
	private void setDialogLocation(View attachView){
		
		int notiBarHeight = EUtil.getStatusBarHeight(mContext);

		int[] location = new int[2];
		attachView.getLocationOnScreen(location); // getLocationOnScreen();getLocationInWindow()

		int[] size = new int[2];
		attachView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		size[0] = attachView.getMeasuredWidth();
		size[1] = attachView.getMeasuredHeight();

		WindowManager manage = ((Activity) mContext).getWindowManager();
		Display display = manage.getDefaultDisplay();
		int[] screenSize = new int[2];
		screenSize[0] = display.getWidth();
		screenSize[1] = display.getHeight();

		/*
		 * DisplayMetrics dm = new DisplayMetrics();
		 * getWindowManager().getDefaultDisplay().getMetrics(dm);
		 */

		int[] targetSize = new int[2];
		parentLl.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		targetSize[0] = parentLl.getMeasuredWidth();
		targetSize[1] = parentLl.getMeasuredHeight();

		int[] targetLocation = new int[2];
		targetLocation[0] = 0;
		if ((location[1] + size[1] + targetSize[1]) >= screenSize[1]) { // 下方显示不开
			targetLocation[1] = location[1] - targetSize[1];
			parentLl.setBackgroundResource(R.drawable.joint_res_oper_down_bg);
		} 
		else { // 下方可以显示
			targetLocation[1] = location[1] + size[1];
			parentLl.setBackgroundResource(R.drawable.joint_res_oper_up_bg);
		}

		// 设置弹出框的位置
		WindowManager.LayoutParams wmlp = getWindow().getAttributes();
		wmlp.gravity = Gravity.TOP | Gravity.RIGHT;
		wmlp.x = targetLocation[0]; // x position
		wmlp.y = targetLocation[1] - notiBarHeight; // y position

//		Log.d(TAG, location.toString());
//		Log.d(TAG, size.toString());
//		Log.d(TAG, screenSize.toString());
	}
	
	/**
	 * 设置操作点击事件
	 * @param listener
	 */
	public void setOnSelectListener(OnOperateSelectListener listener){
		mListener = listener;
	}

	@Override
	public void onClick(View v) {
		if(mListener == null){
			Toast.makeText(mContext, "没有设置监听函数", Toast.LENGTH_SHORT).show();
			return;
		}
		if(v == smsTv){ // 短信
			mListener.onOperateSelect(OperateType.SMS, mRes);
		}
		else if(v == telTv){ // 电话
			mListener.onOperateSelect(OperateType.TEL, mRes);
		}
		else if(v == forwardTv){ // 转发
			mListener.onOperateSelect(OperateType.FORWARD, mRes);
		}
		else if(v == shareTv){ // 分享
			mListener.onOperateSelect(OperateType.SHARE, mRes);
		}
		else if(v == inviteTv){ // 邀请
			mListener.onOperateSelect(OperateType.INVITE, mRes);
		}
		dismiss();
	}
	
	public interface OnOperateSelectListener{
		public void onOperateSelect(OperateType operType, ResourceBase resource);
	}
	
	// 操作类型
	public enum OperateType {
		SMS, TEL, FORWARD, SHARE, INVITE
	}
}
