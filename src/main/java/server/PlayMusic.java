package server;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

import java.util.List;

import Bean.MusicBean;
import Utils.MusicUtil;

public class PlayMusic extends Service implements MediaPlayer.OnCompletionListener{
    private MediaPlayer mPlayer; //多媒体
    private List<MusicBean> mp3Infos;
    private int currentPosition=0;
    private myBinder mBinder=new myBinder();
    private boolean isPause=false;
    public PlayMusic() {

    }
    private OnFinishPlayMusicListener musicListener;

    public void setMusicListener(OnFinishPlayMusicListener musicListener) {
        this.musicListener = musicListener;
    }

    //设置音乐结束回调监听
    public interface OnFinishPlayMusicListener{
        void OnFinishedMusic(int currentPosition);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer = new MediaPlayer();
        mp3Infos = MusicUtil.getMp3Infos(this);
        mPlayer.setOnCompletionListener(this);
    }

    //音乐完成时回调
    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.i("Tag","service中播放完成");
        //最后一首
        if(currentPosition>=mp3Infos.size()-1)
        {
            currentPosition=0;
        }else{
            currentPosition++;
        }
        play(currentPosition);
        if(musicListener!=null)
        {
            //通知主线程，更新UI界面
            musicListener.OnFinishedMusic(currentPosition);
        }
    }

    public class myBinder extends Binder {
       public PlayMusic getMusicServer(){
           return PlayMusic.this;
       }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    //根据位置播放音乐
    public void play(int position) {

        currentPosition=position;
        if (position >= 0 && position < mp3Infos.size()) {
            try {
                String url=mp3Infos.get(position).getUrl();
                mPlayer.reset();
                //设置音乐路径
                mPlayer.setDataSource(String.valueOf(Uri.parse(url)));
                mPlayer.prepare();
                mPlayer.start();
             } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public int getCurrentPosition(){
        if(mPlayer.isPlaying())
         return mPlayer.getCurrentPosition();
        return -1;
    }
    public boolean isPlaying(){
        if(mPlayer.isPlaying())
            return true;
        return false;
    }

    public boolean isPause() {
        return isPause;
    }

    public void pause() {
        if(mPlayer.isPlaying())
        {
            isPause = true;
            mPlayer.pause();
        }else{
            isPause = false;
            mPlayer.start();
        }
    }
    //下一首
    public void next() {
        //若是最后一首
      if(currentPosition>=mp3Infos.size()-1)
      {
          currentPosition=0;
      }else{
          currentPosition++;
      }
        play(currentPosition);
    }
    //前一首
    public void previous() {
        //若是第一首
        if(currentPosition==0)
      {
          currentPosition=mp3Infos.size()-1;
      }else{
          currentPosition--;
      }
        play(currentPosition);
    }
    //播放指定位置
    public void seekTo(int progress){
        if(mPlayer!=null){
            mPlayer.seekTo(progress);
        }
    }
}
