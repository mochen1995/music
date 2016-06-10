package myfragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mmcc.myslidingmenu.MainActivity;
import com.example.mmcc.myslidingmenu.R;

import java.util.List;

import Bean.MusicBean;
import Utils.DividerItemDecoration;
import Utils.MusicUtil;
import adpater.MyAdapter;
import server.PlayMusic;

/**
 * Created by mmcc on 2016/1/28.
 */
public class MineFragment extends BasedFragment implements PlayMusic.OnFinishPlayMusicListener {
    private RecyclerView mRecyclerView;
    private View view;
    private List<MusicBean> mp3Infos;
    private MyAdapter mAdapter;
    private MainActivity mActivity;

    private Handler handler;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
        mActivity.BindMusicService();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.localmusic_layout, container, false);
        Log.i("Tag", "绑定服务");
        initView();
        initDatas();
        //轮询线程为音乐播放完成设置监听
        new Thread(new Runnable() {
            @Override
            public void run() {
                //为空时，即服务未开启时一值轮询检测
                 while (true){
                    if (mActivity.musicServer != null) {
                        Log.i("Tag", "musicServer=" + mActivity.musicServer);
                        mActivity.musicServer.setMusicListener(MineFragment.this);
                        break;
                    }

                }
            }
        }).start();


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mActivity.UnBindMusicService();
        Log.i("Tag", "取消绑定");
    }

    private void initDatas() {
        mp3Infos = MusicUtil.getMp3Infos(mActivity); //加载MP3数据
        LinearLayoutManager llm = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(llm);
        mAdapter = new MyAdapter(mActivity, mp3Infos);
        //设置分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnRecyclerItemClickListener(new MyAdapter.OnRecyclerItemClickListener() {
            @Override
            public void OnItemClickListener(int position) {
                mActivity.musicServer.play(position);
                //发送给主界面更新UI
                Message message = Message.obtain();
                message.arg1 = position;
                message.what = 0x11;
                handler.sendMessage(message);
            }
        });

    }

    private void initView() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_music_list);

    }

    //音乐播放完成时通知主线程更新UI
    @Override
    public void OnFinishedMusic(int currentPosition) {
        Message message = Message.obtain();
        message.what = 0x11;
        message.arg1 = currentPosition;
        handler.sendMessage(message);
    }
}
