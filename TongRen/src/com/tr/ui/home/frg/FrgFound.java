package com.tr.ui.home.frg;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.tr.R;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.conference.home.MeetingPiazzaActivity;

/**
 * @Filename MainActivity.java
 * @Author CJJ
 * @Date 2015-1-12
 * @description 发现
 */
public class FrgFound extends JBaseFragment implements OnClickListener {
	private final String TAG = "FrgFound";
	/* 网格布局存储 */
	private ListView activity_index_list;
	private String mGvTexts[] = null;
	private int images[] = null;
	private SimpleAdapter mGvAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_frg_found, container, false);
		activity_index_list = (ListView) view.findViewById(R.id.activity_index_list);
		setHasOptionsMenu(true);

		images = new int[] {
				R.drawable.gridview_item_people_bg,
				R.drawable.gridview_item_organization_bg,
				R.drawable.gridview_item_know_bg,
				R.drawable.gridview_item_project_bg,//需求
				R.drawable.gridview_item_meet_bg,};

		mGvTexts = new String[] { "人脉" ,"组织" ,"知识" , "需求" ,"会议"};

//		ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
//		for (int i = 0; i < mGvTexts.length; i++) {
//			HashMap<String, Object> map = new HashMap<String, Object>();
//			map.put("itemImage", images[i]);
//			map.put("itemText", mGvTexts[i]);
//			lstImageItem.add(map);
//		}
//		String[] from = new String[] { "itemImage", "itemText" };
//		int[] to = new int[] { R.id.itemImage, R.id.itemText };
//		mGvAdapter = new SimpleAdapter(getActivity(), lstImageItem,R.layout.home_frg_found_item, from, to);
		activity_index_list.setAdapter(mybBaseAdapter);
		activity_index_list.setOnItemClickListener(new OnItemClickListener() {
			/**
			 * 点击项时触发事件
			 * 
			 * @param parent 发生点击动作的AdapterView
			 * @param view 在AdapterView中被点击的视图(它是由adapter提供的一个视图)。
			 * @param position 视图在adapter中的位置
			 * @param rowid 被点击元素的行id。
			 */
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// 根据图片进行相应的跳转
				switch (images[arg2]) {
					//知识
				case R.drawable.gridview_item_know_bg:
					ENavigate.startKnowledgeSquareActivity(getActivity());
					break;
					//会议
				case R.drawable.gridview_item_meet_bg:
					startActivity(new Intent(getActivity(), MeetingPiazzaActivity.class));
					break;
					//组织
				case R.drawable.gridview_item_organization_bg:
					ENavigate.startOrganizationFirstPageActivity(getActivity());
					break;
				case R.drawable.gridview_item_project_bg:
					//需求
					ENavigate.startFindDemandActivity(getActivity(),5);
					break;
					//人脉
				case R.drawable.gridview_item_people_bg:
					ENavigate.startFindPeopleActivity(getActivity(),1);
					break;
				}
			}
		});
		return view;
	}

	class ItemClickListener implements OnItemClickListener {
		/**
		 * 点击项时触发事件
		 * 
		 * @param parent 发生点击动作的AdapterView
		 * @param view 在AdapterView中被点击的视图(它是由adapter提供的一个视图)。
		 * @param position 视图在adapter中的位置
		 * @param rowid 被点击元素的行id。
		 */
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long rowid) {
			HashMap<String, Object> item = (HashMap<String, Object>) parent
					.getItemAtPosition(position);
			// 获取数据源的属性值
			String itemText = (String) item.get("itemText");
			Object object = item.get("itemImage");

			// 根据图片进行相应的跳转
			switch (images[position]) {
			case R.drawable.gridview_item_know_bg:
				ENavigate.startKnowledgeSquareActivity(getActivity());
				break;
			case R.drawable.gridview_item_meet_bg:
				break;
			case R.drawable.gridview_item_organization_bg:
				ENavigate.startOrganizationFirstPageActivity(getActivity());
				break;
			case R.drawable.gridview_item_funds_bg:
				//找资金
				ENavigate.startFindPricesActivity(getActivity(),2);
				break;
			case R.drawable.gridview_item_project_bg:
				//找项目
				ENavigate.startFindDemandActivity(getActivity(),5);
				break;
			case R.drawable.gridview_item_people_bg:
				ENavigate.startFindPeopleActivity(getActivity(),1);
				break;
			}
		}
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.findItem(R.id.home_new_menu_search).setVisible(false);
		menu.findItem(R.id.home_new_menu_more).setVisible(false);
		
	}

	@Override
	public void onClick(View arg0) {

	}
	
	class ViewHolder{
		TextView tv_item;
		ImageView img_item;
		protected View partitionView;
	}
	
	
	BaseAdapter mybBaseAdapter = new BaseAdapter() {
		
		@Override
		public int getCount() {
			return mGvTexts.length;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder holder;
			if (convertView != null) {
				view = convertView;
				holder = (ViewHolder) convertView.getTag();
			} else {
				view = View.inflate(getActivity(), R.layout.home_frg_found_list_item, null);
				holder = new ViewHolder();
				
				holder.tv_item = (TextView) view.findViewById(R.id.tv_item);
				holder.img_item = (ImageView) view.findViewById(R.id.img_item);
				holder.partitionView = view.findViewById(R.id.partitionView);
				view.setTag(holder);
			}
			if (position==2) {
				holder.partitionView.setVisibility(View.VISIBLE);
			}else {
				holder.partitionView.setVisibility(View.GONE);
			}
			holder.tv_item.setText(mGvTexts[position]);
			holder.img_item.setImageResource(images[position]);
			
			return view;
		}
		
		@Override
		public long getItemId(int position) {
			return 0;
		}
		
		@Override
		public Object getItem(int position) {
			return null;
		}
		
	};
	
}
