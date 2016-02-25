package com.utils.log;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.os.Environment;
import android.util.Log;

import com.utils.common.GlobalVariable;
import com.utils.file.FileHelper;

/**
 * 日志类，系统使用的日志类，包含级别的控制。
 *
 * @archko
 */
public class KeelLog {

    public static final String TAG="TongRen";
    public static boolean VERBOSE=true;
    public static boolean DEBUG=true;
    private static boolean LOG2FILE=false;


    private static int logLevel=5;//5输出所有信息
    /**

     * Change current logging level
     *
     * @param level new log level 1 <= level <= 6
     */
    public static void setLogLevel(int level) {
        logLevel=level;
    }

    /**
     * Get the current log level
     *
     * @return the log level
     */
    public static int getLogLevel() {
        return logLevel;
    }

    public static void d(String msg) {
        /*if (DEBUG) {
            Log.d(TAG, msg);
        }*/
        if (logLevel>=4) {
            Log.d(TAG, msg);
        }

        if (LOG2FILE) {
            FileHelper.WriteStringToFile(msg+"\r\n", GlobalVariable.LOG_PATH, true);
        }
    }

    public static void d(String tag, String msg) {
        /*if (DEBUG) {
            Log.d(tag, msg);
        }*/
        if (logLevel>=4) {
            Log.d(tag, msg);
        }

        if (LOG2FILE) {
            FileHelper.WriteStringToFile(msg+"\r\n", GlobalVariable.LOG_PATH, true);
        }
    }

    public static void i(String msg) {
        /*if (DEBUG) {
            Log.i(TAG, msg);
        }*/
        if (logLevel>=3) {
            Log.i(TAG, msg);
        }

        if (LOG2FILE) {
            FileHelper.WriteStringToFile(msg+"\r\n", GlobalVariable.LOG_PATH, true);
        }
    }

    public static void i(String tag, String msg) {
        /*if (DEBUG) {
            Log.i(tag, msg);
        }*/
        if (logLevel>=3) {
            Log.i(tag, msg);
        }

        if (LOG2FILE) {
            FileHelper.WriteStringToFile(msg+"\r\n", GlobalVariable.LOG_PATH, true);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        /*if (DEBUG) {
            Log.i(tag, msg, tr);
        }*/
        if (logLevel>=3) {
            Log.i(tag, msg, tr);
        }

        if (LOG2FILE) {
            FileHelper.WriteStringToFile(msg+"\r\n", GlobalVariable.LOG_PATH, true);
        }
    }

    public static void v(String msg) {
        if (logLevel>=5) {
            Log.v(TAG, msg);
        }

        if (LOG2FILE) {
            FileHelper.WriteStringToFile(msg+"\r\n", GlobalVariable.LOG_PATH, true);
        }
    }

    public static void v(String tag, String msg) {
        if (logLevel>=5) {
            Log.v(TAG, msg);
        }
        if (LOG2FILE) {
            FileHelper.WriteStringToFile(msg+"\r\n", GlobalVariable.LOG_PATH, true);
        }
    }
    
    public static void Logger(String msg){
    	
    }

    public static void e(String msg) {
        /*if (DEBUG) {
            Log.e(TAG, msg);
        }*/
        if (logLevel>=1) {
            Log.e(TAG, msg);
        }

        if (LOG2FILE) {
            FileHelper.WriteStringToFile(msg+"\r\n", GlobalVariable.LOG_PATH, true);
        }
    }

    public static void e(String tag, String msg) {
        /*if (DEBUG) {
            Log.e(tag, msg);
        }*/
        if (logLevel>=1) {
            Log.e(tag, msg);
        }

        if (LOG2FILE) {
            FileHelper.WriteStringToFile(msg+"\r\n", GlobalVariable.LOG_PATH, true);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        /*if (DEBUG) {
            Log.e(tag, msg, tr);
        }*/
        if (logLevel>=1) {
            Log.e(tag, msg, tr);
        }

        if (LOG2FILE) {
            FileHelper.WriteStringToFile(msg+"\r\n", GlobalVariable.LOG_PATH, true);
        }
    }

    public static void w(String msg) {
        /*if (DEBUG) {
            Log.w(TAG, msg);
        }*/
        if (logLevel>=2) {
            Log.w(TAG, msg);
        }

        if (LOG2FILE) {
            FileHelper.WriteStringToFile(msg+"\r\n", GlobalVariable.LOG_PATH, true);
        }
    }

    public static void w(String tag, String msg) {
        /*if (DEBUG) {
            Log.w(tag, msg);
        }*/
        if (logLevel>=2) {
            Log.w(tag, msg);
        }

        if (LOG2FILE) {
            FileHelper.WriteStringToFile(msg+"\r\n", GlobalVariable.LOG_PATH, true);
        }
    }

    public static boolean isDEBUG() {
        return DEBUG;
    }

    public static void setDEBUG(boolean debug) {
        DEBUG=debug;
    }

    public static boolean isLOG2FILE() {
        return LOG2FILE;
    }

    public static void setLOG2FILE(boolean log2File) {
        LOG2FILE=log2File;
    }

    /**
     * 此方法能输出异常详细信息
     * 用于bug定位
     */
    public static String getStackTrace(Exception e) {
        StringWriter writer=new StringWriter();
        e.printStackTrace(new PrintWriter(writer, true));

        return writer.toString();
    }

}
