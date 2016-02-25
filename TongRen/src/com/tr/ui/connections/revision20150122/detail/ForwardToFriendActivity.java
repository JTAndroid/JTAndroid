package com.tr.ui.connections.revision20150122.detail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.api.ConnectionsReqUtil;
import com.tr.model.obj.DynamicNews;
import com.tr.model.obj.ForwardDynamicNews;
import com.tr.model.obj.JTContactMini;
import com.tr.model.obj.JTFile;
import com.tr.model.user.OrganizationMini;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.conference.initiatorhy.InitiatorDataCache;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.im.ChatBaseActivity;
import com.tr.ui.im.IMChatMessageCache;
import com.tr.ui.widgets.CommonSmileyParser;
import com.tr.ui.widgets.SmileyView;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.time.Util;

/***
 * 转发给好友 将转发的内容转成转发动态对象
 * 
 * @author zhongshan
 * 
 */
public class ForwardToFriendActivity extends JBaseFragmentActivity implements OnClickListener, OnPageChangeListener, IBindData {

	private EditText contentEt;
	private ImageView imageIv;
	private TextView nameTv;
	private TextView contentTv;
	private TextView chooseFriendsTv;
	private TextView chooseSmaileTv;
	private ViewPager viewPager;
	private LinearLayout viewPagerCon;
	// private KnoTagGroupView itaFriendGv;
	private LinearLayout itaFriendLL;
	private ForwardDynamicNews forwardDynamicNews;
	// @的用户Mini对象集合
	private ArrayList<JTContactMini> jtContactMinis;
	private ArrayList<OrganizationMini> mOrganizationMinis;
	// 需要移除的用户迷你对象集合
//	private ArrayList<JTContactMini> jtContactMinisForRemove;
	private ArrayList<String> names;
	private String content;

//	private SmileyView smileyView;
//	private SmileyView smileyView2;

	private boolean smaileActive;
	ArrayList<String> listReceiverId;

	@Override
	public void initJabActionBar() {
//		getActionBar().setTitle("分享");
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "分享", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forwardtofriend_activity);
		
		// forwardDynamicNews = (ForwardDynamicNews)
		// intent.getSerializableExtra("ForwardDynamicNews");
		initView();
		initObject();

