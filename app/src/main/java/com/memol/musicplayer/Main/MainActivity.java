package com.memol.musicplayer.Main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.memol.musicplayer.Fragments.ButtonShitPlay;
import com.memol.musicplayer.Fragments.FavouriteFrag;
import com.memol.musicplayer.Fragments.SongsFrag;
import com.memol.musicplayer.Model.TabItems;
import com.memol.musicplayer.PlayService;
import com.memol.musicplayer.R;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;

    MaterialButton btnSearch;
    SearchBar searchBar;
  public static   CardView cardView;
   public static MaterialButton btnPlay_Back;
   public static MaterialButton btnPlay_Pause;
   public static MaterialButton btnPlay_Next;
   public static TextView txtSongName;
   public static TextView txtArtistName;
   public static ImageView imgAlbumeArt;
   public static android.os.Handler MainHandler=new Handler();
    ButtonShitPlay buttonShitPlay;

  public static PlayService playService;
  ViewPagerAdabter adabter;



    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetupView();
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_search_24);
        buttonShitPlay=new ButtonShitPlay();
        getSupportActionBar().setTitle("MemolMusic");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cardView.setVisibility(View.INVISIBLE);


        adabter=new ViewPagerAdabter(MainActivity.this,getSupportFragmentManager());
        adabter.getFragment(new TabItems(new SongsFrag(),"Tracks",getResources().getDrawable(R.drawable.baseline_music_note_24)));
        adabter.getFragment(new TabItems(new AlbumFrag(),"Albums",getResources().getDrawable(R.drawable.baseline_library_music_24)));
        adabter.getFragment(new TabItems(new ArtistFrag(),"Artists",getResources().getDrawable(R.drawable.baseline_person_24)));
        adabter.getFragment(new TabItems(new FavouriteFrag(),"Favourites",getResources().getDrawable(R.drawable.baseline_stars_24)));
        viewPager.setAdapter(adabter);
        tabLayout.setupWithViewPager(viewPager);



        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buttonShitPlay.show(getSupportFragmentManager(),buttonShitPlay.getTag());


            }
        });
    }

    private boolean CheckPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            return true;
        }else {
            return false;
        }
    }
    void requestPermissions(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_MEDIA_AUDIO)){
            Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show();
        }else {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_MEDIA_AUDIO},2);
        }
    }
    void requestPermissionsFile(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show();
        }else {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},3);
        }
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
        if (CheckPermission()==false){
            requestPermissions();
            adabter.notifyDataSetChanged();
        }
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
       cardView=findViewById(R.id.playCardView);
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