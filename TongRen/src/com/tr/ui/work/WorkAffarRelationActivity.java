package com.tr.ui.work;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tr.R;
import com.tr.api.WorkReqUtil;
import com.tr.model.work.BUAffar;
import com.tr.model.work.BUAffarRelation;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.widgets.CircleImageView;
import com.utils.http.EAPIConsts.WorkReqType;
import com.utils.http.IBindData;
import com.utils.image.AnimateFirstDisplayListener;

/**
 * 事务详情----点击关联----关联
 * 
 * 启动方法：	WorkReqType.AFFAIR_RELATION_GET:
 * 
 * @param Type
 * 			关联类型 p:人员，o：组织，k：知识，r:事件"
 * @param jtContact
 * 			BUAffarRelation类型对象
 * 
 * @author Administrator
 *
 */
public class WorkAffarRelationActivity extends JBaseActivity implements
		IBindData {
	/**人脉总布局*/
	private LinearLayout LinearLayoutPerson;
	/**人脉关联头像。。等*/
	private LinearLayout LinearLayoutPersonAdd;
	/**组织总布局*/
	private LinearLayout LinearLayoutOrgine;
	/**组织关联头像。。等*/
	private LinearLayout LinearLayoutOrgineAdd;
	/**知识总布局*/
	private LinearLayout LinearLayoutKnow;
	/**知识条目的布局*/
	private LinearLayout LinearLayoutKnowAdd;
	/**事件总布局*/
	private LinearLayout LinearLayoutEvent;
	/**事件条目的布局*/
	private LinearLayout LinearLayoutEventAdd;
	/**事务详情传来的事务id*/
	private long mAffarId = 0;
	private BUAffar mAffar;//对象

	DisplayImageOptions options = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.ic_default_avatar)// 设置图片在加载中显示的图片
	.showImageForEmptyUri(R.drawable.ic_default_avatar)// 设置图片Uri为空或是错误的时候显示的图片
	.showImageOnFail(R.drawable.ic_default_avatar)// 设置图片加载/解码过程中错误时候显示的图片
	.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
	.cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
	.considerExifParams(false)// 保留Exif信息
	.build();
	/**图像任务加载时的监听*/
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.work_affar_relation_activity);
		//事务详情传来的事务id
		mAffarId = getIntent().getLongExtra("AffarId", 0);
		Log.d("xmx","mAffarId:"+mAffarId);
		if (mAffarId > 0) {
			initView();
			initData();
		}
	}

	public void initView() {

		LinearLayoutPerson = (LinearLayout) findViewById(R.id.LinearLayoutPerson);
		LinearLayoutPersonAdd = (LinearLayout) findViewById(R.id.LinearLayoutPersonAdd);
		LinearLayoutOrgine = (LinearLayout) findViewById(R.id.LinearLayoutOrgine);
		LinearLayoutOrgineAdd = (LinearLayout) findViewById(R.id.LinearLayoutOrgineAdd);
		LinearLayoutKnow = (LinearLayout) findViewById(R.id.LinearLayoutKnow);
		LinearLayoutKnowAdd = (LinearLayout) findViewById(R.id.LinearLayoutKnowAdd);
		LinearLayoutEvent = (LinearLayout) findViewById(R.id.LinearLayoutEvent);
		LinearLayoutEventAdd = (LinearLayout) findViewById(R.id.LinearLayoutEventAdd);

	}
	/**初始化数据*/
	public void initData() {
		mAffar = new BUAffar();
		this.showLoadingDialog("", false, null);
		WorkReqUtil.getAffarRelation(WorkAffarRelationActivity.this, this,
				mAffarId, null);
	}
	/**重置View布局*/
	public void resetView() {
		int i;
		View view = null;

		LayoutInflater flater = LayoutInflater.from(this);
		//关联对象的集合
		List<BUAffarRelation> vList;
		// 人员
		Log.d("xmx",
				"mAffar.getRelationCount(p):" + mAffar.getRelationCount("p"));
		if (mAffar.getRelationCount("p") > 0) {
			CircleImageView vImageViewPic = null;//关联的圆形头像
			TextView vTextViewName = null;//人脉名称
			TextView vTextViewLabel = null;//人脉关系

			vList = mAffar.getRelationList("p");//关联类型 p:人员，o：组织，k：知识，r:事件"
			Log.d("xmx","vList:"+vList.size());
			
			for (i = 0; i < vList.size(); i++) {
				BUAffarRelation vRelaItem = vList.get(i);
				int vIndex = (i % 5);//获取关联人脉的位置
				Log.d("xmx","index:"+vIndex);
				if (vIndex == 0) {//第一个
					view = flater.inflate(
							R.layout.work_affar_relation_person_cell, null);
					LinearLayoutPersonAdd.addView(view);
					Log.d("xmx","add view:"+view);
					Log.d("xmx","add view:"+LinearLayoutPersonAdd);
					
					vImageViewPic = (CircleImageView) view
							.findViewById(R.id.ImageViewPic1);
					vTextViewName = (TextView) view
							.findViewById(R.id.TextViewName1);
					vTextViewLabel = (TextView) view
							.findViewById(R.id.TextViewLabel1);
				}
				if (vIndex == 1) {//第二个（同上）
					vImageViewPic = (CircleImageView) view
							.findViewById(R.id.ImageViewPic2);
					vTextViewName = (TextView) view
							.findViewById(R.id.TextViewName2);
					vTextViewLabel = (TextView) view
							.findViewById(R.id.TextViewLabel2);
				}
				if (vIndex == 2) {//第三个（同上）
					vImageViewPic = (CircleImageView) view
							.findViewById(R.id.ImageViewPic3);
					vTextViewName = (TextView) view
							.findViewById(R.id.TextViewName3);
					vTextViewLabel = (TextView) view
							.findViewById(R.id.TextViewLabel3);
				}
				if (vIndex == 3) {//第四个（同上）
					vImageViewPic = (CircleImageView) view
							.findViewById(R.id.ImageViewPic4);
					vTextViewName = (TextView) view
							.findViewById(R.id.TextViewName4);
					vTextViewLabel = (TextView) view
							.findViewById(R.id.TextViewLabel4);
				}
				if (vIndex == 4) {//第5个   同上
					vImageViewPic = (CircleImageView) view
							.findViewById(R.id.ImageViewPic5);
					vTextViewName = (TextView) view
							.findViewById(R.id.TextViewName5);
					vTextViewLabel = (TextView) view
							.findViewById(R.id.TextViewLabel5);
				}
				//设置全部显示
				vImageViewPic.setVisibility(View.VISIBLE);
				vTextViewName.setVisibility(View.VISIBLE);
				vTextViewLabel.setVisibility(View.VISIBLE);
				//设置内容
				vTextViewLabel.setText(vRelaItem.label);
				vTextViewName.setText(vRelaItem.title);
				
				
				vImageViewPic.setTag(vRelaItem.getRelateId()+"");//图片设置一个Tag
				Log.d("xmx","getRelateId:"+vRelaItem.getRelateId());
				if (vRelaItem.userOrType!=null && vRelaItem.userOrType.equals("u"))//"u:用户，p:人脉。传入参数时，如果是知识，此字段保存知识类型"
					vImageViewPic.setOnClickListener(mPicPersonClick);
				else{
					vImageViewPic.setOnClickListener(mPicRenMaiClick);
				}
				ImageLoader.getInstance().displayImage(vRelaItem.picUrl, vImageViewPic, options, animateFirstListener);
//				ImageLoadUtils.initImageLoader(this).displayImage(vRelaItem.picUrl, vImageViewPic, options, animateFirstListener);
//				com.tr.image.ImageLoader.load(vImageViewPic, vRelaItem.picUrl,R.drawable.ic_default_avatar);
			}
			LinearLayoutPerson.setVisibility(View.VISIBLE);
		} else
		{
			LinearLayoutPerson.setVisibility(View.GONE);
		}

		view = null;
		// 组织
		if (mAffar.getRelationCount("o") > 0) {//关联类型 p:人员，o：组织，k：知识，r:事件"(同上)
			CircleImageView vImageViewPic = null;
			TextView vTextViewName = null;
			TextView vTextViewLabel = null;

			vList = mAffar.getRelationList("o");
			for (i = 0; i < vList.size(); i++) {
				BUAffarRelation vRelaItem = vList.get(i);
				int vIndex = (i % 5);
				if (vIndex == 0) {
					view = flater.inflate(
							R.layout.work_affar_relation_person_cell, null);
					LinearLayoutOrgineAdd.addView(view);

					vImageViewPic = (CircleImageView) view
							.findViewById(R.id.ImageViewPic1);
					vTextViewName = (TextView) view
							.findViewById(R.id.TextViewName1);
					vTextViewLabel = (TextView) view
							.findViewById(R.id.TextViewLabel1);
				}
				if (vIndex == 1) {
					vImageViewPic = (CircleImageView) view
							.findViewById(R.id.ImageViewPic2);
					vTextViewName = (TextView) view
							.findViewById(R.id.TextViewName2);
					vTextViewLabel = (TextView) view
							.findViewById(R.id.TextViewLabel2);
				}
				if (vIndex == 2) {
					vImageViewPic = (CircleImageView) view
							.findViewById(R.id.ImageViewPic3);
					vTextViewName = (TextView) view
							.findViewById(R.id.TextViewName3);
					vTextViewLabel = (TextView) view
							.findViewById(R.id.TextViewLabel3);
				}
				if (vIndex == 3) {
					vImageViewPic = (CircleImageView) view
							.findViewById(R.id.ImageViewPic4);
					vTextViewName = (TextView) view
							.findViewById(R.id.TextViewName4);
					vTextViewLabel = (TextView) view
							.findViewById(R.id.TextViewLabel4);
				}
				if (vIndex == 4) {
					vImageViewPic = (CircleImageView) view
							.findViewById(R.id.ImageViewPic5);
					vTextViewName = (TextView) view
							.findViewById(R.id.TextViewName5);
					vTextViewLabel = (TextView) view
							.findViewById(R.id.TextViewLabel5);
				}
				vImageViewPic.setVisibility(View.VISIBLE);
				vTextViewName.setVisibility(View.VISIBLE);
				vTextViewLabel.setVisibility(View.VISIBLE);

				vTextViewLabel.setText(vRelaItem.label);
				vTextViewName.setText(vRelaItem.title);
				
				
				vImageViewPic.setTag(vRelaItem.getRelateId()+"");
				if (vRelaItem.userOrType.equals("o")) //组织
					vImageViewPic.setOnClickListener(mPicOrgOrgClick);
				else{
					vImageViewPic.setOnClickListener(mPicOrgKehuClick);
				}
				
				ImageLoader.getInstance().displayImage(vRelaItem.picUrl, vImageViewPic, options, animateFirstListener);
//				ImageLoadUtils.initImageLoader(this).displayImage(vRelaItem.picUrl, vImageViewPic, options, animateFirstListener);
//				com.tr.image.ImageLoader.load(vImageViewPicO, vRelaItem.picUrl,R.drawable.ic_default_avatar);
			}
			LinearLayoutOrgine.setVisibility(View.VISIBLE);
		} else
			LinearLayoutOrgine.setVisibility(View.GONE);

		
		view = null;
		// 知识
		if (mAffar.getRelationCount("k") > 0) {
			TextView vTextViewName = null;
			TextView vTextViewLabel = null;
			TextView vTextViewDate = null;

			vList = mAffar.getRelationList("k");
			for (i = 0; i < vList.size(); i++) {
				
				BUAffarRelation vRelaItem = vList.get(i);

				view = flater.inflate(
						R.layout.work_affar_relation_know_cell, null);
				LinearLayoutKnowAdd.addView(view);

				vTextViewName = (TextView) view
						.findViewById(R.id.TextViewName);
				vTextViewLabel = (TextView) view
						.findViewById(R.id.TextViewLabel);
				vTextViewDate = (TextView) view
						.findViewById(R.id.TextViewDate);

				vTextViewLabel.setText(vRelaItem.label);
				vTextViewName.setText(vRelaItem.title);
				vTextViewDate.setText(changeDate(vRelaItem.relateTime));
				
				view.setTag(vRelaItem);
				view.setOnClickListener(mPicKonwClick);
				
			}
			LinearLayoutKnow.setVisibility(View.VISIBLE);
		} else
			LinearLayoutKnow.setVisibility(View.GONE);

		
		view = null;
		// 事件
		if (mAffar.getRelationCount("r") > 0) {
			TextView vTextViewName = null;
			TextView vTextViewLabel = null;
			TextView vTextViewDate = null;

			vList = mAffar.getRelationList("r");
			for (i = 0; i < vList.size(); i++) {
				
				BUAffarRelation vRelaItem = vList.get(i);

				view = flater.inflate(
						R.layout.work_affar_relation_know_cell, null);
				LinearLayoutEventAdd.addView(view);

				vTextViewName = (TextView) view
						.findViewById(R.id.TextViewName);
				vTextViewLabel = (TextView) view
						.findViewById(R.id.TextViewLabel);
				vTextViewDate = (TextView) view
						.findViewById(R.id.TextViewDate);

				vTextViewLabel.setText(vRelaItem.label);
				vTextViewName.setText(vRelaItem.title);
				vTextViewDate.setText(changeDate(vRelaItem.relateTime));
				
				view.setTag(vRelaItem);
				view.setOnClickListener(mPicEventClick);
				
				
			}
			LinearLayoutEvent.setVisibility(View.VISIBLE);
		} else
			LinearLayoutEvent.setVisibility(View.GONE);

		
	}

	//跳转人脉主页的点击--关联人脉
	private OnClickListener mPicPersonClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String vId=v.getTag().toString();
			Log.d("xmx","mPicPersonClick:"+vId);
			ENavigate.startRelationHomeActivity(WorkAffarRelationActivity.this, vId);
		}
	};
	//跳转人脉详情的点击--关联人脉
	private OnClickListener mPicRenMaiClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			try {
				String vId=v.getTag().toString();
				Long vLongId=Long.parseLong(vId);
				Log.d("xmx","mPicRenMaiClick");
				ENavigate.startRelationHomeActivity(WorkAffarRelationActivity.this,vId,false,ENavConsts.TYPE_CONNECTIONS_HOME_PAGE);//跳转到新版人脉主页
//				ENavigate.startContactsDetailsActivity(WorkAffarRelationActivity.this,2, vLongId,0,1);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	};
	
	//跳转他人主页-我的主页的点击--客户
	private OnClickListener mPicOrgOrgClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String vId=v.getTag().toString();
			Long vLongId=Long.parseLong(vId);
			Log.d("xmx","mPicOrgOrgClick");
			ENavigate.startOrgMyHomePageActivityByUseId(WorkAffarRelationActivity.this, vLongId);
		}
	};
	//跳转客户详情--客户
	private OnClickListener mPicOrgKehuClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String vId=v.getTag().toString();
			Long vLongId=Long.parseLong(vId);
			Log.d("xmx","mPicOrgKehuClick");
			ENavigate.startClientDedailsActivity(WorkAffarRelationActivity.this, vLongId, 1,6);
		}
	};
	//跳转知识详情的点击
	private OnClickListener mPicKonwClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			BUAffarRelation vRelation=(BUAffarRelation)v.getTag();
			Log.d("xmx","mPicKonwClick");
			int vType= Integer.parseInt(vRelation.userOrType);
			ENavigate.startKnowledgeOfDetailActivity(WorkAffarRelationActivity.this, vRelation.relateId,vType);
		}
	};
	//跳转需求详情的点击
	private OnClickListener mPicEventClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			BUAffarRelation vRelation=(BUAffarRelation)v.getTag();
			Log.d("xmx","mPicKonwClick");
			ENavigate.startNeedDetailsActivity(WorkAffarRelationActivity.this, vRelation.relateId+"",2);
		}
	};
	//标题居中
	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar(), "关联", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}
	

	@Override
	public void bindData(int tag, Object object) {
		// TODO Auto-generated method stub
		dismissLoadingDialog();
		Log.d("xmx", "bindData:" + tag);
		if (object != null) {
			switch (tag) {
			case WorkReqType.AFFAIR_RELATION_GET: {
				// 事务log
				Log.d("xmx", "WorkReqType.AFFAIR_RELATION_GET");
				mAffar.relations = (List<BUAffarRelation>) object;
				resetView();
			}
			}
		}
	}
	//格式化时间
	public String changeDate(String inDate)
	{
		String vDate="";
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date vinDate = df.parse(inDate);
			vDate=df1.format(vinDate);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return vDate;
	}
}
