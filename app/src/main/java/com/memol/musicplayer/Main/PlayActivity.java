package com.memol.musicplayer.Main;

import static com.memol.musicplayer.Main.MainActivity.btnPlay_Pause;
import static com.memol.musicplayer.Main.MainActivity.imgAlbumeArt;
import static com.memol.musicplayer.Main.MainActivity.playService;
import static com.memol.musicplayer.Main.MainActivity.repeatBoolean;
import static com.memol.musicplayer.Main.MainActivity.shuffleBoolean;
import static com.memol.musicplayer.Main.MainActivity.txtArtistName;
import static com.memol.musicplayer.Main.MainActivity.txtSongName;
import static com.memol.musicplayer.Main.PlayService.mediaPlayer;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.memol.musicplayer.G;
import com.memol.musicplayer.GlideApp;
import com.memol.musicplayer.Model.Song;
import com.memol.musicplayer.R;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

public class PlayActivity extends AppCompatActivity {
    public static SeekBar seekBarBtnShit;
    public static MaterialButton btnBackBtnShit;
    public static MaterialButton btnPlayBtnShit;
    public static MaterialButton btnNextBtnShit;
    public static MaterialButton btnShuffle;
    public static MaterialButton btnReply;
    public static ImageView imageViewBtnShit;
    public static TextView txtSongNameBtnShit;
    public static  TextView txtArtistNameBtnShit;
    public static  TextView txtDuration;
    public static  TextView txtDurationMx;
    Handler handler=new Handler();

    Thread playThread;
    Thread nextThread;
    Thread prevThread;
    int position=-1;
    String uri;
    ArrayList<Song> songs;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        SetupView();
        getInitMethodWhenPlay();
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

