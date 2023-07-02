package com.memol.musicplayer.Details;

import static com.memol.musicplayer.Adabters.AlbumDetailsAdabter.albumDetailsList;
import static com.memol.musicplayer.G.albumsList;
import static com.memol.musicplayer.Main.MainActivity.playService;

import android.content.ContentUris;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.memol.musicplayer.Adabters.AlbumDetailsAdabter;
import com.memol.musicplayer.G;
import com.memol.musicplayer.GlideApp;
import com.memol.musicplayer.Main.MainActivity;
import com.memol.musicplayer.Main.PlayService;
import com.memol.musicplayer.Model.Song;
import com.memol.musicplayer.R;

import java.util.ArrayList;

public class AlbumDetails extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageView imageView;
    String albumeName;
    ArrayList<Song>songDetails = new ArrayList<>();
    ArrayList<Song>generalList = new ArrayList<>();
    AlbumDetailsAdabter adabter;
    TextView txtAlbumeDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);
        SetupView();
        albumsList.clear();
        generalList= G.SongList(getApplicationContext());
        albumeName=getIntent().getStringExtra("albumName");
        txtAlbumeDetails.setText(albumeName);
        int j = 0;
        for (int i =0;i<generalList.size();i++){
            if (albumeName.equals(generalList.get(i).getAlbum())){
                songDetails.add(j,generalList.get(i));
                j  ++;
            }
        }
        new Runnable() {
            @Override
            public void run() {

                GlideApp.with(AlbumDetails.this).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), songDetails.get(0).getAlbumId()))
                        .error(R.drawable.music_blue_night)
                        .placeholder(R.drawable.music_blue_night)
                        .centerCrop()
                        .fallback(R.drawable.music_blue_night)
                        .into(imageView);
            }
        }.run();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!(generalList.size()<1)){
            adabter=new AlbumDetailsAdabter(songDetails,AlbumDetails.this,R.layout.song_list);
            recyclerView.setAdapter(adabter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (MainActivity.getSongs().size()==G.SongList(getApplicationContext()).size()){
            albumDetailsList.clear();
        }
    }

    private void SetupView() {
        recyclerView=findViewById(R.id.AlbumeRcv);
        imageView=findViewById(R.id.AlbumeImgView);
        txtAlbumeDetails=findViewById(R.id.txtAlbumeDetails);

    }
}