package com.tr.ui.organization.create_clientele;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tr.R;
import com.tr.api.OrganizationReqUtil;
import com.tr.ui.organization.model.hight.CustomerHight_re;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.organization.model.stock.CustomerStock;
import com.tr.ui.organization.model.stock.CustomerStockInfo;
import com.tr.ui.organization.model.stock.CustomerTenStock;
import com.tr.ui.organization.model.template.Title;
import com.tr.ui.organization.orgdetails.Edit_Shareholder_Activity;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.utils.MakeListView;
import com.tr.ui.people.cread.utils.Utils;
import com.tr.ui.people.cread.view.MyGridView;
import com.utils.http.EAPIConsts.OrganizationReqType;
import com.utils.http.IBindData;
/**
 * 股东研究
 * @author Wxh07151732
 *
 */
public class ShareholderActivity extends BaseActivity implements IBindData{
	private ImageView edit_shareholder_Tv;
	private RelativeLayout quit_shareholder_Rl;
	
	    //图片的文字标题 
	    private String[] titles = new String[] 
	    { "股东名称", "持股数量（万股）", "增减股数", 
	      
	       };
		private LinearLayout iPOSHODER;
		private LinearLayout floating_stockholder;
		private TextView tv_guname;
		private TextView tv_gusum;
		private TextView tv_jishi_name;
		private TextView tv_jishi_gusum;
		private TextView tv_zuizhong_guname;
		private TextView tv_zuizhong_gusum;
		private ListView listView1;
		private ListView listView2;
		private CustomerStock stock2;
		private ArrayList<CustomerPersonalLine> custom;
		private ListView stockholder_custom;
		private static ListView konggu_Lv;
		private LinearLayout shareholder_Ll;
		
		

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_shareholder);
		edit_shareholder_Tv = (ImageView) findViewById(R.id.edit_shareholder_Tv);
		CustomerHight_re customerHight_re = new CustomerHight_re();
		OrganizationReqUtil.doRequestWebAPI(context, this, customerHight_re, null, OrganizationReqType.ORGANIZATION_REQ_FINDSTOCKONE);
		
		MyGridView gridView1 = new MyGridView(context);
		gridView1.setNumColumns(3);
		MyGridView gridView2 = new MyGridView(context);
		gridView2.setNumColumns(3);
		PictureAdapter adapter = new PictureAdapter();
		gridView1.setAdapter(adapter);
		gridView2.setAdapter(adapter);
		iPOSHODER = (LinearLayout) findViewById(R.id.IPOSHODER);
		iPOSHODER.addView(gridView1);
		
		listView1 = new ListView(context);
		iPOSHODER.addView(listView1);
		
		
//		shareholder_Ll = (LinearLayout) findViewById(R.id.shareholder_Ll);
		tv_guname = (TextView) findViewById(R.id.tv_guname);
		tv_gusum = (TextView) findViewById(R.id.tv_gusum);
		tv_jishi_name = (TextView) findViewById(R.id.tv_jishi_name);
		tv_jishi_gusum = (TextView) findViewById(R.id.tv_jishi_gusum);
		tv_zuizhong_guname = (TextView) findViewById(R.id.tv_zuizhong_guname);
		tv_zuizhong_gusum = (TextView) findViewById(R.id.tv_zuizhong_gusum);
		
		stockholder_custom = (ListView) findViewById(R.id.stockholder_custom);
		konggu_Lv = (ListView) findViewById(R.id.konggu_Lv);
		
		floating_stockholder = (LinearLayout) findViewById(R.id.floating_stockholder);
		floating_stockholder.addView(gridView2);
		listView2 = new ListView(context);
		floating_stockholder.addView(listView2);
		quit_shareholder_Rl = (RelativeLayout) findViewById(R.id.quit_shareholder_Rl);
		quit_shareholder_Rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}
	@Override
	public void bindData(int tag, Object object) {
		Map<String,Object> map = (Map<String, Object>) object;
		CustomerStock stock  =(CustomerStock) map.get("stock");
		if (stock!=null) {
			tv_guname.setText(stock.cShareholder);
			tv_gusum.setText(stock.cStockPercent);
			tv_jishi_name.setText(stock.rShareholder);
			tv_jishi_gusum.setText(stock.rStockPercent);
			tv_zuizhong_guname.setText(stock.fShareholder);
			tv_zuizhong_gusum.setText(stock.fStockPercent);
			stock2 =stock;
		}
		MakeListView.Custom(context, stockholder_custom,stock.personalLineList);
		BaseAdapter adapter1 =(BaseAdapter) stockholder_custom.getAdapter();
		adapter1.notifyDataSetChanged();
		ArrayList<CustomerStockInfo> tenStock = (ArrayList<CustomerStockInfo>) map.get("tenStockList");
		if (tenStock!=null) {
			AdaptiveListView(MakeListView.Stock(this,listView1,tenStock),(int)Utils.convertDpToPixel(42));
		}
		ArrayList<CustomerStockInfo> Stock = (ArrayList<CustomerStockInfo>) map.get("ltStockList");
		if (Stock!=null) {
			AdaptiveListView(MakeListView.Stock(this,listView2,Stock),(int)Utils.convertDpToPixel(42));
		}
	} 
	public void edit(View view){
		Intent intent = new Intent(getApplicationContext(), Edit_Shareholder_Activity.class);
		Bundle bundle = new Bundle();
//		bundle.putSerializable("Edit_Shareholder_Bean", stock2);
		bundle.putSerializable("Custom_Bean", custom);
		bundle.putParcelable("Edit_Shareholder_Bean", stock2);
		intent.putExtras(bundle);
		startActivityForResult(intent,0);
	}
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg2!=null) {
			if (arg1==0) {
				stock2 = (CustomerStock) arg2.getSerializableExtra("Edit_Shareholder_Bean");
				custom = (ArrayList<CustomerPersonalLine>) arg2.getSerializableExtra("Custom_Bean");
				
					runOnUiThread(new Runnable() {
					

					@Override
					public void run() {
						if (stock2!=null) {
							tv_guname.setText(stock2.cShareholder);
							tv_gusum.setText(stock2.cStockPercent);
							tv_jishi_name.setText(stock2.rShareholder);
							tv_jishi_gusum.setText(stock2.rStockPercent);
							tv_zuizhong_guname.setText(stock2.fShareholder);
							tv_zuizhong_gusum.setText(stock2.fStockPercent);
						}
						if (custom!=null) {
							
							MakeListView.Custom(context, stockholder_custom,custom);
							BaseAdapter adapter1 =(BaseAdapter) stockholder_custom.getAdapter();
							adapter1.notifyDataSetChanged();
						}
					}
				});
			}
			
		}
	}
	
	//自定义适配器 
	class PictureAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return titles.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			 ViewHolder viewHolder; 
		      if (convertView == null) 
		      { 
		          convertView = View.inflate(ShareholderActivity.this,R.layout.ten_gridview_item, null); 
		          viewHolder = new ViewHolder(); 
		          viewHolder.title = (TextView) convertView.findViewById(R.id.title); 
		          convertView.setTag(viewHolder); 
		      } else { 
		          viewHolder = (ViewHolder) convertView.getTag(); 
		      } 
		      viewHolder.title.setText(titles[position]);
		      return convertView; 
		} 

	  } 

	

	class ViewHolder 
	{ 
	  public TextView title; 
	  public TextView tv_img_su; 
	}



	

}
