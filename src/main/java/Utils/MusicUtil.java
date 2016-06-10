package Utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import Bean.MusicBean;

/**
 * Created by mmcc on 2016/1/28.
 */
public class MusicUtil {
    //得到单个音乐信息
    public static MusicBean getMp3Info(Context context,long _id){
        //多媒体map的uri
        Uri uri=MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Log.i("Tag","uri="+ uri);
        Cursor cursor=context.getContentResolver().
                query(uri,null,MediaStore.Audio.Media._ID + "="+_id,
                null,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        MusicBean mp3Info=null;
        while(cursor.moveToNext())
        {
            mp3Info=new MusicBean();
            long id=cursor.getLong(cursor.getColumnIndex
                    (MediaStore.Audio.Media._ID));//音乐id
            String title=cursor.getString(cursor.getColumnIndex
                    (MediaStore.Audio.Media.TITLE));//音乐标题
            String artist=cursor.getString(cursor.getColumnIndex
                    (MediaStore.Audio.Media.ARTIST));//歌唱者
            String album=cursor.getString(cursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM));//专辑
            long albumId=cursor.getLong(cursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM_ID));//专辑id
            long duration=cursor.getLong(cursor.getColumnIndex
                    (MediaStore.Audio.Media.DURATION));//时长
            long size=cursor.getLong(cursor.getColumnIndex
                    (MediaStore.Audio.Media.SIZE));//音乐大小
            String url=cursor.getString(cursor.getColumnIndex
                    (MediaStore.Audio.Media.DATA));//路劲
            int isMusic=cursor.getInt(cursor.getColumnIndex
                    (MediaStore.Audio.Media.IS_MUSIC));//是否为音乐
            if(isMusic!=0) //只添加音乐到集合
            {
                mp3Info.setId(id);
                mp3Info.setName(title);
                mp3Info.setArtist(artist);
                mp3Info.setAlbum(album);
                mp3Info.setAlbumId(albumId);
                mp3Info.setDuration(duration);
                mp3Info.setSize(size);
                mp3Info.setUrl(url);
            }
        }
        cursor.close();
        return mp3Info;
    }
    public static List<MusicBean> getMp3Infos(Context context){
        //多媒体map的uri
        Uri uri=MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        if(uri==null)
        {
            uri=MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
        }
        Log.i("Tag","uri="+ uri);
        Cursor cursor=context.getContentResolver().query(uri,null,MediaStore.Audio.Media.DURATION + ">=120000",
                null,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        List<MusicBean> mp3Infos=new ArrayList<MusicBean>();
        MusicBean mp3Info=null;
        Log.i("Tag","size="+cursor.getCount());
        while (cursor.moveToNext())
        {
           mp3Info=new MusicBean();
            long id=cursor.getLong(cursor.getColumnIndex
                    (MediaStore.Audio.Media._ID));//音乐id
            String title=cursor.getString(cursor.getColumnIndex
                    (MediaStore.Audio.Media.TITLE));//音乐标题
            String artist=cursor.getString(cursor.getColumnIndex
                    (MediaStore.Audio.Media.ARTIST));//歌唱者
            String album=cursor.getString(cursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM));//专辑
            long albumId=cursor.getLong(cursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM_ID));//专辑id
            long duration=cursor.getLong(cursor.getColumnIndex
                    (MediaStore.Audio.Media.DURATION));//时长
            long size=cursor.getLong(cursor.getColumnIndex
                    (MediaStore.Audio.Media.SIZE));//音乐大小
            String url=cursor.getString(cursor.getColumnIndex
                    (MediaStore.Audio.Media.DATA));//路劲
            int isMusic=cursor.getInt(cursor.getColumnIndex
                    (MediaStore.Audio.Media.IS_MUSIC));//是否为音乐
            if(isMusic!=0) //只添加音乐到集合
            {
                mp3Info.setId(id);
                mp3Info.setName(title);
                mp3Info.setArtist(artist);
                mp3Info.setAlbum(album);
                mp3Info.setAlbumId(albumId);
                mp3Info.setDuration(duration);
                mp3Info.setSize(size);
                mp3Info.setUrl(url);
                mp3Infos.add(mp3Info);
            }

        }
        cursor.close();
        return mp3Infos;
    }
}
