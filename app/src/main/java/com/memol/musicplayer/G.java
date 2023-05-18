package com.memol.musicplayer;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.memol.musicplayer.Model.Music;
import com.memol.musicplayer.Model.Song;

import java.util.ArrayList;

public class G extends Application {
    public static ArrayList<Song> songsList=new ArrayList<>();
    int POSITION;

    public int getPOSITION() {
        return POSITION;
    }

    public void setPOSITION(int POSITION) {
        this.POSITION = POSITION;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        songsList =SongList();

    }
    public ArrayList<Song> SongList() {
        ArrayList<Song> songArrayList =new ArrayList<>();
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        ContentResolver cr = getApplicationContext().getContentResolver();
        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM_ID

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

            //Uri albumArtworkUri = ContentUris.withAppendedId(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,albumId);



            Song song=new Song(album,title,duration,path,artist,albumId);

            songArrayList.add(song);




        }
        cur.close();
        return songArrayList;
    }
    public ArrayList<Music> GetSongs(){
        ArrayList<Music> songs=new ArrayList<>();
        Uri mediaStoreUri;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
            mediaStoreUri=MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        }else {
            mediaStoreUri=MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        String[] projection=new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.ALBUM_ID
        };
//        String sortOrder = MediaStore.Audio.Media.DATE_ADDED + "DESC";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        try(Cursor cursor =getContentResolver().query(mediaStoreUri,projection,null,null,sortOrder)) {
            int idColumn=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            int nameColumn=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
            int durationColumn=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
            int sizeColumn=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
            int albumIdColumn=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);

            while (cursor.moveToNext()){
                long id =cursor.getLong(idColumn);
                String name =cursor.getString(nameColumn);
                int duration=cursor.getInt(durationColumn);
                int size  =cursor.getInt(sizeColumn);
                long albumId =cursor.getLong(albumIdColumn);

                Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,id);
                Uri albumArtworkUri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"),albumId);

                //  name=name.substring(0,name.lastIndexOf("."));
                Music music=new Music(name,uri,albumArtworkUri,size,duration);
                songs.add(music);

            }
            return songs;

        }





    }
    public static Bitmap convertImageViewToBitmap(ImageView v){

        Bitmap bm=((BitmapDrawable)v.getDrawable()).getBitmap();

        return bm;
    }



}
