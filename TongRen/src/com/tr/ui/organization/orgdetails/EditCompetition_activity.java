package com.tr.ui.organization.orgdetails;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.tr.R;
import com.tr.ui.organization.create_clientele.EnterpriseActivity;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.CustomActivity;
import com.tr.ui.people.cread.utils.Utils;
import com.tr.ui.people.cread.view.MyAddMordView;
import com.tr.ui.people.cread.view.MyDeleteView;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.people.cread.view.MyLineraLayout;
import com.tr.ui.people.cread.view.WordWrapView;

/**
 * 同业竞争 编辑页面
 * 
 * @author User
 * 
 */
public class EditCompetition_activity extends BaseActivity implements
		OnClickListener {

	private RelativeLayout quit_competition_Rl;
	private TextView tv_continun_add;
	private TextView tv_edcom;
	private TextView tv_edcom_name;
	private LinearLayout delete_Ll;
	private ArrayList<String> list;
	private ArrayList<String> list1;
	private ArrayList<MyEditTextView> mEditViewlist;
	private ArrayList<MyLineraLayout> mLineralist;
	private LinearLayout editcompetition_main_Ll;
	private int continueAdd2;
	private int count=0;
	private LinearLayout continueadd_Ll;
	private MyEditTextView company_name_Etv;
	private MyEditTextView label_Etv;
	private TextView tv_edcom2;
	private TextView tv_label;
	private LinearLayout ll_label;
	private MyDeleteView institution_delete_Mdv;
	private LinearLayout editcompetition_Ll;
	private MyAddMordView editcompetition_MAMV;
	private String[] strs = {"推荐岁id卡到爆比较卡是必备道具卡视角看snna","三啊肯德基拉萨空间","接受的话好卡","帮你吧","内部教案看","斯达"};
	private MyEditTextView label_Etv2;
	private LinearLayout ll_competition;
	private MyEditTextView org_competition_Etv;
	private boolean isNull;
	private ArrayList<MyEditTextView> editTextViews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_edit_competition);
		initUI();
		editTextViews = new ArrayList<MyEditTextView>();
		list = new ArrayList<String>();
		list1 = new ArrayList<String>();
		mEditViewlist = new ArrayList<MyEditTextView>();
		mLineralist = new ArrayList<MyLineraLayout>();
		
		quit_competition_Rl.setOnClickListener(this);
		initData();

	}
	private void initData() {
		final WordWrapView wordWrapView = new WordWrapView(this);
		 ImageView imageView = new ImageView(this);
		 imageView.setBackgroundResource(R.drawable.org_wholeaddlabel);
		 LayoutParams layoutParams =new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		 layoutParams.leftMargin=(int)Utils.convertDpToPixel(20);
		 layoutParams.bottomMargin=(int)Utils.convertDpToPixel(10);
		 layoutParams.topMargin =(int) Utils.convertDpToPixel(10);
		 imageView.setOnClickListener(new OnClickListener() {
			
			private AlertDialog create;
			private TextView alertdialog_Tv;
			private TextView alertdialog_Yes;
			private TextView alertdialog_No;
			private EditText alertdialog_Et;

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new Builder(EditCompetition_activity.this);
				View view2 = View.inflate(EditCompetition_activity.this, R.layout.people_alertdialog_module, null);
				builder.setView(view2);
				create = builder.create();
				alertdialog_Tv = (TextView) view2.findViewById(R.id.alertdialog_module_Tv);
				alertdialog_Yes = (TextView) view2.findViewById(R.id.alertdialog_module_Yes);
				alertdialog_No = (TextView) view2.findViewById(R.id.alertdialog_module_No);
				alertdialog_Et = (EditText) view2.findViewById(R.id.alertdialog_module_Et);
				alertdialog_Tv.setText("添加新的标签");	
				alertdialog_Yes.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String text = alertdialog_Et.getText().toString().trim();
						TextView view = new TextView(EditCompetition_activity.this);
						view.setText(text);
						wordWrapView.addView(view);
						create.dismiss();
					}
				});
				alertdialog_No.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						create.dismiss();
					}
				});
				create.show();
			}
		});
		 View view = new View(this);
		 view.setBackgroundColor(Color.LTGRAY);
		
		 editcompetition_Ll.addView(wordWrapView,editcompetition_Ll.indexOfChild(label_Etv)+1);
		 editcompetition_Ll.addView(imageView,editcompetition_Ll.indexOfChild(label_Etv)+2,layoutParams);
		 editcompetition_Ll.addView(view,editcompetition_Ll.indexOfChild(label_Etv)+3,new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, 1));
	}

	private void initUI() {
		ll_competition = (LinearLayout) findViewById(R.id.ll_competition);
		quit_competition_Rl = (RelativeLayout) findViewById(R.id.quit_competition_Rl);
		TextView continueadd_Tv = (TextView) findViewById(R.id.continueadd_Tv);
		continueadd_Ll = (LinearLayout) findViewById(R.id.continueadd_Ll);
		continueadd_Tv.setText("新增竞争企业");
//        tv_edcom = (TextView) findViewById(R.id.tv_edcom);
        company_name_Etv = (MyEditTextView) findViewById(R.id.company_name_Etv);
        label_Etv = (MyEditTextView) findViewById(R.id.label_Etv);
        org_competition_Etv = (MyEditTextView) findViewById(R.id.org_competition_Etv);
        label_Etv.setReadOnly(false);
        editcompetition_MAMV = (MyAddMordView) findViewById(R.id.editcompetition_MAMV);
        company_name_Etv.setReadOnly(true);
        delete_Ll = (LinearLayout) findViewById(R.id.delete_Ll);
        editcompetition_main_Ll = (LinearLayout) findViewById(R.id.editcompetition_main_Ll);
        editcompetition_Ll = (LinearLayout) findViewById(R.id.editcompetition_Ll);
        institution_delete_Mdv = (MyDeleteView) findViewById(R.id.edit_comppetition_delete_Mdv);
        
        org_competition_Etv.setOnClickListener(this);
        institution_delete_Mdv.setOnClickListener(this);
        editcompetition_MAMV.setOnClickListener(this);
	}
    public void save(View v){
    	if (!TextUtils.isEmpty(company_name_Etv.getText())) {
			list.add(company_name_Etv.getTextLabel()+"_"+company_name_Etv.getText());
		}
    	if (!mEditViewlist.isEmpty()) {
			for (int i = 0; i < mEditViewlist.size(); i++) {
				MyEditTextView myEditTextView = mEditViewlist.get(i);
				if(!TextUtils.isEmpty(myEditTextView.getText())){
					list.add(myEditTextView.getTextLabel()+"_"+myEditTextView.getText());
				}
			}
		}
    	finish();
    }
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.quit_competition_Rl:
			finish();
			break;
		case R.id.org_competition_Etv:
			Intent intent = new Intent(this,CustomActivity.class);
			startActivityForResult(intent, 0);
			break;
		case R.id.edit_comppetition_delete_Mdv:
			if (!mLineralist.isEmpty()) {	
				editcompetition_main_Ll.removeAllViews();
			} else {
				finish();
			}		
			break;
		case R.id.editcompetition_MAMV:
			list1.add(company_name_Etv.getTextLabel());
			list1.add(label_Etv.getTextLabel());
			list1.add(org_competition_Etv.getTextLabel());
			final MyLineraLayout layout = new MyLineraLayout(context);
			layout.setOrientation(LinearLayout.VERTICAL);
			MyDeleteView myDeleteView = new MyDeleteView(context);
			for (int i = 0; i < list1.size(); i++) {
				final MyEditTextView editTextView = new MyEditTextView(context);
				if (i==1) {
					editTextView.setReadOnly(true);
				}
				if (i==2) {
					editTextView.setChoose(true);
					editTextView.setAddMore_hint(true);
					editTextView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(EditCompetition_activity.this,CustomActivity.class);
							intent.putExtra("Edit_competition_ID", editTextView.MyEdit_Id);
							startActivityForResult(intent, 1);
						}
					});
				}
				editTextView.setTextLabel(list1.get(i));
				layout.addView(editTextView);
				 mDictionary.put(editTextView.MyEdit_Id, editTextView);
				 mLineraDictionary.put(editTextView.MyEdit_Id, layout);
			}
			
			final WordWrapView wordWrapView = new WordWrapView(this);
			 ImageView imageView = new ImageView(this);
			 imageView.setBackgroundResource(R.drawable.org_wholeaddlabel);
			 LayoutParams layoutParams =new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			 layoutParams.leftMargin=(int)Utils.convertDpToPixel(20);
			 layoutParams.bottomMargin=(int)Utils.convertDpToPixel(10);
			 layoutParams.topMargin =(int) Utils.convertDpToPixel(10);
			 imageView.setOnClickListener(new OnClickListener() {
				
				private AlertDialog create;
				private TextView alertdialog_Tv;
				private TextView alertdialog_Yes;
				private TextView alertdialog_No;
				private EditText alertdialog_Et;

				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new Builder(EditCompetition_activity.this);
					View view2 = View.inflate(EditCompetition_activity.this, R.layout.people_alertdialog_module, null);
					builder.setView(view2);
					create = builder.create();
					alertdialog_Tv = (TextView) view2.findViewById(R.id.alertdialog_module_Tv);
					alertdialog_Yes = (TextView) view2.findViewById(R.id.alertdialog_module_Yes);
					alertdialog_No = (TextView) view2.findViewById(R.id.alertdialog_module_No);
					alertdialog_Et = (EditText) view2.findViewById(R.id.alertdialog_module_Et);
					alertdialog_Tv.setText("添加新的标签");	
					alertdialog_Yes.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							String text = alertdialog_Et.getText().toString().trim();
							TextView view = new TextView(EditCompetition_activity.this);
							view.setText(text);
							wordWrapView.addView(view);
							create.dismiss();
						}
					});
					alertdialog_No.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							create.dismiss();
						}
					});
					create.show();
				}
			});
			 myDeleteView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					layout.removeAllViews();
				}
			});
			 View view = new View(this);
			 view.setBackgroundColor(Color.LTGRAY);
			
			 layout.addView(wordWrapView,3);
			 layout.addView(imageView,4,layoutParams);
			 layout.addView(view,5,new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, 1));
			 layout.addView(myDeleteView);
			
			 mLineralist.add(layout);
			 editcompetition_main_Ll.addView(layout);
			 list1.removeAll(list1);
			break;
		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg2!=null) {
			if (arg0==0) {
				switch (arg1) {
				case 999:
					custom2(arg2, org_competition_Etv, editcompetition_Ll, isNull,editTextViews);
					break;

				default:
					break;
				}
			}
			if (arg0==1) {
				String Edit_competition_ID = arg2.getStringExtra("Edit_competition_ID");
				switch (arg1) {
				case 999:
					custom2(arg2, mDictionary.get(Edit_competition_ID), mLineraDictionary.get(Edit_competition_ID), isNull,editTextViews);
					break;

				default:
					break;
				}
			}
		}
		
	}
	}
