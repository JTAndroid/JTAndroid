package com.tr.ui.base; 
/** 
 * @author xuxinjian
 * @E-mail: xuxinjian@gintong.com
 * @version 1.0
 * @创建时间：2014-5-6 上午8:47:44 
 * @类说明 activity维持栈， 项目中所有activity create时，push到栈中， finish，出栈
 */

import java.util.ArrayList;
import java.util.List;

import com.utils.log.KeelLog;

import android.app.Activity;

public class ActivityHolder {

    private List<Activity> activityList;
    private static ActivityHolder activityHolder;

    private ActivityHolder() {
        activityList = new ArrayList<Activity>();
    }

    public static synchronized ActivityHolder getInstance() {
        if (activityHolder == null) {
            activityHolder = new ActivityHolder();
        }
        return activityHolder;
    }

    /**
     * add the activity in to a list
     * 
     * @param activity
     */
    public void push(Activity activity) {
        if (activity != null) {
            activityList.add(activity);
            int size = activityList.size();
            for(int i=0; i<size; i++) {
            	if (KeelLog.DEBUG) {
            		KeelLog.e("["+i+"]", " "+activityList.get(i));
            	}
            }
        }
    }
    
    public Activity getTop(){
    	 try {
    		 Activity top = null;
    		 if(activityList.size() > 0){
    			 top = activityList.get(activityList.size() - 1);
    		 }
    		 return top;
    	 } 
    	 catch (Exception e) {
         	if (KeelLog.DEBUG) {
         		e.printStackTrace();
         	}
         	return null;
         }
    }

    /**
     * finish all the activity in the list.
     * @param context the activity calling this method hold the context
     */   
    public void finishAllActivity() {
        int size = activityList.size();
        for (int i = size - 1; i >= 0; i--) {
            Activity activity = activityList.get(i);
            if (!activity.isFinishing()) {
                activity.finish();
            }
            activityList.remove(activity);
        }
    }
    
    /**
     * remove the finished activity in the list.
     * @param activity  the activity is removed from activityList
     */
    public void pop(Activity activity) {
        try {
            activityList.remove(activity);
        } catch (Exception e) {
        	if (KeelLog.DEBUG) {
        		e.printStackTrace();
        	}
        }
    }
    
    public boolean checkActivityIsVasivle(Activity activity) {
    	return activityList.contains(activity);
    }
}