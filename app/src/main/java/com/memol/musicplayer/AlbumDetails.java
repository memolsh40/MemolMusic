package com.memol.musicplayer;

import static com.memol.musicplayer.Main.MainActivity.songs;

import android.content.ContentUris;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.memol.musicplayer.Adabters.AlbumDetailsAdabter;
import com.memol.musicplayer.Model.Song;

import java.util.ArrayList;

public class AlbumDetails extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageView imageView;
    String albumeName;
    ArrayList<Song>songDetails = new ArrayList<>();
    AlbumDetailsAdabter adabter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);
        SetupView();

        albumeName=getIntent().getStringExtra("albumName");
        int j = 0;
        for (int i =0;i<songs.size();i++){
            if (albumeName.equals(songs.get(i).getAlbum())){
                songDetails.add(j,songs.get(i));
                Log.i("AlbumeList2", String.valueOf(j+""+i));
                j  ++;
            }
        }
        new Runnable() {
            @Override
            public void run() {

                GlideApp.with(AlbumDetails.this).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), songDetails.get(0).getAlbumId()))
                        .error(R.drawable.music_image)
                        .placeholder(R.drawable.music_image)
                        .centerCrop()
                        .fallback(R.drawable.music_image)
                        .into(imageView);
            }
        }.run();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!(songs.size()<1)){
            adabter=new AlbumDetailsAdabter(songDetails,AlbumDetails.this,R.layout.song_list);
            recyclerView.setAdapter(adabter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        }
    }

    private void SetupView() {
        recyclerView=findViewById(R.id.AlbumeRcv);
        imageView=findViewById(R.id.AlbumeImgView);

    }
}