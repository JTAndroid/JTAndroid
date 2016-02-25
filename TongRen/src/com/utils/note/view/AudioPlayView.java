package com.utils.note.view;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tr.R;

/**
 * Created by zhangchangwei on 2015/8/12.
 */
public class AudioPlayView {
    private View _view;
    private Context _context;
    private Handler _handler;

    private ImageButton _play_pause;
    private Button _stop;
    private SeekBar _seekbar;
    private boolean _isplay = false;
    private MediaPlayer _player = null;
    private boolean _isfirst = true;
    private Timer _timer;
    private TimerTask _timerTask;
    private Chronometer _playTime;
    private TextView _totalTime;
    private File _file;
    private boolean _isChanging =false;
    private String _path;

    public AudioPlayView(Context context,Handler handler){
        _context = context;
        _handler = handler;
        getView();
    }

    public View getView(){
        if(_view == null){
            _view = View.inflate(_context, R.layout.rte_audioplayer, null);

            findViews();// 各组件

        }
        return _view;
    }

    private void findViews() {
        _play_pause = (ImageButton) _view.findViewById(R.id.play_pause);
        _stop = (Button) _view.findViewById(R.id.reset);
        _playTime = (Chronometer) _view.findViewById(R.id.playtime);
        _totalTime = (TextView) _view.findViewById(R.id.totaltime);
        _play_pause.setOnClickListener(new MyClick());
        _stop.setOnClickListener(new MyClick());

        _seekbar = (SeekBar) _view.findViewById(R.id.seekbar);
        _seekbar.setOnSeekBarChangeListener(new MySeekbar());
    }

    class MyClick implements View.OnClickListener {
        public void onClick(View v) {

            // 判断有没有要播放的文件
            if (_file.exists()) {
            	int id = v.getId();
                    if (id == R.id.play_pause){
                    	if (_player != null && !_isplay) {
                    		startPlay(_path);
                    	} else if (_isplay) {
                    		pausePlay();
                    	}
                    }else if(id == R.id.reset){
                    	realse();
                    	hideView();
                    }
            }
        }
    }

    private void resetPlay(){
        _play_pause.setImageResource(R.drawable.ic_audio_play);
        _playTime.setBase(SystemClock.elapsedRealtime());
        _playTime.stop();
        _player.seekTo(0);
        _player.pause();
        _isplay = false;
    }

    private void initPlayer(){
        _player = new MediaPlayer();
        _player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                resetPlay();
            }
        });
    }

    public void startPlay(String path){
        if(_player == null){
            initPlayer();
        }
        _play_pause.setImageResource(R.drawable.ic_audio_pause);
        _playTime.setBase(convertStrTimeToLong(_playTime.getText().toString()));
        _playTime.start();
        if (_isfirst || !path.equals(_path)) {
            _path = path;
            _file = new File(_path);
            _player.reset();
            try {
                _player.setDataSource(_file.getAbsolutePath());
                _player.prepare();// 准备
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            SimpleDateFormat format = new SimpleDateFormat("mm:ss");
            String time = format.format(new Date(_player.getDuration()));
            if("00:00".equals(time)){
                time = "00:01";
            }
            _totalTime.setText(time);
            _seekbar.setMax(_player.getDuration());//设置进度条
            //----------定时器记录播放进度---------//
            _timer = new Timer();
            _timerTask = new TimerTask() {
                @Override
                public void run() {
                    if(_isChanging ==true) {
                        return;
                    }
                    _seekbar.setProgress(_player.getCurrentPosition());
                }
            };
            _timer.schedule(_timerTask, 0, 10);
            _isfirst =false;
        }
        if(_timer == null){
            _timer = new Timer();
            _timerTask = new TimerTask() {
                @Override
                public void run() {
                    if(_isChanging ==true) {
                        return;
                    }
                    _seekbar.setProgress(_player.getCurrentPosition());
                }
            };
            _timer.schedule(_timerTask,0,10);
        }

        _player.start();// 开始
        _isplay = true;
    }

    public void pausePlay(){
        if(_isplay){
            _play_pause.setImageResource(R.drawable.ic_audio_play);
            _playTime.stop();
            _player.pause();
            _isplay = false;
        }
    }

    public void stopPlay(){
        if(_timer != null){
            _timer.cancel();
            _timer = null;
            _timerTask.cancel();
            _timerTask = null;
        }
        if(_player.isPlaying()){
            _player.stop();
            _isplay = false;
        }
    }

    public void hideView(){
        Message msg = Message.obtain();
        msg.what = RichTextEditor.WHAT_STOP_PLAY;
        _handler.sendMessage(msg);
    }

    public void realse(){
        if(_timer != null){
            _timer.cancel();
            _timer = null;
            _timerTask.cancel();
            _timerTask = null;
        }
        if(_playTime != null){
            _playTime.stop();
            _playTime.setBase(SystemClock.elapsedRealtime());
        }
        if(_player != null){
            if(_player.isPlaying()){
                _player.stop();
                _isplay = false;
            }
            _player.release();
            _isfirst = true;
            _player = null;
        }
    }

    public boolean getIsPlay(){
        return _isplay;
    }

    //进度条处理
    class MySeekbar implements SeekBar.OnSeekBarChangeListener {
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            _isChanging =true;
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            if(_player != null){
                _player.seekTo(_seekbar.getProgress());
            }
            _isChanging =false;
        }

    }

    protected long convertStrTimeToLong(String strTime) {
        // TODO Auto-generated method stub
        String []timeArry=strTime.split(":");
        long longTime=0;
        if (timeArry.length==2){
            longTime=Integer.parseInt(timeArry[0])*1000*60+Integer.parseInt(timeArry[1])*1000;
        }else if(timeArry.length==3){
            longTime=Integer.parseInt(timeArry[0])*1000*60*60+Integer.parseInt(timeArry[1])*1000*60+Integer.parseInt(timeArry[0])*1000;
        }
        return SystemClock.elapsedRealtime()-longTime;
    }
}
