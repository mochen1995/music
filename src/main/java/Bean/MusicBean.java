package Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mmcc on 2016/1/28.
 */
public class MusicBean{
    private long id;
    private String name; //歌名
    private String artist; //作者
    private String album; //专辑
    private long albumId;
    private long duration;//时长
    private List<String> mLineWords; //每一个下标存放一行歌词

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    private long size;  //大小
    private String url;  //路径
    private int isMusic;

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setIsMusic(int isMusic) {
        this.isMusic = isMusic;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public long getAlbumId() {
        return albumId;
    }

    public long getSize() {
        return size;
    }

    public String getUrl() {
        return url;
    }

    public int getIsMusic() {
        return isMusic;
    }
}
