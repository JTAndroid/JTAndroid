package com.utils.http;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

import com.tr.App;
import com.utils.common.ApolloUtils;
import com.utils.image.cache.ImageCache;
import com.utils.log.KeelLog;

/**
 * 下载图片线程。
 *
 * @author archko
 */
public class FetchImage extends Thread {

    public static final String TAG="FetchImage";
    private Context mContext;
    DownloadPool.DownloadPiece mPiece;

    /**
     * @param context
     * @param handler
     * @param client
     * @param httpGet
     * @param type     类型，主要用于获取微博或转发微博的图片
     * @param filepath 图片名字，是md5加密后的
     * @param uri      图片url
     * @param dir      图片存储目录
     */
    public FetchImage(Context context, DownloadPool.DownloadPiece piece) {
        this.mContext=context;
        this.mPiece=piece;
    }

    @Override
    public void run() {
        KeelLog.d(TAG, "fetch impl:"+mPiece.uri);
        App app=(App) this.mContext.getApplicationContext();
        if (cancelWork(mPiece.uri)) {
            KeelLog.d(TAG, "run cancel work:"+mPiece.uri);
            app.mDownloadPool.ActiveThread_Pop();
            return;
        }

        try {
            Bitmap bitmap=ImageCache.getInstance(App.getApp()).getBitmapFromMemCache(mPiece.uri);
            if (null==bitmap) {
                bitmap=ApolloUtils.getImageFetcher(App.getApp()).loadBitmap(mPiece.uri, -1, -1, mPiece.config);
            }

            if (null!=bitmap) {
                /*Bundle bundle=new Bundle();
                bundle.putParcelable("name", bitmap);*/
                SendMessage(mPiece.handler, mPiece.uri, bitmap);
                return;
            } else {
                KeelLog.d(TAG, "image load error:"+mPiece.uri);
            }
        } catch (Exception e) {
            KeelLog.d(TAG, "uri:"+mPiece.uri+" exception:"+e.toString());
        } finally {
            // 默认把它移出，不再下载。
            app.mDownloadPool.ActiveThread_Pop();
        }
    }

    private boolean cancelWork(String uri) {
        WeakReference<ImageView> viewWeakReference=mPiece.mImageReference;//DownloadPool.downloading.get(uri);
        if (null==viewWeakReference||viewWeakReference.get()==null) {
            return true;
        }
        return false;
    }

    public void SendMessage(Handler handler, final String uri, final Bitmap bitmap) {
        if (handler==null) {
            KeelLog.v(TAG, "SendMessage:"+uri+" handler is null:");
            return;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!cancelWork(uri)) {
                    WeakReference<ImageView> viewWeakReference=mPiece.mImageReference;//DownloadPool.downloading.get(uri);
                    ImageView view=(ImageView) viewWeakReference.get();
                    if (null!=view) {
                        //KeelLog.v(TAG, "SendMessage "+uri);
                        view.setImageBitmap(bitmap);
                        //view.setTag(uri);
                    } else {
                        KeelLog.v(TAG, "SendMessage view is null:"+uri);
                    }
                } else {
                    KeelLog.d(TAG, "SendMessage,cancel work:"+uri);
                }
            }
        });
    }
}
