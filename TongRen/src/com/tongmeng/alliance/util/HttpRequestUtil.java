package com.tongmeng.alliance.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;

import com.tr.App;
import com.utils.log.KeelLog;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class HttpRequestUtil {

	public static final String TAG = "HttpRequest";
	private static String FILE = "saveSetting";

	public static String sendPost(String url, String param, Context context) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setReadTimeout(5000);
			conn.setConnectTimeout(50000);
			/**
			 * getUserID::441675
			 * getSessionID::ODY0ODk1MDIzODU3Mjc3NTg0OTE0NTc5MjQ2MTY5NTc=
			 */
			KeelLog.e("HttpRequestUtil", "getUserID::"
					+ App.getApp().getAppData().getUserID());
//			conn.setRequestProperty("jtUserID", App.getApp().getAppData()
//					.getUserID());
			conn.setRequestProperty("jtUserID", "1");
			KeelLog.e("HttpRequestUtil", "getSessionID::"
					+ App.getApp().getAppData().getSessionID());
			conn.setRequestProperty("sessionId", App.getApp().getAppData()
					.getSessionID());
			conn.setRequestProperty("s", "android");
			out = new PrintWriter(conn.getOutputStream());
			out.print(param);
			out.flush();
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
			KeelLog.e("HttpRequestUtil", "e::"+e.getMessage());
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static String sendPostObject(String url, JSONObject param,
			Context context) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setReadTimeout(5000);
			conn.setConnectTimeout(50000);
			KeelLog.e("HttpRequestUtil", "getUserID::"
					+ App.getApp().getAppData().getUserID());
//			conn.setRequestProperty("jtUserID", App.getApp().getAppData()
//					.getUserID());
			conn.setRequestProperty("jtUserID", "1");
			KeelLog.e("HttpRequestUtil", "getSessionID::"
					+ App.getApp().getAppData().getSessionID());
			conn.setRequestProperty("sessionId", App.getApp().getAppData()
					.getSessionID());
			conn.setRequestProperty("s", "android");
			out = new PrintWriter(conn.getOutputStream());
			out.print(param);
			out.flush();
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
			KeelLog.e("HttpRequestUtil", "e::"+e.getMessage());
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

}
