package com.example.mmcc.myslidingmenu;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.BindException;
import java.util.ArrayList;
import java.util.List;

import Bean.MusicBean;
import Utils.MusicUtil;
import Utils.ToolbarUtil;
import adpater.MyAdapter;
import myfragment.BasedFragment;
import myfragment.InternetMusicFragment;
import myfragment.MineFragment;
import server.PlayMusic;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private TextView mMusicName, mMusicAuthor;
    private ImageButton mMusicStatus, mMusicNext;
    private LinearLayout mMusicInfo;
    public static final String MUSICOBJECT = "MusicObject";
    public static final String CURRENTPOS = "currentPosition";

    private int currentPosition;  //播放音乐的位置（哪一首）
    private List<MusicBean> allMusics; //所有音乐
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x11) {
                currentPosition = msg.arg1;
                //播放音乐时，更新UI
                upDataMusicBar(currentPosition);
            }

        }
    };

    public void upDataMusicBar(int currentPosition) {
        mMusicName.setText(allMusics.get(currentPosition).getName());
        mMusicAuthor.setText(allMusics.get(currentPosition).getArtist());
        mMusicStatus.setBackgroundResource(R.mipmap.music_play);
    }

    private View tabView;
    private ToolbarUtil toolbarUtil;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> fragments;
    private static final String Title[] = {"我的音乐", "网络音乐"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragment(); //初始化fragment
        initToolbar(); //初始化标题栏
        initData();
        initView();

/*        Log.i("Tag", "musicServer=" + musicServer);
        if(musicServer!=null)
        {
            musicServer.setMusicListener(new PlayMusic.OnFinishPlayMusicListener() {
                @Override
                public void OnFinishedMusic() { //当前歌曲播放完成时回调
                    Log.i("Tag","activity中播放完成");
                    //最后一首
                    if(currentPosition>=allMusics.size()-1)
                    {
                        currentPosition=0;
                    }else{
                        currentPosition++;
                    }
                    musicServer.play(currentPosition);
                    upDataMusicBar(currentPosition);
                }
            });
        }
        else{
            Log.i("Tag","为空");
        }*/
    }

    private void initData() {
        allMusics = getAllMusics();//得到所有音乐
    }

    private void initToolbar() {
        toolbarUtil = getToolbarUtil();
        toolbarUtil.setTitle(null).setSubTitle(null);
        toolbarUtil.setCustomView(tabView);
    }

    private void initFragment() {
        tabView = LayoutInflater.from(this).inflate(R.layout.scroll_tab, null);
        tabLayout = (TabLayout) tabView.findViewById(R.id.id_tablayout);
        viewPager = (ViewPager) findViewById(R.id.id_viewpager);
        fragments = new ArrayList<Fragment>();
        MineFragment mine = new MineFragment();
        mine.setHandler(mHandler);
        InternetMusicFragment Internet = new InternetMusicFragment();
        fragments.add(mine);
        fragments.add(Internet);
        PagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public CharSequence getPageTitle(int position) {
                return Title[position];
            }

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initView() {
        mMusicName = (TextView) findViewById(R.id.id_MusicName);
        mMusicAuthor = (TextView) findViewById(R.id.id_AuthorName);
        mMusicInfo = (LinearLayout) findViewById(R.id.music_info);
        //初始值
        mMusicName.setText(allMusics.get(currentPosition).getName());
        mMusicAuthor.setText(allMusics.get(currentPosition).getArtist());

        mMusicStatus = (ImageButton) findViewById(R.id.id_music_status);
        mMusicNext = (ImageButton) findViewById(R.id.id_music_next);
        mMusicStatus.setOnClickListener(this);
        mMusicNext.setOnClickListener(this);
        mMusicInfo.setOnClickListener(this);
        //左上角按钮的监听事件（弹出侧滑菜单）
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, getToolbarUtil().getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //系统自带策划菜单
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /*//查找手机里的视频
    public void searchVideo() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //内容提供者提供的，视频搜索器
                ContentResolver re = getContentResolver();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                //需要查找的字段
                String[] projection = {MediaStore.Video.Media.DISPLAY_NAME  //视频名称
                        , MediaStore.Video.Media.DURATION   //时长
                        , MediaStore.Video.Media.SIZE   //大小
                        , MediaStore.Video.Media.DATA    //sd卡下的路径
                };
                //得到所有视频
                Cursor cursor = re.query(uri, projection, null, null, null);
                VideoBean item=null;
                while (cursor.moveToNext())
                {
                    item=new VideoBean();
                    item.setName(cursor.getString(0));
                    item.setTime(cursor.getLong(1));
                    item.setSize(cursor.getLong(2));
                    item.setData(cursor.getString(3));
                    //将每一项放入集合列表
                    items.add(item);
                }
                cursor.close();//关闭
                mHandler.sendEmptyMessage(1); //提示主线程，数据加载完毕
            }
        }).start();

    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_search) {
            Toast.makeText(this, "点击了" + item.getTitle(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, SendActivity.class);

            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Toast.makeText(this, "点击了" + item.getTitle(), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(this, "点击了" + item.getTitle(), Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(this, "点击了" + item.getTitle(), Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_manage) {
            Toast.makeText(this, "点击了" + item.getTitle(), Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_share) {
            Toast.makeText(this, "点击了" + item.getTitle(), Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_send) {
            Toast.makeText(this, "点击了" + item.getTitle(), Toast.LENGTH_SHORT).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==100&&resultCode == RESULT_OK) {
            Log.i("Tag","position="+data.getIntExtra("position", 0));
            int position = data.getIntExtra("position", 0);
            Message message=Message.obtain();
            message.arg1=position;
            message.what=0x11;
            mHandler.sendMessage(message);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.music_info:
                Intent intent = new Intent(this, MusicInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(CURRENTPOS, currentPosition);
                intent.putExtras(bundle);
                startActivityForResult(intent, 100);
                //startActivity(intent);
                break;
            case R.id.id_music_status:
                if (MainActivity.musicServer.isPlaying()) {//播放时点击
                    mMusicStatus.setBackgroundResource(R.mipmap.music_stop);
                    MainActivity.musicServer.pause();
                } else if (!musicServer.isPlaying() && !musicServer.isPause()) {   //即没播放也没暂停时，就是刚开始
                    mMusicStatus.setBackgroundResource(R.mipmap.music_play);
                    MainActivity.musicServer.play(0);
                } else { //暂停时点击
                    mMusicStatus.setBackgroundResource(R.mipmap.music_play);
                    MainActivity.musicServer.pause();
                }

                break;
            case R.id.id_music_next:
                if (MainActivity.musicServer.isPlaying() || MainActivity.musicServer.isPause()) {
                    MainActivity.musicServer.next();
                    if (currentPosition >= allMusics.size() - 1) {
                        currentPosition = 0;
                        upDataMusicBar(currentPosition);
                    } else
                        upDataMusicBar(++currentPosition);
                } else {
                    currentPosition = 1;
                    MainActivity.musicServer.play(currentPosition);
                    upDataMusicBar(currentPosition);
                }
                break;
        }
    }


}
