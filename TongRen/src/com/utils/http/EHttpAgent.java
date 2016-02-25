package com.utils.http;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;

import com.tr.App;
import com.tr.R;
import com.utils.log.KeelLog;
import com.utils.string.Base64;
import com.utils.string.StringUtils;


/**
 * @version 1.00.00
 * @description: http访问接口，包括get和post
 * @author: Michael 13-04-19
 */
public class EHttpAgent {
	public static final String SESSIONID = "sessionId";
	public static final int TIMEOUT = 10000;
	private Context mContext;

	/*
	public final String GET_ADDRESS_URL_BY_DITU = "http://ditu.google.cn/";
	public final String GET_ADDRESS_URL_BY_MAP = "http://maps.google.com/";
	*/
	public static String URL = "";
	public static String HTTPS_URL = "";
	
	
	private InputStream is;
	// ����
	public final static String TAG_MAIN_SERVERS = "1";
	// ����
	public final static String TAG_AUXILIARY_SERVERS = "2";
	
	 /** http������ */
    public static final int CODE_HTTP_CODE_SUCCEED = 200;
    /** http�ض��������  */
    public static final int CODE_HTTP_CODE_LOCATION = 300;
    /** ��Ҫ��ʾ���������ʾ */
    public static final String CODE_HTTP_FAIL = "-1";
    /** ��̨������Ҫ��ʾ������� */
    public static final String CODE_HTTP_FAIL_HINT = "-2";
    /** ���ʹ�����Ϣ��bugreport�� */
    public static final String CODE_SEND_ERROR_MESSAGE = "30000";
    /** https��������*/
    public static final String CODE_HTTPS_RECONNECT = "002";
    /** �������errCode */
    public static final String CODE_ERROR_RIGHT = "0";
    /** ����������Ӧ */
    public static final String CODE_SERVER_NOT_RESPONDING = "10000";
    /** �Ự���� */
    public static final String CODE_SESSION_EXPIRED = "2000";
    /** ����ɹ� */
    public static final String CODE_HTTP_SUCCEED = "200";
    
    public static final String CMWAP = "cmwap";
    public static final String CMNET = "cmnet";
    public static final String WAP_3G = "3gwap";
    public static final String UNIWAP = "uniwap";
    public static final String CTWAP = "ctwap";
    public static final String CTNET = "ctnet";
    public static final int TYPE_NET_WORK_DISABLED = 0;// ���粻����
    public static final int TYPE_MNO_CM = 1;// �ƶ�
    public static final int TYPE_MNO_CU = 2;// ��ͨ
    public static final int TYPE_MNO_CT = 3;// ����
    public static final int TYPE_CM_CU_WAP = 4;// �ƶ���ͨwap10.0.0.172
    public static final int TYPE_CT_WAP = 5;// ����wap 10.0.0.200
    public static final int TYPE_OTHER_NET = 6;// ����,�ƶ�,��ͨ,wifi ��net����
    
    public static final Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");

	public EHttpAgent(Context context) {
		this.mContext = context;
		//pref = DXPreferences.getInstance(context);
	}

	public EHttpAgent(Context context, Handler handler) {
		this.mContext = context;
		//pref = DXPreferences.getInstance(context);
	}
	
