package com.utils.note.rteditor.api.media;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import com.tr.R;
import com.utils.common.EUtil;
import com.utils.note.rteditor.utils.Helper;
import com.utils.note.view.FileConstans;

/**
 * Created by zhangchangwei on 2015/8/12.
 */
public class RTAudioImage {
    private String _path;
    private String _audioPath;

    public RTAudioImage(Context context, String name, String time){
        View view = View.inflate(context, R.layout.rte_audioimage, null);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_audio_name);
        TextView tv_time = (TextView) view.findViewById(R.id.tv_audio_time);
        tv_name.setText(name);
        tv_time.setText(time);
        String cacheDir = EUtil.getAppCacheFileDir(context).getAbsolutePath();
		String audioDir = cacheDir + FileConstans.DIR_AUDIO_SAVE;
		String imageDir = cacheDir + FileConstans.DIR_IMAGE_SAVE;
		_audioPath = audioDir + name;
        view.setDrawingCacheEnabled(true);
        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );
        int[] screenSize = Helper.getScreenSize();
        view.layout (0, 0,screenSize[0] , view.getMeasuredHeight());
        Bitmap bp = view.getDrawingCache();
        _path = imageDir + SystemClock.currentThreadTimeMillis() + ".jpg";
        File file = new File(_path);
        if(file.exists()){
            file.delete();
        }
        try {
            file.createNewFile();
            OutputStream out = new FileOutputStream(file);
            bp.compress(Bitmap.CompressFormat.JPEG,100,out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPath(){
        return _path;
    }

    public String getAudioPath(){return _audioPath;}
}
