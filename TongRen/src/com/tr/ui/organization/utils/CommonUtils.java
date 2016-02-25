package com.tr.ui.organization.utils;

import java.io.Serializable;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tr.ui.organization.create_clientele.CreateOrganizationActivity;
import com.tr.ui.widgets.EProgressDialog;

public class CommonUtils implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3817393754305767406L;
	
	private static EProgressDialog mProgressDialog;
	
	
	/**
	 * 取消加载框
	 */
	public static void dismissLoadingDialog() {
    	try{
	        if (mProgressDialog!=null&&mProgressDialog.isShowing()) {
	            mProgressDialog.dismiss();
	        }
    	}catch(Exception e){
    	System.out.println("");	
    	}
    }
	
	
	/**
	 * 显示加载框
	 * @param message
	 * @param context
	 */
	public static void showLoadingDialog(final String message,Context context) {
        if (null==mProgressDialog) {
            mProgressDialog=new EProgressDialog(context);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    //JBaseActivity.this.finish();
                    if("".equals(message)){
                    	onLoadingDialogCancel();
                    }
                    else{
                    	onLoadingDialogCancel("555");
                    }
                }
            });
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }
	
	/**
     * 取消loading对话框动作回调，如果想要处理，就继承该函数，默认关闭当前页面
     */
    private static void onLoadingDialogCancel(){
    	//finish();
    }
    /**
     * 取消loading对话框动作回调,什么都不做
     * @param message
     */
    private static void onLoadingDialogCancel(String message){
    	
    }
    
    /**
	 * 旋转图片
	 * @param bm
	 * @param orientationDegree
	 * @return
	 */
    public static Bitmap rotateBitmap(Bitmap bm, int orientationDegree)
    {
       Matrix m = new Matrix();
       m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);

       try {
    	  Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
          return bm1;
         } catch (OutOfMemoryError ex) {
               }
          return null;
    }
    
    /**
     * 初始化设置图片时根据图片情况决定是否选装图片
     * @param url
     * @return
     */
    public static void initRotateBitmap(String url,final ImageView iv,final int ImageType)
    {
    	
    	ImageLoader.getInstance().loadImage(url, new ImageLoadingListener() {
    		
    		@Override
    		public void onLoadingStarted(String imageUri, View view) {
    			
    		}
    		
    		@Override
    		public void onLoadingFailed(String imageUri, View view,
    				FailReason failReason) {
    			
    		}
    		
    		@Override
    		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
    			Bitmap initTmpBitmap = null;
    			if (loadedImage.getWidth() < loadedImage.getHeight()) {
    				initTmpBitmap = CommonUtils.rotateBitmap(loadedImage,-90);
    				initTmpBitmap.setHeight(loadedImage.getWidth());
    				initTmpBitmap.setWidth(loadedImage.getHeight());
    			}else {
    				initTmpBitmap = loadedImage;
    			}
    			iv.setImageBitmap(initTmpBitmap);
    			Message message = Message.obtain();
    			message.obj = initTmpBitmap;
    			message.arg1 = ImageType;
    			message.what = 111111;
    			CreateOrganizationActivity.handler.sendMessage(message);
    		}
    		
    		@Override
    		public void onLoadingCancelled(String imageUri, View view) {
    			
    		}
    	});
    }
    
    
    /**
	 * 将目的Bitmap转换后设置到ImageView
	 * @param sourceBitmap 目的
	 * @param localBitmap 本地
	 * @param sourceImageview 本地ImageView
	 */
    public static void setBitmap(Bitmap sourceBitmap,Bitmap localBitmap,ImageView sourceImageview){
		if (sourceBitmap.getWidth() < sourceBitmap.getHeight()) {
			localBitmap = CommonUtils.rotateBitmap(sourceBitmap,-90);
		}else {
			localBitmap = sourceBitmap;
		}
		sourceImageview.setBackground(new BitmapDrawable(localBitmap));
		CommonUtils.dismissLoadingDialog();
	}
    
    /**
     * 截取字符串
     * @param imageUrl
     * @return
     */
    public static String alterImageUrl(String imageUrl) {
		String url = "";
		int index = 0;
		imageUrl = imageUrl == null ? "": imageUrl.trim(); 
		if (imageUrl.length() != 0) {
			String[] ss= imageUrl.split(".com");
			url = ss[1].substring(ss[1].indexOf("/"));
		}
		return url;
	}
    
    /**
     * 判断是否只包含数字
     * @param stockNum
     * @return
     */
    public static boolean JudgeOnlyNumber(String stockNum)
    {
    	try {
    		Integer.parseInt(stockNum);
		} catch (Exception e) {
			return false;
		}
		return true;
    	
    }
    
    
//	public static String alterImageUrl(String imageUrl) {
//		String url = "";
//		imageUrl = imageUrl == null ? "": imageUrl.trim(); 
//		if (!StringUtils.isEmpty(imageUrl)) {
//			int l = imageUrl.lastIndexOf(".com");
//			if (imageUrl.indexOf("http://") > -1 && l > -1) {
//				url = imageUrl.substring(l + 4);
//			} else {
//				url = imageUrl;
//			}
//		}
//		return url;
//	}
	

}
