package com.creater.music;

public class MusicFiles {
private String path;
private String title;
private String artist;
private String album;
private String Duration;
private String id;

    public MusicFiles(String path, String title, String artist, String album, String duration,String id) {
        this.path = path;
        this.title = title;
        this.artist = artist;
        this.album = album;
       this.Duration=duration;
       this.id=id;

    }

    public MusicFiles() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
