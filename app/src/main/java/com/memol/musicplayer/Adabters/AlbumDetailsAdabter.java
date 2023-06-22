package com.memol.musicplayer.Adabters;

import static com.memol.musicplayer.Fragments.AlbumFrag.albumAdabter;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.memol.musicplayer.G;
import com.memol.musicplayer.GlideApp;
import com.memol.musicplayer.Main.PlayActivity;
import com.memol.musicplayer.Model.Song;
import com.memol.musicplayer.R;

import java.io.File;
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
                context.startActivity(intent);
            }
        });

        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(context,v);
                popupMenu.getMenuInflater().inflate(R.menu.popup_songlist,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId()==R.id.delete){
                            deleteFile(position,v);
                            albumAdabter.notifyItemRemoved(position);
                            albumAdabter.notifyItemRangeChanged(position,G.albumsList.size());
                        }
                        return true;
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumDetailsList.size();
    }
    private void deleteFile(int position, View v) {
        Uri contentUri=ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Long.parseLong(albumDetailsList.get(position).getId()));
        File file=new File(albumDetailsList.get(position).getPath());
        boolean deleted=file.delete();
        if (deleted){
            context.getContentResolver().delete(contentUri,null,null);
            albumDetailsList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,albumDetailsList.size());
            albumAdabter.notifyItemRemoved(position);
            albumAdabter.notifyItemRangeChanged(position, G.albumsList.size());
            Toast.makeText(context, "File deleted", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Please allow access to the file", Toast.LENGTH_SHORT).show();
        }
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
