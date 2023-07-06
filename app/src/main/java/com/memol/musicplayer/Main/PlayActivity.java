package com.memol.musicplayer.Main;

import static com.memol.musicplayer.Adabters.AlbumDetailsAdabter.albumDetailsList;
import static com.memol.musicplayer.Adabters.RecyclerAdapter.mFiles;
import static com.memol.musicplayer.Main.MainActivity.btnPlay_Pause;
import static com.memol.musicplayer.Main.MainActivity.imgAlbumeArt;
import static com.memol.musicplayer.Main.MainActivity.mainCardView;
import static com.memol.musicplayer.Main.MainActivity.playService;
import static com.memol.musicplayer.Main.MainActivity.repeatBoolean;
import static com.memol.musicplayer.Main.MainActivity.shuffleBoolean;
import static com.memol.musicplayer.Main.MainActivity.txtArtistName;
import static com.memol.musicplayer.Main.MainActivity.txtSongName;
import static com.memol.musicplayer.Main.PlayService.mediaPlayer;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.memol.musicplayer.G;
import com.memol.musicplayer.Model.Song;
import com.memol.musicplayer.R;

import java.util.ArrayList;
import java.util.Timer;

public class PlayActivity extends AppCompatActivity implements ActionPlaying {
    @SuppressLint("StaticFieldLeak")
    public static LinearLayout playerMcontiner;
    public static ImageView gradientBG;
    public static SeekBar seekBarBtnShit;
    public static MaterialButton btnBackBtnShit;
    public static MaterialButton btnPlayBtnShit;
    public static MaterialButton btnNextBtnShit;
    public static MaterialButton btnShuffle;
    public static MaterialButton btnReply;
    @SuppressLint("StaticFieldLeak")
    public static ImageView imageViewBtnShit;
    @SuppressLint("StaticFieldLeak")
    public static TextView txtSongNameBtnShit;
    @SuppressLint("StaticFieldLeak")
    public static  TextView txtArtistNameBtnShit;
    @SuppressLint("StaticFieldLeak")
    public static  TextView txtDuration;
    public static  TextView txtDurationMx;
    Handler handler=new Handler();


