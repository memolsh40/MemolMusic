package com.memol.musicplayer.Model;

import android.graphics.drawable.Drawable;

import androidx.fragment.app.Fragment;

public class TabItems {
    private Fragment fragment;
    private String title;
    private Drawable imageItem;

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getImageItem() {
        return imageItem;
    }

    public void setImageItem(Drawable imageItem) {
        this.imageItem = imageItem;
    }

    public TabItems(Fragment fragment, String title, Drawable imageItem) {
        this.fragment = fragment;
        this.title = title;
        this.imageItem = imageItem;
    }
}
