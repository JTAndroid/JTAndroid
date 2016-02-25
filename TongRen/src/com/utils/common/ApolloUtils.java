package com.utils.common;

import java.lang.ref.WeakReference;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Toast;

import com.tr.App;
import com.utils.image.cache.ImageCache;
import com.utils.image.cache.ImageFetcher;

/**
 * @Filename ApolloUtils.java
 * @Author xuxinjian
 * @Date 2014-3-13
 * @description: 一些常用的操作方法。判断系统版本，等
 */
public final class ApolloUtils {

    /**
     * The threshold used calculate if a color is light or dark
     */
    private static final int BRIGHTNESS_THRESHOLD = 130;

    /* This class is never initiated */
    public ApolloUtils() {
    }

    /**
     * Used to determine if the current device is a Google TV
     * 
     * @param context The {@link Context} to use
     * @return True if the device has Google TV, false otherwise
     */
    public static final boolean isGoogleTV(final Context context) {
        return context.getPackageManager().hasSystemFeature("com.google.android.tv");
    }

    /**
     * Used to determine if the device is running Froyo or greater
     * 
     * @return True if the device is running Froyo or greater, false otherwise
     */
    public static final boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    /**
     * Used to determine if the device is running Gingerbread or greater
     * 
     * @return True if the device is running Gingerbread or greater, false
     *         otherwise
     */
    public static final boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    /**
     * Used to determine if the device is running Honeycomb or greater
     * 
     * @return True if the device is running Honeycomb or greater, false
     *         otherwise
     */
    public static final boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * Used to determine if the device is running Honeycomb-MR1 or greater
     * 
     * @return True if the device is running Honeycomb-MR1 or greater, false
     *         otherwise
     */
    public static final boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    /**
     * Used to determine if the device is running ICS or greater
     * 
     * @return True if the device is running Ice Cream Sandwich or greater,
     *         false otherwise
     */
    public static final boolean hasICS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    /**
     * Used to determine if the device is running Jelly Bean or greater
     * 
     * @return True if the device is running Jelly Bean or greater, false
     *         otherwise
     */
    public static final boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * Used to determine if the device is a tablet or not
     * 
     * @param context The {@link Context} to use.
     * @return True if the device is a tablet, false otherwise.
     */
    public static final boolean isTablet(final Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * Used to determine if the device is currently in landscape mode
     * 
     * @param context The {@link Context} to use.
     * @return True if the device is in landscape mode, false otherwise.
     */
    public static final boolean isLandscape(final Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * Execute an {@link AsyncTask} on a thread pool
     * 
     * @param forceSerial True to force the task to run in serial order
     * @param task Task to execute
     * @param args Optional arguments to pass to
     *            {@link AsyncTask#execute(Object[])}
     * @param <T> Task argument type
     */
    @SuppressLint("NewApi")
    public static <T> void execute(final boolean forceSerial, final AsyncTask<T, ?, ?> task,
            final T... args) {
        final WeakReference<AsyncTask<T, ?, ?>> taskReference = new WeakReference<AsyncTask<T, ?, ?>>(
                task);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.DONUT) {
            throw new UnsupportedOperationException(
                    "This class can only be used on API 4 and newer.");
        }

        try {
            if (Build.VERSION.SDK_INT<Build.VERSION_CODES.HONEYCOMB||forceSerial) {
                taskReference.get().execute(args);
            } else {
                taskReference.get().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, args);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Used to determine if there is an active data connection and what type of
     * connection it is if there is one
     * 
     * @param context The {@link Context} to use
     * @return True if there is an active data connection, false otherwise.
     *         Also, if the user has checked to only download via Wi-Fi in the
     *         settings, the mobile data and other network connections aren't
     *         returned at all
     */
    public static final boolean isOnline(final Context context) {
        /*
         * This sort of handles a sudden configuration change, but I think it
         * should be dealt with in a more professional way.
         */
        if (context == null) {
            return false;
        }

        boolean state = false;
        final boolean onlyOnWifi =false;// PreferenceUtils.getInstace(context).onlyOnWifi();

        /* Monitor network connections */
        final ConnectivityManager connectivityManager = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        /* Wi-Fi connection */
        final NetworkInfo wifiNetwork = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null) {
            state = wifiNetwork.isConnectedOrConnecting();
        }

        /* Mobile data connection */
        final NetworkInfo mbobileNetwork = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mbobileNetwork != null) {
            if (!onlyOnWifi) {
                state = mbobileNetwork.isConnectedOrConnecting();
            }
        }

        /* Other networks */
        final NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (!onlyOnWifi) {
                state = activeNetwork.isConnectedOrConnecting();
            }
        }

        return state;
    }

    /**
     * Display a {@link Toast} letting the user know what an item does when long
     * pressed.
     * 
     * @param view The {@link View} to copy the content description from.
     */
    public static void showCheatSheet(final View view) {

        final int[] screenPos = new int[2]; // origin is device display
        final Rect displayFrame = new Rect(); // includes decorations (e.g.
                                              // status bar)
        view.getLocationOnScreen(screenPos);
        view.getWindowVisibleDisplayFrame(displayFrame);

        final Context context = view.getContext();
        final int viewWidth = view.getWidth();
        final int viewHeight = view.getHeight();
        final int viewCenterX = screenPos[0] + viewWidth / 2;
        final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        final int estimatedToastHeight = (int)(48 * context.getResources().getDisplayMetrics().density);

        final Toast cheatSheet = Toast.makeText(context, view.getContentDescription(),
                Toast.LENGTH_SHORT);
        final boolean showBelow = screenPos[1] < estimatedToastHeight;
        if (showBelow) {
            // Show below
            // Offsets are after decorations (e.g. status bar) are factored in
            cheatSheet.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, viewCenterX
                    - screenWidth / 2, screenPos[1] - displayFrame.top + viewHeight);
        } else {
            // Show above
            // Offsets are after decorations (e.g. status bar) are factored in
            cheatSheet.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, viewCenterX
                    - screenWidth / 2, displayFrame.bottom - screenPos[1]);
        }
        cheatSheet.show();
    }

    /**
     * @param context The {@link Context} to use.
     * @return An {@link AlertDialog} used to show the open source licenses used
     *         in Apollo.
     */
    /*public static final AlertDialog createOpenSourceDialog(final Context context) {
        final WebView webView = new WebView(context);
        webView.loadUrl("file:///android_asset/licenses.html");
        return new AlertDialog.Builder(context).setTitle(R.string.settings_open_source_licenses)
                .setView(webView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int whichButton) {
                        dialog.dismiss();
                    }
                }).create();
    }*/

    /**
     * Calculate whether a color is light or dark, based on a commonly known
     * brightness formula.
     * 
     * @see {@literal http://en.wikipedia.org/wiki/HSV_color_space%23Lightness}
     */
    public static final boolean isColorDark(final int color) {
        return (30 * Color.red(color) + 59 * Color.green(color) + 11 * Color.blue(color)) / 100 <= BRIGHTNESS_THRESHOLD;
    }

    /**
     * Runs a piece of code after the next layout run
     * 
     * @param view The {@link View} used.
     * @param runnable The {@link Runnable} used after the next layout run
     */
    @SuppressLint("NewApi")
    public static void doAfterLayout(final View view, final Runnable runnable) {
        final OnGlobalLayoutListener listener = new OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                /* Layout pass done, unregister for further events */
                if (hasJellyBean()) {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                runnable.run();
            }
        };
        view.getViewTreeObserver().addOnGlobalLayoutListener(listener);
    }

    /**
     * Creates a new instance of the {@link ImageCache} and {@link ImageFetcher}
     * 
     * @param activity The {@link Activity} to use.
     * @return A new {@link ImageFetcher} used to fetch images asynchronously.
     */
    public static final ImageFetcher getImageFetcher(final Activity activity) {
        final ImageFetcher imageFetcher = ImageFetcher.getInstance(activity);
        //imageFetcher.setImageCache(ImageCache.findOrCreateCache(activity));
        imageFetcher.setImageCache(ImageCache.getInstance(App.getApp()));
        return imageFetcher;
    }

    /**
     * Used to know if Apollo was sent into the background
     * 
     * @param context The {@link Context} to use
     */
    public static final boolean isApplicationSentToBackground(final Context context) {
        final ActivityManager activityManager = (ActivityManager)context
                .getSystemService(Context.ACTIVITY_SERVICE);
        final List<RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            final ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public static final ImageFetcher getImageFetcher(final Context context) {
        final ImageFetcher imageFetcher = ImageFetcher.getInstance(context);
        imageFetcher.setImageCache(ImageCache.getInstance(App.getApp()));
        return imageFetcher;
    }
}
