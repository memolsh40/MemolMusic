package com.memol.musicplayer;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.memol.musicplayer.Model.Song;

import java.util.ArrayList;
import java.util.Random;

public class G extends Application {
    public static ArrayList<Song> albumsList=new ArrayList<>();
    public static ArrayList<Song> artistList=new ArrayList<>();
    public static String MY_SORT_PREF="SortOrder";

    public static final String CHANNEL_ID_1="channel1";
    public static final String CHANNEL_ID_2="channel2";
    public static final String ACTION_PREVIOUS="actionprevious";
    public static final String ACTION_NEXT="actionnext";
    public static final String ACTION_PLAY="actionplay";


    @Override
    public void onCreate() {
        super.onCreate();
creatNotificationChannel();

    }

    private void creatNotificationChannel() {
        NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager!=null){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 =
                    new NotificationChannel(CHANNEL_ID_1, "Channel(1)", NotificationManager.IMPORTANCE_DEFAULT);
            channel1.setDescription("Channel 1 Desc...");
            NotificationChannel channel2 =
                    new NotificationChannel(CHANNEL_ID_2, "Channel(2)", NotificationManager.IMPORTANCE_HIGH);
            channel2.setDescription("Channel 2 Desc...");

            notificationManager.createNotificationChannel(channel1);
            notificationManager.createNotificationChannel(channel2);
        }
        }
    }

    public static ArrayList<Song> SongList(Context context) {
        SharedPreferences preferences= context.getSharedPreferences(MY_SORT_PREF,MODE_PRIVATE);
        String sortOrder=preferences.getString("sorting", "sortByName");
        ArrayList<Song> songArrayList =new ArrayList<>();
        ArrayList<String> duplicateAlbum =new ArrayList<>();
        ArrayList<String> duplicateArtist =new ArrayList<>();
        albumsList.clear();
        String order=null;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        //String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        ContentResolver cr = context.getContentResolver();
        switch (sortOrder){
            case "sortByName":
                order=MediaStore.MediaColumns.DISPLAY_NAME + " ASC";
                break;
                case "sortByDate":
                order=MediaStore.MediaColumns.DATE_ADDED + " ASC";
                break;
                case "sortBySize":
                order=MediaStore.MediaColumns.SIZE + " DESC";
                break;

        }
        String[] projection = {
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media._ID

        };

        Cursor cur = cr.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                order);

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
            if (!duplicateAlbum.contains(album)){
                albumsList.add(song);
                duplicateAlbum.add(album);
            } if (!duplicateArtist.contains(artist)){
                artistList.add(song);
                duplicateArtist.add(artist);
            }


        }
        cur.close();
        return songArrayList;
    }


    public static Bitmap convertImageViewToBitmap(ImageView v){

        Bitmap bm=((BitmapDrawable)v.getDrawable()).getBitmap();

        return bm;
    }
    public static int getRandom(int i) {
        Random random=new Random();
        return random.nextInt(i+1);
    }



}