		initControl();
	}

	private void initObject() {
		forwardDynamicNews = new ForwardDynamicNews();
		Intent intent = getIntent();
		JTFile jtFile = (JTFile) intent.getSerializableExtra("JTFile");
		// forwardDynamicNews.setContent(jtContact.getUserJob());
		// forwardDynamicNews.createrIdApp.getUserID());
		// forwardDynamicNews.imgPath = jtContact.getIconUrl());
		// forwardDynamicNews.setTargetId(jtContact.getUser().getId());
		// forwardDynamicNews.setTitle(jtContact.getName());

		forwardDynamicNews.createrId = App.getUserID();// 创建者id
		forwardDynamicNews.imgPath = jtFile.getmUrl();// 图片
		forwardDynamicNews.targetId = jtFile.mTaskId;// 转发源id

		switch (jtFile.getmType()) {
		case 6: // 人脉
			forwardDynamicNews.title = jtFile.mFileName;
			forwardDynamicNews.type = DynamicNews.TYPE_FORWARDING_CONTACTS + "";// 转发类型
			forwardDynamicNews.lowType = "0";// 源类型
			forwardDynamicNews.content = jtFile.reserved2+jtFile.reserved1;// 内容:公司+职位
			break;
		case 9: //组织
		case JTFile.TYPE_ORGANIZATION:
//			imageIv.setBackgroundResource(R.drawable.default_portrait116);
			forwardDynamicNews.title = !TextUtils.isEmpty(jtFile.mFileName)?jtFile.mFileName:jtFile.getmSuffixName();// 标题:姓名
			forwardDynamicNews.type = DynamicNews.TYPE_FORWARDING_ORGANIZATION + "";// 转发类型
			forwardDynamicNews.lowType = jtFile.mModuleType+"";// 源类型
			forwardDynamicNews.content = jtFile.reserved1;// 内容:职位
			break;
		case JTFile.TYPE_CLIENT: //客户
//			imageIv.setBackgroundResource(R.drawable.default_portrait116);
			forwardDynamicNews.title = !TextUtils.isEmpty(jtFile.mFileName)?jtFile.mFileName:jtFile.getmSuffixName();// 标题:姓名
//			forwardDynamicNews.type = DynamicNews.TYPE_CUSTOMER_CARD + "";// 转发类型
			forwardDynamicNews.type = DynamicNews.TYPE_FORWARDING_CUSTOM + "";// 转发类型
			forwardDynamicNews.lowType = jtFile.mModuleType+"";// 源类型
			forwardDynamicNews.content = jtFile.reserved1;// 内容:职位
			break;
		case 10:// 用户
			forwardDynamicNews.title = jtFile.mFileName;// 标题:姓名
			forwardDynamicNews.type = DynamicNews.TYPE_USER_CARD + "";// 转发类型
			forwardDynamicNews.lowType = "0";// 源类型
			forwardDynamicNews.content = jtFile.reserved1;// 内容:职位
			break;
		case 13:// 知识
			forwardDynamicNews.title = jtFile.reserved2;// 标题:知识title
			forwardDynamicNews.type = DynamicNews.TYPE_FORWARDING_KNOWLEDGE + "";// 转发类型
			forwardDynamicNews.lowType = jtFile.reserved1 + "";// 源类型
			forwardDynamicNews.content = jtFile.mSuffixName;// 内容：知识desc
			break;
		case 14:// 会议
			forwardDynamicNews.title = jtFile.mSuffixName;// 标题:知识title
			forwardDynamicNews.type = DynamicNews.TYPE_FORWARDING_MEETING + "";// 转发类型
			forwardDynamicNews.lowType = jtFile.mModuleType + "";// 源类型
			forwardDynamicNews.content = jtFile.reserved1;// 
			break;
		case JTFile.TYPE_DEMAND://需求转发
			forwardDynamicNews.createrId = jtFile.reserved3;//转发需求的时候 传入创建者id
			forwardDynamicNews.title = jtFile.mFileName;// 标题:需求title
			forwardDynamicNews.type = DynamicNews.TYPE_FORWARDING_REQUIREMENT + "";// 转发类型
			forwardDynamicNews.lowType = jtFile.reserved2 + "";// 源类型 融资和投资类型
			forwardDynamicNews.content = jtFile.mSuffixName;// 内容：需求介绍内容
			break;
		default:
			break;
		}
	}

	private void initView() {

		// itaFriendGv = (KnoTagGroupView) findViewById(R.id.add_ita_friend_Gv);
		itaFriendLL = (LinearLayout) findViewById(R.id.add_ita_friend_LL);

		contentEt = (EditText) findViewById(R.id.contentEt);
		imageIv = (ImageView) findViewById(R.id.ImageIv);
		nameTv = (TextView) findViewById(R.id.nameTv);
		contentTv = (TextView) findViewById(R.id.contentTv);
		chooseFriendsTv = (TextView) findViewById(R.id.chooseFriendsTv);
		chooseSmaileTv = (TextView) findViewById(R.id.chooseSmaileTv);

		/** 表情 */
		viewPager = (ViewPager) this.findViewById(R.id.smileyPager);
		viewPagerCon = (LinearLayout) this.findViewById(R.id.smileyPagerContainer);

		// 表情view
//		smileyView = new SmileyView(this, true);
//		smileyView2 = new SmileyView(this, false);

		viewPager.setOnPageChangeListener(this);
		
		listSmileyViews = new ArrayList<SmileyView>();
		int totalPage = (int) Math.ceil(CommonSmileyParser.mEnhancedIconIds.length * 1.0 / SmileyView.MaxSmileyNumber);
		for (int i = 0; i < totalPage; i++) {
			SmileyView sv = new SmileyView(this, i);
			listSmileyViews.add(sv);
			sv.setOnItemClickListener(smileyViewClickListener);
		}

		// 表情切换界面
		viewPager.setAdapter(new PageViewAdpter());
		mSmileyPagerchange = ((ImageView) findViewById(R.id.smileyPagerchange));

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					mSmileyPagerchange.setImageResource(R.drawable.chat_biaoqing_1);
					break;
				case 1:
					mSmileyPagerchange.setImageResource(R.drawable.chat_biaoqing_2);
					break;
				case 2:
					mSmileyPagerchange.setImageResource(R.drawable.chat_biaoqing_3);
					break;
				case 3:
					mSmileyPagerchange.setImageResource(R.drawable.chat_biaoqing_4);
					break;
				case 4:
					mSmileyPagerchange.setImageResource(R.drawable.chat_biaoqing_5);
					break;
				case 5:
					mSmileyPagerchange.setImageResource(R.drawable.chat_biaoqing_6);
					break;
				case 6:
					mSmileyPagerchange.setImageResource(R.drawable.chat_biaoqing_7);
					break;
				case 7:
					mSmileyPagerchange.setImageResource(R.drawable.chat_biaoqing_8);
					break;
				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}

		});

		
		
		
		
