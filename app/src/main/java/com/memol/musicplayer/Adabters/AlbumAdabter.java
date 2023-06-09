package com.memol.musicplayer.Adabters;

import static com.memol.musicplayer.Main.MainActivity.playService;

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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.memol.musicplayer.Details.AlbumDetails;
import com.memol.musicplayer.G;
import com.memol.musicplayer.Model.Song;
import com.memol.musicplayer.R;

import java.util.ArrayList;

public class AlbumAdabter extends RecyclerView.Adapter<AlbumAdabter.ViewHolder> {
    public static   ArrayList<Song> albumList;
    private final Context context;
    private int recource;
    View view;

    public AlbumAdabter(ArrayList<Song> songArrayList, Context context, int recource) {
        this.albumList = songArrayList;
        this.context = context;
        this.recource = recource;

    }

    @NonNull
    @Override
    public AlbumAdabter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(context).inflate(recource,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdabter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Song song= albumList.get(position);
        holder.txtAlbum.setText(song.getAlbum());
        holder.txtAlbumInfo.setText(song.getArtist());

        new Runnable() {
            @Override
            public void run() {

                Glide.with(context).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumList.get(position).getAlbumId()))
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

                Intent intent =new Intent(context, AlbumDetails.class);
                intent.putExtra("albumName", albumList.get(position).getAlbum());
                context.startActivity(intent);

                Log.i("AlbumeSize", String.valueOf(G.albumsList.size()));
                Log.i("AlbumeSize", String.valueOf(G.SongList(context).size()));
                Log.i("AlbumeSize", String.valueOf(playService.getPosition()));




            }
        });

    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAlbum;
        TextView txtAlbum;
      TextView txtAlbumInfo;

        CardView albumeCardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAlbum=itemView.findViewById(R.id.AlbumImage);
            txtAlbum=itemView.findViewById(R.id.txtAlbum);
            txtAlbumInfo=itemView.findViewById(R.id.txtAlbumInfo);
            albumeCardView=itemView.findViewById(R.id.AlbumeListCardView);
        }
    }
}
