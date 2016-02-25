package com.tr.ui.demand;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.tr.R;
import com.tr.model.obj.JTFile;
import com.tr.navigate.ENavConsts;
import com.tr.ui.base.JBaseActivity;

/**
 * 上传头像,与多张选择分离（SelectPictureActivity）
 * 
 * @author zp
 * 
 */

public class HeadPortraitActivity extends JBaseActivity {

	private ArrayList<JTFile> selectedPicture = new ArrayList<JTFile>();
	/**
	 * 存储图片组件的集合
	 */
	private ArrayList<ImageFloder> mDirPaths = new ArrayList<ImageFloder>();
	/**
	 * 临时的辅助类，用于防止同一个文件夹的多次扫描
	 */
	private HashMap<String, Integer> tmpDir = new HashMap<String, Integer>();
	private static final int TAKE_PICTURE = 0;
	private MenuItem findItem;
	private Context context;
	private ContentResolver mContentResolver;
	private ImageLoader loader;
	private DisplayImageOptions options;
	private PictureAdapter adapter;
	private GridView gridview;
	private Button btn_select;
	private String cameraPath;
	private ImageFloder imageAll;
	private ImageFloder currentImageFolder;
	private ListView listview;
	private FolderAdapter folderAdapter;
	private TextView previewTv;
	public static int selectPosition = -1;
	private JTFile paths;
	private JTFile jPath = new JTFile();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demand_select_avator);
		context = this;
		mContentResolver = getContentResolver();
		loader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
		// 设置图片在加载中显示的图片
				.showImageOnLoading(R.drawable.demand_defaultimg)
				// 设置图片Uri为空或是错误的时候显示的图片
				.showImageForEmptyUri(R.drawable.demand_defaultimg)
				// 设置图片加载/解码过程中错误时候显示的图片
				.showImageOnFail(R.drawable.demand_defaultimg)
				// 设置下载的图片是否缓存在内存中
				.cacheInMemory(true)
				// 设置下载的图片是否缓存在SD卡中
				// .cacheOnDisk(true)
				// 保留Exif信息
				.considerExifParams(false)
				// 设置图片以如何的编码方式显示
				.imageScaleType(ImageScaleType.EXACTLY)
				// 设置图片的解码类型
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		initView();
	}

	// 点击选择目录按钮
	public void select(View v) {
		if (listview.getVisibility() == 0) {
			hideListAnimation();
		} else {
			listview.setVisibility(0);
			showListAnimation();
			folderAdapter.notifyDataSetChanged();
		}
	}

	// 目录菜单show动画
	private void showListAnimation() {
		TranslateAnimation ta = new TranslateAnimation(1, 0f, 1, 0f, 1, 1f, 1,
				0f);
		ta.setDuration(200);
		listview.startAnimation(ta);
	}

	// 目录菜单隐藏动画
	private void hideListAnimation() {
		TranslateAnimation ta = new TranslateAnimation(1, 0f, 1, 0f, 1, 0f, 1,
				1f);
		ta.setDuration(200);
		listview.startAnimation(ta);
		ta.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				listview.setVisibility(8);
			}
		});
	}

	// 菜单
	class FolderViewHolder {
		ImageView id_dir_item_image;
		ImageView choose;
		TextView id_dir_item_name;
		TextView id_dir_item_count;
	}

	// 目录菜单ListView的适配器
	class FolderAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mDirPaths.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FolderViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(context,
						R.layout.list_dir_item_demand, null);
				holder = new FolderViewHolder();
				holder.id_dir_item_image = (ImageView) convertView
						.findViewById(R.id.id_dir_item_image);
				holder.id_dir_item_name = (TextView) convertView
						.findViewById(R.id.id_dir_item_name);
				holder.id_dir_item_count = (TextView) convertView
						.findViewById(R.id.id_dir_item_count);
				holder.choose = (ImageView) convertView
						.findViewById(R.id.choose);
				convertView.setTag(holder);
			} else {
				holder = (FolderViewHolder) convertView.getTag();
			}
			ImageFloder item = mDirPaths.get(position);
			loader.displayImage("file://" + item.getFirstImagePath(),
					holder.id_dir_item_image, options);
			holder.id_dir_item_count.setText(item.images.size() + "张");
			holder.id_dir_item_name.setText(item.getName());
			holder.choose.setVisibility(currentImageFolder == item ? 0 : 8);
			return convertView;
		}
	}

	// 初始化数据
	@SuppressWarnings("unchecked")
	private void initView() {
		// 先获取 到传过来的头像
		// jPath = (JTFile) getIntent().getSerializableExtra(
		// ENavConsts.DEMAND_INTENT_SELECTED_PICTURE);se
		// System.out.println("1、是否之前有头像--路径--" + jPath);
		// if (jPath != null) {
		// selectedPicture.add(jPath);
		// } else {
		// System.out.println("之前头像为空---");
		// }

		selectPosition = -1;// 否则会造成选中后，返回，再点完成的白屏空指针异常

		imageAll = new ImageFloder();
		imageAll.setDir("/所有图片");
		currentImageFolder = imageAll;
		mDirPaths.add(imageAll);
		btn_select = (Button) findViewById(R.id.btn_select);

		gridview = (GridView) findViewById(R.id.gridview);
		adapter = new PictureAdapter();
		gridview.setAdapter(adapter);

		gridview.setOnItemClickListener(new OnItemClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					goCamare();
				} else {

					final int positiontemp = position - 1;

					paths = currentImageFolder.images.get(positiontemp);
					File file = new File(paths.mLocalFilePath);
					if (!(paths.mType == JTFile.TYPE_FILE)) {
						if ((file.length() / 1024) > 1024 * 5) {
							Toast.makeText(context, "您选的图片太大了超过了5M",
									Toast.LENGTH_SHORT).show();
							return;
						}
					}

					selectPosition = position;
					// paths.mCreateTime = System.currentTimeMillis();
					adapter.notifyDataSetChanged();
				}
			}
		});
		// 获取图片 把缩略图封装到图片组件里面去
		getThumbnail(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 1);

		listview = (ListView) findViewById(R.id.listview);
		folderAdapter = new FolderAdapter();
		listview.setAdapter(folderAdapter);


		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				currentImageFolder = mDirPaths.get(position);

				hideListAnimation();
				adapter.notifyDataSetChanged();
				btn_select.setText(currentImageFolder.getName());
			}
		});
		tmpDir = null;
	}

	/**
	 * 获取本地的图片地址封装起来
	 * 
	 * @param uri
	 * @param isVideo
	 *            ：如果为1代表图片，如果值为2代表视频
	 */
	private void getThumbnail(Uri uri, int isVideo) {

		String projectionArgs = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
				.equals(uri) ? MediaStore.Images.ImageColumns.DATA
				: MediaStore.Video.VideoColumns.DATA;

		Cursor mCursor = mContentResolver.query(uri,
				new String[] { projectionArgs }, "", null,
				MediaStore.MediaColumns.DATE_ADDED + " DESC");
		if (mCursor.moveToFirst()) {

			String columnName = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
					.equals(uri) ? MediaStore.Images.Media.DATA
					: MediaStore.Video.Media.DATA;

			int _date = mCursor.getColumnIndex(columnName);
			do {
				// 获取图片/视频的路径
				String path = mCursor.getString(_date);
				JTFile jtFile = new JTFile();
				jtFile.mLocalFilePath = path;
				// jtFile.mType = isVideo;
				// jtFile.mFileName = path;
				currentImageFolder.images.add(jtFile);
				// 获取该图片的父路径名
				File parentFile = new File(path).getParentFile();
				if (parentFile == null) {
					continue;
				}
				ImageFloder imageFloder = null;
				String dirPath = parentFile.getAbsolutePath();
				if (!tmpDir.containsKey(dirPath)) {
					// 初始化imageFloder
					imageFloder = new ImageFloder();
					imageFloder.setDir(dirPath);
					imageFloder.setFirstImagePath(path);
					mDirPaths.add(imageFloder);
					tmpDir.put(dirPath, mDirPaths.indexOf(imageFloder));
				} else {
					imageFloder = mDirPaths.get(tmpDir.get(dirPath));
				}
				imageFloder.images.add(jtFile);
			} while (mCursor.moveToNext());
		}
		mCursor.close();
	}

	// 启动相机
	protected void goCamare() {
		selectedPicture.clear();
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Uri imageUri = getOutputMediaFileUri();
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	// 相机的回调
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && cameraPath != null) {
			// 防止空指针异常
			if (selectedPicture == null) {
				selectedPicture = new ArrayList<JTFile>();
			}
			JTFile jtFile = new JTFile();
			jtFile.mLocalFilePath = cameraPath;
			// jtFile.mFileName = cameraPath;
			// jtFile.mType = JTFile.TYPE_FILE;
			selectedPicture.add(jtFile);
			System.out.println("相机回调-----" + selectedPicture);
			Intent data2 = new Intent();
			data2.putExtra(ENavConsts.DEMAND_INTENT_SELECTED_PICTURE,
					selectedPicture);
			setResult(RESULT_OK, data2);
			this.finish();
		}
	}

	private Uri getOutputMediaFileUri() {
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"Night");
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_" + timeStamp + ".jpg");
		cameraPath = mediaFile.getAbsolutePath();
		return Uri.fromFile(mediaFile);
	}

	// 先是缩略图的 Gridview适配器
	@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
	@SuppressLint("NewApi")
	class PictureAdapter extends BaseAdapter {

		boolean tag = false;

		@Override
		public int getCount() {
			// 显示的个数，如果是所有图片的时候那么就显示拍照，不是所有图片和视频的时候就不显示拍照
			return currentImageFolder.getName().contains("所有图片") ? currentImageFolder.images
					.size() + 1 : currentImageFolder.images.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@SuppressLint("NewApi")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(context,
						R.layout.demand_grid_item_picture, null);
				holder = new ViewHolder();
				// 显示图片
				holder.iv = (ImageView) convertView.findViewById(R.id.iv);
				// 第一个条目要显示的拍照
				holder.iv2 = (LinearLayout) convertView.findViewById(R.id.iv2);

				holder.iv_play = (ImageView) convertView
						.findViewById(R.id.iv_play);
				holder.checkBox = (ImageView) convertView
						.findViewById(R.id.check);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (position == 0) {
				holder.iv2.setVisibility(View.VISIBLE);
				holder.iv.setVisibility(View.GONE);
				holder.checkBox.setVisibility(View.GONE);
				holder.iv_play.setVisibility(View.GONE);
			} else {
				holder.iv2.setVisibility(View.GONE);
				holder.iv.setVisibility(View.VISIBLE);
				holder.checkBox.setVisibility(View.VISIBLE);
				// holder.checkBox.setSelected(false);
				final int positiontemp;

				if (currentImageFolder.getName().contains("所有图片")) {
					positiontemp = position - 1;
				} else {
					// positiontemp = position;
					positiontemp = position - 1;
				}

				final JTFile item = currentImageFolder.images.get(positiontemp);

				if (!(item.mType == JTFile.TYPE_FILE)) {
					// 使用框架加载缩略图，流畅不卡顿
					loader.displayImage("file://" + item.mLocalFilePath,
							holder.iv, options);
					holder.iv_play.setVisibility(View.GONE);
				}

				if (jPath != null) {
					// selectedPicture.add(jPath);
				} else {
					holder.checkBox.setSelected(false);
					holder.checkBox.setVisibility(View.GONE);
					selectedPicture.clear();
				}

				/**
				 * 如果单选图片选中 变换背景色 by zp
				 */
				if (position != selectPosition) {
					// holder.iv.setImageAlpha(255);
					holder.iv.setAlpha(1f);
					holder.checkBox.setSelected(false);
					holder.checkBox.setVisibility(View.GONE);
				} else {
					// 如果之前选中，则设为取消
					if (holder.checkBox.isSelected()) {
						holder.checkBox.setSelected(false);
						// holder.iv.setImageAlpha(255);
						holder.iv.setAlpha(1f);
						selectedPicture.clear();
					} else {
						selectedPicture.clear();
						// holder.iv.setImageAlpha(100);
						holder.iv.setAlpha(0.5f);
						selectedPicture.add(paths);
						System.out.println("点击选中图片-----" + selectedPicture);
						holder.checkBox.setSelected(true);
					}
					// // 设置预览按钮为可点击和不可点击
					// if (selectedPicture != null && selectedPicture.size() >
					// 0) {
					// previewTv.setEnabled(true);
					// previewTv.setAlpha(255);
					// } else {
					// previewTv.setEnabled(false);
					// previewTv.setAlpha(100);
					// }
				}

			}

			return convertView;
		}
	}

	final class ViewHolder {
		ImageView iv;
		ImageView iv_play;
		ImageView checkBox;
		LinearLayout iv2;
		// ImageView shadowIv;
	}

	public class ImageFloder {
		/**
		 * 图片的文件夹路径
		 */
		private String dir;

		/**
		 * 第一张图片的路径
		 */
		private String firstImagePath;
		/**
		 * 文件夹的名称
		 */
		private String name;

		public List<JTFile> images = new ArrayList<JTFile>();

		/**
		 * 图片的大小
		 * 
		 * @return
		 */
		private String ImageSize;

		public String getImageSize() {
			return ImageSize;
		}

		public void setImageSize(String imageSize) {
			ImageSize = imageSize;
		}

		public String getDir() {
			return dir;
		}

		public void setDir(String dir) {
			this.dir = dir;
			int lastIndexOf = this.dir.lastIndexOf("/");
			this.name = this.dir.substring(lastIndexOf);
		}

		public String getFirstImagePath() {
			return firstImagePath;
		}

		public void setFirstImagePath(String firstImagePath) {
			this.firstImagePath = firstImagePath;
		}

		public String getName() {
			return name;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.demand_create_label_ok, menu);
		findItem = menu.findItem(R.id.save_ok);
//		findItem.setEnabled(false);
		if (selectedPicture != null && selectedPicture.size() > 0
				&& selectedPicture.get(0).mLocalFilePath != "") {
			findItem.setTitle("完成");
//			findItem.setEnabled(true);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.save_ok:
			selectPosition = -1;
			Intent data = new Intent();
			data.putExtra(ENavConsts.DEMAND_INTENT_SELECTED_PICTURE,
					selectedPicture);
			System.out.println("点完成---" + selectedPicture);
			setResult(RESULT_OK, data);
			finish();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void initJabActionBar() {
		ActionBar mActionBar = jabGetActionBar();
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(true);
		View mCustomView = getLayoutInflater().inflate(
				R.layout.demand_actionbar_title, null);
		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView
				.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK
				| Gravity.CENTER_HORIZONTAL;
		mActionBar.setCustomView(mCustomView, mP);
		mActionBar.setTitle(" ");
		TextView myTitle = (TextView) mCustomView.findViewById(R.id.titleTv);
		myTitle.setText("图片");
		mCustomView.findViewById(R.id.titleIv).setVisibility(View.GONE);

	}
}
