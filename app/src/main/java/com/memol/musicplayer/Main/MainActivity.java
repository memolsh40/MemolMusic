package com.memol.musicplayer.Main;

import static com.memol.musicplayer.Main.PlayActivity.playActList;
import static com.memol.musicplayer.Main.PlayService.mediaPlayer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.palette.graphics.Palette;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.memol.musicplayer.Adabters.ViewPagerAdabter;
import com.memol.musicplayer.Fragments.AlbumFrag;
import com.memol.musicplayer.Fragments.SongsFrag;
import com.memol.musicplayer.G;
import com.memol.musicplayer.Model.InfoActivity;
import com.memol.musicplayer.Model.Song;
import com.memol.musicplayer.Model.TabItems;
import com.memol.musicplayer.R;

import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
  public static   CardView mainCardView;
   public static MaterialButton btnPlay_Back;
   public static MaterialButton btnPlay_Pause;
   public static MaterialButton btnPlay_Next;
   public static TextView txtSongName;
   public static TextView txtArtistName;
   public static ImageView imgAlbumeArt;
   public static android.os.Handler MainHandler=new Handler();
    public static boolean shuffleBoolean =false,repeatBoolean=false;


  ViewPagerAdabter adabter;
  public static ArrayList<Song> songs;
    public static PlayService playService;


  String uri;
  int position;
    String[] PERMISSIONS={Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.POST_NOTIFICATIONS
     };


//    public void Permissions(Activity activity){
//        if (ActivityCompat.checkSelfPermission(activity, notifycationsPer[0])!= PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(activity,notifycationsPer,1);
//        }
//    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetupView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MemolMusic");
        mainCardView.setVisibility(View.INVISIBLE);
        while (!hasPermissions(MainActivity.this,PERMISSIONS)){
            ActivityCompat.requestPermissions(MainActivity.this,PERMISSIONS,5);


        }
        FillSongs(MainActivity.this);









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
                    if (shuffleBoolean&&!repeatBoolean){
                        position=G.getRandom(playActList.size()-1);
                    }
                    else if (!shuffleBoolean&&!repeatBoolean){
                        position=((position+1)%songs.size());

                    }

                    playService.setPosition(position);
                    uri=songs.get(position).getPath();
                    playService.StartMusic(uri);
                    btnPlay_Pause.setIconResource(R.drawable.baseline_pause_24);
                    txtSongName.setText(songs.get(position).getTitle());
                    txtArtistName.setText(songs.get(position).getArtist());



                    new Runnable() {
                        @Override
                        public void run() {

                            Glide.with(getApplicationContext()).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), songs.get(position).getAlbumId()))
                                    .error(R.drawable.music_blue_night)
                                    .placeholder(R.drawable.music_blue_night)
                                    .centerCrop()
                                    .fallback(R.drawable.music_blue_night)
                                    .into(imgAlbumeArt);
                        }
                    }.run();

                }
                else {
                    if (shuffleBoolean&&!repeatBoolean){
                        position=G.getRandom(playActList.size()-1);
                    }
                    else if (!shuffleBoolean&&!repeatBoolean){
                        position=((position+1)%songs.size());
                    }
                    uri=songs.get(position).getPath();
                    playService.setPosition(position);
                    playService.StartWhenStop(uri);
                    btnPlay_Pause.setIconResource(R.drawable._8px);
                    txtSongName.setText(songs.get(position).getTitle());
                    txtArtistName.setText(songs.get(position).getArtist());
                    new Runnable() {
                        @Override
                        public void run() {

                            Glide.with(getApplicationContext()).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), songs.get(position).getAlbumId()))
                                    .error(R.drawable.music_blue_night)
                                    .placeholder(R.drawable.music_blue_night)
                                    .centerCrop()
                                    .fallback(R.drawable.music_blue_night)
                                    .into(imgAlbumeArt);

                        }
                    }.run();

                }
                metaData(songs.get(position).getPath());

            }
        });
        btnPlay_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position=playService.getPosition();
                if (mediaPlayer.isPlaying()){
                    if (shuffleBoolean&&!repeatBoolean){
                        position=G.getRandom(playActList.size()-1);
                    }
                    else if (!shuffleBoolean&&!repeatBoolean){
                        position=((position-1)<0 ? (songs.size()-1):(position-1));
                    }
                    playService.setPosition(position);
                    uri=songs.get(position).getPath();
                    playService.StartMusic(uri);
                    btnPlay_Pause.setIconResource(R.drawable.baseline_pause_24);
                    txtSongName.setText(songs.get(position).getTitle());
                    txtArtistName.setText(songs.get(position).getArtist());
                    new Runnable() {
                        @Override
                        public void run() {

                            Glide.with(getApplicationContext()).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), songs.get(position).getAlbumId()))
                                    .error(R.drawable.music_blue_night)
                                    .placeholder(R.drawable.music_blue_night)
                                    .centerCrop()
                                    .fallback(R.drawable.music_blue_night)
                                    .into(imgAlbumeArt);
                        }
                    }.run();
                }
                else {
                    if (shuffleBoolean&&!repeatBoolean){
                        position=G.getRandom(playActList.size()-1);
                    }
                    else if (!shuffleBoolean&&!repeatBoolean){
                        position=((position-1)<0 ? (songs.size()-1):(position-1));
                    }
                    playService.setPosition(position);
                    uri=songs.get(position).getPath();
                    playService.StartWhenStop(uri);
                    btnPlay_Pause.setIconResource(R.drawable._8px);
                    txtSongName.setText(songs.get(position).getTitle());
                    txtArtistName.setText(songs.get(position).getArtist());
                    new Runnable() {
                        @Override
                        public void run() {

                            Glide.with(getApplicationContext()).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), songs.get(position).getAlbumId()))
                                    .error(R.drawable.music_blue_night)
                                    .placeholder(R.drawable.music_blue_night)
                                    .centerCrop()
                                    .fallback(R.drawable.music_blue_night)
                                    .into(imgAlbumeArt);
                        }
                    }.run();

                }
                metaData(songs.get(position).getPath());
                Log.i("AlbumeSize", String.valueOf(G.albumsList.size())+"   1");
            }
        });



        adabter=new ViewPagerAdabter(MainActivity.this,getSupportFragmentManager());
        adabter.getFragment(new TabItems(new SongsFrag(),"Tracks",getResources().getDrawable(R.drawable.baseline_music_note_24)));
        adabter.getFragment(new TabItems(new AlbumFrag(),"Albums",getResources().getDrawable(R.drawable.baseline_library_music_24)));
 //       adabter.getFragment(new TabItems(new ArtistFrag(),"Artists",getResources().getDrawable(R.drawable.baseline_person_24)));
