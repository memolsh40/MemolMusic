package com.memol.musicplayer.Fragments;


import static com.memol.musicplayer.Main.MainActivity.MainHandler;
import static com.memol.musicplayer.Main.MainActivity.songs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.memol.musicplayer.Adabters.RecyclerAdapter;
import com.memol.musicplayer.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SongsFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongsFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SongsFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SongsFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static SongsFrag newInstance(String param1, String param2) {
        SongsFrag fragment = new SongsFrag();
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
   public static RecyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_songs, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvSongs);
        recyclerView.setHasFixedSize(true);

                if (!(songs.size()<0)){
                    adapter=new RecyclerAdapter(getContext(),songs,MainHandler);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter.notifyDataSetChanged();

                }


        return view;
    }
}