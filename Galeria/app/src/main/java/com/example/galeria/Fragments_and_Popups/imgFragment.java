package com.example.galeria.Fragments_and_Popups;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.galeria.Functions_and_Interfaces.OnMediaListener;
import com.example.galeria.Functions_and_Interfaces.OnSwipeListener;
import com.example.galeria.Media;
import com.example.galeria.R;

import java.util.ArrayList;


public class imgFragment extends Fragment {

    OnMediaListener onMediaListener;

    int mediaPathIdx;
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Media> media;

    public imgFragment(Context context, ArrayList<Media> media, int mediaPathIdx, OnMediaListener onMediaListener) {
        this.mediaPathIdx = mediaPathIdx;
        this.context = context;
        this.media = media;
        this.onMediaListener = onMediaListener;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_img, container, false);
        setImageView(view);
        setImageViewListeners(view);
        return view;
    }

    private void setImageView(View view) {
        ImageView imageView = view.findViewById(R.id.single_imageView);
        imageView.setImageURI(Uri.parse(media.get(mediaPathIdx).getPath()));
    }

    private void setImageViewListeners(View view){

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