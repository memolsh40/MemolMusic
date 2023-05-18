package com.memol.musicplayer.Model;

import android.net.Uri;

public class Music {
    String title;
    Uri uri;
    Uri artworkUri;
    int size;
    int duartion;

    @Override
    public String toString() {
        return "Music{" +
                "title='" + title + '\'' +
                ", uri=" + uri +
                ", artworkUri=" + artworkUri +
                ", size=" + size +
                ", duartion=" + duartion +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public Uri getArtworkUri() {
        return artworkUri;
    }

    public void setArtworkUri(Uri artworkUri) {
        this.artworkUri = artworkUri;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getDuartion() {
        return duartion;
    }

    public void setDuartion(int duartion) {
        this.duartion = duartion;
    }

    public Music(String title, Uri uri, Uri artworkUri, int size, int duartion) {
        this.title = title;
        this.uri = uri;
        this.artworkUri = artworkUri;
        this.size = size;
        this.duartion = duartion;
    }
}
