package com.memol.musicplayer.Main;

import static com.memol.musicplayer.PlayService.mediaPlayer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.search.SearchBar;
import com.google.android.material.tabs.TabLayout;
import com.memol.musicplayer.Adabters.ViewPagerAdabter;
import com.memol.musicplayer.Fragments.AlbumFrag;
import com.memol.musicplayer.Fragments.ArtistFrag;
import com.memol.musicplayer.Fragments.FavouriteFrag;
import com.memol.musicplayer.Fragments.SongsFrag;
import com.memol.musicplayer.G;
import com.memol.musicplayer.GlideApp;
import com.memol.musicplayer.Model.Song;
import com.memol.musicplayer.Model.TabItems;
import com.memol.musicplayer.PlayService;
import com.memol.musicplayer.R;

import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;

    MaterialButton btnSearch;
    SearchBar searchBar;
  public static   CardView mainCardView;
   public static MaterialButton btnPlay_Back;
   public static MaterialButton btnPlay_Pause;
   public static MaterialButton btnPlay_Next;
   public static TextView txtSongName;
   public static TextView txtArtistName;
   public static ImageView imgAlbumeArt;
   public static android.os.Handler MainHandler=new Handler();


  public static PlayService playService;
  ViewPagerAdabter adabter;
  public static ArrayList<Song> songs;
  String uri;
  int position;



    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetupView();
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_search_24);
        getSupportActionBar().setTitle("MemolMusic");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainCardView.setVisibility(View.INVISIBLE);

        if (CheckPermission()==false){
            requestPermissions();
        }

        btnPlay_Pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    btnPlay_Pause.setIconResource(R.drawable._8px);

                }else {
                    btnPlay_Pause.setIconResource(R.drawable.baseline_pause_24);
                    mediaPlayer.start();

                }
            }
        });
        btnPlay_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position=playService.getPosition();
                if (mediaPlayer.isPlaying()){
                    position=((position+1)%songs.size());
                    playService.setPosition(position);
                    uri=songs.get(position).getPath();
                    playService.StartMusic(uri);
                    btnPlay_Pause.setIconResource(R.drawable.baseline_pause_24);
                    txtSongName.setText(songs.get(position).getTitle());
                    txtArtistName.setText(songs.get(position).getArtist());

                    new Runnable() {
                        @Override
                        public void run() {

                            GlideApp.with(getApplicationContext()).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), songs.get(position).getAlbumId()))
                                    .error(R.drawable.music_image)
                                    .placeholder(R.drawable.music_image)
                                    .centerCrop()
                                    .fallback(R.drawable.music_image)
                                    .into(imgAlbumeArt);
                        }
                    }.run();

                }else {
                    position=((position+1)%songs.size());
                    uri=songs.get(position).getPath();
                    playService.setPosition(position);
                    playService.StartWhenStop(uri);
                    btnPlay_Pause.setIconResource(R.drawable._8px);
                    txtSongName.setText(songs.get(position).getTitle());
                    txtArtistName.setText(songs.get(position).getArtist());
                    new Runnable() {
                        @Override
                        public void run() {

                            GlideApp.with(getApplicationContext()).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), songs.get(position).getAlbumId()))
                                    .error(R.drawable.music_image)
                                    .placeholder(R.drawable.music_image)
                                    .centerCrop()
                                    .fallback(R.drawable.music_image)
                                    .into(imgAlbumeArt);

                        }
                    }.run();

                }
            }
        });
        btnPlay_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position=playService.getPosition();
                if (mediaPlayer.isPlaying()){
                    position=((position-1)<0 ? (songs.size()-1):(position-1));
                    playService.setPosition(position);
                    uri=songs.get(position).getPath();
                    playService.StartMusic(uri);
                    btnPlay_Pause.setIconResource(R.drawable.baseline_pause_24);
                    txtSongName.setText(songs.get(position).getTitle());
                    txtArtistName.setText(songs.get(position).getArtist());
                    new Runnable() {
                        @Override
                        public void run() {

                            GlideApp.with(getApplicationContext()).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), songs.get(position).getAlbumId()))
                                    .error(R.drawable.music_image)
                                    .placeholder(R.drawable.music_image)
                                    .centerCrop()
                                    .fallback(R.drawable.music_image)
                                    .into(imgAlbumeArt);
                        }
                    }.run();

                }else {
                    position=((position-1)<0 ? (songs.size()-1):(position-1));
                    playService.setPosition(position);
                    uri=songs.get(position).getPath();
                    playService.StartWhenStop(uri);
                    btnPlay_Pause.setIconResource(R.drawable._8px);
                    txtSongName.setText(songs.get(position).getTitle());
                    txtArtistName.setText(songs.get(position).getArtist());
                    new Runnable() {
                        @Override
                        public void run() {

                            GlideApp.with(getApplicationContext()).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), songs.get(position).getAlbumId()))
                                    .error(R.drawable.music_image)
                                    .placeholder(R.drawable.music_image)
                                    .centerCrop()
                                    .fallback(R.drawable.music_image)
                                    .into(imgAlbumeArt);
                        }
                    }.run();

                }
            }
        });



        adabter=new ViewPagerAdabter(MainActivity.this,getSupportFragmentManager());
        adabter.getFragment(new TabItems(new SongsFrag(),"Tracks",getResources().getDrawable(R.drawable.baseline_music_note_24)));
        adabter.getFragment(new TabItems(new AlbumFrag(),"Albums",getResources().getDrawable(R.drawable.baseline_library_music_24)));
        adabter.getFragment(new TabItems(new ArtistFrag(),"Artists",getResources().getDrawable(R.drawable.baseline_person_24)));
        adabter.getFragment(new TabItems(new FavouriteFrag(),"Favourites",getResources().getDrawable(R.drawable.baseline_stars_24)));
        viewPager.setAdapter(adabter);
        tabLayout.setupWithViewPager(viewPager);




    }

    public static void FillSongs(Context context){
        songs= G.SongList(context);
    }

    private boolean CheckPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            return true;
        }else {
            return false;
        }
    }
    void requestPermissions(){

        while (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_AUDIO) !=PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_MEDIA_AUDIO},2);
        }
        FillSongs(MainActivity.this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.toolbar_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.SearchMenu){
             adabter.notifyDataSetChanged();

        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onStart() {
        super.onStart();
        Intent intent=new Intent(MainActivity.this,PlayService.class);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);

    }
    ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
                PlayService.MyBinder myBinder= (PlayService.MyBinder) service;
                 playService=myBinder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    private void SetupView() {
       toolbar=findViewById(R.id.toolbar);
       tabLayout=findViewById(R.id.tabbar);
       viewPager=findViewById(R.id.fragContainer);
       btnSearch=findViewById(R.id.btnSearchIcon);
        mainCardView=findViewById(R.id.playCardView);
       btnPlay_Pause=findViewById(R.id.btnPlay);
       btnPlay_Next=findViewById(R.id.btnNext);
       btnPlay_Back=findViewById(R.id.btnBack);
       txtSongName=findViewById(R.id.txtMusicName);
       txtArtistName=findViewById(R.id.txtArtistName);
       imgAlbumeArt=findViewById(R.id.imageListMain);
   //searchBar=findViewById(R.id.search_bar);
    }
    private Bitmap convertImageViewToBitmap(ImageView v){

        Bitmap bm=((BitmapDrawable)v.getDrawable()).getBitmap();

        return bm;
    }


}