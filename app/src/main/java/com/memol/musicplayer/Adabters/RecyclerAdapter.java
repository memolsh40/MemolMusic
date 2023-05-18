package com.memol.musicplayer.Adabters;

import static com.memol.musicplayer.MainActivity.btnPlay_Back;
import static com.memol.musicplayer.MainActivity.btnPlay_Next;
import static com.memol.musicplayer.MainActivity.btnPlay_Pause;
import static com.memol.musicplayer.MainActivity.cardView;
import static com.memol.musicplayer.MainActivity.imgAlbumeArt;
import static com.memol.musicplayer.MainActivity.playService;
import static com.memol.musicplayer.MainActivity.txtArtistName;
import static com.memol.musicplayer.MainActivity.txtSongName;
import static com.memol.musicplayer.PlayService.mediaPlayer;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.memol.musicplayer.GlideApp;
import com.memol.musicplayer.Model.Song;
import com.memol.musicplayer.MyThread;
import com.memol.musicplayer.R;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    Context context;
    ArrayList<Song> songArrayList;
    int count;
    public static int POSITION;
    Bitmap bitmap;
    android.os.Handler handler;
    MyThread thread;




    public RecyclerAdapter(Context context, ArrayList<Song> songs, android.os.Handler handler) {
        this.context=context;
        this.songArrayList=songs;
        this.handler=handler;

    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder holder;
        View view = null;

            view =LayoutInflater.from(context).inflate(R.layout.song_list,parent,false);
           holder = new MyViewHolder(view);
            return holder;



    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,  int position) {
        POSITION = position;
        Song song = songArrayList.get(position);


        holder.txtMusicName.setText(song.getTitle());
        holder.txtArtistName.setText(song.getArtist());


        new Runnable() {
            @Override
            public void run() {

                GlideApp.with(context).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), songArrayList.get(position).getAlbumId()))
                        .error(R.drawable.music_image)
                        .placeholder(R.drawable.music_image)
                        .centerCrop()
                        .fallback(R.drawable.music_image)
                        .into(holder.imageView);
            }
        }.run();








String url=song.getPath();


holder.cardView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        playService.StartMusic(url);
        cardView.setVisibility(View.VISIBLE);
        btnPlay_Pause.setIconResource(R.drawable.baseline_pause_24);
     POSITION=position;
        txtSongName.setText(song.getTitle());
        txtArtistName.setText(song.getArtist());
        new Runnable() {
            @Override
            public void run() {

                GlideApp.with(context).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), songArrayList.get(POSITION).getAlbumId()))
                        .error(R.drawable.music_image)
                        .placeholder(R.drawable.music_image)
                        .centerCrop()
                        .fallback(R.drawable.music_image)
                        .into(imgAlbumeArt);
            }
        }.run();

    }
});
btnPlay_Pause.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            btnPlay_Pause.setIconResource(R.drawable._8px);
        } else {
            mediaPlayer.start();
            btnPlay_Pause.setIconResource(R.drawable.baseline_pause_24);
        }



    }

});
btnPlay_Next.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        int PositinNext=POSITION+=1;
        Log.i("MyTagNext", String.valueOf(PositinNext));
       String UriNext=songArrayList.get(PositinNext).getPath();
        Log.i("MyTagNext", UriNext);
        playService.StartMusic(UriNext);
        cardView.setVisibility(View.VISIBLE);
        btnPlay_Pause.setIconResource(R.drawable.baseline_pause_24);
        txtSongName.setText(songArrayList.get(PositinNext).getTitle());
        txtArtistName.setText(songArrayList.get(PositinNext).getArtist());
        new Runnable() {
            @Override
            public void run() {

                GlideApp.with(context).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), songArrayList.get(PositinNext).getAlbumId()))
                        .error(R.drawable.baseline_music_note_24)
                        .placeholder(R.drawable.baseline_music_note_24)
                        .centerCrop()
                        .fallback(R.drawable.ic_launcher_foreground)
                        .into(imgAlbumeArt);
            }
        }.run();

    }

});
btnPlay_Back.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {


        int PositionBack=(POSITION-=1);
        int PositionSize=(songArrayList.size()-1);
        txtSongName.setText(songArrayList.get(PositionBack).getTitle());
        txtArtistName.setText(songArrayList.get(PositionBack).getArtist());
  if (PositionBack<0){
      Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
      Log.i("TagMe", String.valueOf(PositionBack));
  }else {
      String UriNext = songArrayList.get(PositionBack).getPath();
      playService.StartMusic(UriNext);
      cardView.setVisibility(View.VISIBLE);
      btnPlay_Pause.setIconResource(R.drawable.baseline_pause_24);
      new Runnable() {
          @Override
          public void run() {

              GlideApp.with(context).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), songArrayList.get(PositionBack).getAlbumId()))
                      .error(R.drawable.baseline_music_note_24)
                      .placeholder(R.drawable.baseline_music_note_24)
                      .centerCrop()
                      .fallback(R.drawable.ic_launcher_foreground)
                      .into(imgAlbumeArt);
          }
      }.run();
  }










    }

});











    }

    @Override
    public int getItemCount() {
        return songArrayList.size();
    }

    public static class MyViewHolder  extends RecyclerView.ViewHolder{
        public TextView txtMusicName;
        public TextView txtArtistName;
        public ImageView imageView;
        public CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtArtistName = itemView.findViewById(R.id.txtArtistName);
            txtMusicName = itemView.findViewById(R.id.txtMusicName);
            imageView= itemView.findViewById(R.id.imageListFrag);
            cardView=itemView.findViewById(R.id.cardViewList);




        }
    }



}
