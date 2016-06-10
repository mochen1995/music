package com.example.mmcc.myslidingmenu;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.List;

import Bean.MusicBean;
import Utils.MusicUtil;
import Utils.ToolbarUtil;
import server.PlayMusic;

/**
 * Created by mmcc on 2016/1/24.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private FrameLayout mContent; //内容区域
    private ToolbarUtil toolbarUtil;
    private Toolbar toolbar;
    public static PlayMusic musicServer; //整个应用共用的服务，定义为静态
    private boolean isBind = false;
    private List<MusicBean> allMusics=null; //在基类里得到所有音乐,避免其它类多次获取

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("Tag","建立服务连接");
            PlayMusic.myBinder binder = (PlayMusic.myBinder) service;
            musicServer = binder.getMusicServer();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicServer = null;
        }
    };

    public void BindMusicService() {
       Intent intent = new Intent(this, PlayMusic.class);
        if (!isBind) {
            isBind = true;
            bindService(intent, conn, BIND_AUTO_CREATE);
        }

    }

    public void UnBindMusicService() {
        if(isBind)
        {
            isBind=false;
            unbindService(conn);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.based_layout);
        initData();
        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        toolbarUtil = new ToolbarUtil(toolbar, this);
        toolbarUtil.setTitle("晨晨音乐").setSubTitle("music");
         /*toolbar.setName("Music");*/
        init();
        View view = LayoutInflater.from(this).inflate(getLayoutId(), null);
        mContent.addView(view);
    }

    private void initData() {
        if(allMusics==null)
        {
            allMusics= MusicUtil.getMp3Infos(this);//得到所有音乐
        }
    }
    public List<MusicBean> getAllMusics(){
        if(allMusics==null)
        {
            return MusicUtil.getMp3Infos(this);
        }
        return allMusics;
    }


    //得到一个ToolbarUtil实例，用以对toolbar操作
    public ToolbarUtil getToolbarUtil() {
        if (null == toolbarUtil) {
            return new ToolbarUtil(toolbar, this);
        }
        return toolbarUtil;
    }

    public void init() {
        mContent = (FrameLayout) findViewById(R.id.id_content);
    }

    /**
     * 自定义一个自己的基类activity，可以做一些初始不变的操作
     * 如:重写进出activity动画
     * 也可以定义一个自定义导航栏，并将其他activity布局加入到该布局下
     *
     * @param intent
     */

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.activity_in_right_left, R.anim.activity_out_right_left);
    }


    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.activity_in_right_left, R.anim.activity_out_right_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.finish_in_left_right, R.anim.finish_out_left_right);
    }


    abstract int getLayoutId();
}
