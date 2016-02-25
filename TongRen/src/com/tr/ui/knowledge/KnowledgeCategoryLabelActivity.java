package com.tr.ui.knowledge;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.api.KnowledgeReqUtil;
import com.tr.api.PeopleReqUtil;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.obj.Connections;
import com.tr.model.obj.JTFile;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.home.frg.FrgConnections2.ContactAdapter;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.knowledge.MyKnowledgeActivity.KnowledgeLvAdapter.ViewHolder;
import com.tr.ui.people.model.MyContacts;
import com.tr.ui.people.model.PersonSimple;
import com.tr.ui.people.model.PersonSimpleList;
import com.tr.ui.widgets.ConnsListDelDialog;
import com.tr.ui.widgets.ConnsListDelDialog.OnSelectListener;
import com.tr.ui.widgets.KnoCategoryAlertDialog.OperType;
import com.tr.ui.widgets.SideBar;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.EAPIConsts.KnoReqType;
import com.utils.http.EAPIConsts.PeopleRequestType;
import com.utils.http.IBindData;
import com.utils.image.LoadImage;
import com.utils.time.TimeUtil;

public class KnowledgeCategoryLabelActivity extends JBaseActivity implements
		OnItemClickListener, IBindData {

	private XListView lvContact;// 内容list
	private RelativeLayout loadConnectionsWaitView;
	// private TextView mDialogText;// 中间提示的text

	// private SwipeRefreshLayout swipeLayout;
	/** 联系人操作对话框 */
	private ConnsListDelDialog connsListDelDialog;
	// ******************* 控件 end ******************* /

	private long cid;
//	private String categoryName;
	private long tid;

	private PersonSimpleList personSimpleList;
	
	private ActionBar actionBar;
	private KnowledgeLvAdapter adapter;
	private Integer total;
	private Integer index;
	private Integer size;
	private String knoTagName;
	private String knoCategoryName;
	private int deleteSourceType = 0;// 删除知识的类型：来源,1-我收藏的,2-分享给我的,其他默认0
	private static final int TYPE_SHARE_TO_ME = 2; // 分享给我
	private KnowledgeMini2 DeleteKnowledge;
	private ArrayList<KnowledgeMini2> knowledgeList =new ArrayList<KnowledgeMini2>();;
	private ArrayList<KnowledgeMini2> knowledgeCheckedList = new ArrayList<KnowledgeMini2>();
	
	public static final int REQUESTCODE_EDIT_KNOWLEDGE_TAG_ACTIVITY = 1003;
	
	@Override
	public void initJabActionBar() {
		actionBar = jabGetActionBar();
		
//		actionBar.setTitle("人脉");
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_frg_connections_list3);
		Intent intent = getIntent();
		if (intent != null) {
			cid = intent.getLongExtra(EConsts.Key.CATEGORY_KEY, 0);
			knoTagName = intent.getStringExtra(EConsts.Key.LABEL_NAME);
			knoCategoryName = intent.getStringExtra(EConsts.Key.CATEGORY_NAME);
			if(!TextUtils.isEmpty(knoCategoryName)){
				actionBar.setTitle(knoCategoryName);
				HomeCommonUtils.initLeftCustomActionBar(this, actionBar, knoCategoryName, false, null, true, true);
			}else if(!TextUtils.isEmpty(knoTagName)){
				actionBar.setTitle(knoTagName);
				HomeCommonUtils.initLeftCustomActionBar(this, actionBar, knoTagName, false, null, true, true);
			}
			tid = intent.getLongExtra(EConsts.Key.LABEL_KEY, 0);
		}

		findView();
		
		
		requestJson(cid, tid);
	}

	private void requestJson(long cid, long tid) {
		showLoadingDialog();
		if (cid!=0) {
			KnowledgeReqUtil.doGetKnowledgeByUserCategoryAndKeyword(this, this, App.getUserID(), cid, "", 0, 9999, null);

		}else if(!TextUtils.isEmpty(knoTagName)){
			KnowledgeReqUtil.doGetKnowledgeByTagAndKeyword(this, this, App.getUserID(), knoTagName, "", 0, 9999, null);

		}

	}

	private void findView() {
		loadConnectionsWaitView = (RelativeLayout) 
				findViewById(R.id.waitview);
		lvContact = (XListView) findViewById(R.id.lvContact);
		setXlistViewConfig(); //add by wenxiaohua 不需要下拉刷新

		connsListDelDialog = new ConnsListDelDialog(KnowledgeCategoryLabelActivity.this);
		connsListDelDialog.setOnSelectListener(new OnSelectListener() {

			@Override
			public void onSelect(OperType operType,
					Connections mAttachConnections) {

			}
		});

		adapter = new KnowledgeLvAdapter(this);
		lvContact.setAdapter(adapter);
		lvContact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Adapter myAdapter =  parent.getAdapter();
				if (myAdapter!=null) {
					KnowledgeMini2 connections = (KnowledgeMini2) myAdapter.getItem(position);
					ENavigate.startKnowledgeOfDetailActivity(KnowledgeCategoryLabelActivity.this, connections.id, connections.type);
				}
				
			}
		});
	}

	/** 设置XListView的参数 */
	private void setXlistViewConfig() {
		lvContact.showFooterView(false);
		// 设置xlistview可以加载、刷新
		lvContact.setPullRefreshEnable(false);
		lvContact.setPullLoadEnable(false);

		
	}
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	super.onActivityResult(requestCode, resultCode, data);
		if (REQUESTCODE_EDIT_KNOWLEDGE_TAG_ACTIVITY == requestCode) {
			// 编辑标签完成，刷新界面
			requestJson(cid, tid);
		}
}
	class KnowledgeLvAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<KnowledgeMini2> knowledgeList = new ArrayList<KnowledgeMini2>();

		private boolean editMode;
		private int tagLlWidth;

		public KnowledgeLvAdapter(Context context) {
			super();
			this.context = context;
		}

		public void setEditMode(boolean editMode) {
			this.editMode = editMode;
		}

		public void setKnowledgeList(ArrayList<KnowledgeMini2> knowledgeList) {
			if (knowledgeList != null) {
				this.knowledgeList = knowledgeList;
			}
		}

		@Override
		public int getCount() {
			return knowledgeList.size();
		}

		@Override
		public KnowledgeMini2 getItem(int position) {
			return knowledgeList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.item_my_knowledge_lv, null);
				viewHolder.cb = (CheckBox) convertView.findViewById(R.id.cb);
				viewHolder.cb.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						KnowledgeMini2 knowledgeMini2 = (KnowledgeMini2) v.getTag();
						// long id = knowledgeMini2.getId();
						notifyDataSetChanged();
					}
				});
				viewHolder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						KnowledgeMini2 knowledgeMini2 = (KnowledgeMini2) buttonView.getTag();
					}
				});
				

				viewHolder.titleTv = (TextView) convertView.findViewById(R.id.titleTv);
