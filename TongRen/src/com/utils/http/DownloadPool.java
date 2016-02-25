package com.utils.http;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpVersion;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

import com.tr.App;
import com.utils.image.cache.ImageCache;
import com.utils.log.KeelLog;

/**
 * 图片下载线程池,暂时可允许最多三个线程同时下载。
 *
 * @author archko
 */
public class DownloadPool extends Thread {

    public static final String TAG="DownloadPool";
    public static final int MAX_THREAD_COUNT=2;
    //private DefaultHttpClient httpClient;
    private int mActiveThread=0;
    private App mApp;
    private List<DownloadPiece> mQuery;
    private HttpParams params;
    ClientConnectionManager connectionManager;
    private boolean isStop=false;
    public static final int READ_TIMEOUT=20000;
    public static final int CONNECT_TIMEOUT=20000;
    //public static Map<String, WeakReference<View>> downloading=new Hashtable<String, WeakReference<View>>();

    {
        params=new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "UTF-8");
        HttpProtocolParams.setUseExpectContinue(params, true);

        params.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        params.setBooleanParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
        params.setIntParameter(ClientPNames.MAX_REDIRECTS, 100);

        //  params.setLongParameter(ClientPNames.conCONNECTION_MANAGER_TIMEOUT, TIMEOUT);
        //  HttpConnectionManagerParams.setMaxTotalConnections(params, 3000);

        //  HttpConnectionParams.setSoTimeout(params, 60*1000);
        //  HttpConnectionParams.setConnectionTimeout(params, 60*1000); 
        //  ConnManagerParams.setTimeout(params, 60*1000); 

