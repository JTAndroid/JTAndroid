package com.common.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.constvalue.EnumConst.ModuleType;
import com.tr.App;
import com.tr.R;
import com.tr.api.KnowledgeReqUtil;
import com.tr.image.ImageLoader;
import com.tr.model.demand.NeedItemData;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.knowledge.UserCategory;
import com.tr.model.obj.Connections;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.MobilePhone;
import com.tr.navigate.ENavigate;
import com.tr.ui.demand.util.ChooseDataUtil;
import com.tr.ui.organization.adapter.ListViewAdapter;
import com.tr.ui.organization.model.Contacts;
import com.tr.ui.widgets.CircleImageView;
import com.tr.ui.widgets.ConnsCallAndSendSmsDialog;
import com.utils.common.EUtil;
import com.utils.common.Util;
import com.utils.display.DisplayUtil;
import com.utils.http.EAPIConsts.KnoReqType;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;
import com.utils.time.TimeUtil;
/***
 * 这个类是一个四大组件<人脉/好友、组织、需求、知识>相关的目录展示、管理
 * 目录adapter：
 *mContext上下文；
 *mListKnowledgeMini2s知识集合；
 *itemDatas需求集合；
 *connections人脉集合；
 *contacts组织集合；
 *mListCategory目录集合;
 *maxCategory点击条目数;
 */
public class CategoryAdapter extends BaseAdapter implements OnClickListener,IBindData {

	private Activity mContext;
	private ArrayList<KnowledgeMini2> mListKnowledgeMini2s = new ArrayList<KnowledgeMini2>();
	private ArrayList<NeedItemData> itemDatas = new ArrayList<NeedItemData>();
	private ArrayList<Connections> connections = new ArrayList<Connections>();
	private ArrayList<Contacts> contacts = new ArrayList<Contacts>();
	private ArrayList<UserCategory> mListCategory = new ArrayList<UserCategory>();
	private ArrayList<UserCategory> mListCategorys1 = new ArrayList<UserCategory>();
	private ArrayList<UserCategory> mListCategorys2 = new ArrayList<UserCategory>();
	private int maxCategory; //点击条目数
	private final int SELECT_ITEM_COLOR = 0XFFFFA500; // 选中项目颜色
	private final int NORMAL_TEXT_COLOR = 0XFF000000; // 默认文字颜色

	public  ModuleType fileType = ModuleType.CATEGORY;
	private ModuleType categoryType;
	private String mKeyword;
	private boolean isCategorySelect;
	private boolean editMode;
	private int mWidth;
	private LayoutParams lp;
	private KnowledgeMini2 DeleteKnowledge;
/**
 * 
 * @param context
 * @param categoryType类型
 * @param mKeyword搜索的关键字
 * @param isCategorySelect目录是否选中
 */
	public CategoryAdapter(Activity context,ModuleType categoryType,String mKeyword,boolean isCategorySelect) {
		mContext = context;
		this.categoryType =categoryType;
		this.mKeyword = mKeyword;
		this.isCategorySelect = isCategorySelect;
		DisplayMetrics metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);
		mWidth = metric.widthPixels;
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		int px = Util.DensityUtil.dip2px(context, 5);
		lp.setMargins(px, 0, px, 0);
	}
/**
 * 
 * @return返回目录类型
 */
	public ModuleType getCategoryType() {
		return categoryType;
	}
/**
 * 
 * @param categoryType 设置目录类型
 */
	public void setCategoryType(ModuleType categoryType) {
		this.categoryType = categoryType;
	}
/**
 * 是否是编辑状态 true为是
 * @return
 */
	public boolean isEditMode() {
		return editMode;
	}
