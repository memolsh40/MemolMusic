package com.memol.musicplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.widget.ImageView;

import com.memol.musicplayer.Model.Song;

import java.io.IOException;

public class MyThread extends Thread{
    ImageView imageView;
    android.os.Handler handler;
    Context context;
    Song song;

    public MyThread(ImageView imageView, android.os.Handler handler,Song song,Context context) {
        this.imageView = imageView;
        this.handler = handler;
        this.song=song;
        this.context=context;
    }

    @Override
    public void run() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] img =getAlbumArt(song.getPath());

                    if (imageView!=null){
                        GlideApp.with(context)
                                .asBitmap()
                                .load(img)
                                .autoClone()
                                .placeholder(R.drawable.baseline_music_note_24)
                                .thumbnail( 0.3f )
                                .override( 60, 60)
                                .into(imageView);
                        imageView.setTag(img);
                    }else {
                        GlideApp.with(context)
                                .asBitmap()
                                .load(imageView.getTag())
                                .autoClone()
                                .placeholder(R.drawable.baseline_music_note_24)
                                .thumbnail( 0.3f )
                                .override( 60, 60)
                                .into(imageView);
                    }

                }catch (Exception e){
                    Log.i("Glide" ,e.getMessage());
                }
            }
        });

    }

    public static byte[] getAlbumArt(String uri) {
        Bitmap bitmap1=null;
        byte[] art;
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(uri);
            art=retriever.getEmbeddedPicture();
            retriever.release();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return art;

    }
}