        HttpConnectionParams.setConnectionTimeout(params, CONNECT_TIMEOUT);// Set the default socket timeout (SO_TIMEOUT) // in milliseconds which is the timeout for waiting for data.
        HttpConnectionParams.setSoTimeout(params, READ_TIMEOUT);
    }

    private final static SchemeRegistry createRegitry() {
        SchemeRegistry schemeRegistry=new SchemeRegistry();
        // Register the "http" and "https" protocol schemes, they are
        // required by the default operator to look up socket factories.
        SocketFactory sf=PlainSocketFactory.getSocketFactory();
        schemeRegistry.register(new Scheme("http", sf, 80));
        //    sf = SSLSocketFactory.getSocketFactory();

        SSLSocketFactory ssf=SSLSocketFactory.getSocketFactory();
        schemeRegistry.register(new Scheme("https", ssf, 443));

        return schemeRegistry;
    }

    public DownloadPool(App app) {
        this.mQuery=new ArrayList<DownloadPiece>();
        //this.httpClient=new DefaultHttpClient(params);
        this.mApp=app;

        connectionManager=new ThreadSafeClientConnManager(params, createRegitry());
    }

    public void setStop(boolean stop) {
        synchronized (this) {
            notifyAll();
        }
        isStop=stop;
    }

    public void ActiveThread_Pop() {
        synchronized (this) {
            int i=this.mActiveThread-1;
            this.mActiveThread=i;
            notifyAll();
        }
    }

    public void ActiveThread_Push() {
        synchronized (this) {
            mActiveThread++;
        }
        KeelLog.v(TAG, "download mActiveThread:"+mActiveThread);
    }

    public DownloadPiece Get(int paramInt) {
        synchronized (this) {
            int size=this.mQuery.size();
            if (paramInt<=size) {
                return mQuery.get(paramInt);
            }
        }
        return null;
    }

    public int GetCount() {
        synchronized (this) {
            return mQuery.size();
        }
    }

    public int GetThreadCount() {
        synchronized (this) {
            return mActiveThread;
        }
    }

    public DownloadPiece Pop() {
        synchronized (this) {
            DownloadPiece downloadPiece=(DownloadPiece) this.mQuery.get(0);
            this.mQuery.remove(0);
            return downloadPiece;
        }
    }

    /**
     * 清除未下载的列表,只保留5个等待队列.
     */
    public void PopPiece() {
        synchronized (this) {
            int size=mQuery.size();
            mQuery=mQuery.subList(size-6, size);
        }
    }

    /**
     * 清除队列所有的任务，用于主页的清除按钮，
     */
    public void cleanAllQuery() {
        synchronized (this) {
            mQuery.clear();
            //downloading.clear();
            notifyAll();
        }
    }

    /**
     * 添加下载的url
     *
     * @param handler  回调用的
     * @param uri      图片url
     * @param type     图片的类型，是微博图片，还是转发内容图片或者用户头像等
     * @param dir      图片存储目录，废除，不需要再靠图片的url与目录计算出存储路径，减少运算
     * @param filepath 图片存储的路径，绝对的
     * @param cache    是否缓存
     */
    public void Push(Handler handler, String uri, int type, String filepath, boolean cache, String dir) {
        synchronized (this) {   //这里的同步造成了ui缓慢。
            /*for (DownloadPiece piece : mQuery) {    //在这里先排除总比在FrechImg_Impl()中查找文件要快.
                if (piece.uri.equals(uri)) {
                    KeelLog.d(TAG, "已经存在url:"+uri);
                    mQuery.remove(piece);
                    break;
                }
            }*/
            DownloadPiece piece=new DownloadPiece(handler, uri, type, filepath, cache, dir);
            mQuery.add(piece);

            notifyAll();
        }
    }

    /**
     * 加入下载队列
     *
     * @param uri       下载的url
     * @param imageView 显示的View
     * @param handler
     * @param config    图片下载使用的解析配置
     */
    public void Push(String uri, ImageView imageView, Handler handler, Bitmap.Config config) {
        synchronized (this) {   //这里的同步造成了ui缓慢。
            //downloading.put(uri, new WeakReference<View>(imageView));
            DownloadPiece piece=new DownloadPiece(uri, handler, config, imageView);
            mQuery.add(piece);

            notifyAll();
        }
    }

    public void Push(String uri, ImageView imageView, Handler handler) {
        //downloading.put(uri, new WeakReference<View>(imageView));
        synchronized (this) {   //这里的同步造成了ui缓慢。
            for (DownloadPiece piece : mQuery) {    //在这里先排除总比在FrechImg_Impl()中查找文件要快.
                if (piece.uri.equals(uri)) {
                    //KeelLog.v(TAG, "已经存在url:"+uri);
                    mQuery.remove(piece);
                    break;
                }
            }
            DownloadPiece piece=new DownloadPiece(uri, handler, Bitmap.Config.RGB_565, imageView);
            mQuery.add(piece);

            notifyAll();
        }
    }

    @Override
    public void run() {
        KeelLog.d(TAG, "download.");
        while (true) {
            synchronized (this) {
                if (isStop) {
                    KeelLog.d(TAG, "downloadpool stop.");
                    break;
                }

                notifyAll();
                if ((GetCount()!=0)&&(GetThreadCount()<=MAX_THREAD_COUNT)) {
                    DownloadPiece piece=Pop();
                    FrechImg_Impl(piece);
                } else {
                    //KeelLog.v(TAG, "download wait."+GetThreadCount());
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public class DownloadPiece {

        Handler handler;
        //public String name; //md5加密过的名字,废除
        public String dir;//存储目录
        public String uri;//图片的url
        public int type;// 图片的类型，是微博图片，还是转发内容图片或者用户头像等
        public boolean cache;
        public Bitmap.Config config;
        public WeakReference<ImageView> mImageReference;

        /**
         * 构造一个下载实体
         *
         * @param h        回调
         * @param uri      图片的下载地址
         * @param type     类型，目前有头像与微博图片
         * @param filepath 图片的路径，废除了，现在在这里对图片进行解码，而不用绝对路径，用目录与url进行构造
         * @param cache    是否缓存
         * @param dir      存储目录，这是图片存储的绝对目录，可以由url+dir计算得到存储的绝对路径。
         */
        public DownloadPiece(Handler h, String uri, int type, String filepath, boolean cache, String dir) {
            this.uri=uri;
            this.handler=h;
            this.type=type;
            this.cache=cache;
            this.dir=dir;
        }

        public DownloadPiece(String uri, Handler h, Bitmap.Config config, ImageView imageView) {
            this.uri=uri;
            this.handler=h;
            this.config=config;
            mImageReference=new WeakReference<ImageView>(imageView);
        }
    }

    private void FrechImg_Impl(DownloadPiece piece) {
        final String uri=piece.uri;
        KeelLog.v(TAG, "FrechImg_Impl:"+uri);
        if (uri==null) {
            KeelLog.w(TAG, "名字不存在。");
            return;
        }

        final WeakReference<ImageView> viewWeakReference=piece.mImageReference;//downloading.get(uri);
        if (null==viewWeakReference) {
            KeelLog.i(TAG, "viewWeakRef is null."+uri);
            //downloading.remove(uri);
            return;
        }

        if (null==viewWeakReference.get()) {
            KeelLog.i(TAG, "viewWeakRef get is null."+uri);
            //downloading.remove(uri);
            return;
        }

        final Bitmap bitmap=ImageCache.getInstance(App.getApp()).getBitmapFromMemCache(uri);
        if (null!=bitmap) {
            //Bundle bundle=new Bundle();
            //bundle.putParcelable("name", bitmap);
            //FetchImage.SendMessage(handler, type, bundle, uri);

            if (null!=viewWeakReference&&null!=viewWeakReference.get()&&null!=piece.handler) {
                piece.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ImageView view=(ImageView) viewWeakReference.get();
                        if (null!=view) {
                            KeelLog.v(TAG, "mem:"+uri);
                            view.setImageBitmap(bitmap);
                            //view.setTag(uri);
                        } else {
                            KeelLog.v(TAG, "view is null;"+uri);
                        }
                    }
                });
            } else {
                KeelLog.v(TAG, "null==viewWeakReference:"+uri);
            }
            return;
        }

        //synchronized (this) {
        mApp.mDownloadPool.ActiveThread_Push();
            /*String str3=Uri.encode(uri, ":/");
            HttpGet httpGet=new HttpGet(str3);
            //httpGet.setHeader("User-Agent", BaseApi.USERAGENT);
            DefaultHttpClient httpClient=new DefaultHttpClient(connectionManager, params);*/
        //FetchImage fetchImage=new FetchImage(mApp, handler, httpClient, httpGet, type, imagepath, uri, cache);
        FetchImage fetchImage=new FetchImage(mApp, piece);
        fetchImage.setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        fetchImage.start();
            /*if (null!=viewWeakReference&&null!=viewWeakReference.get()) {
                piece.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ApolloUtils.getImageFetcher(App.getApp()).loadHomeImage(uri, (ImageView) viewWeakReference.get());
                    }
                });
            }*/
        //}
    }
}
