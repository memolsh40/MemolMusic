package com.memol.musicplayer.Adabters;

import static com.memol.musicplayer.Adabters.AlbumAdabter.albumList;
import static com.memol.musicplayer.Adabters.AlbumDetailsAdabter.albumDetailsList;
import static com.memol.musicplayer.Fragments.AlbumFrag.albumAdabter;
import static com.memol.musicplayer.Main.MainActivity.btnPlay_Pause;
import static com.memol.musicplayer.Main.MainActivity.imgAlbumeArt;
import static com.memol.musicplayer.Main.MainActivity.mainCardView;
import static com.memol.musicplayer.Main.MainActivity.playService;
import static com.memol.musicplayer.Main.MainActivity.txtArtistName;
import static com.memol.musicplayer.Main.MainActivity.txtSongName;
import static com.memol.musicplayer.Main.PlayActivity.playerMcontiner;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.memol.musicplayer.G;
import com.memol.musicplayer.GlideApp;
import com.memol.musicplayer.Main.MainActivity;
import com.memol.musicplayer.Main.PlayActivity;
import com.memol.musicplayer.Model.Song;
import com.memol.musicplayer.R;

import java.io.File;
import java.util.ArrayList;

@SuppressWarnings("ALL")
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    Context context;
    ArrayList<Song> songArrayList;
    int count;
    public static int POSITION;
    Bitmap bitmap;
    android.os.Handler handler;





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
        G.artistList.clear();
        albumDetailsList.clear();
        G.albumsList.clear();
       MainActivity.setSongs(G.SongList(context));
        mainCardView.setVisibility(View.VISIBLE);
        metaData(songArrayList.get(position).getPath());
        String uri=song.getPath();
        playService.StartMusic(uri);
        playService.setPosition(position);
        btnPlay_Pause.setIconResource(R.drawable.baseline_pause_24);
        txtSongName.setText(song.getTitle());
        txtArtistName.setText(song.getArtist());

        new Runnable() {
            @Override
            public void run() {

                GlideApp.with(context).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), songArrayList.get(position).getAlbumId()))
                        .error(R.drawable.music_blue_night)
                        .placeholder(R.drawable.music_blue_night)
                        .centerCrop()
                        .fallback(R.drawable.music_blue_night)
                        .into(imgAlbumeArt);

            }
        }.run();

        Log.i("AlbumeSize", String.valueOf(G.albumsList.size()));
        Log.i("AlbumeSize", String.valueOf(G.SongList(context).size()));

    }
});


//mainCardView is Mini Player in MainActivity
mainCardView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        Intent intent =new Intent(context,PlayActivity.class);
        intent.putExtra("position",playService.getPosition());
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

    private void deleteFile(int position, View v) {
        Uri contentUri=ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Long.parseLong(songArrayList.get(position).getId()));
        File file=new File(songArrayList.get(position).getPath());
        boolean deleted=file.delete();
        if (deleted){
            context.getContentResolver().delete(contentUri,null,null);
            songArrayList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,songArrayList.size());
            albumAdabter.notifyItemRemoved(position);
            albumAdabter.notifyItemRangeChanged(position,G.albumsList.size());
            Toast.makeText(context, "File deleted", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Please allow access to the file", Toast.LENGTH_SHORT).show();
        }
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
        public MaterialButton btnMore;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtArtistName = itemView.findViewById(R.id.txtArtistName);
            txtMusicName = itemView.findViewById(R.id.txtMusicName);
            imageView= itemView.findViewById(R.id.imageListFrag);
            cardView=itemView.findViewById(R.id.cardViewList);
            btnMore=itemView.findViewById(R.id.iconButton);

        }

    }

    private int getPos (int position) {
        if(position < 0)
            return songArrayList.size()-1;
        else
            return position;
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
                        GradientDrawable gradientDrawableBg=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{swatch.getRgb(),swatch.getRgb()});
                        mainCardView.setBackground(gradientDrawableBg);
                        txtSongName.setTextColor(Color.WHITE);
                        txtArtistName.setTextColor(Color.WHITE);
                    }
                    else {
                        mainCardView.setBackgroundColor(Color.GRAY);

                    }

                }
            });

        }

    }




}
