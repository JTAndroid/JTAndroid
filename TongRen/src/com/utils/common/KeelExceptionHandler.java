package com.utils.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Properties;
import java.util.TreeSet;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Looper;
import android.text.format.DateFormat;
import android.widget.Toast;

import com.tr.App;
import com.utils.log.KeelLog;
//import com.umeng.analytics.MobclickAgent;

/**
 * 全局异常处理
 */
public class KeelExceptionHandler implements UncaughtExceptionHandler {

    //获取application 对象； 
    private Context mContext;
    private UncaughtExceptionHandler defaultExceptionHandler;
    //单例声明CustomException; 
    private static KeelExceptionHandler customException=null;

    /**
     * Debug Log tag
     */
    public static final String TAG="CrashHandler";
    /**
     * 使用Properties来保存设备的信息和错误堆栈信息
     */
    private Properties mDeviceCrashInfo=new Properties();
    private static final String VERSION_NAME="versionName";
    private static final String VERSION_CODE="versionCode";
    private static final String STACK_TRACE="STACK_TRACE";
    /**
     * 错误报告文件的扩展名
     */
    private static final String CRASH_REPORTER_EXTENSION=".txt";

    /**
     * 保证只有一个CrashHandler实例
     */
    private KeelExceptionHandler() {

    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static KeelExceptionHandler getInstance() {
        if (customException==null) {
            customException=new KeelExceptionHandler();
        }
        return customException;
    }

    /**
     * 初始化,注册Context对象,
     * 获取系统默认的UncaughtException处理器,
     * 设置该CrashHandler为程序的默认处理器
     *
     * @param ctx
     */
    public void init(Context context) {
        mContext=context;
        defaultExceptionHandler=Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);

    }

    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        if (KeelLog.DEBUG) {// TODO 正式发布时需要屏蔽此处代码
            exception.printStackTrace();
        }

