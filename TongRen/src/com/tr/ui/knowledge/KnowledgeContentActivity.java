package com.tr.ui.knowledge;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.tr.App;
import com.tr.R;
import com.tr.model.obj.JTFile;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.conference.initiatorhy.ConferenceIntroduceActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.widgets.BasicGridView;
import com.tr.ui.widgets.RichEditor;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.common.FileUploader;
import com.utils.common.FileUploader.OnFileUploadListenerEx;
import com.utils.common.TaskIDMaker;
import com.utils.http.DownHtmlUrlService;
import com.utils.log.KeelLog;

/**
 * 知识描述
 * 界面以startActivityForResult的方式启动
 * 传入参数为 EConsts.Key.TASK_ID（String）
 * ResultCode为Activity.RESULT_OK 或者 Activity.RESULT_CANCELED
 * 返回值为 EConsts.Key.KNOWLEDGE_CONTENT（String）和 EConsts.Key.KNOWLEDGE_LIST_JTFILE（List<JTFile>）
 * @author leon
 */
public class KnowledgeContentActivity extends JBaseActivity implements OnFileUploadListenerEx{

	private final String TAG = getClass().getSimpleName();

	private final String TIP_PICK_PHOTO_FAILED = "选取图片失败";
	private final String TIP_ILLEGAL_PHOTO_PATH = "无法解析图片路径，请选择其他图片";
	private final String TIP_EMPTY_CONTENT = "请填写知识内容";
	public String LOCAL_PATH = Environment.getExternalStorageDirectory()+ "/gintong/gtpic/";

	private EditText contentEt;
	private BasicGridView imageGv;
	private TextView countTv;