	public static boolean isAvailable(Context context) {
        final ConnectivityManager cwjManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cwjManager != null) {
            final NetworkInfo[] info = cwjManager.getAllNetworkInfo();
            if (info != null)
            {
                final int size = info.length;
                for (int i = 0; i < size; i++)
                {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
	
	/***
     * �ж�Network�������ͣ���ͨ�ƶ�wap������wap������net��
     * 
     * */
    public static int checkNetworkType(Context mContext) {
        try {
            final ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo mobNetInfoActivity = connectivityManager
                    .getActiveNetworkInfo();
            if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {
                // ��ȡ��������������ʱ��net����
                return TYPE_OTHER_NET;
            } else {
                // NetworkInfo��Ϊnull��ʼ�ж�����������
                final int netType = mobNetInfoActivity.getType();
                if (netType == ConnectivityManager.TYPE_WIFI) {
                    // wifi net����
                    return TYPE_OTHER_NET;
                } else if (netType == ConnectivityManager.TYPE_MOBILE) {

                    // �ж��Ƿ��ǵ��ŵ�wap
                    // Ϊʲô��ͨ��getExtraInfo�ж��Ƿ����wap�ǣ�
                    // ��Ϊͨ��Ŀǰ���ֻ��Ͳ��Ե���apn��ƶ�Ϊ#777����null
                    // ��ݵ���apn���û���
                    final Cursor c = mContext.getContentResolver().query(
                            PREFERRED_APN_URI, null, null, null, null);
                    if (c != null) {
                        c.moveToFirst();
                        final String user = c.getString(c
                                .getColumnIndex("user"));
                        if (!TextUtils.isEmpty(user)) {
                            if (user.startsWith(CTWAP)) {
                                return TYPE_CT_WAP;
                            }
                        }
                        c.close();
                    }

                    // �ж����ֻ��net����wap
                    final String netMode = mobNetInfoActivity.getExtraInfo();
                    if (netMode != null) {
                        // ͨ��apn����ж��Ƿ�����ͨ���ƶ�wap
                        if (netMode.equals(CMWAP)
                                || netMode.equals(WAP_3G)
                                || netMode.equals(UNIWAP)) {
                            return TYPE_CM_CU_WAP;
                        }

                    }

                }
            }
        } catch (Exception ex) {
            return TYPE_OTHER_NET;
        }
        return TYPE_OTHER_NET;
    }

	/**
	 * send message �����ж��Ƿ�200 �����200 �򷵻�����ʧ�� ����� ��errorcode��errorstring
	 * ���ͷ��Ϣ��û������error�򷵻���ȷ��� ����� �򷵻�errocode��errorstring
	 * 
	 * @param action
	 *            ������
	 * @param request
	 *            �������
	 * @param sessionId
	 * @return
	 */
	public synchronized String[] sendMessage(String action, String request, String sessionId) {
	    return sendMessage("", action, request, sessionId);
	}
	
	
	public synchronized String[] sendMessage(String actionUrl, String action, String request, String sessionId) {
		
		//�����ڴ�
		System.gc();
		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
		DefaultHttpClient httpClient = null;
		String state = null;
		String response = null;
		
		String availableServers = "";
		String url;
		if (TextUtils.isEmpty(actionUrl)) {
    		 KeelLog.e("url empty");
    		 url = action;
		} else {
		    url = actionUrl + "/" + action;
		}
		
		if (KeelLog.DEBUG ) {
			KeelLog.d("http dest: " + url);
			KeelLog.Logger("request=======================>");
			KeelLog.Logger(request);
			KeelLog.Logger("URL=" + url);
			KeelLog.Logger("request<=======================");
		}
		
		if (!isAvailable(mContext)) {
			return new String[] {CODE_HTTP_FAIL, "" };
		}
		try {

			HttpParams params = createHttpParams();
			
			HttpPost httpPost = new HttpPost(url);

			if(!TextUtils.isEmpty(App.getUserID())){
            	httpPost.addHeader(mContext.getResources().getString(R.string.userid), App.getUserID());
            }
            
            if(!TextUtils.isEmpty(App.getNick())){
            	httpPost.addHeader(mContext.getResources().getString(R.string.nick), App.getNick());
            }
            
			if (!TextUtils.isEmpty(sessionId)) {
				httpPost.addHeader(SESSIONID, sessionId);
			}
			httpPost.addHeader("Accept-Encoding", "gzip,deflate");
			if (KeelLog.DEBUG ) {
				KeelLog.Logger("DXHttpAgent==>sendMessage==>sessionId=" + sessionId);
			}

			byte[] sendData;

			if (request != null) {
				sendData = request.getBytes("UTF-8");

				ByteArrayEntity byteArrayEntity = new ByteArrayEntity(sendData);
				httpPost.setEntity(byteArrayEntity);
			}

			httpClient = new DefaultHttpClient(params);
			// ���PHP������ó�ʱ 
			httpClient.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, TIMEOUT * 1000); 
			httpClient.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT, TIMEOUT * 1000); 

			
			HttpHost proxy = null;
			
			
			switch(checkNetworkType(mContext)){
			case TYPE_CM_CU_WAP:
				proxy = new HttpHost("10.0.0.172", 80, "http");
				httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
    			break;
			case TYPE_CT_WAP:
				proxy = new HttpHost("10.0.0.200", 80, "http");
				httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
    			break;
			}
			
			HttpResponse httpResponse = httpClient.execute(httpPost);
			StatusLine sl = httpResponse.getStatusLine();
			if (sl == null) {
		        return new String[] { CODE_SERVER_NOT_RESPONDING, "����������Ӧ"};
			}

			int httpCode = sl.getStatusCode();
			if (KeelLog.DEBUG) {
	            KeelLog.e("StatusCode " + httpCode + ", " + url);
	        }
			
			HttpEntity entity = httpResponse.getEntity();
			if(entity == null)
			{
                return new String[] { String.valueOf(httpCode), "���ӷ�����ʧ��" };
			}
			
			if ((CODE_HTTP_CODE_SUCCEED != httpCode)
			        && (httpCode != CODE_HTTP_CODE_LOCATION))
			{
				return new String[] { String.valueOf(httpCode), "����������Ժ�����" };
			}
			
			final long len = entity.getContentLength();
			if (KeelLog.DEBUG ) {
				KeelLog.Logger("DXHttpAgent==>sendMessage==>len=" + len);
			}
			
			state = "" + httpResponse.getStatusLine().getStatusCode();
			Header contentEncoding = httpResponse.getFirstHeader("Content-Encoding");
			try {
				is = entity.getContent();
				if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) { 
					if (KeelLog.DEBUG) {
						KeelLog.e("send gzip ѹ��");
					}
					response = readDataForZgip(is, "UTF-8"); 
				} else {
					if (KeelLog.DEBUG) {
						KeelLog.e("��ѹ��");
					}
					response = readData(is, "UTF-8");
				}
			} catch (OutOfMemoryError e) {
				if (KeelLog.DEBUG) {
					e.printStackTrace();
				}
			}
			
			if (KeelLog.DEBUG ) {
				KeelLog.Logger("response =================>>");
				KeelLog.Logger(response);
				KeelLog.Logger("response <<=================");
			}
			
			 
//			if (TextUtils.isEmpty(availableServers)) {
//				if (URL.equals(MAIN_URL)) {
//					pref.setCurrentAvailableServers(TAG_MAIN_SERVERS);
//				} else if (URL.equals(AUXILIARY_URL)) {
//					pref.setCurrentAvailableServers(TAG_AUXILIARY_SERVERS);
//				}
//			}

		} catch (OutOfMemoryError e) {
			if (KeelLog.DEBUG) {
				e.printStackTrace();
				KeelLog.e(KeelLog.TAG, "DXHttpAgent==>sendMessage==>" + e.toString());
			}
			return new String[] {CODE_HTTP_FAIL_HINT, ""};
		} catch (Exception e) {
			if (KeelLog.DEBUG) {
				e.printStackTrace();
				KeelLog.e(KeelLog.TAG, "DXHttpAgent==>sendMessage==>" + e.toString());
			}
			return new String[] { CODE_HTTP_FAIL_HINT, ""};
		} finally {
			if (httpClient != null) {
				httpClient.getConnectionManager().shutdown();
				httpClient = null;
			}
		}
		return new String[] { state, response };

	}
	
