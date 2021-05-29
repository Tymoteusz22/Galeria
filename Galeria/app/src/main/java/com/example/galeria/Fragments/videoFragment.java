package com.example.galeria.Fragments;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;

import com.example.galeria.Functions_and_Interfaces.OnMediaListener;
import com.example.galeria.Functions_and_Interfaces.OnSwipeListener;
import com.example.galeria.Media;
import com.example.galeria.R;

public class videoFragment extends Fragment {

    OnMediaListener onMediaListener;

    int mediaPathIdx;
    Context context;
    LayoutInflater layoutInflater;
    Media media;
    VideoView videoView;

    public videoFragment(Context context, Media media, int mediaPathIdx, OnMediaListener onMediaListener) {
        this.mediaPathIdx = mediaPathIdx;
        this.context = context;
        this.media = media;
        this.onMediaListener = onMediaListener;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_video, container, false);

        setVideoView(view);

        setVideoFragmentListeners(view);

        return view;
    }

    private void setVideoView(View view) {
        videoView = view.findViewById(R.id.single_videoView);
        videoView.setVideoURI(Uri.parse(media.getPath()));
        MediaController mediaController = new MediaController(context);

        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo();
            }
        });
    }
    private void playVideo() {
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVolume(0,0);
            }
        });
        videoView.start();
    }

    private void setVideoFragmentListeners(View view){
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