/*		smileyView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				chooseSmaile1(position);
			}
		});
		smileyView2.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				chooseSmaile2(position);
			}
		});*/
	}
	private static final String LEFTSPECCHAR = ((char) 0X1B) + "";
	private static final String RIGHTSPECCHAR = ((char) 0X11) + "";
	/**
	 * 表情点击事件
	 */
	private SmileyView.OnItemClickListener smileyViewClickListener = new SmileyView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg) {
			final CommonSmileyParser parser = CommonSmileyParser.getInstance(ForwardToFriendActivity.this);
			if (position == SmileyView.MaxSmileyNumber) { // 删除表情
				String text = contentEt.getText().toString();

				if (text.length() > 0) {

					if (text.lastIndexOf(RIGHTSPECCHAR) == text.length() - 1) {
						text = text.substring(0, text.lastIndexOf(LEFTSPECCHAR));
					} else {
						text = text.substring(0, text.length() - 1);
					}
					contentEt.setText(text);
					contentEt.setSelection(text.length());
				}
				return;
			}

			final String text = contentEt.getText().toString() + LEFTSPECCHAR + parser.getmSmileyTexts()[(int) arg] + RIGHTSPECCHAR;
			contentEt.setText(text);
			contentEt.setSelection(text.length());
		}
	};
	/**表情选择*//*
	private void chooseSmaile1(int position) {
		final SmileyParser parser = SmileyParser.getInstance(ForwardToFriendActivity.this);
		final SmileyParser2 parser2 = SmileyParser2.getInstance(ForwardToFriendActivity.this);
		if (position == 20) {
			String text = contentEt.getText().toString();
			if (text.length() > 0) {
				int subLength = 1;
				for (int i = 0; i < 20; i++) {
					if (text.endsWith(parser.getmSmileyTexts()[i])) {
						subLength = parser.getmSmileyTexts()[i].length();
						break;
					}
					if (text.endsWith(parser2.getmSmileyTexts()[i])) {
						subLength = parser2.getmSmileyTexts()[i].length();
						break;
					}
				}

				text = text.substring(0, text.length() - subLength);
				CharSequence charSequence = parser.addSmileySpans(text);
				charSequence = parser2.addSmileySpans(charSequence);
				contentEt.setText(charSequence);
				contentEt.setSelection(text.length());
			}
			return;
		}
		if (contentEt.getText().toString().length() <= 140 - parser.getmSmileyTexts()[position].length()) {
			final String text = contentEt.getText().toString() + parser.getmSmileyTexts()[position];
			CharSequence dd = parser2.addSmileySpans(text);
			CharSequence dd1 = parser.addSmileySpans(dd);
			contentEt.setText(dd1);
			contentEt.setSelection(text.length());
		}
	}
	private void chooseSmaile2(int position) {
		final SmileyParser parser = SmileyParser.getInstance(ForwardToFriendActivity.this);
		final SmileyParser2 parser2 = SmileyParser2.getInstance(ForwardToFriendActivity.this);
		if (position == 20) {
			String text = contentEt.getText().toString();
			if (text.length() > 0) {
				int subLength = 1;
				for (int i = 0; i < 20; i++) {
					if (text.endsWith(parser2.getmSmileyTexts()[i])) {
						subLength = parser2.getmSmileyTexts()[i].length();
						break;
					}
					if (text.endsWith(parser.getmSmileyTexts()[i])) {
						subLength = parser.getmSmileyTexts()[i].length();
						break;
					}
				}
				
				text = text.substring(0, text.length() - subLength);
				CharSequence charSequence = parser.addSmileySpans(text);
				charSequence = parser2.addSmileySpans(charSequence);
				contentEt.setText(charSequence);
				contentEt.setSelection(text.length());
			}
			return;
		}
		if (contentEt.getText().toString().length() <= 140 - parser2.getmSmileyTexts()[position].length()) {
			final String text = contentEt.getText().toString() + parser2.getmSmileyTexts()[position];
			CharSequence dd = parser2.addSmileySpans(text);
			CharSequence dd1 = parser.addSmileySpans(dd);
			contentEt.setText(dd1);
			contentEt.setSelection(text.length());
		}
	}*/

	private void initControl() {
		jtContactMinis = new ArrayList<JTContactMini>();
		mOrganizationMinis = new ArrayList<OrganizationMini>();
//		jtContactMinisForRemove = new ArrayList<JTContactMini>();
		names = new ArrayList<String>();
		listReceiverId = new ArrayList<String>();

		if (TextUtils.isEmpty(forwardDynamicNews.imgPath)) {
			imageIv.setVisibility(View.GONE);
		} else {
			ImageLoader.getInstance().displayImage(forwardDynamicNews.imgPath, imageIv);
		}
		nameTv.setText(forwardDynamicNews.title);
		contentTv.setText(forwardDynamicNews.content);

		chooseFriendsTv.setOnClickListener(this);
		chooseSmaileTv.setOnClickListener(this);
		contentEt.addTextChangedListener(textWatcher);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chooseFriendsTv:
//			InitiatorDataCache.getInstance().inviteAttendSelectedMap.clear();
			ENavigate.startInviteFriendActivity(this, ForwardToFriendActivity.class.getSimpleName(), 2, 100);
			break;
		case R.id.chooseSmaileTv:
			if (smaileActive) {
				smaileActive = false;
				viewPagerCon.setVisibility(View.GONE);
			} else {
				smaileActive = true;
				viewPagerCon.setVisibility(View.VISIBLE);
			}

			break;

		default:
			break;
		}
	}
	private String beforeText = "";
	private int mStart = 0;
	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			beforeText = s.toString();
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			mStart = start;
			if(s.length() > beforeText.length()){
				String temp_after = s.charAt(start)+"";
				if(start == 0){
					if(temp_after.equals("@")){
						ENavigate.startInviteFriendActivity(ForwardToFriendActivity.this, ForwardToFriendActivity.class.getSimpleName(), 5, 200);
					}
				}else if (start >= 1) {
					String temp_before = beforeText.charAt(start-1)+"";
					if(temp_after.equals("@") && (Util.checkIsChinses(temp_before) || !Util.checkIsEngANum(temp_before))){//输入字符是“@”,其之前的字符是中文或者不是英文和数字
						ENavigate.startInviteFriendActivity(ForwardToFriendActivity.this, ForwardToFriendActivity.class.getSimpleName(), 5, 200);
					}
				}
			}else if (s.length() < beforeText.length() && start >= 1) {
				StringBuilder builder = new StringBuilder();
				// 删除的左侧特殊字符 向后索引（暂不处理此种情况）
				if ((beforeText.charAt(start) + "").equals(LEFTSPECCHAR)) {
				}
				// 如果删除的是@
				else if ((beforeText.charAt(start - 1) + "")
						.equals(LEFTSPECCHAR)
						&& (beforeText.charAt(start) + "").equals("@")) {
					for (int i = start - 1; i < beforeText.length(); i++) {
						// 等于右侧字符
						if ((beforeText.charAt(i) + "").equals(RIGHTSPECCHAR)) {
							builder.append(beforeText.subSequence(0, start - 1))
									.append(beforeText.subSequence(i + 1,
											beforeText.length()));
							contentEt.setText(builder.toString());
							contentEt.setSelection(start);
							break;
						}
					}
				}
				// 删除的右侧的特殊字符 向前索引
				else if ((beforeText.charAt(start) + "").equals(RIGHTSPECCHAR)) {
					for (int i = start - 1; i >= 0; i--) {
						// 等于右侧或左侧字符
						if ((s.charAt(i) + "").equals(LEFTSPECCHAR)) {
							builder.append(s.subSequence(0, i)).append(
									s.subSequence(start, s.length()));
							contentEt.setText(builder.toString());
							contentEt.setSelection(i);
							break;
						} else if ((s.charAt(i) + "").equals(RIGHTSPECCHAR)) {
							builder.append(s.subSequence(0, i))
									.append(RIGHTSPECCHAR)
									.append(s.subSequence(start, s.length()));
							contentEt.setText(builder.toString());
							contentEt.setSelection(i + 1);
							break;
						}
					}
				}
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch(requestCode){
		case 100:
			Iterator<Entry<String, JTContactMini>> attendSelMap = InitiatorDataCache.getInstance().inviteAttendSelectedMap.entrySet().iterator();
			itaFriendLL.removeAllViews();
			jtContactMinis.clear();
			names.clear();
			int countNum = 0;
			while (attendSelMap.hasNext()) {
				Map.Entry entry = (Map.Entry) attendSelMap.next();
				JTContactMini item = (JTContactMini) entry.getValue();
				jtContactMinis.add(item);
				names.add(item.getName());
				listReceiverId.add(item.getId());
			}

			if (jtContactMinis != null && jtContactMinis.size() >= 1) {
				for (int i = jtContactMinis.size(); i > 0; i--) {
					String ita = "@" + jtContactMinis.get(i - 1).getName();
					final TextView forItaTv = creatTagTextViewForIta(ita);
					itaFriendLL.addView(forItaTv, itaFriendLL.getChildCount());
					/*forItaTv.setOnClickListener(new OnClickListener() {//不让点击标签消失了
						@Override
						public void onClick(View v) {
							removeView(forItaTv);
						}
					});*/
				}
				countNum = jtContactMinis.size();
			}
			
			if(InitiatorDataCache.getInstance().forwardingAndSharingOrgMap != null 
					&& InitiatorDataCache.getInstance().forwardingAndSharingOrgMap.size() >=1){
				/*添加组织业务逻辑*/
				Iterator<Entry<String, OrganizationMini>> orgMap = InitiatorDataCache.getInstance().forwardingAndSharingOrgMap.entrySet().iterator();
				mOrganizationMinis.clear();
				while (orgMap.hasNext()) {
					Map.Entry entry = (Map.Entry) orgMap.next();
					OrganizationMini item = (OrganizationMini) entry.getValue();
					mOrganizationMinis.add(item);
					names.add(item.fullName);
					listReceiverId.add(item.getId());
				}
				if(mOrganizationMinis != null && mOrganizationMinis.size() >= 1){
					for (int i = mOrganizationMinis.size(); i > 0; i--) {
						String ita = "@" + mOrganizationMinis.get(i - 1).fullName;
						final TextView forItaTv = creatTagTextViewForIta(ita);
						itaFriendLL.addView(forItaTv, itaFriendLL.getChildCount());
					}
					countNum += mOrganizationMinis.size();
				}
			}
//			(mOrganizationMinis != null && mOrganizationMinis.size() >= 1) || (jtContactMinis != null && jtContactMinis.size() >= 1) ||
			if(countNum > 0){
				chooseFriendsTv.setText("共选中"+ countNum +"位好友");
			}
			else {
				chooseFriendsTv.setText("全部好友");
			}
			break;
		case 200:
			if(intent!=null){
				String before_text = contentEt.getText().toString();
				String after_text = before_text.substring(0, mStart) + LEFTSPECCHAR +"@"+ intent.getStringExtra("at_name") + RIGHTSPECCHAR + before_text.substring(mStart+1, before_text.length());
				contentEt.setText(after_text);
				contentEt.setSelection(contentEt.getText().length());
			}
		break;
		}
		

	}

