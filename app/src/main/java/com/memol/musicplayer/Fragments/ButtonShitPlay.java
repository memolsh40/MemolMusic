package com.memol.musicplayer.Fragments;

import static com.memol.musicplayer.MainActivity.btnPlay_Pause;
import static com.memol.musicplayer.MainActivity.imgAlbumeArt;
import static com.memol.musicplayer.MainActivity.txtArtistName;
import static com.memol.musicplayer.MainActivity.txtSongName;
import static com.memol.musicplayer.PlayService.mediaPlayer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.memol.musicplayer.G;
import com.memol.musicplayer.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ButtonShitPlay#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ButtonShitPlay extends BottomSheetDialogFragment {
  public static   SeekBar seekBarBtnShit;
    public static MaterialButton btnBackBtnShit;
    public static MaterialButton btnPlayBtnShit;
    public static MaterialButton btnNextBtnShit;
    public static  ImageView imageViewBtnShit;
    public static TextView txtSongNameBtnShit;
    public static  TextView txtArtistNameBtnShit;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ButtonShitPlay() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ButtonShitPlay.
     */
    // TODO: Rename and change types and number of parameters
    public static ButtonShitPlay newInstance(String param1, String param2) {
        ButtonShitPlay fragment = new ButtonShitPlay();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View view= inflater.inflate(R.layout.fragment_button_shit_play, container, false);
      imageViewBtnShit=view.findViewById(R.id.imageViewbtnShit);
      seekBarBtnShit=view.findViewById(R.id.SeekbarbtnShit);
      btnBackBtnShit=view.findViewById(R.id.btnBackbtnShit);
      btnPlayBtnShit=view.findViewById(R.id.btnPlaybtnShit);
      btnNextBtnShit=view.findViewById(R.id.btnNextbtnShit);
      txtSongNameBtnShit=view.findViewById(R.id.txtMusicNameBtnShit);
      txtArtistNameBtnShit=view.findViewById(R.id.txtMusicArtistBtnShit);

        if(!mediaPlayer.isPlaying()){
            btnPlayBtnShit.setIconResource(R.drawable._8px);
        }


        imageViewBtnShit.setImageBitmap(G.convertImageViewToBitmap(imgAlbumeArt));
        txtSongNameBtnShit.setText(txtSongName.getText());
        txtArtistNameBtnShit.setText(txtArtistName.getText());


      seekBarBtnShit.setMax(mediaPlayer.getDuration());
      seekBarBtnShit.setProgress(0);
      seekBarBtnShit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
       if (fromUser){
           mediaPlayer.seekTo(progress);
       }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    });
    new Timer().scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
            seekBarBtnShit.setProgress(mediaPlayer.getCurrentPosition());
        }
    }, 0, 1000);
        btnPlayBtnShit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    btnPlayBtnShit.setIconResource(R.drawable._8px);
                    btnPlay_Pause.setIconResource(R.drawable._8px);
                } else {
                    mediaPlayer.start();
                    btnPlayBtnShit.setIconResource(R.drawable.baseline_pause_24);
                    btnPlay_Pause.setIconResource(R.drawable.baseline_pause_24);
                }



            }

        });


        return view;
    }
}