/**
 * 设置编辑状态 true为编辑状态
 * @param editMode
 */
	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	@Override
	public int getCount() {
		return mListCategory.size() + mListKnowledgeMini2s.size()+ itemDatas.size()+ connections.size()+ contacts.size();
	}

	/**
	 * 
	 * @return 获取隐藏的项目数
	 */
	private int getHiddenCount() {
		int count = 0;
		for (UserCategory category : mListCategory) {
			if (!category.isVisiable()) {
				count++;
			}
		}
		return count;
	}

	// 获取显示项目的位置
	private int getRealPosition(int position) {
		try {
			int hElements = getHiddenCountUpTo(position);
			int diff = 0;
			for (int i = 0; i < hElements; i++) {
				diff++;
				if (!mListCategory.get(position + diff).isVisiable()) {
					i--;
				}
			}
			maxCategory = position;
			return position + diff;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;

	}
	@Override
	public int getViewTypeCount() {
		return 5;
	}
	@Override
	public int getItemViewType(int position) {
		Object object = getItem(position);
		ModuleType type = null;
		if (object!=null) {
			type  = doDetectionDataType(object);
			switch (type) {
			case CATEGORY:
				return 0;
			case KNOWLEDGE:
				return 1;
			case PEOPLE:
				return 2;
			case ORG:
				return 3;
			case DEMAND:
				return 4;
			}
		}

		return 0;
	}
	/**
	 * 检测数据类
	 * 
	 * @param object
	 * @return
	 */
	private ModuleType doDetectionDataType(Object object) {
		if (object instanceof KnowledgeMini2) {
			fileType = ModuleType.KNOWLEDGE;
		}else if (object instanceof NeedItemData) {
			fileType = ModuleType.DEMAND;
		}else if (object instanceof Connections) {
			fileType = ModuleType.PEOPLE;
		}else if (object instanceof Contacts) {
			fileType = ModuleType.ORG;
		} else if (object instanceof UserCategory) {
			fileType = ModuleType.CATEGORY;
		}
		return fileType;
	}
	private int getHiddenCountUpTo(int location) {
		int count = 0;
		for (int i = 0; i <= location; i++) {
			if (!mListCategory.get(i).isVisiable()) {
				count++;
			}
		}
		return count;
	}
/**
 * 获取搜索的关键字
 * @return
 */
	public String getmKeyword() {
		return mKeyword;
	}
/**
 * 设置搜索的关键字
 * @param mKeyword
 */
	public void setmKeyword(String mKeyword) {
		this.mKeyword = mKeyword;
		if (!TextUtils.isEmpty(mKeyword)) {
			mListCategorys2.clear();
			for (int i = 0; i < mListCategorys1.size(); i++) {
				if (mListCategorys1.get(i).getCategoryname().contains(mKeyword)) {
					mListCategorys2.add(mListCategorys1.get(i));
				}
			}
			setmListCategory(mListCategorys2,2);
		}else{
			setmListCategory(mListCategorys1,1);
		}
	}
/**
 * 
 * @return返回知识集合列表
 */
	public ArrayList<KnowledgeMini2> getmListKnowledgeMini2s() {
		return mListKnowledgeMini2s;
	}
/**
 * 设置知识集合列表
 * @param mListKnowledgeMini2s知识集合列表
 */
	public void setmListKnowledgeMini2s(
			ArrayList<KnowledgeMini2> mListKnowledgeMini2s) {
		this.mListKnowledgeMini2s = (ArrayList<KnowledgeMini2>) mListKnowledgeMini2s;
	}
/**
 * 
 * @return返回需求集合列表
 */
	public ArrayList<NeedItemData> getItemDatas() {
		return itemDatas;
	}
/**
 * 设置需求集合列表
 * @param itemDatas需求集合列表
 */
	public void setItemDatas(ArrayList<NeedItemData> itemDatas) {
		this.itemDatas = (ArrayList<NeedItemData>) itemDatas;
	}
/**
 * 
 * @return返回人脉集合列表
 */
	public ArrayList<Connections> getConnections() {
		return connections;
	}
/**
 * 设置人脉集合列表
 * @param connections 人脉集合列表
 */
	public void setConnections(ArrayList<Connections> connections) {
		this.connections = (ArrayList<Connections>) connections;
	}
/**
 * 
 * @return返回组织集合列表
 */
	public ArrayList<Contacts> getContacts() {
		return contacts;
	}
/**
 * 设置组织集合列表
 * @param contacts组织集合列表
 */
	public void setContacts(ArrayList<Contacts> contacts) {
		this.contacts = (ArrayList<Contacts>) contacts;
	}
/**
 * 
 * @return返回目录集合列表
 */
	public ArrayList<UserCategory> getmListCategory() {
		return mListCategory;
	}
/**
 * 设置目录集合列表
 * @param mListCategory目录集合列表
 */
	public void setmListCategory(ArrayList<UserCategory> mListCategory, int type) {
		if (type == 1) {
			this.mListCategorys1 = (ArrayList<UserCategory>) mListCategory;
			this.mListCategory = this.mListCategorys1;
		}else{
			this.mListCategorys2 = (ArrayList<UserCategory>) mListCategory;
			this.mListCategory = this.mListCategorys2;
		}
	}

	@Override
	public Object getItem(int position) {
		switch (categoryType) {
		case KNOWLEDGE:

			if (getmListKnowledgeMini2s().isEmpty()&&getmListCategory().isEmpty()) {
				return null;
			}
			if (position<getmListCategory().size()&&!getmListCategory().isEmpty()) {

				if(getmListCategory().size()==1){
					return getmListCategory().get(0);
				}else{
					return getmListCategory().get(position);
				}
			}else{
				return getmListKnowledgeMini2s().get(position-getmListCategory().size());
			}
		
		case DEMAND:

			if (getItemDatas().isEmpty()&&getmListCategory().isEmpty()) {
				return null;
			}
			if (position<getmListCategory().size()&&!getmListCategory().isEmpty()) {
				if(getmListCategory().size()==1){
					return getmListCategory().get(0);
				}else{
					return getmListCategory().get(position);
				}

			}else{
				return getItemDatas().get(position-getmListCategory().size());
			}
		case PEOPLE:

			if (getConnections().isEmpty()&&getmListCategory().isEmpty()) {
				return null;
			}
			if (position<getmListCategory().size()&&!getmListCategory().isEmpty()) {
				if(getmListCategory().size()==1){
					return getmListCategory().get(0);
				}else{
					return getmListCategory().get(position);
				}

			}else{
				return getConnections().get(position-getmListCategory().size());
			}
		case ORG:

			if (getContacts().isEmpty()&&getmListCategory().isEmpty()) {
				return null;
			}
			if (position<getmListCategory().size()&&!getmListCategory().isEmpty()) {
				if(getmListCategory().size()==1){
					return getmListCategory().get(0);
				}else{
					return getmListCategory().get(position);
				}

			}else{
				return getContacts().get(position-getmListCategory().size());
			}
		
		default:

			if (getmListCategory().isEmpty()) {
				return null;
			}
			return getmListCategory().get(position);
		
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		int itemViewType = getItemViewType(position);
		if (getItem(position)!=null) {

			if (getmListCategory()!=null&&!getmListCategory().isEmpty()&&getItem(position) instanceof UserCategory) { //目录
				CategoryViewHolder holder;
				UserCategory category = (UserCategory) getItem(position);
				if (convertView == null) {
					convertView = LayoutInflater.from(mContext).inflate(
							R.layout.kno_list_item_category, parent, false);
					holder = new CategoryViewHolder();
					holder.foldIv = (ImageView) convertView
							.findViewById(R.id.foldIv); // 展开或合起
					holder.titleTv = (TextView) convertView
							.findViewById(R.id.titleTv); // 目录标题
					holder.selectCb = (CheckBox) convertView
							.findViewById(R.id.selectCb);
					holder.selectCb2 = (CheckBox) convertView
							.findViewById(R.id.selectCb2);
					convertView.setTag(holder);
				} else {
					holder = (CategoryViewHolder) convertView.getTag();
				}

				// 是否收起
				// holder.foldIv = (ImageView)
				// convertView.findViewById(R.id.foldIv); // 展开或合起
				if (category.getListUserCategory() == null
						|| category.getListUserCategory().size() == 0) {
					holder.foldIv.setVisibility(View.VISIBLE);
					holder.foldIv.setOnClickListener(null);
				} else {
					if (category.isFolded()) { // 合起
						holder.foldIv
						.setImageResource(R.drawable.kno_category_unfold);
					} else { // 展开
						holder.foldIv
						.setImageResource(R.drawable.kno_category_unfold);
					}
					holder.foldIv.setVisibility(View.VISIBLE); // 展开
					holder.foldIv.setOnClickListener(null);
				}
//				holder.foldIv.setPadding(
//						EUtil.convertDpToPx(category.getLevel() * 15), 0, 0, 0);
				holder.foldIv.setPadding(EUtil.convertDpToPx(15), 0, 0, 0);
				holder.foldIv.setTag(position); // 目标目录位置
				// 是否高亮
				holder.titleTv = (TextView) convertView.findViewById(R.id.titleTv);
				if (!TextUtils.isEmpty(getmKeyword())) { // 关键字不为空
					if (category.getCategoryname().contains(getmKeyword())) { // 包含关键字
						holder.titleTv.setTextColor(NORMAL_TEXT_COLOR);
						SpannableString spannableString = new SpannableString(
								category.getCategoryname());
						spannableString.setSpan(new ForegroundColorSpan(
								SELECT_ITEM_COLOR), category.getCategoryname()
								.indexOf(getmKeyword()), category.getCategoryname()
								.indexOf(getmKeyword()) + getmKeyword().length(),
								Spanned.SPAN_INCLUSIVE_INCLUSIVE);
						holder.titleTv.setText(spannableString);
					} else { // 不包关键字
						holder.titleTv.setTextColor(NORMAL_TEXT_COLOR);
						holder.titleTv.setText(category.getCategoryname());
					}
				} else {
					holder.titleTv.setTextColor(NORMAL_TEXT_COLOR);
					holder.titleTv.setText(category.getCategoryname());
				}
				if (isCategorySelect) {
					holder.selectCb2.setVisibility(View.VISIBLE);
					// 是否选中
					holder.selectCb2.setChecked(category.isSelected());
					holder.selectCb2.setTag(position);
					holder.selectCb2.setOnClickListener(CategoryAdapter.this);
				} else {
					holder.selectCb2.setVisibility(View.GONE);// 单选
				}
				// 是否选中
				if (editMode) {
					holder.selectCb.setVisibility(View.VISIBLE);
					// 是否选中
					holder.selectCb.setChecked(category.isSelected());
					holder.selectCb.setTag(position);
					holder.selectCb.setOnClickListener(CategoryAdapter.this);
				} else {
					holder.selectCb.setVisibility(View.GONE);// 单选
				}

			}



			if (itemViewType==2) { //人脉
				Connections connections = (Connections) getItem(position);
				PersonviewHolder personviewHolder;
				if (convertView == null) {
					convertView = View.inflate(mContext,R.layout.im_relationcontactmain_item, null);
					personviewHolder = new PersonviewHolder();
					personviewHolder.contactNameTv = (TextView) convertView.findViewById(R.id.contactNameTv);
					personviewHolder.contactCompanyOfferTv = (TextView) convertView.findViewById(R.id.contactCompanyOfferTv);
					personviewHolder.contactAvatarIv = (CircleImageView) convertView.findViewById(R.id.contactAvatarIv);
					personviewHolder.sendSmsIv = (ImageView) convertView.findViewById(R.id.sendSmsIv);
					personviewHolder.callIv = (ImageView) convertView.findViewById(R.id.callIv);
					personviewHolder.selectCb = (CheckBox)convertView.findViewById(R.id.selectCb);
					convertView.setTag(personviewHolder);
				}
				personviewHolder = (PersonviewHolder) convertView.getTag();
				if (personviewHolder != null) {
					//用户/人脉时样式设置
					Util.initAvatarImage(mContext, personviewHolder.contactAvatarIv, connections.getName(), connections.getImage(), connections.getJtContactMini().getGender(), 1);
					Drawable drawable = null;
					if (editMode) {
						personviewHolder.selectCb.setVisibility(View.VISIBLE);
						personviewHolder.selectCb.setTag(position);
						personviewHolder.selectCb.setOnClickListener(CategoryAdapter.this);
					}else{
						personviewHolder.selectCb.setVisibility(View.GONE);
					}


					if (connections != null && connections.isOnline()) {
						drawable = mContext.getResources().getDrawable(R.drawable.contactfriendtag);
					} else {
						drawable = mContext.getResources().getDrawable(R.drawable.contactpeopletag);
					}
					if (drawable != null) {
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
						if (personviewHolder.contactNameTv != null) {
							personviewHolder.contactNameTv.setCompoundDrawables(drawable,null, null, null);
							personviewHolder.contactNameTv.setCompoundDrawablePadding(DisplayUtil.dip2px(mContext, 10));
						}
					}
					if (personviewHolder.contactNameTv != null) {
						personviewHolder.contactNameTv.setText(connections.getName());
					}
					if (personviewHolder.contactCompanyOfferTv != null) {
						personviewHolder.contactCompanyOfferTv.setText(connections.getCompany() + connections.getCareer());
					}

					final ArrayList<MobilePhone> mobilePhoneList = new ArrayList<MobilePhone>();
					for (MobilePhone mobilePhone : connections.getMobilePhoneList()) {
						if (!StringUtils.isEmpty(mobilePhone.mobile)&& !StringUtils.isEmpty(mobilePhone.name)) {
							mobilePhoneList.add(mobilePhone);
						}
					}
					final ArrayList<MobilePhone> fixedPhoneList = new ArrayList<MobilePhone>();
					for (MobilePhone mobilePhone : connections.getFixedPhoneList()) {
						if (!StringUtils.isEmpty(mobilePhone.mobile)&& !StringUtils.isEmpty(mobilePhone.name)) {
							fixedPhoneList.add(mobilePhone);
						}
					}
					if (personviewHolder.sendSmsIv != null && personviewHolder.callIv != null) {
						// 如果有手机号列表
						if (mobilePhoneList.size() > 0|| (fixedPhoneList.size() > 0)&& (!TextUtils.isEmpty(fixedPhoneList.get(0).getMobile()))) {
							personviewHolder.sendSmsIv.setVisibility(View.VISIBLE);
							personviewHolder.callIv.setVisibility(View.VISIBLE);
						} else {
							personviewHolder.sendSmsIv.setVisibility(View.GONE);
							personviewHolder.callIv.setVisibility(View.GONE);
						}
						OnClickListener mOnClickListener = new OnClickListener() {

							@Override
							public void onClick(View v) {

								switch (v.getId()) {
								case R.id.sendSmsIv:
									new ConnsCallAndSendSmsDialog(mContext,ConnsCallAndSendSmsDialog.TYPE_SEND_SMS,mobilePhoneList, null).show();
									break;
								case R.id.callIv:
									new ConnsCallAndSendSmsDialog(mContext,ConnsCallAndSendSmsDialog.TYPE_CALL,mobilePhoneList, fixedPhoneList).show();
									break;
								}
							}
						};
						personviewHolder.callIv.setOnClickListener(mOnClickListener);
						personviewHolder.sendSmsIv.setOnClickListener(mOnClickListener);
					}
				}
			}else if(itemViewType==1){//知识
				KnowledgeViewHolder knowledgeviewHolder = null;
				if (convertView == null) {
					knowledgeviewHolder = new KnowledgeViewHolder();
					convertView = LayoutInflater.from(mContext).inflate(R.layout.item_my_knowledge_lv, null);
					knowledgeviewHolder.cb = (CheckBox) convertView.findViewById(R.id.cb);
					knowledgeviewHolder.cb.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							KnowledgeMini2 knowledgeMini2 = (KnowledgeMini2) v.getTag();
							// long id = knowledgeMini2.getId();
							//						if (checkedHm.get(knowledgeMini2) != null) {
							//							boolean b = checkedHm.get(knowledgeMini2);
							//							checkedHm.put(knowledgeMini2, !b);
							//						} else {
							//							checkedHm.put(knowledgeMini2, true);
							//						}
							notifyDataSetChanged();

						}
					});

					knowledgeviewHolder.titleTv = (TextView) convertView.findViewById(R.id.titleTv);
					//				viewHolder.numTv = (TextView) convertView.findViewById(R.id.numTv);
					knowledgeviewHolder.tagTv = (TextView) convertView.findViewById(R.id.tagTv);
					knowledgeviewHolder.timeTv = (TextView) convertView.findViewById(R.id.timeTv);
					knowledgeviewHolder.knowledgeIv = (ImageView) convertView.findViewById(R.id.knowledgeIv);
					knowledgeviewHolder.moreIv= (ImageView) convertView.findViewById(R.id.moreIv);
					convertView.setTag(knowledgeviewHolder);
				} else {
					knowledgeviewHolder = (KnowledgeViewHolder) convertView.getTag();
				}
				final KnowledgeMini2 knowledge = (KnowledgeMini2) getItem(position);
				knowledgeviewHolder.moreIv.setOnClickListener(new OnClickListener() {

					private PopupWindow popupWindow;

					@Override
					public void onClick(View v) {
						View view = View.inflate(mContext, R.layout.moreknowledgepop, null);
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

								// if(listKnowledge.size() == 1){
								// ENavigate.startShareActivity(MyKnowledgeActivity.this,
								// listKnowledge.get(0).toJTFile());
								// }
								// else{
								/**
								 * String mSuffixName = mKnowledge2.toJTFile().getmSuffixName();
							mKnowledge2.setDesc(filterHtml(mSuffixName));
							JTFile jtFile = mKnowledge2.toJTFile();
							jtFile.mFileName = "[知识] "+mKnowledge2.getTitle();
							jtFile.virtual = mKnowledge2.getType()+"";
								 */
								String mSuffixName = knowledge.toJTFile().getmSuffixName();
								knowledge.setDesc(filterHtml(mSuffixName));
								JTFile jtFile = knowledge.toJTFile();
								jtFile.messageID=EUtil.genMessageID();
								//										jtFile.mFileName ="分享了[知识] ";//+knowledge.getTitle();
								jtFile.mFileName =knowledge.getTitle();
								jtFile.virtual = knowledge.getType()+"";
								// ENavigate.startShareActivity(MyKnowledgeActivity.this,
								// JTFile.TYPE_KNOWLEDGE2, listJtFile);
								ENavigate.startSocialShareActivity(mContext, jtFile);
								// }

							}
						});
						biaoqianLl.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								popupWindow.dismiss();
								ArrayList<Long> listKnowledgeId=  new ArrayList<Long>();
								listKnowledgeId.add(knowledge.id);
								ArrayList<Integer> listKnowledgeType = new ArrayList<Integer>();
								listKnowledgeType.add(knowledge.type);
								ENavigate.startEditKnowledgeTagActivityForResult(mContext, 99, listKnowledgeId, listKnowledgeType);
							}
						});
						shanchuLl.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								popupWindow.dismiss();
								Builder builder = new AlertDialog.Builder(mContext);
								builder.setTitle("提示：");
								builder.setMessage("确定要删除知识吗？");
								builder.setNegativeButton("取消", null);
								builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {


									@Override
									public void onClick(DialogInterface dialog, int which) {
										// swipeLayout.setRefreshing(true);
										ArrayList<Long> listKnowledgeId=  new ArrayList<Long>();
										listKnowledgeId.add(knowledge.id);
										ArrayList<Integer> listKnowledgeType = new ArrayList<Integer>();
										listKnowledgeType.add(knowledge.type);
										DeleteKnowledge = knowledge;
										KnowledgeReqUtil.doDeleteKnowledgeById(mContext,CategoryAdapter.this, App.getUserID(), listKnowledgeId, listKnowledgeType,0, null);
									}
								});
								builder.show();
							}
						});
					}
				});
				if (knowledge.tag.equals("")) {
					knowledgeviewHolder.tagTv.setVisibility(View.GONE);
				}else{
					knowledgeviewHolder.tagTv.setVisibility(View.VISIBLE);
					knowledgeviewHolder.tagTv.setText(knowledge.tag);
				}

				//			ArrayList<String> lsitTag = knowledge.listTag;// 标签集合
				//			if (null != lsitTag && !lsitTag.isEmpty() /*&& type != TYPE_ALL*/) {// 在我创建的下不显示标签:::
				//				knowledgeviewHolder.tagTv.setVisibility(View.VISIBLE);
				//				knowledgeviewHolder.tagTv.setText(lsitTag.toString());
				//			} else {
				//				knowledgeviewHolder.tagTv.setVisibility(View.GONE);
				//				knowledgeviewHolder.cb.setGravity(Gravity.CENTER_VERTICAL);
				//			}
				// if (null != lsitTag && !lsitTag.isEmpty() &&type!=TYPE_ALL)
				// {//在我创建的下不显示标签:::
				// viewHolder.tagLl.setVisibility(View.VISIBLE);
				// for (int i = 0; i < lsitTag.size(); i++) {
				// if (lsitTag.get(i).isEmpty()) {
				// continue;
				// }
				// measureView(viewHolder.tagLl);
				// if (viewHolder.tagLl.getMeasuredWidth()<(mWidth*0.8)) {
				// TextView tagTv = newTagTv(lsitTag.get(i));
				// tagTv.setEllipsize(TruncateAt.END);
				// viewHolder.tagLl.addView(tagTv, lp);
				// }
				// }
				// }else {
				// viewHolder.cb.setGravity(Gravity.CENTER_VERTICAL);
				// }
				//
				if (editMode) {
					knowledgeviewHolder.cb.setVisibility(View.VISIBLE);
					//				if (checkedHm.get(knowledge) != null) {
					//					boolean b = checkedHm.get(knowledge);
					//					knowledgeviewHolder.cb.setChecked(b);
					//				} else {
					//					knowledgeviewHolder.cb.setChecked(false);
					//				}
				} else {
					knowledgeviewHolder.cb.setVisibility(View.GONE);
				}
				knowledgeviewHolder.timeTv.setText(TextUtils.isEmpty(knowledge.modifytime)?"":TimeUtil.TimeFormat(knowledge.modifytime));
				knowledgeviewHolder.cb.setTag(knowledge);
				knowledgeviewHolder.titleTv.setText(knowledge.title);
				if(TextUtils.isEmpty(knowledge.pic)){
					knowledgeviewHolder.knowledgeIv.setVisibility(View.GONE);
				}else{
//					knowledgeviewHolder.knowledgeIv.setVisibility(View.VISIBLE);
					knowledgeviewHolder.knowledgeIv.setVisibility(View.GONE);
					ImageLoader.load(knowledgeviewHolder.knowledgeIv, knowledge.pic);
				}

			}else if(itemViewType==3){//组织
				List<String> mobileList = new ArrayList<String>();// 手机号码
				List<String> planeList = new ArrayList<String>();// 座机号码
				final List<String> all_Phone = new ArrayList<String>();// 所有可以打电话的
				final List<String> message_Phone = new ArrayList<String>();// 所有可以发短信的
				OrganizationViewHolder organizationholder;
				if (convertView == null) {
					organizationholder = new OrganizationViewHolder();
					convertView = View.inflate(mContext,
							R.layout.org_orgorcustomer_listviewitem, null);
					organizationholder.org_RL = (RelativeLayout) convertView
							.findViewById(R.id.org_RL);// 首字母所在的布局
					organizationholder.tv_word = (TextView) convertView
							.findViewById(R.id.tv_word);// 开头首字母
					organizationholder.org_tv_name = (TextView) convertView
							.findViewById(R.id.org_tv_name);// 名称
					organizationholder.iv_message = (ImageView) convertView
							.findViewById(R.id.iv_message);// 短信图标
					organizationholder.iv_dial = (ImageView) convertView
							.findViewById(R.id.iv_dial);// 电话图标
					organizationholder.org_iv_headprotrait = (CircleImageView) convertView
							.findViewById(R.id.org_iv_headprotrait);// Logo
					organizationholder.org_iv_head = (ImageView) convertView
							.findViewById(R.id.org_iv_head);// 组织 客户图标
					organizationholder.org_tv_location = (TextView) convertView
							.findViewById(R.id.org_tv_location);// 地区
					organizationholder.org_tv_work = (TextView) convertView
							.findViewById(R.id.org_tv_work);// 行业
					organizationholder.selectCb = (CheckBox) convertView
							.findViewById(R.id.selectCb);
					convertView.setTag(organizationholder);
				} else {
					organizationholder = (OrganizationViewHolder) convertView.getTag();

				}
				if (editMode) {
					organizationholder.selectCb.setVisibility(View.VISIBLE);
					organizationholder.selectCb.setTag(position);
					organizationholder.selectCb.setOnClickListener(CategoryAdapter.this);
				}else{
					organizationholder.selectCb.setVisibility(View.GONE);
				}
				final Contacts contacts = (Contacts) getItem(position);
				if (!TextUtils.isEmpty(contacts.getShortName())) {// 设置名称
					organizationholder.org_tv_name.setText(contacts.getShortName());
				} else {
					organizationholder.org_tv_name.setText(contacts.getName());
				}
				String org_name = organizationholder.org_tv_name.getText().toString();
				Util.initAvatarImage(mContext, organizationholder.org_iv_headprotrait, org_name, contacts.getPicLogo(), 1, 2);

				organizationholder.org_tv_name.setCompoundDrawables(null,null, null, null);
				Drawable drawable = null;
				if (contacts.getVirtual() == 0) {// 设置是否是组织/客户
					drawable = mContext.getResources().getDrawable(R.drawable.contactclienttag);
				} else if (contacts.getVirtual() == 1
						|| contacts.getVirtual() == 2) {
					drawable = mContext.getResources().getDrawable(R.drawable.contactorganizationtag);


					mobileList.clear();
					planeList.clear();

				}
				String linkedMobiless = contacts.getLinkMobile();
				if (!TextUtils.isEmpty(linkedMobiless.trim())) {
					organizationholder.iv_message.setVisibility(View.VISIBLE);
					organizationholder.iv_dial.setVisibility(View.VISIBLE);
				}else{
					organizationholder.iv_message.setVisibility(View.GONE);
					organizationholder.iv_dial.setVisibility(View.GONE);
				}
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
				organizationholder.org_tv_name.setCompoundDrawables(drawable,null, null, null);
				organizationholder.org_tv_name.setCompoundDrawablePadding(DisplayUtil.dip2px(mContext, 10));

				if (!TextUtils.isEmpty(contacts.getCity())) {
					organizationholder.org_tv_location.setText(contacts.getCity());// 设置城市
				}else{
					organizationholder.org_tv_location.setVisibility(View.GONE);
				}

				if (!"(null)".equals(contacts.getIndustrys())) {
					organizationholder.org_tv_work.setText(contacts.getIndustrys());// 设置行业
				}

				organizationholder.iv_message.setOnClickListener(new OnClickListener() {// 短信
					@Override
					public void onClick(View v) {
						message_Phone.clear();
						String linkMobiles = contacts.getLinkMobile();
						String reg1 = "^[1][358][0-9]{9}";// 手机号码验证
						// 正则表达式
						// String
						// reg2 =
						// "^[0][1-9]{2,3}-[0-9]{5,10}$";//
						// 座机号码正则
						if (!TextUtils.isEmpty(linkMobiles)) {// 设置电话　　短信的图标
							String[] linkedMobiles = linkMobiles
									.split(",");
							for (String linkedMobile : linkedMobiles) {
								if (linkedMobile.matches(reg1)) {// 手机号码
									message_Phone.add(linkedMobile);
								}
							}

						}
						AlertDialog alertDialog_message = new AlertDialog.Builder(
								mContext).create();
						if (message_Phone.size() > 0) {
							alertDialog_message.show();
						} else {
							toastMsg("手机号码格式不正确，无法发送短信");
						}

						Window window_message = alertDialog_message
								.getWindow();
						View convertView = View.inflate(mContext,
								R.layout.people_maincontacts_message,
								null);
						window_message.setContentView(convertView);
						alertDialog_message
						.setCanceledOnTouchOutside(true);
						ListView listView = (ListView) convertView
								.findViewById(R.id.listView);
						listView.setAdapter(new ListViewAdapter(
								message_Phone, mContext));
						listView.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(
									AdapterView<?> parent, View view,
									int position, long id) {
								TextView phone = (TextView) view
										.findViewById(R.id.phone);
								sendMessage(phone.getText().toString());
							}
						});
					}
				});

				organizationholder.iv_dial.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						all_Phone.clear();
						String linkMobiles = contacts.getLinkMobile();
						String reg1 = "^[1][358][0-9]{9}";// 手机号码验证 正则表达式
						String reg2 = "^[0][1-9]{2,3}-[0-9]{5,10}$";// 座机号码正则
						if (!TextUtils.isEmpty(linkMobiles)) {// 设置电话　　短信的图标
							String[] linkedMobiles = linkMobiles.split(",");
							for (String linkedMobile : linkedMobiles) {
								if (linkedMobile.matches(reg1)) {// 手机号码
									all_Phone.add(linkedMobile);
								} else if (linkedMobile.matches(reg2)) {
									all_Phone.add(linkedMobile);
								}
							}
						}

						AlertDialog alertDialog_message = new AlertDialog.Builder(
								mContext).create();
						if (all_Phone.size() > 0) {
							alertDialog_message.show();
						} else {
							toastMsg("手机号码格式不正确，无法拨打电话");
						}

						Window window_message = alertDialog_message.getWindow();
						View convertView = View.inflate(mContext,
								R.layout.people_maincontacts_message, null);
						window_message.setContentView(convertView);
						alertDialog_message.setCanceledOnTouchOutside(true);
						ListView listView = (ListView) convertView
								.findViewById(R.id.listView);
						listView.setAdapter(new ListViewAdapter(all_Phone,
								mContext));
						listView.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								/*TextView phone = (TextView) view
									.findViewById(R.id.phone);
							callPhone(phone.getText().toString());*/
								callSystemPhone(view);
							}

						});
					}
				});

				// 对首字母进行判断
				String currentFirstWord = contacts.getNameFirst();
				if (position > 0) {
					String lastFirstWord = getContacts().get(position - 1)
							.getNameFirst();
					if (currentFirstWord.equals(lastFirstWord)) {
						organizationholder.org_RL.setVisibility(View.GONE);
					} else {
						organizationholder.org_RL.setVisibility(View.VISIBLE);
						organizationholder.tv_word.setText(currentFirstWord);
					}
				} else {
					organizationholder.org_RL.setVisibility(View.VISIBLE);
					organizationholder.tv_word.setText(currentFirstWord);
				}



			}else if(itemViewType==4){//需求
				NeedItemData needItem = (NeedItemData) getItem(position);
				NeedViewHolder needViewHolder = null ;
				if (convertView == null) {
					needViewHolder =new NeedViewHolder();
					convertView = View.inflate(mContext,
							R.layout.demand_me_need_listview, null);
					needViewHolder. typeIv =(ImageView) convertView .findViewById( R.id.typeIv);
					needViewHolder. titleTv = (TextView) convertView .findViewById( R.id.titleTv);
					needViewHolder. editSateIv = (ImageView) convertView .findViewById( R.id.editSateIv);
					needViewHolder. priceTv =(TextView) convertView .findViewById( R.id.priceTv);
					needViewHolder. timeTv = (TextView) convertView .findViewById( R.id.timeTv);
					needViewHolder. selectCb = (CheckBox) convertView .findViewById( R.id.selectCb);
					convertView.setTag(needViewHolder);			
				}else{
					needViewHolder = (NeedViewHolder) convertView.getTag();
				}

				if (editMode) {
					needViewHolder.selectCb.setVisibility(View.VISIBLE);
					needViewHolder.selectCb.setTag(position);
					needViewHolder.selectCb.setOnClickListener(CategoryAdapter.this);
				}else{
					needViewHolder.selectCb.setVisibility(View.GONE);
				}
				try {
					needViewHolder.timeTv.setText(TimeUtil.TimeFormat(needItem.createTime));
				} catch (Exception exception) {
					needViewHolder.timeTv.setText("时间解析错误");
				}
				needViewHolder.titleTv.setText(needItem.title.value);// 标题对象
				if (needItem.amount != null
						&& !TextUtils.isEmpty(needItem.amount.unit)) {
					needViewHolder.priceTv.setText(needItem.amount.getAmountData()); // 显示金额信息
				} else {
					needViewHolder.priceTv.setText("");// 没有金额信息
				}
				//			// 显示选择的状态
				//			if (selectedId.contains(needItem.demandId)) {
				//				editSateIv.setSelected(true);
				//			} else {
				//				editSateIv.setSelected(false);
				//			}
				//			editSateIv.setVisibility(eidtItem ? View.VISIBLE : View.GONE);
				if (needItem.demandType == ChooseDataUtil.CHOOSE_type_OutInvestType) {
					needViewHolder.typeIv.setImageResource(R.drawable.demand_me_need01);
				} else {
					needViewHolder.typeIv.setImageResource(R.drawable.demand_me_need02);
				}
			}

		}
		return convertView;
	}
	class OrganizationViewHolder {
		public RelativeLayout org_RL;
		public TextView tv_word;// 顶部的字母
		public TextView org_tv_name;// 每个listView子条目的名称
		public ImageView iv_message;// 每个listView子条目的短信图标
		public ImageView iv_dial;// 每个listView子条目的电话图标
		public CircleImageView org_iv_headprotrait;// 每个listView子条目的Logo图标
		public ImageView org_iv_head;// 每个listView子条目的组织/客户的图标
		public TextView org_tv_location;// 每个ListView的子条目的地区
		public TextView org_tv_work;// 每个ListView的子条目的行业
		public CheckBox selectCb;// 
	}
	class KnowledgeViewHolder {
		public TextView tagTv;
		public ImageView moreIv;
		CheckBox cb;
		TextView titleTv;
		TextView timeTv;
		ImageView knowledgeIv;
	}
	class NeedViewHolder {
		public TextView priceTv;
		public ImageView editSateIv;
		CheckBox selectCb;
		TextView titleTv;
		TextView timeTv;
		ImageView typeIv;
	}
	public class CategoryViewHolder {
		public CheckBox selectCb2;
		public ImageView foldIv; // 展开收起
		public TextView titleTv; // 是否高亮
		public CheckBox selectCb; // 是否选中
	}
	class PersonviewHolder {
		public CheckBox selectCb;
		public TextView contactNameTv;// 类型+名字
		public TextView avatarText;// 头像上面的字
		public TextView contactCompanyOfferTv;// 公司+职位
		public CircleImageView contactAvatarIv;// 头像
		public ImageView sendSmsIv;// 发短信
		public ImageView callIv;// 打电话
	}
	// 激活发短信页面，并将电话号码携带过去
	private void sendMessage(String message_number_business) {
		Uri uri = Uri.parse("smsto:" + message_number_business);
		Intent it = new Intent(Intent.ACTION_SENDTO, uri);
		mContext.startActivity(it);
	}
	private void callSystemPhone(View view) {
		TextView phone = (TextView) view
				.findViewById(R.id.phone);
		Uri uri = Uri.parse("tel:" +phone.getText());
		Intent intent = new Intent(Intent.ACTION_DIAL, uri);     
		mContext.startActivity(intent);
	}
	private void toastMsg(String text) {
		Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
	}
	private TextView newTagTv(String tag) {
		TextView tagTv = new TextView(mContext);
		tagTv.setTextSize(12);
		tagTv.setTextColor(R.color.text_gray);
		tagTv.setSingleLine(true);
		tagTv.setPadding(8, 0, 8, 0);
		tagTv.setBackgroundResource(R.drawable.taggv_item_bg_shape);
		tagTv.setGravity(Gravity.CENTER);
		tagTv.setText(tag);
		return tagTv;
	}
	/**
	 * 通知父布局，占用的宽，高；
	 * 
	 * @param view
	 */
	private void measureView(View view) {
		// ViewGroup.LayoutParams p = view.getLayoutParams();
		// if (p == null) {
		// p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
		// ViewGroup.LayoutParams.WRAP_CONTENT);
		// }
		// int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		// int height;
		// int tempHeight = p.height;
		// if (tempHeight > 0) {
		// height = MeasureSpec.makeMeasureSpec(tempHeight,
		// MeasureSpec.EXACTLY);
		// } else {
		// height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		// }
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
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
	public void onClick(View v) { // 点击事件
		//		if (v.getId() == R.id.foldIv) {
		//
		//			int position = ((Integer) v.getTag()).intValue();
		//			UserCategory category = mListCategory.get(position);
		//			if (category.getListUserCategory() != null
		//					&& category.getListUserCategory().size() > 0) {
		//				// 置反状态
		//				category.setFolded(!category.isFolded());
		//				if (!category.isFolded()) { // 由合起变为展开,一级子项由隐藏变为显示,二级及以下子项仍然隐藏
		//					for (int i = position + 1; i < mListCategory.size(); i++) {
		//						if (mListCategory.get(i).getLevel() == category
		//								.getLevel() + 1) { // 一级子目录
		//							mListCategory.get(i).setVisiable(true);
		//							mListCategory.get(i).setFolded(true);
		//							// mListCategory.get(i).setFolded(false);
		//						} else if (mListCategory.get(i).getLevel() <= category
		//								.getLevel()) { // 同级或以上目录
		//							break;
		//						} else { // 二级或以下子目录
		//
		//						}
		//					}
		//				} else { // 由展开变为合起,所有子项由显示变为隐藏
		//					for (int i = position + 1; i < mListCategory.size(); i++) {
		//						if (mListCategory.get(i).getLevel() > category
		//								.getLevel()) {
		//							mListCategory.get(i).setVisiable(false);
		//							mListCategory.get(i).setFolded(true);
		//						} else {
		//							break;
		//						}
		//
		//					}
		//				}
		//			}
		//		} else 
		if (v.getId() == R.id.selectCb2) {

			int position = ((Integer) v.getTag()).intValue();
			mListCategory.get(position).setSelected(
					!mListCategory.get(position).isSelected());
		}
		notifyDataSetChanged();
	}

	@Override
	public void bindData(int tag, Object object) {
		if (KnoReqType.DeleteKnowledgeById == tag) {

			Map<String, Object> hm = (Map<String, Object>) object;
			boolean b = (Boolean) hm.get("success");
			if (b) {
				Toast.makeText(mContext, "删除成功", 0).show();
				if (DeleteKnowledge!=null) {
					getmListKnowledgeMini2s().remove(DeleteKnowledge);
					this.notifyDataSetChanged();
				}

			} else {
				Toast.makeText(mContext, "删除失败", 0).show();
			}
		}
	}
}


