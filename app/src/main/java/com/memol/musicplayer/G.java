package com.memol.musicplayer;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.memol.musicplayer.Model.Song;

import java.util.ArrayList;

public class G extends Application {
    public static ArrayList<Song> albums=new ArrayList<>();
    int POSITION;


    @Override
    public void onCreate() {
        super.onCreate();

    }

    public static ArrayList<Song> SongList(Context context) {
        ArrayList<Song> songArrayList =new ArrayList<>();
        ArrayList<String> duplicate =new ArrayList<>();
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        ContentResolver cr = context.getContentResolver();
        String[] projection = {
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media._ID

        };

        Cursor cur = cr.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                sortOrder);

        while (cur.moveToNext()) {
            String album=cur.getString(0);
            String title=cur.getString(1);
            String duration=cur.getString(2);
            String path=cur.getString(3);
            String artist=cur.getString(4);
            long albumId=cur.getLong(5);
            String id =cur.getString(6);

            Song song=new Song(album,title,duration,path,artist,albumId,id);
            songArrayList.add(song);
            if (!duplicate.contains(album)){
                albums.add(song);
                duplicate.add(album);
            }


        }
        cur.close();
        return songArrayList;
    }


    public static Bitmap convertImageViewToBitmap(ImageView v){

        Bitmap bm=((BitmapDrawable)v.getDrawable()).getBitmap();

        return bm;
    }



}
