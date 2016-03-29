package com.tongmeng.alliance.util;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tongmeng.alliance.dao.ApplyForm;
import com.tongmeng.alliance.dao.ApplyLabel;
import com.tongmeng.alliance.dao.CancleActivity;
import com.tongmeng.alliance.dao.Category;
import com.tongmeng.alliance.dao.ConferenceClass;
import com.tongmeng.alliance.dao.DemandTag;
import com.tongmeng.alliance.dao.HotComment;
import com.tongmeng.alliance.dao.InteresteDao;
import com.tongmeng.alliance.dao.Label;
import com.tongmeng.alliance.dao.Meeting;
import com.tongmeng.alliance.dao.MyCreateActivitis;
import com.tongmeng.alliance.dao.MyCustomerList;
import com.tongmeng.alliance.dao.MyNoteList;
import com.tongmeng.alliance.dao.MyparticipateActivity;
import com.tongmeng.alliance.dao.OptionDao;
import com.tongmeng.alliance.dao.ParticipantsHistory;
import com.tongmeng.alliance.dao.PayForm;
import com.tongmeng.alliance.dao.ServerResultDao;
import com.tongmeng.alliance.dao.SupplyTag;
import com.utils.image.cache.ImageCache.RetainFragment;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class Utils {

	/**
	 * 获取报名表单详情
	 * 
	 * @param result
	 * @return
	 */
	public static ApplyForm getApplyForm(String s) {
		ApplyForm form = new ApplyForm();

		try {
			JSONObject reObj = new JSONObject(getContent(s));
			String result = reObj.getString("applyForm");
			JSONObject job = new JSONObject(result);
			form.id = job.getInt("id");
			form.fee = job.getString("fee");
			form.startTime = job.getString("startTime");
			form.title = job.getString("title");
			form.address = job.getString("address");
			form.province = job.getString("province");
			form.endTime = job.getString("endTime");
			form.city = job.getString("city");
			String listStr = job.getString("applyLabelList");
			Log.e("Util>>>>>>>>>", "listStr::" + listStr);
			form.list = new ArrayList<ApplyLabel>();
			if (listStr != null && !"".equals(listStr)) {
				JSONArray array = new JSONArray(listStr);
				JSONObject jsonObj;
				for (int i = 0; i < array.length(); i++) {
					ApplyLabel label = new ApplyLabel();
					/**
					 * {"limit":10,"type":1,"optionList" :null,"name":"姓名"}
					 */
					jsonObj = (JSONObject) array.get(i);
					Log.e("Util>>>>>>>>>", "jsonObj::" + jsonObj.toString());

					label.limit = jsonObj.getInt("limit");
					label.type = jsonObj.getInt("type");
					label.name = jsonObj.getString("name");
					label.option = jsonObj.getString("optionList");

					form.list.add(label);
				}
			} else {
				form.list = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}

		return form;
	}

	/**
	 * 获取支付表单
	 * 
	 * @param result
	 * @return
	 */
	public static PayForm getPayForm(String result) {
		try {
			Log.e("", "获取信息：：" + getContent(result));
			String payForm = new JSONObject(getContent(result))
					.getString("payForm");
			Log.e("", "活动详情：：" + payForm);
			Gson gson = new Gson();
			return gson.fromJson(payForm, PayForm.class);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 调用结果成功则获取返回数据中 responseData的数据，失败返回提示信息
	 * 
	 * @param result
	 * @return
	 */
	public static String getContent(String result) {
		try {
			JSONObject obj = new JSONObject(result);
			String notification = obj.getString("notification");
			if (TextUtils.equals("0001",
					new JSONObject(notification).getString("notifyCode"))) {
				return obj.getString("responseData");
			} else {
				return new JSONObject(notification).getString("notifyInfo");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// 数组转换ArrayList
	public static ArrayList<String> arrToList(String[] strs) {
		if (strs.equals(null) || strs.length == 0) {
			return null;
		}
		ArrayList<String> list = new ArrayList<String>();
		for (String s : strs) {
			list.add(s);
		}
		return list;
	}

	/**
	 * 判断两个String类型时间是否相同
	 * 
	 * @param DATE1
	 * @param DATE2
	 * @return
	 */
	public static int compare_date(String DATE1, String DATE2) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				System.out.println("DATE1  在 DATE2 后");
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				System.out.println("DATE1  在 DATE2 前");
				return -1;
			} else {
				System.out.println("两者时间相同");
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	/**
	 * 判断一个String类型字符串是否是合法的日期格式
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isValidDate(String str) {
		boolean convertSuccess = true;
		// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			// 设置lenient为false.
			// 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
			format.setLenient(false);
			format.parse(str);
		} catch (ParseException e) {
			// e.printStackTrace();
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			convertSuccess = false;
		}
		return convertSuccess;
	}

	// List<String>转化为json数组
	public static String ListToJson(List<String> list) {
		String s = "[\"";
		if (list == null) {
			return "[]";
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (i == list.size() - 1) {
					s = s + list.get(i) + "\"]";
				} else {
					s = s + list.get(i) + "\",\"";
				}
			}
			return s;
		}
	}

	public static String ListToJsondemand(List<DemandTag> list) {
		String s = "[\"";
		if (list == null) {
			return "[]";
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (i == list.size() - 1) {
					s = s + list.get(i).getName() + "\"]";
				} else {
					s = s + list.get(i).getName() + "\",\"";
				}
			}
			return s;
		}
	}

	public static String ListToJsonsupply(List<SupplyTag> list) {
		String s = "[\"";
		if (list == null) {
			return "[]";
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (i == list.size() - 1) {
					s = s + list.get(i).getName() + "\"]";
				} else {
					s = s + list.get(i).getName() + "\",\"";
				}
			}
			return s;
		}
	}

	// List<String>转化为json数组
	public static String ListJson(List<Category> list) {
		String s = "[";
		if (list == null) {
			return "[]";
		} else {
			for (int i = 0; i < list.size(); i++) {
				// if (i == list.size() - 1) {
				s = s + list.get(i).getId() + "]";
				// }
				// else {
				// s = s + list.get(i).getId() + ",";
				// }
			}
			return s;
		}
	}

	private static class SSLSocketFactoryEx extends SSLSocketFactory {

		SSLContext sslContext = SSLContext.getInstance("TLS");

		public SSLSocketFactoryEx(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(X509Certificate[] chain,
						String authType)
						throws java.security.cert.CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain,
						String authType)
						throws java.security.cert.CertificateException {
				}
			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host,
					port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}

	public static ServerResultDao getServerResult(String result) {
		ServerResultDao dao = new ServerResultDao();
		try {
			JSONObject resultJob = new JSONObject(result);
			String notification = resultJob.getString("notification");
			JSONObject notifyObj = new JSONObject(notification);
			String notifyCode = notifyObj.getString("notifyCode");
			dao.setNotifyCode(notifyCode);
			if (notifyCode.equals("0001")) {
				String responseData = resultJob.getString("responseData");
				dao.setResponseData(responseData);
			} else if (notifyCode.equals("0003")) {
				dao.setNotifyInfo("请重新登录");
				dao.setResponseData(null);
			} else {
				if (result.contains("notifyInfo")) {
					dao.setNotifyInfo(notifyObj.getString("notifyInfo"));
				} else if (result.contains("notifInfo")) {
					dao.setNotifyInfo(notifyObj.getString("notifInfo"));
				}
				dao.setResponseData(null);
			}
			return dao;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析服务器返回数据，返回List<String>
	 * 
	 * @param result
	 *            要解析的数据，由服务器返回
	 * @param data
	 *            list对应的字段
	 * @param strName
	 *            String对应的字段
	 * @return
	 */
	public static List<String> getStringList(String result, String data,
			String strName) {
		List<String> list = new ArrayList<String>();
		try {
			JSONObject resultJob = new JSONObject(result);
			String s = resultJob.getString(data);
			JSONArray arr = new JSONArray(s);
			for (int i = 0; i < s.length(); i++) {
				JSONObject job = (JSONObject) arr.opt(i);
				list.add(job.getString(strName));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 多条数据转换成json
	 * 
	 * @param map
	 * @return
	 */
	public static String simpleMapToJsonStr(Map<String, String> map) {
		if (map == null || map.isEmpty()) {
			return "null";
		}
		String jsonStr = "{";
		Set<?> keySet = map.keySet();
		for (Object key : keySet) {
			jsonStr += "\"" + key + "\":\"" + map.get(key) + "\",";
		}
		jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
		jsonStr += "}";
		return jsonStr;
	}

	// 创建文件
	public static boolean createFile(File file) throws Exception {
		if (!file.exists()) {
			makeDir(file.getParentFile());
		}
		return file.createNewFile();
	}

	public static void makeDir(File dir) {
		if (!dir.getParentFile().exists()) {
			makeDir(dir.getParentFile());
		}
		dir.mkdirs();
	}

	public static ArrayList<String> getListInfo(String result) {
		ArrayList<String> list = new ArrayList<String>();
		try {
			Log.e("", "获取信息：：" + getContent(result));
			JSONArray jsonArray = new JSONObject(getContent(result))
					.getJSONArray("labelList");
			for (int i = 0; i < jsonArray.length(); i++) {
				Gson gson = new Gson();
				Label label = gson.fromJson(jsonArray.opt(i).toString(),
						Label.class);

				list.add(label.getName());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}
}