    private void nextBtnClicked() {
        if (mediaPlayer.isPlaying()){
            if (shuffleBoolean&&!repeatBoolean){
                position=getRandom(songs.size()-1);
            }
            else if (!shuffleBoolean&&!repeatBoolean){
                position=((position+1)%songs.size());
            }
            playService.setPosition(position);
            uri=songs.get(position).getPath();
            playService.StartMusic(uri);
            btnPlayBtnShit.setIconResource(R.drawable.baseline_pause_24);
            txtSongNameBtnShit.setText(songs.get(position).getTitle());
            txtArtistNameBtnShit.setText(songs.get(position).getArtist());
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
                            .into(imageViewBtnShit);
                    GlideApp.with(getApplicationContext()).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), songs.get(position).getAlbumId()))
                            .error(R.drawable.music_image)
                            .placeholder(R.drawable.music_image)
                            .centerCrop()
                            .fallback(R.drawable.music_image)
                            .into(imgAlbumeArt);
                }
            }.run();
            PlayActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBarBtnShit.setProgress(mCurrentPosition);
                        txtDuration.setText(formatted(mCurrentPosition));
                    }
                    handler.postDelayed(this,1000);
                }
            });
            int durationMx=Integer.parseInt(songs.get(position).getDuration())/1000;
            txtDurationMx.setText(formatted(durationMx));
        }else {
            if (shuffleBoolean&&!repeatBoolean){
                position=getRandom(songs.size()-1);
            }
            else if (!shuffleBoolean&&!repeatBoolean){
                position=((position+1)%songs.size());
            }
            playService.setPosition(position);
            uri=songs.get(position).getPath();
            playService.StartWhenStop(uri);
            btnPlayBtnShit.setIconResource(R.drawable._8px);
            txtSongNameBtnShit.setText(songs.get(position).getTitle());
            txtArtistNameBtnShit.setText(songs.get(position).getArtist());
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
                            .into(imageViewBtnShit);
                    GlideApp.with(getApplicationContext()).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), songs.get(position).getAlbumId()))
                            .error(R.drawable.music_image)
                            .placeholder(R.drawable.music_image)
                            .centerCrop()
                            .fallback(R.drawable.music_image)
                            .into(imgAlbumeArt);

                }
            }.run();
            PlayActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBarBtnShit.setProgress(mCurrentPosition);
                        txtDuration.setText(formatted(mCurrentPosition));
                    }
                    handler.postDelayed(this,1000);
                }
            });
            int durationMx=Integer.parseInt(songs.get(position).getDuration())/1000;
            txtDurationMx.setText(formatted(durationMx));
        }
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

    private void playPauseBtnClicked() {
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
        }else {
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

    private void backBtnClicked() {
        if (mediaPlayer.isPlaying()){
            if (shuffleBoolean&&!repeatBoolean){
                position=getRandom(songs.size()-1);
            }
            else if (!shuffleBoolean&&!repeatBoolean){
                position=((position-1)<0 ? (songs.size()-1):(position-1));
            }
            playService.setPosition(position);
            uri=songs.get(position).getPath();
            playService.StartMusic(uri);
            btnPlayBtnShit.setIconResource(R.drawable.baseline_pause_24);
            txtSongNameBtnShit.setText(songs.get(position).getTitle());
            txtArtistNameBtnShit.setText(songs.get(position).getArtist());
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
                            .into(imageViewBtnShit);
                    GlideApp.with(getApplicationContext()).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), songs.get(position).getAlbumId()))
                            .error(R.drawable.music_image)
                            .placeholder(R.drawable.music_image)
                            .centerCrop()
                            .fallback(R.drawable.music_image)
                            .into(imgAlbumeArt);
                }
            }.run();
            PlayActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBarBtnShit.setProgress(mCurrentPosition);
                        txtDuration.setText(formatted(mCurrentPosition));
                    }
                    handler.postDelayed(this,1000);
                }
            });
            int durationMx=Integer.parseInt(songs.get(position).getDuration())/1000;
            txtDurationMx.setText(formatted(durationMx));
        }else {
            if (shuffleBoolean&&!repeatBoolean){
                position=getRandom(songs.size()-1);
            }
            else if (!shuffleBoolean&&!repeatBoolean){
                position=((position-1)<0 ? (songs.size()-1):(position-1));
            }
            playService.setPosition(position);
            uri=songs.get(position).getPath();
            playService.StartWhenStop(uri);
            btnPlayBtnShit.setIconResource(R.drawable._8px);
            txtSongNameBtnShit.setText(songs.get(position).getTitle());
            txtArtistNameBtnShit.setText(songs.get(position).getArtist());
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
                            .into(imageViewBtnShit);
                    GlideApp.with(getApplicationContext()).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), songs.get(position).getAlbumId()))
                            .error(R.drawable.music_image)
                            .placeholder(R.drawable.music_image)
                            .centerCrop()
                            .fallback(R.drawable.music_image)
                            .into(imgAlbumeArt);
                }
            }.run();
            PlayActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBarBtnShit.setProgress(mCurrentPosition);
                        txtDuration.setText(formatted(mCurrentPosition));
                    }
                    handler.postDelayed(this,1000);
                }
            });
            int durationMx=Integer.parseInt(songs.get(position).getDuration())/1000;
            txtDurationMx.setText(formatted(durationMx));
        }
    }


    private void getInitMethodWhenPlay() {
        songs=G.SongList(getApplicationContext());
        position=getIntent().getIntExtra("position",-1);
       if (songs!=null&&mediaPlayer.isPlaying()) {
           btnPlayBtnShit.setIconResource(R.drawable.baseline_pause_24);
           uri = songs.get(position).getPath();
           txtSongNameBtnShit.setText(songs.get(position).getTitle());
           txtArtistNameBtnShit.setText(songs.get(position).getArtist());
           new Runnable() {
               @Override
               public void run() {

                   GlideApp.with(getApplicationContext()).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), songs.get(position).getAlbumId()))
                           .error(R.drawable.music_image)
                           .placeholder(R.drawable.music_image)
                           .centerCrop()
                           .fallback(R.drawable.music_image)
                           .into(imageViewBtnShit);
               }
           }.run();
           seekBarBtnShit.setMax(mediaPlayer.getDuration()/1000);
           seekBarBtnShit.setProgress(0);
           seekBarBtnShit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
               @Override
               public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                   if (mediaPlayer != null && fromUser) {
                       mediaPlayer.seekTo(progress * 1000);
                   }
               }

               @Override
               public void onStartTrackingTouch(SeekBar seekBar) {

               }

               @Override
               public void onStopTrackingTouch(SeekBar seekBar) {

               }
           });

           PlayActivity.this.runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   if (mediaPlayer != null) {
                       int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                       seekBarBtnShit.setProgress(mCurrentPosition);
                       txtDuration.setText(formatted(mCurrentPosition));
                   }
                   handler.postDelayed(this,1000);
               }
           });
           int durationMx=Integer.parseInt(songs.get(position).getDuration())/1000;
           txtDurationMx.setText(formatted(durationMx));
       }
       if (!mediaPlayer.isPlaying()){
           btnPlayBtnShit.setIconResource(R.drawable._8px);
           uri = songs.get(position).getPath();
           txtSongNameBtnShit.setText(songs.get(position).getTitle());
           txtArtistNameBtnShit.setText(songs.get(position).getArtist());
           new Runnable() {
               @Override
               public void run() {

                   GlideApp.with(getApplicationContext()).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), songs.get(position).getAlbumId()))
                           .error(R.drawable.music_image)
                           .placeholder(R.drawable.music_image)
                           .centerCrop()
                           .fallback(R.drawable.music_image)
                           .into(imageViewBtnShit);
               }
           }.run();
           seekBarBtnShit.setMax(mediaPlayer.getDuration()/1000);
           seekBarBtnShit.setProgress(0);
           seekBarBtnShit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
               @Override
               public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                   if (mediaPlayer != null && fromUser) {
                       mediaPlayer.seekTo(progress * 1000);
                   }
               }

               @Override
               public void onStartTrackingTouch(SeekBar seekBar) {

               }

               @Override
               public void onStopTrackingTouch(SeekBar seekBar) {

               }
           });

           PlayActivity.this.runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   if (mediaPlayer != null) {
                       int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                       seekBarBtnShit.setProgress(mCurrentPosition);
                       txtDuration.setText(formatted(mCurrentPosition));
                   }
                   handler.postDelayed(this,1000);
               }
           });
           int durationMx=Integer.parseInt(songs.get(position).getDuration())/1000;
           txtDurationMx.setText(formatted(durationMx));
       }
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
    }
    private int getRandom(int i) {
        Random random=new Random();
        return random.nextInt(i+1);
    }

    private void metaData(Uri uri, long albumId){
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource(String.valueOf(uri));
        byte[] art=retriever.getEmbeddedPicture();
        Bitmap bitmap;

//        if (art!=null){
//            bitmap= BitmapFactory.decodeByteArray(art,0,art.length);
//            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//                @Override
//                public void onGenerated(@Nullable Palette palette) {
//                Palette.Swatch swatch= palette.getDominantSwatch();
//                if (swatch!=null){
//                    ImageView grediend=findViewById(R.id.imageGrd);
//                    LinearLayout mContainer=findViewById(R.id.play_activity);
//                    grediend.setBackgroundResource(R.drawable.gradient_bg);
//                    mContainer.setBackgroundResource(R.drawable.main_pg);
//                    GradientDrawable gradientDrawable=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
//                            new int[]{swatch.getRgb(),0x00000000});
//                    grediend.setBackground(gradientDrawable);
//                    GradientDrawable gradientDrawableBg=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
//                            new int[]{swatch.getRgb(),swatch.getRgb()});
//                    mContainer.setBackground(gradientDrawable);
//                    txtSongName.setTextColor(swatch.getTitleTextColor());
//                    txtArtistName.setTextColor(swatch.getBodyTextColor());
//                }else {
//                    ImageView grediend=findViewById(R.id.imageGrd);
//                    LinearLayout mContainer=findViewById(R.id.play_activity);
//                    grediend.setBackgroundResource(R.drawable.gradient_bg);
//                    mContainer.setBackgroundResource(R.drawable.main_pg);
//                    GradientDrawable gradientDrawable=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
//                            new int[]{0xff000000,0x00000000});
//                    grediend.setBackground(gradientDrawable);
//                    GradientDrawable gradientDrawableBg=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
//                            new int[]{0xff000000,0xff000000});
//                    mContainer.setBackground(gradientDrawable);
//                    txtSongName.setTextColor(Color.WHITE);
//                    txtArtistName.setTextColor(Color.DKGRAY);
//                }
//                }
//            });
//
//
//
//
//
//
//        }

    }
}