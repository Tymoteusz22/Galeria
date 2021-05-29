package com.example.galeria.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.galeria.Functions_and_Interfaces.OnMediaListener;
import com.example.galeria.Functions_and_Interfaces.OnSwipeListener;
import com.example.galeria.Media;
import com.example.galeria.R;

import java.util.ArrayList;


public class gifFragment extends Fragment {

    OnMediaListener onMediaListener;

    Media media;
    Context context;
    int mediaPathIdx;

    public gifFragment(Context context, Media media, int mediaPathIdx, OnMediaListener onMediaListener) {
        this.context = context;
        this.media = media;
        this.mediaPathIdx = mediaPathIdx;
        this.onMediaListener = onMediaListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gif, container, false);

        setGifView(view);
        setGifFragmentListeners(view);

        return view;
    }

    private void setGifView(View view) {
        ImageView gifView = view.findViewById(R.id.single_gifView);
        Glide.with(context).asGif().load(media.getPath()).into(gifView);
    }
    private void setGifFragmentListeners(View view){
        view.setOnTouchListener(new OnSwipeListener(context){
            @Override
            public void onSwipeLeftRight(){
                onMediaListener.returnIdx(mediaPathIdx - 1);
            }
            @Override
            public void onSwipeRightLeft(){
                onMediaListener.returnIdx(mediaPathIdx + 1);
            }
            @Override
            public void onLongClick(){
                onMediaListener.longClickPerformed();
            }
        });
    }
}