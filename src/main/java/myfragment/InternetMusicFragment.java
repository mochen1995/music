package myfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import Bean.MusicBean;


/**
 * Created by mmcc on 2016/1/29.
 */
public class InternetMusicFragment extends BasedFragment{
    private List<MusicBean> allMusics;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView tv=new TextView(getActivity());
        tv.setText("网络歌曲");
       /* allMusics=new ArrayList<MusicBean>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://music.qq.com/musicbox/shop/v3/data/hit/hit_newsong.js";
                OkHttpClient mOkhttpClient=new OkHttpClient();

                final Request request=new Request.Builder().url(url).addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8").build();
                Call call=mOkhttpClient.newCall(request);

                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {

                        String htmlStr = response.body().string();
                        String result=htmlStr.substring(13,htmlStr.length()-1).toString();
                        Log.i("Tag","htmlStr="+result);
                       *//* try {
                            JSONObject jsonObject=new JSONObject(result);
                            Log.i("Tag", "retcode=" + jsonObject.getString("retcode"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*//*
                        try {
                            JSONArray jsonArray = new JSONObject(result).getJSONArray("songlist");
                            JSONObject jsonObject;
                            MusicBean mp3Info;

                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                mp3Info = new MusicBean();
                                int song_id = Integer.parseInt(jsonObject.getString("id"));
                                String album =jsonObject.getString("albumName");
                                long album_id = Long.parseLong(jsonObject.getString("albumId"));
                                String artist_name = jsonObject.getString("singerName");
                                String name = jsonObject.getString("songName");
                                String url=jsonObject.getString("url");
                                long duration=Long.parseLong(jsonObject.getString("playtime"));
                                mp3Info.setName(name);
                                mp3Info.setId(song_id);
                                mp3Info.setAlbum(album);
                                mp3Info.setAlbumId(album_id);
                                mp3Info.setArtist(artist_name);
                                mp3Info.setUrl(url);
                                mp3Info.setDuration(duration);
                                allMusics.add(mp3Info);
                                Log.i("Tag", "artist_name=" + mp3Info.getArtist() + " id=" + mp3Info.getId() + " album=" + mp3Info.getAlbum() + " name=" + name+" url="+url+"  duration="+duration);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                Log.i("Tag","size="+allMusics.size());

            }
        }).start();*/

        return tv;
    }
}
