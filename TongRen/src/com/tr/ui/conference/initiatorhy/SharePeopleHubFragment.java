package com.tr.ui.conference.initiatorhy;


import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.tr.R;
import com.tr.model.conference.MExpFriendContact;
import com.tr.model.obj.JTContactMini;
import com.tr.model.user.OrganizationMini;
import com.tr.navigate.ENavConsts;
import com.tr.ui.adapter.conference.ExpListviewSharePeopleHubAdapter;
import com.tr.ui.conference.im.MeetingChatActivity;
import com.tr.ui.widgets.hy.SlideLetterView;
import com.tr.ui.widgets.hy.SlideLetterView.OnTouchingLetterChangedListener;
import com.utils.time.Util;
/**
 * 拓展会议人脉分享页面，兼容组织分享
 * @author thinkpad      modified by zhongshan
 *
 */
public class SharePeopleHubFragment extends Fragment implements OnTouchingLetterChangedListener{
	
	private final String TAG = getClass().getSimpleName();
	
	private Context context;
	private ExpandableListView expLv;
	private ExpListviewSharePeopleHubAdapter expLvAdp;
	private SlideLetterView slideLetterView;
	private TextView showSelLetterView;
	private  List<MExpFriendContact> connectList;
	// add by leon
	private String fromActivity; 
	
	public enum ShareType {
		/** 人脉 */
		people,
		/** 组织 */
		organization
	}
	private ShareType shareType;
	
	public SharePeopleHubFragment(Context context,ShareType sharetype){
		this.context = context;
		this.shareType = sharetype;
	}
	