    Thread playThread;
    Thread nextThread;
    Thread prevThread;
    int position=-1;
    int mCurrentPosition;
    String uri;
   public static ArrayList<Song> playActList =new ArrayList<>();


    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        SetupView();
        getInitMethodWhenPlay();
        metaData(playActList.get(position).getPath());
        mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
        int durationMx=Integer.parseInt(playActList.get(position).getDuration())/1000;
        txtDurationMx.setText(formatted(durationMx));
        seekBarBtnShit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
                }


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                seekBarBtnShit.setProgress(mCurrentPosition);
                txtDuration.setText(formatted(mCurrentPosition));

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        PlayActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                     mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBarBtnShit.setProgress(mCurrentPosition);
                    txtDuration.setText(formatted(mCurrentPosition));
                }
                handler.postDelayed(this,1000);
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                nextBtnClicked();
                mediaPlayer.start();
                btnPlayBtnShit.setIconResource(R.drawable.baseline_pause_24);
            }
        });


        if (shuffleBoolean==false){
            btnShuffle.setIconResource(R.drawable.baseline_shuffle_24);
        } else if (shuffleBoolean==true) {
            btnShuffle.setIconResource(R.drawable.baseline_shuffle_on_24);
        }
        if (repeatBoolean==false){
            btnReply.setIconResource(R.drawable.baseline_replay_24);
        } else if (repeatBoolean==true) {
            btnReply.setIconResource(R.drawable.baseline_replay_circle_filled_24);
        }

        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shuffleBoolean==true){
                    shuffleBoolean=false;
                    btnShuffle.setIconResource(R.drawable.baseline_shuffle_24);
                } else {
                    shuffleBoolean=true;
                    btnShuffle.setIconResource(R.drawable.baseline_shuffle_on_24);
                }
            }
        });
        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeatBoolean==true){
                    repeatBoolean=false;
                    btnReply.setIconResource(R.drawable.baseline_replay_24);
                } else {
                    repeatBoolean=true;
                    btnReply.setIconResource(R.drawable.baseline_replay_circle_filled_24);
                }
            }
        });


    }

    @Override
    protected void onResume() {

        prevThreadBtn();
        playThreadBtn();
        nextThreadBtn();
        super.onResume();
    }


    private void nextThreadBtn() {
        nextThread=new Thread(new Runnable() {
            @Override
            public void run() {
                btnNextBtnShit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextBtnClicked();
                    }
                });
            }
        });nextThread.start();
    }

    public void nextBtnClicked() {
        if (mediaPlayer.isPlaying()){
            if (shuffleBoolean&&!repeatBoolean){
                position=G.getRandom(playActList.size()-1);
            }
            else if (!shuffleBoolean&&!repeatBoolean){
                position=((position+1)% playActList.size());
            }

            playService.setPosition(position);
            uri= playActList.get(position).getPath();
            playService.StartMusic(uri);
            btnPlayBtnShit.setIconResource(R.drawable.baseline_pause_24);
            txtSongNameBtnShit.setText(playActList.get(position).getTitle());
            txtArtistNameBtnShit.setText(playActList.get(position).getArtist());
            txtSongName.setText(playActList.get(position).getTitle());
            txtArtistName.setText(playActList.get(position).getArtist());
            seekBarBtnShit.setMax(mediaPlayer.getDuration()/1000);
            PlayActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBarBtnShit.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });

            new Runnable() {
                @Override
                public void run() {

                    Glide.with(getApplicationContext()).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), playActList.get(position).getAlbumId()))
                            .error(R.drawable.music_blue_night)
                            .placeholder(R.drawable.music_blue_night)
                            .centerCrop()
                            .fallback(R.drawable.music_blue_night)
                            .into(imageViewBtnShit);
                    Glide.with(getApplicationContext()).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), playActList.get(position).getAlbumId()))
                            .error(R.drawable.music_blue_night)
                            .placeholder(R.drawable.music_blue_night)
                            .centerCrop()
                            .fallback(R.drawable.music_blue_night)
                            .into(imgAlbumeArt);
                }
            }.run();

            int durationMx=Integer.parseInt(playActList.get(position).getDuration())/1000;
            txtDurationMx.setText(formatted(durationMx));
        }
        else {
            if (shuffleBoolean&&!repeatBoolean){
                position=G.getRandom(playActList.size()-1);
            }
            else if (!shuffleBoolean&&!repeatBoolean){
                position=((position+1)% playActList.size());
            }
            seekBarBtnShit.setMax(mediaPlayer.getDuration()/1000);
            PlayActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBarBtnShit.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            playService.setPosition(position);
            uri= playActList.get(position).getPath();
            playService.StartWhenStop(uri);
            btnPlayBtnShit.setIconResource(R.drawable._8px);
            txtSongNameBtnShit.setText(playActList.get(position).getTitle());
            txtArtistNameBtnShit.setText(playActList.get(position).getArtist());
            txtSongName.setText(playActList.get(position).getTitle());
            txtArtistName.setText(playActList.get(position).getArtist());
            new Runnable() {
                @Override
                public void run() {

                    Glide.with(getApplicationContext()).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), playActList.get(position).getAlbumId()))
                            .error(R.drawable.music_blue_night)
                            .placeholder(R.drawable.music_blue_night)
                            .centerCrop()
                            .fallback(R.drawable.music_blue_night)
                            .into(imageViewBtnShit);
                    Glide.with(getApplicationContext()).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), playActList.get(position).getAlbumId()))
                            .error(R.drawable.music_blue_night)
                            .placeholder(R.drawable.music_blue_night)
                            .centerCrop()
                            .fallback(R.drawable.music_blue_night)
                            .into(imgAlbumeArt);

                }
            }.run();

            int durationMx=Integer.parseInt(playActList.get(position).getDuration())/1000;
            txtDurationMx.setText(formatted(durationMx));
        }
        seekBarBtnShit.setMax(mediaPlayer.getDuration()/1000);
        seekBarBtnShit.setProgress(0);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                nextBtnClicked();
                mediaPlayer.start();
                btnPlayBtnShit.setIconResource(R.drawable.baseline_pause_24);
            }
        });
        metaData(playActList.get(position).getPath());
    }

    private void playThreadBtn() {
        playThread=new Thread(new Runnable() {
            @Override
            public void run() {
                btnPlayBtnShit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playPauseBtnClicked();
                    }
                });
                btnPlay_Pause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playPauseBtnClicked();
                    }
                });
            }
        });playThread.start();
    }

    public void playPauseBtnClicked() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            btnPlayBtnShit.setIconResource(R.drawable._8px);
            btnPlay_Pause.setIconResource(R.drawable._8px);
            seekBarBtnShit.setMax(mediaPlayer.getDuration()/1000);
            PlayActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBarBtnShit.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
        }
        else {
            btnPlayBtnShit.setIconResource(R.drawable.baseline_pause_24);
            btnPlay_Pause.setIconResource(R.drawable.baseline_pause_24);
            mediaPlayer.start();
            seekBarBtnShit.setMax(mediaPlayer.getDuration()/1000);
            PlayActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBarBtnShit.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
        }


    }

    private void prevThreadBtn() {
        prevThread=new Thread(new Runnable() {
            @Override
            public void run() {
                btnBackBtnShit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        backBtnClicked();
                    }
                });
            }
        });prevThread.start();
    }

    public void backBtnClicked() {
        if (mediaPlayer.isPlaying()){
            if (shuffleBoolean&&!repeatBoolean){
                position=G.getRandom(playActList.size()-1);
            }
            else if (!shuffleBoolean&&!repeatBoolean){
                position=((position-1)<0 ? (playActList.size()-1):(position-1));
            }
            if (mCurrentPosition>5) {
                position=playService.getPosition();
                playService.StartMusic(uri);

            }

            playService.setPosition(position);
            uri= playActList.get(position).getPath();
            playService.StartMusic(uri);
            btnPlayBtnShit.setIconResource(R.drawable.baseline_pause_24);
            txtSongNameBtnShit.setText(playActList.get(position).getTitle());
            txtArtistNameBtnShit.setText(playActList.get(position).getArtist());
            txtSongName.setText(playActList.get(position).getTitle());
            txtArtistName.setText(playActList.get(position).getArtist());
            seekBarBtnShit.setMax(mediaPlayer.getDuration()/1000);
            PlayActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBarBtnShit.setProgress(mCurrentPosition);


                    }
                    handler.postDelayed(this,1000);
                }
            });
            new Runnable() {
                @Override
                public void run() {

                    Glide.with(getApplicationContext()).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), playActList.get(position).getAlbumId()))
                            .error(R.drawable.music_blue_night)
                            .placeholder(R.drawable.music_blue_night)
                            .centerCrop()
                            .fallback(R.drawable.music_blue_night)
                            .into(imageViewBtnShit);
                    Glide.with(getApplicationContext()).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), playActList.get(position).getAlbumId()))
                            .error(R.drawable.music_blue_night)
                            .placeholder(R.drawable.music_blue_night)
                            .centerCrop()
                            .fallback(R.drawable.music_blue_night)
                            .into(imgAlbumeArt);
                }
            }.run();

            int durationMx=Integer.parseInt(playActList.get(position).getDuration())/1000;
            txtDurationMx.setText(formatted(durationMx));
        }
        else {
            if (shuffleBoolean&&!repeatBoolean){
                position=G.getRandom(playActList.size()-1);
            }
            else if (!shuffleBoolean&&!repeatBoolean){
                position=((position-1)<0 ? (playActList.size()-1):(position-1));
            }
            if (mCurrentPosition>3) {
                position=playService.getPosition();
                playService.StartMusic(uri);

            }
            playService.setPosition(position);
            uri= playActList.get(position).getPath();
            playService.StartWhenStop(uri);
            btnPlayBtnShit.setIconResource(R.drawable._8px);
            txtSongNameBtnShit.setText(playActList.get(position).getTitle());
            txtArtistNameBtnShit.setText(playActList.get(position).getArtist());
            txtSongName.setText(playActList.get(position).getTitle());
            txtArtistName.setText(playActList.get(position).getArtist());
            seekBarBtnShit.setMax(mediaPlayer.getDuration()/1000);
            PlayActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBarBtnShit.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            new Runnable() {
                @Override
                public void run() {

                    Glide.with(getApplicationContext()).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), playActList.get(position).getAlbumId()))
                            .error(R.drawable.music_blue_night)
                            .placeholder(R.drawable.music_blue_night)
                            .centerCrop()
                            .fallback(R.drawable.music_blue_night)
                            .into(imageViewBtnShit);
                    Glide.with(getApplicationContext()).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), playActList.get(position).getAlbumId()))
                            .error(R.drawable.music_blue_night)
                            .placeholder(R.drawable.music_blue_night)
                            .centerCrop()
                            .fallback(R.drawable.music_blue_night)
                            .into(imgAlbumeArt);
                }
            }.run();

            int durationMx=Integer.parseInt(playActList.get(position).getDuration())/1000;
            txtDurationMx.setText(formatted(durationMx));
        }
        seekBarBtnShit.setMax(mediaPlayer.getDuration()/1000);
        seekBarBtnShit.setProgress(0);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                nextBtnClicked();
                mediaPlayer.start();
                btnPlayBtnShit.setIconResource(R.drawable.baseline_pause_24);
            }
        });
        metaData(playActList.get(position).getPath());
    }


    private void getInitMethodWhenPlay() {
        position=getIntent().getIntExtra("position",-1);
        String sender=getIntent().getStringExtra("sender");
        if (sender!=null &&sender.equals("albumDetails")){
            playActList =albumDetailsList;
            MainActivity.setSongs(albumDetailsList);
            playService.StartMusic(playActList.get(position).getPath());
            btnPlayBtnShit.setIconResource(R.drawable._8px);
            uri = playActList.get(position).getPath();
            txtSongNameBtnShit.setText(playActList.get(position).getTitle());
            txtArtistNameBtnShit.setText(playActList.get(position).getArtist());
            new Runnable() {
                @Override
                public void run() {

                    Glide.with(getApplicationContext()).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), playActList.get(position).getAlbumId()))
                            .error(R.drawable.music_blue_night)
                            .placeholder(R.drawable.music_blue_night)
                            .centerCrop()
                            .fallback(R.drawable.music_blue_night)
                            .into(imageViewBtnShit);
                }
            }.run();


            mainCardView.setVisibility(View.VISIBLE);
            String uri=playActList.get(position).getPath();
            playService.setPosition(position);
            btnPlay_Pause.setIconResource(R.drawable.baseline_pause_24);
            txtSongName.setText(playActList.get(position).getTitle());
            txtArtistName.setText(playActList.get(position).getArtist());
            new Runnable() {
                @Override
                public void run() {

                    Glide.with(getApplicationContext()).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), playActList.get(position).getAlbumId()))
                            .error(R.drawable.music_blue_night)
                            .placeholder(R.drawable.music_blue_night)
                            .centerCrop()
                            .fallback(R.drawable.music_blue_night)
                            .into(imgAlbumeArt);
                }
            }.run();
        }
        else {
            playActList =mFiles;
        }
       if (mediaPlayer.isPlaying()&&albumDetailsList.size()>0) {
           playActList=albumDetailsList;
           btnPlayBtnShit.setIconResource(R.drawable.baseline_pause_24);
           uri = playActList.get(position).getPath();
           Log.i("URI",uri);
           txtSongNameBtnShit.setText(playActList.get(position).getTitle());
           txtArtistNameBtnShit.setText(playActList.get(position).getArtist());
           new Runnable() {
               @Override
               public void run() {

                   Glide.with(getApplicationContext()).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), playActList.get(position).getAlbumId()))
                           .error(R.drawable.music_blue_night)
                           .placeholder(R.drawable.music_blue_night)
                           .centerCrop()
                           .fallback(R.drawable.music_blue_night)
                           .into(imageViewBtnShit);
               }
           }.run();



       }
       if (mediaPlayer.isPlaying()&&albumDetailsList.size()==0) {
           playActList =mFiles;
           btnPlayBtnShit.setIconResource(R.drawable.baseline_pause_24);
           uri = playActList.get(position).getPath();
           Log.i("position2", String.valueOf(position));
           txtSongNameBtnShit.setText(playActList.get(position).getTitle());
           txtArtistNameBtnShit.setText(playActList.get(position).getArtist());
           new Runnable() {
               @Override
               public void run() {

                   Glide.with(getApplicationContext()).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), playActList.get(position).getAlbumId()))
                           .error(R.drawable.music_blue_night)
                           .placeholder(R.drawable.music_blue_night)
                           .centerCrop()
                           .fallback(R.drawable.music_blue_night)
                           .into(imageViewBtnShit);
               }
           }.run();



       }
       if (!mediaPlayer.isPlaying()&&albumDetailsList.size()>0){
           playActList=albumDetailsList;
           btnPlayBtnShit.setIconResource(R.drawable._8px);
           uri = playActList.get(position).getPath();
           txtSongNameBtnShit.setText(playActList.get(position).getTitle());
           txtArtistNameBtnShit.setText(playActList.get(position).getArtist());
           new Runnable() {
               @Override
               public void run() {

                   Glide.with(getApplicationContext()).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), playActList.get(position).getAlbumId()))
                           .error(R.drawable.music_blue_night)
                           .placeholder(R.drawable.music_blue_night)
                           .centerCrop()
                           .fallback(R.drawable.music_blue_night)
                           .into(imageViewBtnShit);
               }
           }.run();




       }
       if (!mediaPlayer.isPlaying()&&albumDetailsList.size()==0){
           playActList =mFiles;
           btnPlayBtnShit.setIconResource(R.drawable._8px);
           uri = playActList.get(position).getPath();
           txtSongNameBtnShit.setText(playActList.get(position).getTitle());
           txtArtistNameBtnShit.setText(playActList.get(position).getArtist());
           new Runnable() {
               @Override
               public void run() {

                   Glide.with(getApplicationContext()).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), playActList.get(position).getAlbumId()))
                           .error(R.drawable.music_blue_night)
                           .placeholder(R.drawable.music_blue_night)
                           .centerCrop()
                           .fallback(R.drawable.music_blue_night)
                           .into(imageViewBtnShit);
               }
           }.run();




       }

        seekBarBtnShit.setMax(mediaPlayer.getDuration()/1000);
        seekBarBtnShit.setProgress(0);
    }

    private String formatted(int mCurrentPosition) {
        String totalout="";
        String totalNew="";
        String secounds = String.valueOf(mCurrentPosition%60);
        String minutes = String.valueOf(mCurrentPosition/60);
        totalout=minutes+ ":" +secounds;
        totalNew=minutes+ ":" + "0" +secounds;
        if (secounds.length()==1){
            return totalNew;
        }
        return totalout;
    }

    @SuppressLint("CutPasteId")
    private void SetupView() {
        imageViewBtnShit=findViewById(R.id.imageViewbtnShit);
        seekBarBtnShit=findViewById(R.id.SeekbarbtnShit);
        btnBackBtnShit=findViewById(R.id.btnBackbtnShit);
        btnPlayBtnShit=findViewById(R.id.btnPlaybtnShit);
        btnNextBtnShit=findViewById(R.id.btnNextbtnShit);
        btnShuffle=findViewById(R.id.btnShuffle);
        btnReply=findViewById(R.id.btnReply);
        txtSongNameBtnShit=findViewById(R.id.txtMusicNameBtnShit);
        txtArtistNameBtnShit=findViewById(R.id.txtMusicArtistBtnShit);
        txtDuration=findViewById(R.id.txtTime);
        txtDurationMx=findViewById(R.id.txtCompletTime);
        playerMcontiner=findViewById(R.id.play_activity);

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
                       // GradientDrawable gradientDrawable=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{swatch.getRgb(),0x00000000});
                        GradientDrawable gradientDrawableBg=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{swatch.getRgb(),swatch.getRgb()});
                       playerMcontiner.setBackground(gradientDrawableBg);
                        txtArtistNameBtnShit.setTextColor(Color.WHITE);
                        txtSongNameBtnShit.setTextColor(Color.WHITE);

                    }
                    else {
                        playerMcontiner.setBackgroundColor(Color.GRAY);
                        txtArtistNameBtnShit.setTextColor(Color.WHITE);
                        txtSongNameBtnShit.setTextColor(Color.WHITE);
                    }

                }
            });

        }

    }




}