        //出现异常， 直接关闭所有activity
//        if (!handleException(exception)&&defaultExceptionHandler!=null) {
//            //如果用户没有处理则让系统默认的异常处理器来处理   
//            defaultExceptionHandler.uncaughtException(thread, exception);
//        } else {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                if (KeelLog.DEBUG) {
//                    e.printStackTrace();
//                }
//            }
//
//            /* 发送错误报告
//            if (!KeelLog.IS_SEND_REPORT) {
//                mContext.startService(new Intent(mContext, SendExceptionReportService.class));
//                CloseApplication();
//            }
//            */
//        }
        CloseApplication();
    }

    /**
     * 自定义错误处理,收集错误信息
     * 发送错误报告等操作均在此完成.
     * 开发者可以根据自己的情况来自定义异常处理逻辑
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable ex) {
        if (ex==null) {
            return true;
        }
        final String msg=ex.getLocalizedMessage();
        if (KeelLog.DEBUG) {
            KeelLog.e("tag", "exception >>>>>>>"+msg);
        }

        //收集设备信息   
        collectCrashDeviceInfo(mContext);
        //保存错误报告文件   
        /*String crashFileName = */
        saveCrashInfoToFile(ex);
        //发送错误报告到服务器   
        //sendCrashReportsToServer(mContext);

        // 显示错误提示
        new Thread() {

            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "Keel System Crash Error!", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();

        return true;
    }

    /**
     * 在程序启动时候, 可以调用该函数来发送以前没有发送的报告
     */
    public void sendPreviousReportsToServer() {
        sendCrashReportsToServer(mContext);
    }

    /**
     * 把错误报告发送给服务器,包含新产生的和以前没发送的.
     *
     * @param ctx
     */
    private void sendCrashReportsToServer(Context ctx) {
        final String[] crFiles=getCrashReportFiles(ctx);
        if (crFiles!=null&&crFiles.length>0) {
            TreeSet<String> sortedFiles=new TreeSet<String>();
            sortedFiles.addAll(Arrays.asList(crFiles));

            for (String fileName : sortedFiles) {
                if (KeelLog.DEBUG) {
                    KeelLog.e("log日志文件名称："+fileName);
                }
                final File cr=new File(ctx.getFilesDir(), fileName);
                postReport(cr);
                cr.delete();// 删除已发送的报告   
            }

            // 停止发送错误报告service
            //mContext.stopService(new Intent(mContext, SendExceptionReportService.class));
        }
    }

    /**
     * 发送错误报告到服务器
     *
     * @param file
     */
    private void postReport(File file) {
        if (isNetworkAvailable(mContext)&&file!=null) {
            /*
               try {
                   final FileInputStream inputStream = new FileInputStream(file);
                   final int length = inputStream.available(); 
                   final byte [] buffer = new byte[length]; 
                   inputStream.read(buffer);     
                   final String res = new String(Base64.encode(buffer));
                   inputStream.close();
                   
                   // 发送错误报告到服务器
                   final ArrayList<String> listTag = new ArrayList<String>();
                   listTag.add(file.getName());
                   listTag.add(res);
                   new NetWorkTask().execute(mContext, DXUtils.TAG_ADD_RECORD, DXUtils.ADDLOGFILE, listTag);	
               } catch (FileNotFoundException e) {
                   e.printStackTrace();
               } catch (IOException e) {
                   e.printStackTrace();
               }     
               */
        }
    }

    /**
     * 获取错误报告文件名
     *
     * @param ctx
     * @return
     */
    private String[] getCrashReportFiles(Context ctx) {
        final File filesDir=ctx.getFilesDir();
        if (KeelLog.DEBUG) {
            KeelLog.e("异常报告保存路径："+filesDir);
        }
        FilenameFilter filter=new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith(CRASH_REPORTER_EXTENSION);
            }
        };
        return filesDir.list(filter);
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return
     */
    private String saveCrashInfoToFile(Throwable ex) {
        final Writer info=new StringWriter();
        final PrintWriter printWriter=new PrintWriter(info);
        ex.printStackTrace(printWriter);

        Throwable cause=ex.getCause();
        while (cause!=null) {
            cause.printStackTrace(printWriter);
            cause=cause.getCause();
        }

        final String result=info.toString();
        if (KeelLog.DEBUG) {
            //TODO FileHelper.WriteStringToFile(result+"\r\n", KeelApplication.KEEL_PATH+"ust_"+System.currentTimeMillis()+".txt", false);
        }
        //MobclickAgent.reportError(mContext, result);//发送到友盟服务器
        printWriter.close();
        /*mDeviceCrashInfo.put(STACK_TRACE, result);

        try {
            final long timestamp=System.currentTimeMillis();
            final String fileName="crash-"+timestamp+CRASH_REPORTER_EXTENSION;
            final FileOutputStream trace=mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
            mDeviceCrashInfo.store(trace, "");
            trace.flush();
            trace.close();
            return fileName;
        } catch (Exception e) {
            if (KeelLog.DEBUG) {
                e.printStackTrace();
            }
        }*/
        writeLogcat();
        return null;
    }

    /**
     * 收集程序崩溃的设备信息
     *
     * @param ctx
     */
    public void collectCrashDeviceInfo(Context ctx) {
        try {
            final PackageManager pm=ctx.getPackageManager();
            final PackageInfo pi=pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi!=null) {
                mDeviceCrashInfo.put(VERSION_NAME, pi.versionName==null ? "not set" : pi.versionName);
                mDeviceCrashInfo.put(VERSION_CODE, ""+pi.versionCode);
            }
        } catch (NameNotFoundException e) {
            if (KeelLog.DEBUG) {
                e.printStackTrace();
            }
        }

        //使用反射来收集设备信息.在Build类中包含各种设备信息,   
        //例如: 系统版本号,设备生产商 等帮助调试程序的有用信息   
        //具体信息请参考后面的截图   
        final Field[] fields=Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                mDeviceCrashInfo.put(field.getName(), ""+field.get(null));
                if (KeelLog.DEBUG) {
                    KeelLog.e(TAG, field.getName()+" : "+field.get(null));
                }
            } catch (Exception e) {
                if (KeelLog.DEBUG) {
                    e.printStackTrace();
                }
            }

        }

    }

    /**
     * 检测网络连接是否可用
     *
     * @param ctx
     * @return true 可用; false 不可用
     */
    private boolean isNetworkAvailable(Context ctx) {
        final ConnectivityManager cm=(ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm==null) {
            return false;
        }
        final NetworkInfo[] netinfo=cm.getAllNetworkInfo();
        if (netinfo==null) {
            return false;
        }
        for (int i=0; i<netinfo.length; i++) {
            if (netinfo[i].isConnected()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 关闭应用程序，保存一些数据
     */
    public void CloseApplication() {
        //ActivityHolder.getInstance().finishAllActivity();
    	App.getApp().finishAllActivity();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    private void writeLogcat() {
        String name=KeelApplication.LOG_DIR+"epg_logcat";
        CharSequence timestamp=DateFormat.format("yyyyMMdd_kkmmss", System.currentTimeMillis());
        String filename=name+"_"+timestamp+".log";

        try {
        	KeelLog.d(TAG, "crash path: " + filename);
            FileOutputStream trace=new FileOutputStream(filename, true);
            mDeviceCrashInfo.store(trace, "");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] args={"logcat", "-v", "time", "-d"};

        try {
            Process process=Runtime.getRuntime().exec(args);

            InputStreamReader input=new InputStreamReader(process.getInputStream());
            OutputStreamWriter output=new OutputStreamWriter(new FileOutputStream(filename, true));
            BufferedReader br=new BufferedReader(input);
            BufferedWriter bw=new BufferedWriter(output);
            String line;

            while ((line=br.readLine())!=null) {
                bw.write(line);
                bw.newLine();
            }

            bw.close();
            output.close();
            br.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