//				viewHolder.numTv = (TextView) convertView.findViewById(R.id.numTv);
				viewHolder.tagTv = (TextView) convertView.findViewById(R.id.tagTv);
				viewHolder.timeTv = (TextView) convertView.findViewById(R.id.timeTv);
				viewHolder.knowledgeIv = (ImageView) convertView.findViewById(R.id.knowledgeIv);
				viewHolder.moreIv = (ImageView) convertView.findViewById(R.id.moreIv);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			final KnowledgeMini2 knowledge = getItem(position);
			viewHolder.moreIv.setOnClickListener(new OnClickListener() {
				
				private PopupWindow popupWindow;

				@Override
				public void onClick(View v) {
					View view = View.inflate(context, R.layout.moreknowledgepop, null);
					popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
					popupWindow.setAnimationStyle(R.style.PupwindowAnimation);
					popupWindow.setBackgroundDrawable(new BitmapDrawable());
					popupWindow.setOutsideTouchable(true);
					popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
					LinearLayout fenxiangLl = (LinearLayout) view.findViewById(R.id.fenxiangLl);
					LinearLayout biaoqianLl = (LinearLayout) view.findViewById(R.id.biaoqianLl);
					LinearLayout shanchuLl = (LinearLayout) view.findViewById(R.id.shanchuLl);
					TextView cancel = (TextView)view.findViewById(R.id.cancel);
					cancel.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							popupWindow.dismiss();
							
						}
					});
					fenxiangLl.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							popupWindow.dismiss();

							String mSuffixName = knowledge.toJTFile().getmSuffixName();
							knowledge.setDesc(filterHtml(mSuffixName));
							JTFile jtFile = knowledge.toJTFile();
							jtFile.messageID = EUtil.genMessageID();
							// jtFile.mFileName
							// ="分享了[知识] ";//+knowledge.getTitle();
							jtFile.mFileName = knowledge.getTitle();
							jtFile.virtual = knowledge.getType() + "";
							// ENavigate.startShareActivity(MyKnowledgeActivity.this,
							// JTFile.TYPE_KNOWLEDGE2, listJtFile);
							ENavigate.startSocialShareActivity(KnowledgeCategoryLabelActivity.this, jtFile);
							// }
							
						}
					});
					biaoqianLl.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							popupWindow.dismiss();
							ArrayList<Long> listKnowledgeId=  new ArrayList<Long>();
							if (deleteSourceType == TYPE_SHARE_TO_ME) {
								listKnowledgeId.add(knowledge.shareMeId);
							} else {
								listKnowledgeId.add(knowledge.id);
							}
							ArrayList<Integer> listKnowledgeType = new ArrayList<Integer>();
							listKnowledgeType.add(knowledge.type);
							if (knowledgeCheckedList != null)
								knowledgeCheckedList.clear();
							knowledgeCheckedList.add(knowledge);
							// ENavigate.startEditKnowledgeTagActivityForResult(MyKnowledgeActivity.this,REQUESTCODE_EDIT_KNOWLEDGE_TAG_ACTIVITY,listKnowledgeId, listKnowledgeType);
							ENavigate.startEditKnowledgeTagActivityForResult(KnowledgeCategoryLabelActivity.this, REQUESTCODE_EDIT_KNOWLEDGE_TAG_ACTIVITY, knowledgeCheckedList);
						}
					});
					shanchuLl.setOnClickListener(new OnClickListener() {
	
						@Override
						public void onClick(View v) {
							popupWindow.dismiss();
								Builder builder = new AlertDialog.Builder(context);
								builder.setTitle("提示：");
								builder.setMessage("确定要删除知识吗？");
								builder.setNegativeButton("取消", null);
								builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
									

									@Override
									public void onClick(DialogInterface dialog, int which) {
										showLoadingDialog();
										// swipeLayout.setRefreshing(true);
										ArrayList<Long> listKnowledgeId=  new ArrayList<Long>();
										if (deleteSourceType == TYPE_SHARE_TO_ME) {
											listKnowledgeId.add(knowledge.shareMeId);
										} else {
											listKnowledgeId.add(knowledge.id);
										}
										ArrayList<Integer> listKnowledgeType = new ArrayList<Integer>();
										listKnowledgeType.add(knowledge.type);
										DeleteKnowledge = knowledge;
										KnowledgeReqUtil.doDeleteKnowledgeById(context, KnowledgeCategoryLabelActivity.this, App.getUserID(), listKnowledgeId, listKnowledgeType,deleteSourceType, null);
									}
								});
								builder.show();
						}
					});
				}
			});
			
			if (knowledge.tag.equals("")) {
				viewHolder.tagTv.setVisibility(View.GONE);
			}else{
				viewHolder.tagTv.setVisibility(View.VISIBLE);
				viewHolder.tagTv.setText(knowledge.tag);
			}

