package com.tongmeng.alliance.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.tongmeng.alliance.util.LoadUtil;
import com.tr.R;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.people.homepage.ContactsMainPageActivity;
import com.utils.log.KeelLog;

public class ChangeImgActivity extends JBaseActivity implements OnClickListener {

	int[] images = { R.drawable.bg_0001, R.drawable.bg_0002,
			R.drawable.bg_0003, R.drawable.bg_0004, R.drawable.bg_0005,
			R.drawable.bg_0006 };

	ImageView defindImg;
	TextView defindText, defaltText;
	ListView listview;
	RelativeLayout defindLayout;
	
	MyImageAdapter adapter;
	int choosePosition, tempId;
	List<LinearLayout> imageLsit;
	public static String imageId;

	private ImageView search;
	private TextView myTitle;
	private TextView create_Tv;
	String index = "defalt";
	String imagePath = null;
	Uri uri, mOutPutFileUri;
	String fileName;

	public Dialog dialog;
	Button phonegraph_complete_info, image_complete_info, cancle_complete_info,
			comeinto_main;

	Bitmap photo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_image);
		tempId = getIntent().getIntExtra("imageId", 0);
		KeelLog.e("", "tempId::" + tempId);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		defindText = (TextView) findViewById(R.id.activity_change_image_defindText);
		defindImg = (ImageView) findViewById(R.id.activity_change_defined_showImg);
		defindLayout = (RelativeLayout) findViewById(R.id.activity_change_defined_choose);
		defaltText = (TextView) findViewById(R.id.activity_change_defalt_textview);
		listview = (ListView) findViewById(R.id.activity_change_image_lsitview);
		defaltText.setOnClickListener(this);
		defindText.setOnClickListener(this);

		imageLsit = new ArrayList<LinearLayout>();

		adapter = new MyImageAdapter();
		adapter.setSelect(0);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				choosePosition = position;

				adapter.setSelect(position);
				adapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.activity_change_defalt_textview:
			index = "defalt";
			if (listview.getVisibility() == View.GONE) {
				listview.setVisibility(View.VISIBLE);
			}
			if (defindLayout.getVisibility() == View.VISIBLE) {
				defindLayout.setVisibility(View.GONE);
			}
			break;
		case R.id.activity_change_image_defindText:
			createDialog();
			break;
		case R.id.phonegraph_complete_info:// 拍照
			dialog.dismiss();
			Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent1, 1);
			break;
		case R.id.image_complete_info:
			dialog.dismiss();
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intent, 2);

			break;
		case R.id.cancle_complete_info:
			dialog.dismiss();

			break;

		default:
			break;
		}
	}

	@Override
	public void initJabActionBar() {
		KeelLog.e("ContactsMainPageActivity", "initJabActionBar");
		ActionBar mActionBar = jabGetActionBar();
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(true);
		View mCustomView = getLayoutInflater().inflate(R.layout.org_firstpage_actionbar_title, null);
		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK | Gravity.CENTER_HORIZONTAL;
		mActionBar.setCustomView(mCustomView, mP);
		mActionBar.setTitle(" ");
		myTitle = (TextView) mCustomView.findViewById(R.id.titleTv);
		myTitle.setText("活动封面");
		create_Tv = (TextView) mCustomView.findViewById(R.id.create_Tv);
		create_Tv.setText("确定");
		create_Tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent returnIntent = new Intent();
				returnIntent.putExtra("type", index);
				if (index.equals("defind")) {
					// intent.putExtra("bitmap",
					// ((BitmapDrawable)defindImg.getDrawable()).getBitmap());
					returnIntent.putExtra("path", imagePath + "");
					new Thread() {
						public void run() {
							String result = LoadUtil.multiPart(imagePath,
									ChangeImgActivity.this);
							imageId = LoadUtil.getFileServerInfo(result, "id");
						};
					}.start();

				} else if (index.equals("defalt")) {
					returnIntent.putExtra("imageID", choosePosition + "");
				}
				setResult(RESULT_OK, returnIntent);
				finish();
			}
		});
		search = (ImageView) mCustomView.findViewById(R.id.titleIv);
		search.setVisibility(View.GONE);
	}

	private class MyImageAdapter extends BaseAdapter {

		private int select = -1;

		public void setSelect(int select) {
			this.select = select;
		}

		public int getSelect() {
			return select;
		}

		@Override
		public int getCount() {
			return images.length;
		}

		@Override
		public Object getItem(int position) {
			return position - 1;
		}

		@Override
		public long getItemId(int position) {
			return position - 1;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.e("", "listview加载，当前是第" + position + "个view");
			ViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(ChangeImgActivity.this)
						.inflate(R.layout.activity_change_imagelistitem, null);
				viewHolder.imageview = (ImageView) convertView
						.findViewById(R.id.activity_change_imagelistitem_showImg);
				viewHolder.layout = (LinearLayout) convertView
						.findViewById(R.id.activity_change_imagelistitem_choose);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			if (position == select) {
				viewHolder.layout.setVisibility(View.VISIBLE);
			} else {
				viewHolder.layout.setVisibility(View.GONE);
			}

			viewHolder.imageview.setBackgroundResource(images[position]);
			return convertView;
		}

		private class ViewHolder {
			ImageView imageview;
			LinearLayout layout;
		}
	}

	private void changeImg(int position) {
		for (int i = 0; i < imageLsit.size(); i++) {
			if (i != position) {
				imageLsit.get(i).setVisibility(View.GONE);
				Log.e("", "第" + i + "个隐藏了");
			} else if (i == position) {
				imageLsit.get(i).setVisibility(View.VISIBLE);
				Log.e("", "第" + i + "个显示了---------------");
			}
		}
	}
	
	
	@SuppressWarnings("unused")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		int weight = this.getWindowManager().getDefaultDisplay().getWidth();

		if (requestCode == 1 && resultCode == RESULT_OK) {
			index = "defind";
			if (defindLayout.getVisibility() == View.GONE) {
				defindLayout.setVisibility(View.VISIBLE);
			}
			if (listview.getVisibility() == View.VISIBLE) {
				listview.setVisibility(View.GONE);
			}

			String sdStatus = Environment.getExternalStorageState();
			// ���sd�Ƿ����
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
				Log.v("TestFile",
						"SD card is not avaiable/writeable right now.");
				return;
			}

			Bundle bundle = data.getExtras();
			String str;
			Date date;
			try {
				Bitmap bitmap = (Bitmap) bundle.get("data");
				FileOutputStream b = null;
				File file = new File("/sdcard/myImage/");
				file.mkdirs();
				str = null;
				date = null;

				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd-HH-mm-ss");
				date = new Date();
				str = format.format(date);
				// private static final String IMAGE_PATH = "/sdcard/myImage/";
				fileName = "/sdcard/myImage/" + str + ".jpg";

				try {
					b = new FileOutputStream(fileName);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally {
					try {
						b.flush();
						b.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				imagePath = fileName;
				Log.e("", "相机照片地址imagePath：：：" + imagePath);
				defindImg.setImageBitmap(Bitmap.createScaledBitmap(bitmap,
						weight, 186, true));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if (requestCode == 2 && resultCode == RESULT_OK) {
			if (data == null) {
				Log.e("", "相册返回数据为空");
			} else {
				Log.e("", " 相册data::" + data);

				uri = data.getData();
				index = "defind";
				Log.e("", "uri::" + uri);

				if (defindLayout.getVisibility() == View.GONE) {
					defindLayout.setVisibility(View.VISIBLE);
				}
				if (listview.getVisibility() == View.VISIBLE) {
					listview.setVisibility(View.GONE);
				}
				Bitmap bitmap = null, bitmap1 = null;
				imagePath = LoadUtil.getImageAbsolutePath(
						ChangeImgActivity.this, uri);
				Log.e("", "相册图片地址：：" + imagePath);
				ContentResolver cr = getApplicationContext()
						.getContentResolver();
				try {
					bitmap1 = BitmapFactory.decodeStream(cr
							.openInputStream(uri));
				} catch (Exception e) {
					e.printStackTrace();
				}
				bitmap = Bitmap.createScaledBitmap(bitmap1, weight, 186,
						true);
				defindImg.setImageBitmap(bitmap);
			}
		}
	}

	public void createDialog() {
		dialog = new Dialog(ChangeImgActivity.this, R.style.MyDialogStyle);
		View layout = LayoutInflater.from(this).inflate(
				R.layout.dialog_complete_info, null);
		dialog.setContentView(layout);
		phonegraph_complete_info = (Button) layout
				.findViewById(R.id.phonegraph_complete_info);
		image_complete_info = (Button) layout
				.findViewById(R.id.image_complete_info);
		cancle_complete_info = (Button) layout
				.findViewById(R.id.cancle_complete_info);
		phonegraph_complete_info.setOnClickListener(this);
		image_complete_info.setOnClickListener(this);
		cancle_complete_info.setOnClickListener(this);
		WindowManager m = getWindowManager();
		Display d = m.getDefaultDisplay();

		Window dialogWindow = dialog.getWindow();
		android.view.WindowManager.LayoutParams p = dialogWindow
				.getAttributes(); 
		p.width = (int) (d.getWidth());
		dialogWindow.setGravity(Gravity.BOTTOM);
		dialog.getWindow().setAttributes(p);
		dialog.show();
	}

	/**
	 * 销毁图片文件
	 */
	private void destoryBimap() {
		if (photo != null && !photo.isRecycled()) {
			photo.recycle();
			photo = null;
		}
	}
}