	// add by leon
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		if (getArguments()!=null) {
			fromActivity = getArguments().getString(ENavConsts.EFromActivityName);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.hy_layout_sharetab_peoplehub, container, false);
		findAndInitViews(v);
		return v;
	}
	private void findAndInitViews(View v){
		slideLetterView = (SlideLetterView)v.findViewById(R.id.hy_layoutShare_peoplehub_slideLetterView);
		showSelLetterView = (TextView)v.findViewById(R.id.hy_layoutShare_peoplehub_showSelLetter);
		slideLetterView.setOnTouchingLetterChangedListener(this);
		showSelLetterView.setVisibility(View.GONE);
		
		expLv = (ExpandableListView)v.findViewById(R.id.hy_layoutShare_peoplehub_Expandlistview);
		
		connectList = ShareType.people.equals(shareType)?InitiatorDataCache.getInstance().sharePeoplehubList:InitiatorDataCache.getInstance().shareOrghubList;
		
		expLvAdp = new ExpListviewSharePeopleHubAdapter(context, connectList);
		
		expLv.setAdapter(expLvAdp);
		expLv.setVerticalScrollBarEnabled(true);
		expLv.setGroupIndicator(null);
		expLv.setOnChildClickListener(new MyExpLvOnChildClickListener());
		expFriLvUnfold();
		forbitGroupClicked();
		expLv.setOnScrollListener(new OnScrollListener(){
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				slideLetterView.setPaintColor(selectTopListViewitems(firstVisibleItem));
			}
		});
	}
	private void expFriLvUnfold(){
		for (int i = 0; i < expLvAdp.getGroupCount(); i++) {
			expLv.expandGroup(i);
		};
	}
	private void forbitGroupClicked() {
		expLv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
					@Override
					public boolean onGroupClick(ExpandableListView parent,
							View v, int groupPosition, long id) {
						// TODO Auto-generated method stub
						return true;
					}
				});
	}
	/***
     * 判断滚动到顶部条目内容所对应的字母
     * @param index 条目索引
     * @return
     */
	private String selectTopListViewitems(int index) {
		int count = 0;
		String str = "";
		List<MExpFriendContact> datalist = expLvAdp.getExpFriendContact();
		if(Util.isNull(datalist)){
			return str;
		}
		for (int i = 0; i < datalist.size(); i++) {
			if (index == count + i) {
				str = datalist.get(i).nameCh;
				return str;
			} else {
				count = datalist.get(i).contactList==null?count + datalist.get(i).organizationList.size():count + datalist.get(i).contactList.size();
				if (count + i >= index) {
					str = datalist.get(i).nameCh;
					return str;
				}
			}
		}
		return str;
	}
	public void update(List<MExpFriendContact> dataList){
		if(expLvAdp != null){
			expLvAdp.update(dataList);
			refreshExpLv();
		}
	}
	public void refreshExpLv() {
		expLvAdp.notifyDataSetChanged();
		expFriLvUnfold();
	}
	@Override
	public void onTouchingLetterChanged(String s, boolean fal) {
		// TODO Auto-generated method stub
		if (fal) {
			showSelLetterView.setVisibility(View.GONE);
		} else {
			int count = 0;
			showSelLetterView.setText(s);
			showSelLetterView.setVisibility(View.VISIBLE);
			List<MExpFriendContact> datalist = expLvAdp.getExpFriendContact();
			if(Util.isNull(datalist)){
				return;
			}
			for (int i = 0; i < datalist.size(); i++) {
				if (datalist.get(i).nameCh.equals(s)) {
					expLv.setSelection(i + count);
					break;
				}
				count = datalist.get(i).contactList==null?count + datalist.get(i).organizationList.size():count + datalist.get(i).contactList.size();
			}
		}
	}
	private class MyExpLvOnChildClickListener implements OnChildClickListener {

		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			
			List<MExpFriendContact> datalist = expLvAdp.getExpFriendContact();
			JTContactMini item =null;
			if (datalist.get(groupPosition).contactList!=null) {
				item = datalist.get(groupPosition).contactList.get(childPosition);
			}
			
			// add by leon
			// 只能单选
			ImageView cb = (ImageView) v.findViewById(R.id.hy_itemInvitefriend_checkbox);
			if(!TextUtils.isEmpty(fromActivity)
					&& fromActivity.equals(MeetingChatActivity.class.getSimpleName())&&item!=null){
				if(InitiatorDataCache.getInstance().sharePeopleHubSelectedMap.containsKey(item.id)){
					cb.setImageResource(R.drawable.hy_check_norm);
					InitiatorDataCache.getInstance().sharePeopleHubSelectedMap.remove(item.id);
				}else{
					if(InitiatorDataCache.getInstance().sharePeopleHubSelectedMap.isEmpty()){
						cb.setImageResource(R.drawable.hy_check_pressed);
						InitiatorDataCache.getInstance().sharePeopleHubSelectedMap.put(item.id, item);
					}else{
						InitiatorDataCache.getInstance().sharePeopleHubSelectedMap.clear();
						cb.setImageResource(R.drawable.hy_check_pressed);
						InitiatorDataCache.getInstance().sharePeopleHubSelectedMap.put(item.id, item);
						expLvAdp.notifyDataSetChanged();
					}
				}
			}else if(item!=null){
				
				if(InitiatorDataCache.getInstance().sharePeopleHubSelectedMap.containsKey(item.id)){
					cb.setImageResource(R.drawable.hy_check_norm);
					InitiatorDataCache.getInstance().sharePeopleHubSelectedMap.remove(item.id);
				}else{
					cb.setImageResource(R.drawable.hy_check_pressed);
					InitiatorDataCache.getInstance().sharePeopleHubSelectedMap.put(item.id, item);
				}
			}else if(item==null){
				if (datalist.get(groupPosition).organizationList!=null) {
					OrganizationMini organizationItem = datalist.get(groupPosition).organizationList.get(childPosition);
					if(InitiatorDataCache.getInstance().shareOrgHubSelectedMap.containsKey(organizationItem.id)){
						cb.setImageResource(R.drawable.hy_check_norm);
						InitiatorDataCache.getInstance().shareOrgHubSelectedMap.remove(organizationItem.id);
					}else{
						cb.setImageResource(R.drawable.hy_check_pressed);
						InitiatorDataCache.getInstance().shareOrgHubSelectedMap.put(organizationItem.id, organizationItem);
					}
				}
			}
			return true;
		}
		
	}
}