	private ImageAdapter mAdapter;
	private String mTaskId; // 上传文件所需
	private String coverTaskId; //
	private ArrayList<FileUploader> mListUploader; // 文件上传类
	private ArrayList<JTFile> mListJtFile; // 所选文件（目前只是图片）
	private int index = 0;
	private int total = 9;
	private Context context;
	private Intent intent;
	private String allLocalUrlHtml;
	private FileUploader coverUploader;
	private ArrayList<String> filepathes = new ArrayList<String>();
	private ArrayList<String> allLocalUrlHtmlpathes = new ArrayList<String>();
	//	private String pic;
	
	
	/**  富文本编辑器  */
	private RichEditor mEditor;
	/** html源码预览框 */
    private TextView mPreview;
    private String htmlString = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
            "<!DOCTYPE html PUBLIC \"-//WAPFORUM//DTD XHTML Mobile 1.0//EN\" \"http://www.wapforum.org/DTD/xhtml-mobile10.dtd\">" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\"><!--STATUS OK--><head><meta http-equiv=\"Content-Type\" content=\"application/xhtml+xml; charset=utf-8\"/><meta http-equiv=\"Cache-control\" content=\"no-cache\" /><style type=\"text/css\">body{font-size:medium;line-height:1.6em;text-align:center;}img{border:0;}form{margin:0;padding:0;}.a{padding-top:6px;margin-top:6px;margin-bottom:6px;}.h{color:#C60A00;}.s{font-size:small;}.b{font-size:small;color:#77C;}</style><title>百度一下,你就知道</title></head><body><div><img src=\"http://m.baidu.com/static/index/l.gif\" alt=\"百度首页\" /><br/><form action=\"ssid=0/from=0/bd_page_type=1/uid=0/baiduid=2B534167638A851D06FA3AB1F62FBAD6/s\" method=\"get\"><div><input type=\"text\" name=\"word\" maxlength=\"64\" size=\"20\" id=\"word\"/><br/><input type=\"hidden\" value=\"upssntdnvelami\" name=\"uc_param_str\"/><input type=\"hidden\" value=\"ib\" name=\"sa\"/><input type=\"hidden\" value=\"111041\" name=\"st_1\"/><input type=\"hidden\" value=\"102041\" name=\"st_2\"/><input type=\"hidden\" value=\"sz@224_220,ta@middle___3_537\" name=\"pu\"/><input type=\"hidden\" name=\"idx\" value=\"20000\"/><input type=\"hidden\" value=\"middle\" name=\"tn_1\"/><input type=\"hidden\" value=\"middle\" name=\"tn_2\"/><input type=\"submit\" value=\"搜网页\" name=\"ct_1\"/>&nbsp;<input type=\"submit\" value=\"搜Wap\" name=\"ct_2\"/></div></form><div class=\"a\"><a href=\"http://duokoo.baidu.com/novel/?fr=home&amp;ssid=0&amp;from=0&amp;bd_page_type=1&amp;uid=0&amp;pu=sz%40224_220%2Cta%40middle___3_537\">小说</a>&nbsp;&nbsp;<a href=\"ssid=0/from=0/bd_page_type=1/uid=0/pu=sz%40224_220%2Cta%40middle___3_537/news?idx=20000&amp;itj=22\">新闻</a>&nbsp;&nbsp;<a href=\"http://tieba.baidu.com/?ssid=0&amp;from=0&amp;bd_page_type=1&amp;uid=0&amp;pu=sz%40224_220%2Cta%40middle___3_537&amp;idx=20000&amp;itj=23&amp;mo_device=1\">贴吧</a>&nbsp;&nbsp;<a href=\"http://zhidao.baidu.com/?ssid=0&amp;from=0&amp;bd_page_type=1&amp;uid=0&amp;pu=sz%40224_220%2Cta%40middle___3_537&amp;idx=20000&amp;itj=24&amp;device=mobile\">知道</a>&nbsp;&nbsp;<a href=\"http://wapmap.baidu.com/?ssid=0&amp;from=0&amp;bd_page_type=1&amp;uid=0&amp;pu=sz%40224_220%2Cta%40middle___3_537&amp;idx=20000&amp;itj=25&amp;wtj=wi\">地图</a><br/><a href=\"http://image.baidu.com/i?tn=wiseindex&amp;wiseps=1\">图片</a>&nbsp;&nbsp;<a href=\"http://m.hao123.com/?ssid=0&amp;from=0&amp;bd_page_type=1&amp;uid=0&amp;pu=sz%40224_220%2Cta%40middle___3_537&amp;idx=20000&amp;itj=29\">hao123</a></div><div class=\"a\"><a href=\"http://duokoo.baidu.com/game/?fr=home&amp;ssid=0&amp;from=0&amp;bd_page_type=1&amp;uid=0&amp;pu=sz%40224_220%2Cta%40middle___3_537&amp;itj=222\">游戏</a>&nbsp;&nbsp;<a href=\"http://duokoo.baidu.com/d/?fr=home&amp;ssid=0&amp;from=0&amp;bd_page_type=1&amp;uid=0&amp;pu=sz%40224_220%2Cta%40middle___3_537&amp;itj=212\">下载</a>&nbsp;&nbsp;<a href=\"pub/jump.php?itj=223&amp;url=http%3A%2F%2Fw.guotv.com%2Ftv100-baidu-wap%2Ftv100%2Findex%3Fpid%3D643%26ssid%3D0%26from%3D0%26bd_page_type%3D1%26uid%3D0%26pu%3Dsz%40224_220%2Cta%40middle___3_537&amp;wtj=wi\">视频</a>&nbsp;&nbsp;<a href=\"pub/more.php?idx=20000&amp;ssid=0&amp;from=0&amp;bd_page_type=1&amp;uid=0&amp;pu=sz%40224_220%2Cta%40middle___3_537&amp;itj=210\">更多</a><br/>客户端:<a href=\"http://mo.baidu.com/zhangbai/?ssid=0&amp;from=0&amp;bd_page_type=1&amp;uid=0&amp;pu=sz%40224_220%2Cta%40middle___3_537&amp;page_type=1&amp;itj=217\">掌上百度</a>|<a href=\"http://mo.baidu.com/input/?ssid=0&amp;from=0&amp;bd_page_type=1&amp;uid=0&amp;pu=sz%40224_220%2Cta%40middle___3_537&amp;page_type=1&amp;itj=219\">输入法</a></div><div class=\"a\"><a href=\"http://m.baidu.com/pub/help.php?ssid=0&amp;from=0&amp;bd_page_type=1&amp;uid=0&amp;pu=sz%40224_220%2Cta%40middle___3_537&amp;tt=I1211\">帮助</a>-<a href=\"http://wapp.baidu.com/f?kw=%B0%D9%B6%C8%B8%F6%C8%CB%CA%D7%D2%B3&amp;ssid=0&amp;from=0&amp;bd_page_type=1&amp;uid=0&amp;pu=sz%40224_220%2Cta%40middle___3_537\">反馈</a><br/>&lt;<a href=\"?ssid=0&amp;from=0&amp;bd_page_type=0&amp;uid=0&amp;pu=sz%40224_220%2Cta%40middle___3_537%2Csz%40176_208\">极简版</a>|炫彩版|<a href=\"?ssid=0&amp;from=0&amp;bd_page_type=1&amp;uid=0&amp;pu=sz%40224_220%2Cta%40middle___3_537%2Csz%401330_240\">触屏版</a>&gt;<br/><span class=\"b\">Baidu&nbsp;京ICP证030173号</span></div></div></body></html>";