//	/** 移出好友,同时将好友对象从集合中移除 */
//	private void removeView(TextView tv) {
//		itaFriendLL.removeView(tv);
//		String[] arrayStr = tv.getText().toString().trim().split("@");
//		// names中移除
//		names.remove(arrayStr[1]);
//		for (int i = 0; i < jtContactMinis.size(); i++) {
//			if (jtContactMinis.get(i).getName().equals(arrayStr[1])) {
//				jtContactMinisForRemove.add(jtContactMinis.get(i));
//				listReceiverId.remove(jtContactMinis.get(i).getId());
//			}
//		}
//		jtContactMinis.removeAll(jtContactMinisForRemove);
//	}

	private TextView creatTagTextViewForIta(String str) {
		TextView newTagTv = new TextView(this);
		newTagTv.setText(str);
		newTagTv.setTextColor(Color.rgb(90, 132, 181));
		newTagTv.setTextSize(16);
		newTagTv.setSingleLine(true);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(5, 0, 0, 0);
		newTagTv.setLayoutParams(lp);
		newTagTv.setGravity(Gravity.CENTER);
		newTagTv.setEllipsize(TruncateAt.END);
		newTagTv.setSingleLine(true);
		return newTagTv;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			InitiatorDataCache.getInstance().inviteAttendSelectedMap.clear();
			InitiatorDataCache.getInstance().forwardingAndSharingOrgMap.clear();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.send, menu);
		MenuItem menuSend = menu.findItem(R.id.send);
		menuSend.setVisible(true);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.send) {
			// showToast(contentEt.getText().toString()+listReceiverId.toString());
			// if (jtContactMinisForRemove!=null) {
			// jtContactMinis.removeAll(jtContactMinisForRemove);
			// }
//			if (listReceiverId.size() < 1) {
//				showToast("您未选择好友");
//			} else {
				forwardDynamicNews.listReceiverId  = listReceiverId;
				forwardDynamicNews.forwardingContent = contentEt.getText().toString().trim();
				ConnectionsReqUtil.doAddDynamic(this, this, forwardDynamicNews, null);
				showLoadingDialog();
//			}
		}
		return super.onOptionsItemSelected(item);
	}
	private ArrayList<SmileyView> listSmileyViews;
	private ImageView mSmileyPagerchange;
	class PageViewAdpter extends PagerAdapter {

		@Override
		public int getCount() {
			return listSmileyViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(listSmileyViews.get(position));
			return listSmileyViews.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(listSmileyViews.get(position));
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {

		if (arg0 == 0) {
			((ImageView) findViewById(R.id.smileyPagerchange)).setImageResource(R.drawable.chat_biaoqing_1);
		} else {
			((ImageView) findViewById(R.id.smileyPagerchange)).setImageResource(R.drawable.chat_biaoqing_2);
		}
	}


	@Override
	public void bindData(int tag, Object object) {
		if (tag == EAPIConsts.concReqType.addDynamic) {
			dismissLoadingDialog();
			finish();
			if (object != null) {
				String str = (String) object;
				if (str.equals("true")) {
					showLongToast("发送成功");
				} else {
					showToast("发送失败");
				}
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		InitiatorDataCache.getInstance().inviteAttendSelectedMap.clear();
		InitiatorDataCache.getInstance().forwardingAndSharingOrgMap.clear();
		InitiatorDataCache.getInstance().friendCheckAll = false;
	}

}
