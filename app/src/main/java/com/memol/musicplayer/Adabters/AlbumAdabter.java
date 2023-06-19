package com.memol.musicplayer.Adabters;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
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

import com.memol.musicplayer.AlbumDetails;
import com.memol.musicplayer.GlideApp;
import com.memol.musicplayer.Model.Song;
import com.memol.musicplayer.R;

import java.util.ArrayList;

public class AlbumAdabter extends RecyclerView.Adapter<AlbumAdabter.ViewHolder> {
    private  ArrayList<Song> songArrayList=new ArrayList<>();
    private final Context context;
    private int recource;

    public AlbumAdabter(ArrayList<Song> songArrayList, Context context, int recource) {
        this.songArrayList = songArrayList;
        this.context = context;
        this.recource = recource;
    }

    @NonNull
    @Override
    public AlbumAdabter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(recource,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdabter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Song song=songArrayList.get(position);
        holder.txtAlbum.setText(song.getAlbum());
        Log.i("AlbumeList",song.getAlbum());
        new Runnable() {
            @Override
            public void run() {

                GlideApp.with(context).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), songArrayList.get(position).getAlbumId()))
                        .error(R.drawable.music_blue_night)
                        .placeholder(R.drawable.music_blue_night)
                        .centerCrop()
                        .fallback(R.drawable.music_blue_night)
                        .into(holder.imgAlbum);
            }
        }.run();
        holder.albumeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (songArrayList.get(position).equals(0)){
                    Toast.makeText(context, "Not Exists", Toast.LENGTH_SHORT).show();
                }
                Intent intent =new Intent(context, AlbumDetails.class);
                intent.putExtra("albumName",songArrayList.get(position).getAlbum());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return songArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAlbum;
        TextView txtAlbum;

        CardView albumeCardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAlbum=itemView.findViewById(R.id.AlbumImage);
            txtAlbum=itemView.findViewById(R.id.txtAlbum);
            albumeCardView=itemView.findViewById(R.id.AlbumeListCardView);
        }
    }
}
