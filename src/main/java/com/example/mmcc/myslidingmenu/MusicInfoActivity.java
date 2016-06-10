package com.example.mmcc.myslidingmenu;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import Bean.MusicBean;
import Bean.PlayTime;
import Utils.ToolbarUtil;
import Utils.Utils;
import server.PlayMusic;

/**
 * Created by mmcc on 2016/2/3.
 */
public class MusicInfoActivity extends BaseActivity implements Runnable,View.OnClickListener, PlayMusic.OnFinishPlayMusicListener {
    @Override
    int getLayoutId() {
        return R.layout.musicinfo_layout;
    }

    private ToolbarUtil toolbarUtil; //导航栏
    private LinearLayout mMusicBar; //需要隐藏底部音乐信息
    private MusicBean mp3Obj; //单个mp3对象
    private SeekBar mSeekBar;
    private TextView startTime, endTime;  //音乐的播放时间
    private Utils StringUtil;
    private ImageButton music_pre,music_status,music_next;
    private ImageView id_albumPic;  //专辑图片

    private int currentPosition;
    private List<MusicBean> allMusics;
    private int playPosition=-1;
    private Thread updataSeekbarThread;
    private boolean ThreadIsFinish=false;

    private Handler MusicInfoHandler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //更新主界面
            currentPosition=msg.arg1;
            upData(currentPosition);
          //  ThreadIsFinish=false;
            updataSeekbarThread=new Thread(getInstance());
            updataSeekbarThread.start();
        }
    };

    private void upData(int position) {
        mp3Obj=allMusics.get(position);
        toolbarUtil.setTitle(mp3Obj.getName());
        startTime.setText("00:00");
        PlayTime.endTime = StringUtil.stringForTime((int) mp3Obj.getDuration());
        endTime.setText(PlayTime.endTime);
        mSeekBar.setMax((int) mp3Obj.getDuration());
        music_status.setBackgroundResource(R.mipmap.music_play);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDatas();  //传来的数据和音乐数据初始化
        initView();
        initMusicBar();
        updataSeekbarThread=new Thread(getInstance()); //开启线程更新seekbar
        updataSeekbarThread.start();
        initToolbar();

        //轮询线程为音乐播放完成设置监听
        new Thread(new Runnable() {
            @Override
            public void run() {
                //为空时，即服务未开启时一值轮询检测
                while (true){
                    if (MainActivity.musicServer != null) {
                        Log.i("Tag", "musicServer=" + MainActivity.musicServer);
                        MainActivity.musicServer.setMusicListener(MusicInfoActivity.this);
                        break;
                    }

                }
            }
        }).start();
    }

    private void initDatas() {
        allMusics=getAllMusics();  //得到所有音乐
        Bundle bundle = getIntent().getExtras();
        currentPosition=bundle.getInt(MainActivity.CURRENTPOS);
        mp3Obj = allMusics.get(currentPosition);
        Log.i("Tag", "album=" + mp3Obj.getAlbum());
        Log.i("Tag", "url=" + mp3Obj.getUrl());
        Log.i("Tag", "albumId=" + mp3Obj.getAlbumId());
        StringUtil = new Utils(); //转换时间格式
    }

    private MusicInfoActivity getInstance(){
        return this;
    }
    private void initMusicBar() {
        if(MainActivity.musicServer.isPlaying())
        {
           music_status.setBackgroundResource(R.mipmap.music_play);
        }
    }

    /**
     * 得到当前专辑图片
     * @return
     */
    private String getBitmapFromAlbum(long albumId){
        Cursor albumCursor = getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART,MediaStore.Audio.Albums.ALBUM},
                MediaStore.Audio.Albums._ID + "=?",
                new String[]{String.valueOf(albumId)},
                null);
        String result = null;
        Log.i("Tag","cursor="+albumCursor.moveToFirst());
        if(albumCursor.moveToFirst())
        {
            Log.i("Tag","index="+albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
            Log.i("Tag","albumid="+albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums._ID)));
            Log.i("Tag","albumart="+albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)));
            Log.i("Tag","album="+albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)));
            result = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
        }
        Log.i("Tag","result="+result);
        albumCursor.close();
        return result;
    }
    private Bitmap getAlbumArt(long albumId) {

        // 读取专辑图片
        String album_uri = "content://media/external/audio/albumart"; // 专辑Uri对应的字符串
        Log.i("Tag","coverPhoto="+album_uri);
        Uri albumArtUri = ContentUris.withAppendedId(Uri.parse(album_uri), albumId);
        Log.i("Tag","coverPhoto2="+albumArtUri);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), albumArtUri);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

            return bitmap;
    }

    private void initView() {
        mMusicBar = (LinearLayout) findViewById(R.id.music_bar);
        mMusicBar.setVisibility(View.GONE);

        id_albumPic= (ImageView) findViewById(R.id.id_albumPic);//专辑图片
       // id_albumPic.setImageBitmap(getBitmapFromAlbum());
       /* Log.i("Tag","Album="+mp3Obj.getAlbum()+" albumid="+mp3Obj.getAlbumId());*/
    //  Log.i("Tag","bitmap="+getAlbumArt(mp3Obj.getAlbumId()));
     //   Log.i("Tag","bitmapPath="+getBitmapFromAlbum(mp3Obj.getAlbumId()));
        music_pre = (ImageButton) findViewById(R.id.music_layout_pre);
        music_status = (ImageButton) findViewById(R.id.music_layout_status);
        music_next = (ImageButton) findViewById(R.id.music_layout_next);
        music_pre.setOnClickListener(this);
        music_status.setOnClickListener(this);
        music_next.setOnClickListener(this);

        startTime = (TextView) findViewById(R.id.id_startTime);
        endTime = (TextView) findViewById(R.id.id_endTime);

        mSeekBar = (SeekBar) findViewById(R.id.id_seekbar);
        mSeekBar.setProgress(MainActivity.musicServer.getCurrentPosition());
        mSeekBar.setMax((int) mp3Obj.getDuration());

        Log.i("Tag", "max=" + mSeekBar.getMax());
        PlayTime.startTime = StringUtil.stringForTime(MainActivity.musicServer.getCurrentPosition());
        PlayTime.endTime = StringUtil.stringForTime((int) mp3Obj.getDuration());
        startTime.setText(PlayTime.startTime);
        endTime.setText(PlayTime.endTime);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                 //seekBar.setProgress(progress);

                startTime.setText(StringUtil.stringForTime(progress));
                endTime.setText(StringUtil.stringForTime((int) (mp3Obj.getDuration() - progress)));
                Log.i("Tag", "startTime=" + startTime.getText() + " endtime=" + endTime.getText()+" progress="+progress);
                if(fromUser)
                {
                    MainActivity.musicServer.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(!MainActivity.musicServer.isPause())
                {
                    MainActivity.musicServer.pause();
                }
                PlayTime.startTime = StringUtil.stringForTime(MainActivity.musicServer.getCurrentPosition());
                PlayTime.endTime = StringUtil.stringForTime((int) mp3Obj.getDuration()-MainActivity.musicServer.getCurrentPosition());
             //  Log.i("Tag","startTime="+PlayTime.startTime+" endtime="+ PlayTime.endTime );
                /*ThreadIsFinish=true;
                try {
                    updataSeekbarThread.join(); //等待该线程终止，再继续执行下面程序
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                Log.i("Tag", "线程结束1");
            //   updataSeekbarThread.interrupt();
                /* try {
                    updataSeekbarThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                Log.i("Tag", "线程结束");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            //    ThreadIsFinish=false;
                Log.i("Tag", "拖动后开启新线程");
                PlayTime.startTime = StringUtil.stringForTime(MainActivity.musicServer.getCurrentPosition());
                PlayTime.endTime = StringUtil.stringForTime((int) mp3Obj.getDuration()-MainActivity.musicServer.getCurrentPosition());
              //  Log.i("Tag","startTime="+PlayTime.startTime+" endtime="+ PlayTime.endTime );
                MainActivity.musicServer.pause();
                updataSeekbarThread=new Thread(getInstance());
                updataSeekbarThread.start();
                music_status.setBackgroundResource(R.mipmap.music_play);


            }
        });
    }

    private void initToolbar() {
        toolbarUtil = getToolbarUtil();
        toolbarUtil.setTitle(mp3Obj.getName()).setSubTitle(null).
                setBackgroundColor(Color.parseColor("#55ff0000")).
                setDisplayShowHomeEnabled(true)
                .setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
    }

    /**
     * 监听歌曲播放，设置seekbar
     */
    @Override
    public synchronized void run() {
        long total=mp3Obj.getDuration();
        while (MainActivity.musicServer.isPlaying() && MainActivity.musicServer.
                getCurrentPosition() < total)
        {
            if(ThreadIsFinish)   //退出该页面时，设置退出线程标志
            {
                Log.i("Tag","finishThread");
                break;
            }
            playPosition=MainActivity.musicServer.
                    getCurrentPosition();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i("Tag","currentposition="+StringUtil.stringForTime(MainActivity.musicServer.
                    getCurrentPosition()));
            mSeekBar.setProgress(playPosition);
        }
        Log.i("Tag","当前线程结束");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.music_layout_pre:
               // updataSeekbarThread.interrupt();
                /*ThreadIsFinish=true;

                try {
                    updataSeekbarThread.join(); //等待该线程终止，再继续执行下面程序
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                if (MainActivity.musicServer.isPlaying() || MainActivity.musicServer.isPause()) {
                    MainActivity.musicServer.previous();
                    if(currentPosition==0)
                    {
                        currentPosition=allMusics.size()-1;
                        //upDataMusicBar(currentPosition);
                    }
                    else
                        --currentPosition;
                    //upDataMusicBar(++currentPosition);
                } else {
                    currentPosition=allMusics.size()-1;
                    MainActivity.musicServer.play(currentPosition);
                    //  upDataMusicBar(currentPosition);
                }
                Message messagePre=Message.obtain();
                messagePre.arg1=currentPosition;
                MusicInfoHandler.sendMessage(messagePre);
                break;
            case R.id.music_layout_status:
                if (MainActivity.musicServer.isPlaying()) {//播放时点击
                   // ThreadIsFinish=true; //结束线程标志
                 //  updataSeekbarThread.interrupt();
                    music_status.setBackgroundResource(R.mipmap.music_stop);
                    MainActivity.musicServer.pause();

                    if(playPosition!=-1){
                        startTime.setText(StringUtil.stringForTime(playPosition));
                        endTime.setText(StringUtil.stringForTime((int) (mp3Obj.getDuration() - playPosition)));
                        mSeekBar.setProgress(playPosition);
                    }

                }else if(!musicServer.isPlaying()&&!musicServer.isPause())
                {   //即没播放也没暂停时，就是刚开始
                    music_status.setBackgroundResource(R.mipmap.music_play);
                    MainActivity.musicServer.play(0);
                    updataSeekbarThread=new Thread(getInstance());
                    updataSeekbarThread.start();
                } else { //暂停时点击
                 //   updataSeekbarThread.interrupt();
                    /*try {
                        updataSeekbarThread.join(); //等待该线程终止，再继续执行下面程序
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                    //ThreadIsFinish=false;
                    music_status.setBackgroundResource(R.mipmap.music_play);
                    MainActivity.musicServer.pause();
                    updataSeekbarThread=new Thread(getInstance());
                    updataSeekbarThread.start();
                }
                break;
            case R.id.music_layout_next:
               // updataSeekbarThread.interrupt();
                /*ThreadIsFinish=true;
                try {
                    updataSeekbarThread.join(); //等待该线程终止，再继续执行下面程序
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                if (MainActivity.musicServer.isPlaying() || MainActivity.musicServer.isPause()) {
                    MainActivity.musicServer.next();
                    if(currentPosition>=allMusics.size()-1)
                    {
                        currentPosition=0;
                        //upDataMusicBar(currentPosition);
                    }
                    else
                        ++currentPosition;
                        //upDataMusicBar(++currentPosition);
                } else {
                    currentPosition=1;
                    MainActivity.musicServer.play(currentPosition);
                  //  upDataMusicBar(currentPosition);
                }
                Message message=Message.obtain();
                message.arg1=currentPosition;
                MusicInfoHandler.sendMessage(message);

                break;
        }
    }

    @Override
    public void finish() {
        ThreadIsFinish = true;
        Log.i("Tag","finish");
        Intent data=new Intent();
        data.putExtra("position",currentPosition);
        setResult(RESULT_OK,data);
        super.finish();
    }

    @Override
    public void onBackPressed() {
        Log.i("Tag","onBackPressed");
        finish();
    }

    @Override
    public void OnFinishedMusic(int currentPosition) {
        Message messagePre=Message.obtain();
        messagePre.arg1=currentPosition;
        MusicInfoHandler.sendMessage(messagePre);
    }
}
