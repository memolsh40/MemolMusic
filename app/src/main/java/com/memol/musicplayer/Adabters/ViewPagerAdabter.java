package com.memol.musicplayer.Adabters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.memol.musicplayer.Model.TabItems;

import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class ViewPagerAdabter extends FragmentPagerAdapter {
    ArrayList<TabItems> items = new ArrayList<>();
    private Context context;

    public ViewPagerAdabter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return items.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = items.get(position).getTitle();
        Drawable drawable = items.get(position).getImageItem();
        SpannableStringBuilder sb = new SpannableStringBuilder("   "+title);
        try {
            drawable.setBounds(5,5,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
            ImageSpan imageSpan=new ImageSpan(drawable, DynamicDrawableSpan.ALIGN_BASELINE);
            sb.setSpan(imageSpan,0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
        }
        return items.get(position).getTitle();

    }
    public void getFragment(TabItems tabItems){
        items.add(tabItems);
    }
}