	public String[] getPostNetMessage(String url,String request,int tag) {
        
        //回收内存
        System.gc();
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        String state = null;
        String response = null;
        byte[] responseByteArray;
        DefaultHttpClient httpClient = null;
        
        KeelLog.d("url: " + url);
        
        if (!isAvailable(mContext)) {
            return new String[] { CODE_HTTP_FAIL, "" };
        }

        try {
            HttpParams params = createHttpParams();
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Accept-Language", "zh,zh-cn");
			String sessionid = App.getSessionID();
            
            if(!StringUtils.isEmpty(sessionid)){
            	httpPost.addHeader(mContext.getResources().getString(R.string.session), sessionid);
            }
            if(!TextUtils.isEmpty(App.getUserID())){
            	httpPost.addHeader(mContext.getResources().getString(R.string.userid), App.getUserID());
            }
            if(!TextUtils.isEmpty(App.getNick())){
            	httpPost.addHeader(mContext.getResources().getString(R.string.nick), App.getNick());
            }
            if(tag == EAPIConsts.IMReqType.IM_REQ_FETCHHISTORYMESSAGE_CHAT){
            	httpPost.addHeader("message_type", "0");
            }else if(tag == EAPIConsts.IMReqType.IM_REQ_FETCHHISTORYMESSAGE_MUC){
            	httpPost.addHeader("message_type", "1");
            }
            
            httpPost.addHeader("Accept-Encoding", "gzip,deflate");
            httpPost.addHeader("Accept-Language", "zh,zh-cn");
            if(tag==EAPIConsts.concReqType.CONNECTIONSLIST||tag==EAPIConsts.concReqType.im_upphonebook){
            	params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1200000);
                params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 1200000);
            }else{
            	params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT);
                params.setParameter(CoreConnectionPNames.SO_TIMEOUT, TIMEOUT);
            }
            
            
            httpClient = new DefaultHttpClient(params);
            HttpHost proxy = null;
            switch(checkNetworkType(mContext)){
            case TYPE_CM_CU_WAP:
                proxy = new HttpHost("10.0.0.172", 80, "http");
                httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
                break;
            case TYPE_CT_WAP:
                proxy = new HttpHost("10.0.0.200", 80, "http");
                httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
                break;
            }
            StringEntity e=new StringEntity(request, HTTP.UTF_8);
            httpPost.setEntity(e);
            HttpResponse httpResponse = null;
            try {
            	httpResponse = httpClient.execute(httpPost);
			} catch (Exception e2) {
			}
           
            StatusLine sl = httpResponse.getStatusLine();
            if (sl == null) {
                return new String[] { CODE_HTTP_FAIL, /*mContext.getString(R.string.str_connect_failed)*/ };
            }

            int httpCode = sl.getStatusCode();
            if (KeelLog.DEBUG) {
               KeelLog.e("StatusCode " + httpCode);
            }

            final HttpEntity entity = httpResponse.getEntity();
            if(entity == null)
            {
                return new String[] { String.valueOf(httpCode), null };
            }
            
            if ((CODE_HTTP_CODE_SUCCEED != httpCode)
                    && (httpCode != CODE_HTTP_CODE_LOCATION))
            {
                return new String[] { String.valueOf(httpCode), "服务器连接失败，请重试" };
            }

            final Header ecHeader = httpResponse.getFirstHeader(EAPIConsts.Header.ERRORCODE);
            final Header emHeader = httpResponse.getFirstHeader(EAPIConsts.Header.ERRORMESSAGE);

            String errorCode = null;
            String errorMessage = null;

            if (ecHeader != null) {
                errorCode = ecHeader.getValue();
            }
            if (emHeader != null) {
                errorMessage = emHeader.getValue();
            }

            if (!TextUtils.isEmpty(errorCode) && !CODE_ERROR_RIGHT.equals(errorCode)) {
               
                try {
                    byte datas[]=Base64.decode(errorMessage);
                    errorMessage=new String(datas,"UTF-8");
                } catch (Exception e1) {
                    if (KeelLog.DEBUG) {
                        e1.printStackTrace();
                        KeelLog.e("Base64.decode error : " + e.toString());
                    }
                    errorMessage = new String(errorMessage.getBytes("iso-8859-1"), "UTF-8");
                }
                
                if (errorCode.equals("null")) {
                    errorCode = CODE_HTTPS_RECONNECT;
                    errorMessage = "";
                }
                return new String[] {errorCode, errorMessage };
            }

            final long len = entity.getContentLength();
			if (KeelLog.DEBUG ) {
				KeelLog.Logger("DXHttpAgent==>sendMessage==>len=" + len);
			}
			
			state = "" + httpResponse.getStatusLine().getStatusCode();
			Header contentEncoding = httpResponse.getFirstHeader("Content-Encoding");
			try {
				is = entity.getContent();
				if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) { 
					if (KeelLog.DEBUG) {
						KeelLog.e("send gzip");
					}
					response = readDataForZgip(is, "UTF-8"); 
				} else {
					if (KeelLog.DEBUG) {
						KeelLog.e("send unzip");
					}
					response = readData(is, "UTF-8");
				}
			} catch (OutOfMemoryError ex) {
				if (KeelLog.DEBUG) {
					ex.printStackTrace();
				}
			}
			
			if (KeelLog.DEBUG ) {
				KeelLog.Logger("response =================>>");
				KeelLog.Logger(response);
				KeelLog.Logger("response <<=================");
			}
          
        } catch (OutOfMemoryError e) {
            if (KeelLog.DEBUG) {
                e.printStackTrace();
                KeelLog.e(KeelLog.TAG, "DXHttpAgent==>sendMessage==>" + e.toString());
            }
            return new String[] { CODE_HTTP_FAIL, "" }; 
        } catch (Exception e) {
            if (KeelLog.DEBUG) {
                e.printStackTrace();
                KeelLog.e(KeelLog.TAG, "DXHttpAgent==>sendMessage==>" + e.toString());
            }
            return new String[] { CODE_HTTP_FAIL, "" }; 
        } finally {
            if (KeelLog.DEBUG) {
                KeelLog.i("DXHttpAgent==>sendMessage==>finally");
            }
            if (is != null) {
                try {
                    is.close();
                    is = null;
                } catch (IOException e) {
                    if (KeelLog.DEBUG) {
                        e.printStackTrace();
                        KeelLog.e(KeelLog.TAG, "DXHttpAgent==>sendMessage==>close is err", e);
                    }
                }
            }
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
                httpClient = null;
            }
        }
        return new String[] { state, response };
    }	
	
	public String[] getNetMessage(String url) {
        
        //回收内存
        System.gc();
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        String state = null;
        String response = null;
        byte[] responseByteArray;
        DefaultHttpClient httpClient = null;

        //String url = "http://sns.demo.cibntv.tv:8080/yst-search/program!getRetrievalProgramSerialListByKeywordXML.action?year=-1&area=-1&type=%E7%94%B5%E5%BD%B1&classType=-1&source=-1&content=-1&pageNumber=0&pageSize=18";

//        if (!TextUtils.isEmpty(action)) {
//            url = GET_ADDRESS_URL_BY_DITU + action;
//        }
        
        if (!isAvailable(mContext)) {
            return new String[] { CODE_HTTP_FAIL, "" };
        }

        try {
            HttpParams params = createHttpParams();
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Accept-Language", "zh,zh-cn");
            
            params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 25000);
            params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 25000);
            
            httpClient = new DefaultHttpClient(params);
            HttpHost proxy = null;
            switch(checkNetworkType(mContext)){
            case TYPE_CM_CU_WAP:
                proxy = new HttpHost("10.0.0.172", 80, "http");
                httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
                break;
            case TYPE_CT_WAP:
                proxy = new HttpHost("10.0.0.200", 80, "http");
                httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
                break;
            }
            
            HttpResponse httpResponse = httpClient.execute(httpGet);
            StatusLine sl = httpResponse.getStatusLine();
            if (sl == null) {
                return new String[] { CODE_HTTP_FAIL, /*mContext.getString(R.string.str_connect_failed)*/ };
            }

            int httpCode = sl.getStatusCode();
            if (KeelLog.DEBUG) {
               KeelLog.e("StatusCode " + httpCode);
            }

            final HttpEntity entity = httpResponse.getEntity();
            if(entity == null)
            {
                return new String[] { String.valueOf(httpCode), null };
            }
            
            if ((CODE_HTTP_CODE_SUCCEED != httpCode)
                    && (httpCode != CODE_HTTP_CODE_LOCATION))
            {
                return new String[] { String.valueOf(httpCode), "服务器连接失败，请重试" };
            }

            is = entity.getContent();
            
            state = "" + httpResponse.getStatusLine().getStatusCode()/*String.valueOf(HttpURLConnection.HTTP_OK)*/;

            ByteArrayBuffer bab = new ByteArrayBuffer(1024);
            int line = -1;

            responseByteArray = new byte[1024];
            while ((line = is.read(responseByteArray)) != -1) {
                bab.append(responseByteArray, 0, line);
                responseByteArray = new byte[1024];
            }

            byte[] tmp = bab.toByteArray();
            
            final int len = tmp.length;
           
            response = new String(tmp, "UTF-8");
            bab = null;
          
        } catch (OutOfMemoryError e) {
            if (KeelLog.DEBUG) {
                e.printStackTrace();
                KeelLog.e(KeelLog.TAG, "DXHttpAgent==>sendMessage==>" + e.toString());
            }
            return new String[] { CODE_HTTP_FAIL, "" }; 
        } catch (Exception e) {
            if (KeelLog.DEBUG) {
                e.printStackTrace();
                KeelLog.e(KeelLog.TAG, "DXHttpAgent==>sendMessage==>" + e.toString());
            }
            return new String[] { CODE_HTTP_FAIL, "" }; 
        } finally {
            if (KeelLog.DEBUG) {
                KeelLog.i("DXHttpAgent==>sendMessage==>finally");
            }
            if (is != null) {
                try {
                    is.close();
                    is = null;
                } catch (IOException e) {
                    if (KeelLog.DEBUG) {
                        e.printStackTrace();
                        KeelLog.e(KeelLog.TAG, "DXHttpAgent==>sendMessage==>close is err", e);
                    }
                }
            }
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
                httpClient = null;
            }
        }
        return new String[] { state, response };
    }
	
	/**
     * �ϴ�ͼƬ
     * 
     * @param actionUrl
     * @param imageDdata
     * @param imageTitle
     * @param entityType
     * @param entityID
     * @param imageFileSuffix
     * @return
     */
	public synchronized String[] uploadImage(String action, byte[] imageDdata, String imageTitle,
            String entityType, String entityID, String imageFileSuffix, ArrayList<String> listSyncService,
            Handler handler, String messageImageID) {
        final String lineEnd = "\r\n";
        final String boundary = Double.toString(System.currentTimeMillis());
        final String MPboundary = "--" + boundary;
        final String endMPboundary = MPboundary + "--";

        HttpURLConnection con = null;
        DataOutputStream dos = null;
        InputStream ins = null;
        String state = "";
        String response = "";
        try {

            String actionUrl = URL + "/" + action;

            String host = "";
            switch(checkNetworkType(mContext)){
            case TYPE_CM_CU_WAP:
                if ((host = findString(actionUrl, "http://", "/")).indexOf(":") < 0) {
                  host += ":80";
                  actionUrl = new StringBuffer().append("http://10.0.0.172:80/")
                  .append(findString(actionUrl, "/", 10)).toString();
                }
                break;
            case TYPE_CT_WAP:
                if ((host = findString(actionUrl, "http://", "/")).indexOf(":") < 0) {
                    host += ":80";
                    actionUrl = new StringBuffer().append("http://10.0.0.200:80/")
                    .append(findString(actionUrl, "/", 10)).toString();
                 }
                break;
            }
            
            final URL url = new URL(actionUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(TIMEOUT * 1000);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Charset", "UTF-8");  
            con.setRequestMethod("POST");
            con.setRequestProperty("Connection", "Close");
            con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            con.setRequestProperty("Accept-Encoding", "gzip,deflate");
            
            /*
            if (!TextUtils.isEmpty(pref.getSessionId())) {
                con.setRequestProperty(SESSIONID, pref.getSessionId());
            }
            if (KeelLog.DEBUG ) {
                KeelLog.Logger("DXHttpAgent==>sendHttpsMessage==>sessionId=" + pref.getSessionId() );
            }
            */
            
            if (!TextUtils.isEmpty(host)) {
                con.setRequestProperty("X-online-Host", host);
                con.setRequestProperty("Accept", "*/*");
            }
            
            if (KeelLog.DEBUG ) {
                KeelLog.Logger("request=======================>");
                KeelLog.Logger("��ʼ�ϴ�ͼƬ");
                KeelLog.d("URL=" + actionUrl);
                KeelLog.Logger("request<=======================");
            }

            dos = new DataOutputStream(con.getOutputStream());
            if (imageDdata != null) {
                // write image file info
                dos.writeBytes(MPboundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"imageFile\";filename=\"" + "uploadImage" + "\"" + lineEnd);
                dos.writeBytes("Content-Type: image/png, image/jpeg, image/gif " + lineEnd + lineEnd);
                dos.write(imageDdata, 0, imageDdata.length);
                dos.writeBytes(lineEnd);
            }
            
            if (listSyncService != null) {
                // write listSyncService info
                dos.writeBytes(MPboundary + lineEnd);
                dos.writeBytes("content-disposition: form-data; name=\"listSyncService\"" + lineEnd + lineEnd);
                dos.writeBytes(listSyncService.toString() + lineEnd);
            }

            // write imageTitle info
            dos.writeBytes(MPboundary + lineEnd);
            dos.writeBytes("content-disposition: form-data; name=\"imageTitle\"" + lineEnd + lineEnd);
            dos.writeBytes(imageTitle + lineEnd);
            
            // write entityType info
            dos.writeBytes(MPboundary + lineEnd);
            dos.writeBytes("content-disposition: form-data; name=\"entityType\"" + lineEnd + lineEnd);
            dos.writeBytes(entityType + lineEnd);
            
            // write imageFileSuffix info
            dos.writeBytes(MPboundary + lineEnd);
            dos.writeBytes("content-disposition: form-data; name=\"imageFileSuffix\"" + lineEnd + lineEnd);
            dos.writeBytes(imageFileSuffix + lineEnd);
            
            if (!TextUtils.isEmpty(messageImageID)) {
                // write messageImageID info
                dos.writeBytes(MPboundary + lineEnd);
                dos.writeBytes("content-disposition: form-data; name=\"messageImageID\"" + lineEnd + lineEnd);
                dos.writeBytes(messageImageID + lineEnd);
            }

            // write entityID info
            dos.writeBytes(MPboundary + lineEnd);
            dos.writeBytes("content-disposition: form-data; name=\"entityID\"" + lineEnd + lineEnd);
            dos.writeBytes(entityID);
            dos.writeBytes(lineEnd + endMPboundary);
            
            dos.flush();
            dos.close();
            
            final int len = con.getContentLength();
            if (KeelLog.DEBUG ) {
                KeelLog.Logger("DXHttpAgent==>sendHttpsMessage==>len=" + len);
            }
            state = "" + con.getResponseCode();
            ins = con.getInputStream();
            
            final String contentEncoding = con.getContentEncoding();
            if (contentEncoding.equalsIgnoreCase("gzip")) { 
                if (KeelLog.DEBUG) {
                    KeelLog.e("gzip ѹ��");
                }
                response = readDataForZgip(ins, "UTF-8", handler); 
            } else {
                if (KeelLog.DEBUG) {
                    KeelLog.e("��ѹ��");
                }
                response = readData(ins, "UTF-8");
            }

            if (KeelLog.DEBUG ) {
                KeelLog.Logger("response =================>>");
                KeelLog.Logger(response);
                KeelLog.Logger("response <<=================");
            }
            
        } catch (MalformedURLException e) {
            if (KeelLog.DEBUG) {
                e.printStackTrace();
            }
            return new String[] {CODE_HTTP_FAIL_HINT, "" };
        } catch (IOException e) {
            if (KeelLog.DEBUG) {
                e.printStackTrace();
            }
            return new String[] {CODE_HTTP_FAIL_HINT, "" };
        } catch (Exception e) {
            if (KeelLog.DEBUG) {
                e.printStackTrace();
            }
            return new String[] {CODE_HTTP_FAIL_HINT, "" };
        } finally {
            if (ins != null) {
                try {
                    ins.close();
                } catch (Exception e) {
                    if (KeelLog.DEBUG) {
                        e.printStackTrace();
                    }
                }
                ins = null;
            }
            if (con != null) {
                con.disconnect();
                con = null;
            }
        }
        return new String[] {state, response };
    }
	
	 //��һ������Ϊ������,�ڶ�������Ϊ�ַ����
	public String readData(InputStream inSream, String charsetName) throws Exception {
		final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		final byte[] buffer = new byte[1024];
		int len = -1;
		try{
    		while ((len = inSream.read(buffer)) != -1) {
    			outStream.write(buffer, 0, len);
    		}
		} catch(IOException e) {
			if (KeelLog.DEBUG) {
			    e.printStackTrace();
			}
		}
		
		final byte[] data = outStream.toByteArray();
		outStream.close();
		inSream.close();
		
		return new String(data, charsetName);
	}
	
	//��һ������Ϊ������,�ڶ�������Ϊ�ַ����
	public String readDataForZgip(InputStream inStream, String charsetName) throws Exception {
	    return readDataForZgip(inStream, charsetName, null);
	}
	public String readDataForZgip(InputStream inStream, String charsetName, Handler handler) throws Exception {
		String data1 = null;
		byte[] data = null;
		try{
			final GZIPInputStream gzipStream = new GZIPInputStream(inStream);
			final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			
			final byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = gzipStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			
			data = outStream.toByteArray();
			outStream.close();
			gzipStream.close();
			inStream.close();
			data1 = new String(data, charsetName);
		}catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		
		if (data != null) {
		    data = null;
		}
		
		return data1;
	}
	
	private HttpParams createHttpParams() {
		final HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setStaleCheckingEnabled(params, false);
		HttpConnectionParams.setConnectionTimeout(params, TIMEOUT * 1000);
		HttpConnectionParams.setSoTimeout(params, TIMEOUT * 1000);
		HttpConnectionParams.setSocketBufferSize(params, 8192 * 5);
		return params;
	}
	
	/************************************Https******************************************************/
	
	public synchronized String[] sendHttpsMessage(String action, String request, String sessionId) {
		//�����ڴ�
		System.gc();
		android.os.Process
				.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
		String state = null;
		String response = null;
		HttpsURLConnection cnx = null;

		String url = HTTPS_URL + "/" + action;
		
		if (KeelLog.DEBUG ) {
			KeelLog.Logger("request=======================>");
			KeelLog.Logger(request);
			KeelLog.Logger("URL=" + url);
			KeelLog.Logger("request<=======================");
		}
		
		if (!isAvailable(mContext)) {
			return new String[] { CODE_HTTP_FAIL, "" };
		}
		byte[] responseByteArray = request.getBytes();
		
		InputStream ins = null;
		
		try {
			cnx = getConnection(url);
			cnx.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			cnx.setRequestProperty("Content-Length", String.valueOf(responseByteArray.length));
			cnx.setRequestProperty("Accept-Encoding", "gzip,deflate");
			
			if (sessionId != null) {
			    cnx.setRequestProperty(SESSIONID, sessionId);
			}
			if (KeelLog.DEBUG ) {
				KeelLog.Logger("DXHttpAgent==>sendHttpsMessage==>sessionId=" + sessionId);
			}

			((HttpURLConnection) cnx).setRequestMethod("POST");
			cnx.setDoOutput(true);
			cnx.setDoInput(true);
			cnx.connect();

			// �ѷ�װ�õ�ʵ����ݷ��͵������
			OutputStream outStream = cnx.getOutputStream();
			outStream.write(responseByteArray);
			outStream.flush();
			outStream.close();
	
			final int len = cnx.getContentLength();
			if (KeelLog.DEBUG ) {
				KeelLog.Logger("DXHttpAgent==>sendHttpsMessage==>len=" + len);
			}
			 
			state = "" + cnx.getResponseCode()/*String.valueOf(HttpsURLConnection.HTTP_OK)*/;
			ins = cnx.getInputStream();
			 final String contentEncoding = cnx.getContentEncoding();
	            if (contentEncoding.equalsIgnoreCase("gzip")) { 
	                if (KeelLog.DEBUG) {
	                    KeelLog.e("gzip 压缩");
	                }
	                response = readDataForZgip(ins, "UTF-8"); 
	            } else {
	                if (KeelLog.DEBUG) {
	                    KeelLog.e("不压缩");
	                }
	                response = readData(ins, "UTF-8");
	                
	            }

			if (KeelLog.DEBUG ) {
				KeelLog.Logger("response =================>>");
				KeelLog.Logger(response);
				KeelLog.Logger("response <<=================");
			}
			
			 // ��ǰû�ÿ��õĵ�ַ������ʹ�øÿͻ���ʱ�õ�
			/*
			if (TextUtils.isEmpty(availableServers)) {
				if (HTTPS_URL.equals(MAIN_HTTPS_URL)) {
					pref.setCurrentAvailableServers(TAG_MAIN_SERVERS);
				} else if (HTTPS_URL.equals(AUXILIARY_HTTPS_URL)){
					pref.setCurrentAvailableServers(TAG_AUXILIARY_SERVERS);
				}
			}
			*/

		} catch (KeyManagementException e) {
			if (KeelLog.DEBUG) {
				e.printStackTrace();
				KeelLog.e(KeelLog.TAG, "DXHttpAgent==>sendHttpsMessage==>" + e.toString());
			}
			return new String[] { CODE_SEND_ERROR_MESSAGE, "" + e.toString()};
		} catch (NoSuchAlgorithmException e) {
			if (KeelLog.DEBUG) {
				e.printStackTrace();
				KeelLog.e(KeelLog.TAG, "DXHttpAgent==>sendHttpsMessage==>" + e.toString());
			}
			return new String[] { CODE_SEND_ERROR_MESSAGE, "" + e.toString()}; 
		} catch (IOException e) {
			if (KeelLog.DEBUG) {
				e.printStackTrace();
				KeelLog.e(KeelLog.TAG, "DXHttpAgent==>sendHttpsMessage==>" + e.toString());
			}
			return new String[] { CODE_SEND_ERROR_MESSAGE, "" + e.toString()};
		} catch (Exception e) {
			if (KeelLog.DEBUG) {
				e.printStackTrace();
				KeelLog.e(KeelLog.TAG, "DXHttpAgent==>sendHttpsMessage==>" + e.toString());
			}
			return new String[] { CODE_SEND_ERROR_MESSAGE, "" + e.toString()};
		} finally {
			if (ins != null) {
                try {
                    ins.close();
                } catch (Exception e) {
                    if (KeelLog.DEBUG) {
                        e.printStackTrace();
                    }
                }
                ins = null;
            }
			if (cnx != null) {
				cnx.disconnect();
				cnx = null;
			}
		}
		return new String[] { state, response };
	}

//	ʹ��HttpsURLConnectionʱ��Ҫʵ��HostnameVerifier �� X509TrustManager��������ʵ���Ǳ���ģ�Ҫ���ᱨ��ȫ��֤�쳣��
//	Ȼ���ʼ��X509TrustManager�е�SSLContext��Ϊjavax.net.ssl.HttpsURLConnection����Ĭ�ϵ�SocketFactory��HostnameVerifier
	private static final TrustManager[] TRUST_MANAGER = { new NaiveTrustManager() };
	private static final AllowAllHostnameVerifier HOSTNAME_VERIFIER = new AllowAllHostnameVerifier();

	private static HttpsURLConnection getConnection(String url)
			throws IOException, NoSuchAlgorithmException,
			KeyManagementException {
		//�����ڴ�
		System.gc();
		final HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection();
		if (conn instanceof HttpsURLConnection) {
			// Trust all certificates
			final SSLContext context = SSLContext.getInstance("TLS");
			context.init(new KeyManager[0], TRUST_MANAGER, new SecureRandom());
			final SSLSocketFactory socketFactory = context.getSocketFactory();
			(conn).setSSLSocketFactory(socketFactory);
			// Allow all hostnames
			(conn).setHostnameVerifier(HOSTNAME_VERIFIER);
			
		}
		conn.setConnectTimeout(TIMEOUT * 1000);
		conn.setReadTimeout(TIMEOUT * 1000);
		return conn;
	}

	public static class NaiveTrustManager implements X509TrustManager {
		private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[] {};

		public void checkClientTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
		}

		public boolean isClientTrusted(X509Certificate[] chain) {
			return (true);
		}

		public boolean isServerTrusted(X509Certificate[] chain) {
			return (true);
		}

		public X509Certificate[] getAcceptedIssuers() {
			return (_AcceptedIssuers);
		}
	}
	
	public static String findString(String content, String startStr, int startPos) {
        int pos;
        if ((pos = content.indexOf(startStr, startPos)) == -1) {
            pos = 0;
        }
        return content.substring(pos + 1);
    }

    public static String findString(String mainStr, String from, String to) {
        int pos;
        if ((pos = mainStr.indexOf(from)) == -1) {
            return "";
        }
        pos += from.length();
        int j1;
        if ((j1 = mainStr.indexOf(to, pos + 1)) == -1) {
            j1 = mainStr.length();
        }
        return mainStr.substring(pos, j1);
    }

}
