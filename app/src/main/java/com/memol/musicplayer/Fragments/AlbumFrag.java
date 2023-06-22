package com.memol.musicplayer.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.memol.musicplayer.Adabters.AlbumAdabter;
import com.memol.musicplayer.G;
import com.memol.musicplayer.Main.MainActivity;
import com.memol.musicplayer.Model.Song;
import com.memol.musicplayer.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlbumFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AlbumFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlbumFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static AlbumFrag newInstance(String param1, String param2) {
        AlbumFrag fragment = new AlbumFrag();
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
    RecyclerView recyclerView;
    public static  AlbumAdabter albumAdabter;
    ArrayList<Song> albumFragList=new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view =inflater.inflate(R.layout.fragment_album, container, false);
         recyclerView=view.findViewById(R.id.albumRv);
         recyclerView.setHasFixedSize(true);
         albumFragList= G.albumsList;

        if (!(albumFragList.size()<0)){
            albumAdabter=new AlbumAdabter(albumFragList,getContext(),R.layout.album_list);
            recyclerView.setAdapter(albumAdabter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            albumAdabter.notifyDataSetChanged();

        }
         return view;
    }
}