	Context getContext(){
		return context;
	}

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(KnowledgeContentActivity.this, getActionBar(), "知识内容", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kno_act_content);
		intent = new Intent();
		initVars();
		initControls();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuItem item = menu.add(0, 1, 0, "完成");
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId() == android.R.id.home){ // 返回

			// 取消文件上传
			for(FileUploader uploader : mListUploader){
				uploader.cancel();
			}
			setResult(Activity.RESULT_CANCELED);
			finish();

			/*
			for(FileUploader uploader : mListUploader){
				if(uploader.getStatus() != FileUploader.Status.Success){
					new AlertDialog.Builder(this)
					.setMessage("有未上传的图片，是否继续？")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							for(FileUploader uploader : mListUploader){
								uploader.cancel();
							}
							finish();
						}
					})
					.setNegativeButton("取消", null)
					.create().show();
				}
				break;
			}
			 */
		}
		else if(item.getItemId() == 1){ // 完成
			/*if(TextUtils.isEmpty(contentEt.getText().toString())){ // 内容是否为空
				showToast(TIP_EMPTY_CONTENT);
			}
			else*/ 
			//将本地地址替换成文本网页地址 allLocalUrlHtml
			allLocalUrlHtml = contentEt.getText().toString();
			for (String string : allLocalUrlHtmlpathes) {
				allLocalUrlHtml = allLocalUrlHtml.replace(LOCAL_PATH+string, filepathes.get(allLocalUrlHtmlpathes.indexOf(string)));
			}
			contentEt.setText(allLocalUrlHtml);
			if(!isAllPictureUpload()){ // 有图片未上传

				new AlertDialog.Builder(this)
				.setMessage("有未上传的图片，是否继续？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String MSG = "onClick()";

						for(int i = 0; i < mListUploader.size(); i++){ // 删除未上传成功的图片
							if(mListUploader.get(i).getStatus() != FileUploader.Status.Success){
								//								mListUploader.remove(i);
								/* 
								 * 存在编辑图片时不能使用此种方法
								if(mListJtFile.size() > i){
									mListJtFile.remove(i);
								}
								 */
								for(JTFile jtFile : mListJtFile){
									if(jtFile.mCreateTime == mListUploader.get(i).getJTFile().mCreateTime){
										mListJtFile.remove(jtFile);
										break;
									}
								}
								mListUploader.remove(i);
								i--;
							}
						}
						//						setResult(Activity.RESULT_CANCELED);

						if (coverUploader.getStatus() == FileUploader.Status.Success) {
							intent.putExtra("coverUrl", coverUploader.getJTFile().getmUrl());
							Log.i(TAG, MSG + " coverUrl = " + coverUploader.getJTFile().getmUrl());
						}
						// 传回的也是超文本
						String htmlContent = Html.toHtml(contentEt.getText());
						String decodedString = StringEscapeUtils.unescapeHtml4(htmlContent);
						intent.putExtra(EConsts.Key.KNOWLEDGE_CONTENT, decodedString);
						intent.putExtra(EConsts.Key.KNOWLEDGE_LIST_JTFILE, mListJtFile);
						setResult(Activity.RESULT_OK, intent);
						finish();
					}
				})
				.setNegativeButton("取消", null)
				.create().show();
			}
			else{
				intent.putExtra("coverUrl", coverUploader.getJTFile().getmUrl());
				String htmlContent = Html.toHtml(contentEt.getText());
				String decodedString = StringEscapeUtils.unescapeHtml4(htmlContent);
				if (decodedString.contains("<br>\n")) {
					String[] split = decodedString.split("\n");
					decodedString = "";
					for (int i = 0; i < split.length; i++) {
						decodedString+= split[i];
					}
				}
				intent.putExtra(EConsts.Key.KNOWLEDGE_CONTENT,  decodedString);
				intent.putExtra(EConsts.Key.KNOWLEDGE_LIST_JTFILE, mListJtFile);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		}
		return false;
	}


	void updateCountTv(){
		countTv.setText(index + ""/*+ "/" + total*/);
	}

	@SuppressWarnings("unchecked")
	private void initVars(){
		context = this;

		mAdapter = new ImageAdapter();
		mListUploader = new ArrayList<FileUploader>();
		mListJtFile = new ArrayList<JTFile>();
		//		mListJtFile.add(new JTFile());
		// mTaskId = getIntent().getStringExtra(EConsts.Key.TASK_ID);
		mTaskId = TaskIDMaker.getTaskId(App.getUserName()); // 根据用户名生成TaskId


	}

	private void initControls(){
		contentEt = (EditText) findViewById(R.id.contentEt);
		imageGv = (BasicGridView) findViewById(R.id.imageGv);
		imageGv.setAdapter(mAdapter);
		imageGv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == mListJtFile.size()) { // 添加
					//					if(index < total ){
//					EUtil.dispatchPickPictureIntent(KnowledgeContentActivity.this, EConsts.REQ_CODE_PICK_PICTURE);
					
//					ENavigate.startCreateKnoeledgeAccessoryActivity(KnowledgeContentActivity.this, null, EConsts.REQ_CODE_PICK_PICTURE, true);
					ENavigate.startSelectPictureActivity(KnowledgeContentActivity.this, EConsts.REQ_CODE_PICK_PICTURE,null , false);
					//					}
					//					else if (index >= total ){
					//						Toast.makeText(getContext(), "最多只能上传9张图片", Toast.LENGTH_LONG).show();
					//					}
				}
				else { // 浏览

				}
			}
		});
		countTv = (TextView) findViewById(R.id.countTv);

		Intent srcInt = getIntent();
		if(srcInt.hasExtra(EConsts.Key.KNOWLEDGE_CONTENT)){ // 知识内容
			// 处理富文本
			String htmlContent = srcInt.getStringExtra(EConsts.Key.KNOWLEDGE_CONTENT);
			Spanned sp = Html.fromHtml(filterHtml(htmlContent));
			contentEt.setText(sp);
		}
		if(srcInt.hasExtra(EConsts.Key.KNOWLEDGE_LIST_JTFILE)){ // 知识图片
			mListJtFile = (ArrayList<JTFile>) srcInt.getSerializableExtra(EConsts.Key.KNOWLEDGE_LIST_JTFILE);
			index = mListJtFile.size();
		}

		String coverUrl = srcInt.getStringExtra("pic");
		if( coverUrl != null ){
			JTFile coverJtFile = new JTFile();
			coverJtFile.setmUrl(coverUrl);
			coverUploader = new FileUploader(coverJtFile, this);
			coverUploader.setmStatus(FileUploader.Status.Success);
		}
		else {
			JTFile coverJtFile = new JTFile();
			coverJtFile.setmUrl("");
			coverUploader = new FileUploader(coverJtFile, this);
			coverUploader.setmStatus(FileUploader.Status.Success);
		}
		updateCountTv();
	}

	/**
	 * 将字符串写入文本
	 * @param htmlString
	 */
	public void stringToFile(String htmlString) {
		FileWriter fw = null;
		File f = new File(LOCAL_PATH+"mmmm.txt");

		try {
			if(!f.exists()){
				f.createNewFile();
			}
			fw = new FileWriter(f);
			BufferedWriter out = new BufferedWriter(fw);
			out.write(htmlString, 0, htmlString.length()-1);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("end");
	}


	/**
	 * 利用XUtils下载图片
	 * @param view
	 * @param content
	 * @param imgsrcLists
	 */
	public void downLoad(List<String> imgsrcLists) {
		HttpUtils http = new HttpUtils();

		for (String path : imgsrcLists) {

			http.download(path, LOCAL_PATH + allLocalUrlHtmlpathes.get(imgsrcLists.indexOf(path)), false, new RequestCallBack<File>() {
				@Override
				public void onSuccess(ResponseInfo<File> responseInfo) {
					Toast.makeText(KnowledgeContentActivity.this,"下载完成，文件保存在"+ responseInfo.result.getAbsolutePath(), 0).show();
				}
				@Override
				public void onFailure(HttpException error, String msg) {
					Toast.makeText(KnowledgeContentActivity.this, "下载失败," + msg, 0).show();
				}
			});
		}
	}

	/**
	 * 返回将URL替换为本地地址后的结果
	 * @param remoteURL
	 * @param UrlString
	 * @return
	 */
	public String getLocalString(ArrayList<String> remoteURL,String UrlString) {
		for (String string : remoteURL) {
			String uutmp =getSuffix(string);
			allLocalUrlHtmlpathes.add(uutmp);
			UrlString = UrlString.replace(string, LOCAL_PATH + uutmp);
		}
		return UrlString;
	}

	/**
	 * 获得URL中的文件名
	 * @param localpath
	 * @return
	 */
	public String getSuffix(String UrlPath) {
		UUID uuid = UUID.nameUUIDFromBytes(UrlPath.getBytes());
		String uuidString =uuid.toString().replaceAll("-", "");
		return uuidString+".png";
	}

	/**
	 * 过滤指定标签
	 * @param html
	 * @return
	 */
	public String filterHtml(String html){

		Pattern pattern = Pattern.compile("<style[^>]*?>[\\D\\d]*?<\\/style>");  
		Matcher matcher = pattern.matcher(html);
		String htmlStr = matcher.replaceAll("");
		pattern = Pattern.compile("<img[^>]*/>");
		String  filterStr = pattern.matcher(htmlStr).replaceAll("");
		filterStr.replace("&nbsp;", " ");
		return filterStr;
	}


	/**
	 * 过滤指定标签
	 * @param html
	 * @return
	 */
	public String filterStyleHtml(String html){
		Pattern pattern = Pattern.compile("<style[^>]*?>[\\D\\d]*?<\\/style>");  
		Matcher matcher = pattern.matcher(html);
		String htmlStr = matcher.replaceAll("");
		htmlStr.replaceAll("&nbsp;", "");
		return htmlStr;
	}


	/**
	 * 得到网页中图片的地址
	 */
	public ArrayList<String> getImgStrs(String htmlStr) {
		String img = "";
		Pattern p_image;
		Matcher m_image;
		ArrayList<String> pics = new ArrayList<String>();

		String regEx_img = "<img.*src=(.*?)[^>]*?>"; // 图片链接地址
		p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
		m_image = p_image.matcher(htmlStr);
		while (m_image.find()) {
			img = img + "," + m_image.group();
			Matcher m = Pattern.compile("src=\"?(.*?)(\"|>|\\s+)").matcher(img); // 匹配src
			while (m.find()) {
				pics.add(m.group(1).replaceAll("'", ""));
			}
		}
		return pics;
	}


	// 是否所有图片都已上传
	private boolean isAllPictureUpload() {

		boolean result = true;
		for (FileUploader uploader : mListUploader) {
			if (uploader.getStatus() != FileUploader.Status.Success) {
				result = false;
			}
			break;
		}

		if (coverUploader.getStatus() != FileUploader.Status.Success) {
			result = false;
		}

		return result;
	}

	class ViewHolder{
		ImageView imgIv;
		TextView statusTv;
	}

	class ImageAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mListJtFile.size() + 1;
		}

		@Override
		public Object getItem(int position) {
			return mListJtFile.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			String MSG = "getView()";

			final int finalPosition = position;
			ViewHolder holder;
			if(convertView == null){
				convertView = LayoutInflater.from(KnowledgeContentActivity.this).inflate(R.layout.kno_grid_item_image, null);
				holder = new ViewHolder();
				holder.imgIv = (ImageView) convertView.findViewById(R.id.imgIv);
				holder.statusTv = (TextView) convertView.findViewById(R.id.statusTv);
				convertView.setTag(holder);
			}
			else{
				holder = (ViewHolder) convertView.getTag();
			}
			if(position < mListJtFile.size()){
				Bitmap bitmap = getSmallBitmap(mListJtFile.get(position).mLocalFilePath);
				holder.imgIv.setImageBitmap(bitmap);
				holder.statusTv.setVisibility(View.VISIBLE);

				JTFile jTFile = mListJtFile.get(position);

				for (int j = 0; j < mListUploader.size(); j++) {
					if(jTFile.mCreateTime == mListUploader.get(j).getJTFile().mCreateTime){
						FileUploader  fileUploader = mListUploader.get(j);
						int status = fileUploader.getStatus();
						//						 Log.i(TAG, MSG + "status = " + status );
						if(status == 1){
							holder.statusTv.setText("准备上传");
						}
						else if(status == 2){
							holder.statusTv.setText("开始上传");
						}
						else if(status == 3){
							holder.statusTv.setText("上传成功");
						}
						else if(status == 4){
							holder.statusTv.setText("上传失败\n点击重试");
						}
						else if(status == 5){
							holder.statusTv.setText("上传失败\n点击重试");
						}

					}
				}


			}
			else{
				holder.imgIv.setImageResource(R.drawable.kno_add_pic);
				holder.statusTv.setVisibility(View.GONE);
			}
			// 处理图片重传事件
			holder.statusTv.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					for(FileUploader uploader : mListUploader){
						if(uploader.getJTFile().mCreateTime == mListJtFile.get(finalPosition).mCreateTime){
							if(uploader.getStatus() == FileUploader.Status.Error
									|| uploader.getStatus() == FileUploader.Status.Canceled){
								// 重新上传
								uploader.start(); 
							}
							break;
						}
					}
				}
			});
			return convertView;
		}
	}


	public static  Bitmap getSmallBitmap(String filePath) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		Bitmap bm = BitmapFactory.decodeFile(filePath, options);
		if(bm == null){
			return  null;
		}
		ByteArrayOutputStream baos = null ;
		try{
			baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.JPEG, 30, baos);

		}
		finally {
			try {
				if (baos != null)
					baos.close();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bm ;

	}

	//得到封面小图
	public static  Bitmap getCoverSmallBitmap(String filePath) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 400, 300);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		Bitmap bm = BitmapFactory.decodeFile(filePath, options);
		if(bm == null){
			return  null;
		}
		ByteArrayOutputStream baos = null ;
		try{
			baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.PNG, 90, baos);
		}
		finally {
			try {
				if (baos != null)
					baos.close();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bm ;

	}

	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
		}
		return inSampleSize;
	}

	@Override
	public void onPrepared(long uid) {
		Message msg = new Message();
		msg.what = 1;
		msg.obj = uid;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onStarted(long uid) {
		Message msg = new Message();
		msg.what = 2;
		msg.obj = uid;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onUpdate(long uid, int progress) {
		Message msg = new Message();
		msg.what = 3;
		msg.arg1 = progress;
		msg.obj = uid;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onCanceled(long uid) {
		Message msg = new Message();
		msg.what = 4;
		msg.obj = uid;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onSuccess(long uid, String fileUrl) {
		Message msg = new Message();
		msg.what = 5;
		msg.obj = uid;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onError(long uid, int code, String message) {
		Message msg = new Message();
		msg.what = 6;
		msg.obj = uid;
		mHandler.sendMessage(msg);
	}

	@SuppressLint("HandlerLeak") 
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			String MSG = "handleMessage()";
			for(int i = 0; i < mListJtFile.size(); i++){

				if(mListJtFile.get(i).mCreateTime == (Long) msg.obj){
					View v = imageGv.getChildAt(i - imageGv.getFirstVisiblePosition());
					if(v == null ){
						Log.i(TAG, MSG + " v == null "  + " i = " + i);
					}

					if(v != null){
						ViewHolder holder = (ViewHolder) v.getTag();
						if(holder != null){
							switch(msg.what){
							case 1:
								holder.statusTv.setText("准备上传");
								holder.statusTv.setVisibility(View.VISIBLE);
								break;
							case 2:
								holder.statusTv.setText("开始上传");
								holder.statusTv.setVisibility(View.VISIBLE);
								break;
							case 3:
								holder.statusTv.setText(String.format("上传中...%d", msg.arg1));
								holder.statusTv.setVisibility(View.VISIBLE);
								break;
							case 4:
								holder.statusTv.setText("上传已取消\n点击重试");
								holder.statusTv.setVisibility(View.VISIBLE);
								break;
							case 5:
								holder.statusTv.setText("上传成功");
								holder.statusTv.setVisibility(View.VISIBLE);
								break;
							case 6:
								holder.statusTv.setText("上传失败\n点击重试");
								holder.statusTv.setVisibility(View.VISIBLE);
								showToast("图片上传失败");
								break;
							}
						}
					}
					break;
				}
			}
		}
	};

	private ArrayList<JTFile> selectedPictureSDAndNet;


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent){
		if(requestCode == EConsts.REQ_CODE_PICK_PICTURE){
			if(resultCode == Activity.RESULT_OK){
//				HashSet<String> selectList = (HashSet<String>) intent.getSerializableExtra("select_list");
//				ArrayList<String> list = new ArrayList<String>(selectList);
//				for (String string : list) {
//					uploadPic(string);
//				}
				selectedPictureSDAndNet = (ArrayList<JTFile>) intent.getSerializableExtra(ENavConsts.DEMAND_INTENT_SELECTED_PICTURE);
				for (JTFile jtFile : selectedPictureSDAndNet) {
					uploadPic(jtFile.mLocalFilePath);
				}
			}
			else if(resultCode == Activity.RESULT_CANCELED){ // 取消选择

			}
			else{
				showToast(TIP_PICK_PHOTO_FAILED); // 弹出提示
			}
		}
	}
	
	//上传
	private void uploadPic(String filePath)
	{

//		filePath = EUtil.uri2Path(getContentResolver(),intent.getData());
		if(TextUtils.isEmpty(filePath) || ! new File(filePath).exists()){ // 图片地址合法并且图片文件存在
			showToast(TIP_ILLEGAL_PHOTO_PATH);
		}
		else{

			// 添加到文件列表
			JTFile jtFile = new JTFile();
			jtFile.mTaskId = mTaskId;
			jtFile.mType = JTFile.TYPE_IMAGE;
			jtFile.mLocalFilePath = filePath;
			jtFile.mCreateTime = System.currentTimeMillis(); // 创建时间，作为文件的识别码
			//					mListJtFile.add(mListJtFile.size() - 1, jtFile); 
			mListJtFile.add(jtFile); 
			// 更新列表
			mAdapter.notifyDataSetChanged();
			// 添加到上传队列
			FileUploader uploader = new FileUploader(jtFile, this);
			mListUploader.add(0, uploader);
			uploader.start();

			index = mListJtFile.size();
			updateCountTv();

			if(mListJtFile.size() == 1){
				JTFile coverJtFile = new JTFile();
				coverTaskId = TaskIDMaker.getTaskId(App.getUserName()); // 根据用户名生成TaskId
				coverJtFile.mTaskId = coverTaskId;
				coverJtFile.mType = JTFile.TYPE_IMAGE;
				coverJtFile.mCreateTime = System.currentTimeMillis(); // 创建时间，作为文件的识别码
				coverJtFile.mLocalFilePath = jtFile.getmLocalFilePath();

				Bitmap coverBitmap = getCoverSmallBitmap(coverJtFile.mLocalFilePath);
				//目录
				//						File coverDirectoryFile = context.getCacheDir();
				//						if (!coverDirectoryFile.exists()) {  
				//							coverDirectoryFile.mkdirs();  
				//			            } 

				//						BufferedOutputStream bos = null;
				try {
					//							String coverLocalPath =context.getCacheDir()+ "/coverImage.png";
					String coverLocalPath = Environment.getExternalStorageDirectory()+ "/coverImage.png";
					File coverFile = new File( coverLocalPath);
					if (!coverFile.exists()) {
						coverFile.createNewFile(); 
					}
					FileOutputStream fos = new FileOutputStream(coverFile);
					coverBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
					coverJtFile.mLocalFilePath = coverLocalPath;
					coverUploader = new FileUploader(coverJtFile, this);
					coverUploader.start();

					/*						BufferedInputStream in=new BufferedInputStream(fi);
					 FileOutputStream fo=new FileOutputStream(context.getCacheDir() + coverLocalPath); 
					 BufferedOutputStream out=new BufferedOutputStream(fo);   
					 byte[] buf=new byte[1024]; 
					 int len=in.read(buf);//读文件，将读到的内容放入到buf数组中，返回的是读到的长度 
					 while(len!=-1){  
						 out.write(buf, 0, len); 
						 len=in.read(buf);  
					}
					fo.close();
					out.close();*/


				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{

				}

			}

		}
	
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopService(intent);
		intent = null;
	}



}