package com.memol.musicplayer.Adabters;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.memol.musicplayer.GlideApp;
import com.memol.musicplayer.Main.MainActivity;
import com.memol.musicplayer.Main.PlayActivity;
import com.memol.musicplayer.Model.Song;
import com.memol.musicplayer.R;

import java.util.ArrayList;

public class AlbumDetailsAdabter extends RecyclerView.Adapter<AlbumDetailsAdabter.ViewHolder> {
    public static ArrayList<Song> albumDetailsList=new ArrayList<>();
    private Context context;
    private int recource;

    public AlbumDetailsAdabter(ArrayList<Song> albumDetailsList, Context context, int recource) {
        this.albumDetailsList = albumDetailsList;
        this.context = context;
        this.recource = recource;
    }

    @NonNull
    @Override
    public AlbumDetailsAdabter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(recource,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumDetailsAdabter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Song song=albumDetailsList.get(position);
        holder.txtArtistName.setText(song.getArtist());
        holder.txtMusicName.setText(song.getTitle());
        new Runnable() {
            @Override
            public void run() {

                GlideApp.with(context).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumDetailsList.get(position).getAlbumId()))
                        .error(R.drawable.music_blue_night)
                        .placeholder(R.drawable.music_blue_night)
                        .centerCrop()
                        .fallback(R.drawable.music_blue_night)
                        .into(holder.imageView);
            }
        }.run();
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(context, PlayActivity.class);
                intent.putExtra("sender","albumDetails");
                intent.putExtra("position",position);
                MainActivity.playService.setPosition(position);
                MainActivity.setSongs(albumDetailsList);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return albumDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtMusicName;
        public TextView txtArtistName;
        public ImageView imageView;
        public CardView cardView;
        public MaterialButton btnMore;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtArtistName = itemView.findViewById(R.id.txtArtistName);
            txtMusicName = itemView.findViewById(R.id.txtMusicName);
            imageView= itemView.findViewById(R.id.imageListFrag);
            cardView=itemView.findViewById(R.id.cardViewList);
            btnMore=itemView.findViewById(R.id.iconButton);
        }
    }
}
