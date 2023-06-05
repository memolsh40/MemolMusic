package com.memol.musicplayer.Model;

public class Song {
    private String album;
    private String title;
    private String duration;
    private String path;
    private String artist;

    private long albumId;
    private String id;

    public Song(String album, String title, String duration, String path, String artist, long albumId, String id) {
        this.album = album;
        this.title = title;
        this.duration = duration;
        this.path = path;
        this.artist = artist;
        this.albumId = albumId;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }



    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }




    public Song() {
    }






}