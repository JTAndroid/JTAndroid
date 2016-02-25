package com.tr.ui.customization;

import java.util.ArrayList;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.utils.common.ApolloUtils;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @ClassName:     CustomizationActivity.java
 * @Description:   个性化定制 选择列表页面
 * @author         xuxinjian
 * @version        V1.0  
 * @Date           2014-3-28 上午8:21:05
 */
public class CustomizationActivity extends JBaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom);
		initListView();
	}
	
	private void initListView() {  
		 //绑定Layout里面的ListView  
        ListView list = (ListView)findViewById(R.id.CustomizationListview);  
          
        //生成动态数组，加入数据  
        ArrayList<MCustomItem> listItem = new ArrayList<MCustomItem>();  
        String[] strTitle = {"定制投资意向", "定制融资意向", "定制机会", "资讯", "动态展示内容"};
        for(int i=0; i<strTitle.length; i++)  
        {  
        	MCustomItem customItem = new MCustomItem();
        	customItem.setTitle(strTitle[i]);
        	customItem.setContent("text" + i);
            listItem.add(customItem);  
        }  
        //生成适配器的Item和动态数组对应的元素  
        CustomAdapter listItemAdapter = new CustomAdapter(this);  
        listItemAdapter.setData(listItem);
        list.setAdapter(listItemAdapter);  
          
        //添加点击单击事件
        list.setOnItemClickListener(new OnItemClickListener() {  
            @Override  
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
                    long arg3) {  
               switch(arg2){
               case 0:
            	   break;
               default:
            	   break;
               }
            }  
        });  
	}  
	
	@Override
	public void initJabActionBar() {
		// 将下拉列表添加到actionbar中
		ActionBar actionbar = jabGetActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowTitleEnabled(true);
		actionbar.setIcon(R.drawable.refresh);// 设置左上角图标
		actionbar.setTitle("返回");// 设置显示标题
	}

	/**
	 * actionbar 中菜单点击事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			//Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.jtmenu_null, menu);
		return true;
	}
	
	@Override
	public void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	/**
	 * @ClassName:     MCustomItem.java
	 * @Description:   个性化定制选择页面元素数据对象
	 */
	public class MCustomItem {
		private String mTitle;
		private String mContent;
		private String mImageUrl;//图片链接地址

		public String getContent() {
			return mContent;
		}

		public void setContent(String content) {
			this.mContent = content;
		}

		public String getTitle() {
			return mTitle;
		}

		public void setTitle(String title) {
			this.mTitle = title;
		}

		public String getmImageUrl() {
			return mImageUrl;
		}

		public void setmImageUrl(String mImageUrl) {
			this.mImageUrl = mImageUrl;
		}
	}

	public class CustomAdapter extends BaseAdapter {

	    private Context mContext;
	    private ArrayList<MCustomItem> mData;

	    public CustomAdapter(Context context) {
	        this.mContext = context;
	        mData = new ArrayList<MCustomItem>();
	    }

	    public void setData(ArrayList<MCustomItem> mData) {
	        this.mData = mData;
	    }

	    @Override
	    public int getCount() {
	        return mData.size();
	    }

	    @Override
	    public Object getItem(int i) {
	        return mData.get(i);
	    }

	    @Override
	    public long getItemId(int i) {
	        return i;
	    }

	    @Override
	    public View getView(final int position, View convertView, final ViewGroup parent) {
	        ItemHolder holder;
	        if (convertView == null) {
	            convertView = LayoutInflater.from(mContext).inflate(R.layout.home_frg_find_listview_item, 
	            		parent, false);
	            holder = new ItemHolder();
	            holder.mTitle = (TextView) convertView.findViewById(R.id.ItemTitle);
	            holder.mContent = (TextView)convertView.findViewById(R.id.ItemText);
	            convertView.setTag(holder);
	        } else {
	            holder = (ItemHolder) convertView.getTag();
	        }

	        // Retrieve the data holder
	        final MCustomItem dataHolder = mData.get(position);
	        holder.mTitle.setText(dataHolder.getTitle());
	        holder.mContent.setText(dataHolder.getContent());
	        ApolloUtils.getImageFetcher((Activity) mContext).loadHomeImage(dataHolder.getmImageUrl(), holder.mImg);
	        return convertView;
	    }

	    private class ItemHolder {
	        public ImageView mImg;
	        public TextView mTitle;
	        public TextView mContent;
	    }
	}

}