//			ArrayList<String> lsitTag = knowledge.listTag;// 标签集合
//			if (null != lsitTag && !lsitTag.isEmpty() /*&& type != TYPE_ALL*/) {// 在我创建的下不显示标签:::
//				viewHolder.tagTv.setVisibility(View.VISIBLE);
//				viewHolder.tagTv.setText(lsitTag.toString());
//			} else {
//				viewHolder.tagTv.setVisibility(View.GONE);
//				viewHolder.cb.setGravity(Gravity.CENTER_VERTICAL);
//			}

			if (editMode) {
				viewHolder.cb.setVisibility(View.VISIBLE);
//				viewHolder.numTv.setVisibility(View.GONE);
			} else {
				viewHolder.cb.setVisibility(View.GONE);
//				viewHolder.numTv.setVisibility(View.VISIBLE);
//				viewHolder.numTv.setText(position + 1 + "");
			}

			viewHolder.cb.setTag(knowledge);
			viewHolder.titleTv.setText(knowledge.title);
			
			viewHolder.timeTv.setText(TextUtils.isEmpty(knowledge.modifytime)?"":TimeUtil.TimeFormat(knowledge.modifytime));
			if(TextUtils.isEmpty(knowledge.pic)){
				viewHolder.knowledgeIv.setVisibility(View.GONE);
			}else{
//				viewHolder.knowledgeIv.setVisibility(View.VISIBLE);
				viewHolder.knowledgeIv.setVisibility(View.GONE);
				ImageLoader.getInstance().displayImage( knowledge.pic, viewHolder.knowledgeIv, LoadImage.mHomeDefaultHead);
			}

			// TagGvAdper tagGvAdaper = new TagGvAdper(context);
			// viewHolder.tagLl.setAdapter(tagGvAdaper);
			// tagGvAdaper.setTagList(lsitTag);
			// tagGvAdaper.notifyDataSetChanged();

			return convertView;
		}

		class ViewHolder {
			public TextView tagTv;
			public ImageView moreIv;
			CheckBox cb;
			TextView numTv;
			TextView titleTv;
			TextView timeTv;
			ImageView knowledgeIv;
		}

	}
	/**
	 * @param html
	 * @return
	 */
	public String filterHtml(String html) {
		Pattern pattern = Pattern.compile("<style[^>]*?>[\\D\\d]*?<\\/style>");
		Matcher matcher = pattern.matcher(html);
		String htmlStr = matcher.replaceAll("");
		pattern = Pattern.compile("<[^>]+>");
		String filterStr = pattern.matcher(htmlStr).replaceAll("");
		filterStr.replace("&nbsp;", " ");
		return filterStr;
	}
	@Override
	public void bindData(int tag, Object object) {
		if (KnoReqType.GetKnowledgeByTypeAndKeyword == tag || KnoReqType.GetKnowledgeByTagAndKeyword == tag || KnoReqType.GetKnowledgeByUserCategoryAndKeyword == tag) {
			Map<String, Object> hm = (Map<String, Object>) object;
			ArrayList<KnowledgeMini2> newKnowledgeList = (ArrayList<KnowledgeMini2>) hm.get("listKnowledgeMini");
			if (newKnowledgeList != null) {

				total = (Integer) hm.get("total");
				index = (Integer) hm.get("index");
				size = (Integer) hm.get("size");
				if (knowledgeList != null) {
					knowledgeList.clear(); // 如果当前刷新中，则清除之前数据
				}
				knowledgeList.addAll(newKnowledgeList);
				adapter.setKnowledgeList(knowledgeList);
				adapter.notifyDataSetChanged();
				// reSetActionBarTitle();
			}
		}// 根据id删除知识
		else if (KnoReqType.DeleteKnowledgeById == tag) {

			Map<String, Object> hm = (Map<String, Object>) object;
			boolean b = (Boolean) hm.get("success");
			if (b) {
				// TODO:
				if (DeleteKnowledge!=null) {
					knowledgeList.remove(DeleteKnowledge);
				}
				adapter.notifyDataSetChanged();
				Toast.makeText(KnowledgeCategoryLabelActivity.this, "删除成功", 0).show();
			} else {
				Toast.makeText(KnowledgeCategoryLabelActivity.this, "删除失败", 0).show();
			}
		}
		dismissLoadingDialog();
	}

	

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
}