//        adabter.getFragment(new TabItems(new FavouriteFrag(),"Favourites",getResources().getDrawable(R.drawable.baseline_stars_24)));
        viewPager.setAdapter(adabter);
        tabLayout.setupWithViewPager(viewPager);




    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent=new Intent(MainActivity.this,PlayService.class);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);

    }



    public static ArrayList<Song> getSongs() {
        return songs;
    }

    public static void setSongs(ArrayList<Song> songs) {
        MainActivity.songs = songs;
    }

    public static void FillSongs(Context context){
        songs= G.SongList(context);
    }



    private boolean hasPermissions(Context context,String... PERMISSIONS){
        if (context!=null&&PERMISSIONS !=null){
           for (String permissions:PERMISSIONS){
               if (ActivityCompat.checkSelfPermission(context,permissions)!=PackageManager.PERMISSION_GRANTED){
                   return false;
               }
           }
        }
        return true;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        new MenuInflater(this).inflate(R.menu.toolbar_menu,menu);
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem menuItem=menu.findItem(R.id.btnSearch);
        SearchView searchView= (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput =newText.toLowerCase();
        ArrayList<Song> myFiles=new ArrayList<>();
        for (Song song:songs){
            if (song.getPath().toLowerCase().contains(userInput)){
                myFiles.add(song);
            }
        }
        SongsFrag.Tracksadapter.updateList(myFiles);
    return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences.Editor editor=getSharedPreferences(G.MY_SORT_PREF,MODE_PRIVATE).edit();
        if (item.getItemId()==R.id.exit){
             finish();
        }
        if (item.getItemId()==R.id.info){
             Intent intent=new Intent(MainActivity.this, InfoActivity.class);
             startActivity(intent);
        }
        if (item.getItemId()==R.id.by_name){
             editor.putString("sorting","sortByName");
             editor.apply();
             this.recreate();
        }
        if (item.getItemId()==R.id.by_date){
            editor.putString("sorting","sortByDate");
            editor.apply();
            this.recreate();
        }
        if (item.getItemId()==R.id.by_size){
            editor.putString("sorting","sortBySize");
            editor.apply();
            this.recreate();
        }

        return super.onOptionsItemSelected(item);
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
        mainCardView=findViewById(R.id.playCardView);
       btnPlay_Pause=findViewById(R.id.btnPlay);
       btnPlay_Next=findViewById(R.id.btnNext);
       btnPlay_Back=findViewById(R.id.btnBack);
       txtSongName=findViewById(R.id.txtMusicName);
       txtArtistName=findViewById(R.id.txtArtistName);
       imgAlbumeArt=findViewById(R.id.imageListMain);
   //searchBar=findViewById(R.id.search_bar);
    }
    private void  metaData(String uri){
        MediaMetadataRetriever retriever =new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        byte[] art=retriever.getEmbeddedPicture();
        Bitmap bitmap;
        if (art!=null){
            bitmap= BitmapFactory.decodeByteArray(art,0,art.length);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@Nullable Palette palette) {
                    Palette.Swatch swatch=palette.getMutedSwatch();
                    if (swatch!=null){
                        GradientDrawable gradientDrawable=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{swatch.getRgb(),0x00000000});
                        GradientDrawable gradientDrawableBg=new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,new int[]{swatch.getRgb(),swatch.getRgb()});
                        mainCardView.setBackground(gradientDrawableBg);
                        txtArtistName.setTextColor(Color.WHITE);
                        txtSongName.setTextColor(Color.WHITE);}


                    else {
                        mainCardView.setBackgroundColor(Color.GRAY);
                        txtArtistName.setTextColor(Color.WHITE);
                        txtSongName.setTextColor(Color.WHITE);

                    }

                }
            });

        }

    }



}