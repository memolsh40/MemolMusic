package com.memol.musicplayer.Adabters;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.memol.musicplayer.GlideApp;
import com.memol.musicplayer.Model.Song;
import com.memol.musicplayer.R;

import java.util.ArrayList;

public class AlbumAdabter extends RecyclerView.Adapter<AlbumAdabter.ViewHolder> {
    private ArrayList<Song> songArrayList;
    private Context context;
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
        new Runnable() {
            @Override
            public void run() {

                GlideApp.with(context).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), songArrayList.get(position).getAlbumId()))
                        .error(R.drawable.music_image)
                        .placeholder(R.drawable.music_image)
                        .centerCrop()
                        .fallback(R.drawable.music_image)
                        .into(holder.imgAlbum);
            }
        }.run();

    }

    @Override
    public int getItemCount() {
        return songArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAlbum;
        TextView txtAlbum;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAlbum=itemView.findViewById(R.id.AlbumImage);
            txtAlbum=itemView.findViewById(R.id.txtAlbum);
        }
    }
}
