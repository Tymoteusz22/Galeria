package com.example.galeria.Functions_and_Interfaces;

import android.os.Binder;

import com.example.galeria.Media;

import java.util.ArrayList;

public class MediaWrapperForBinder extends Binder {

    private final ArrayList<Media> mData;

    public MediaWrapperForBinder(ArrayList<Media> media) {
        mData = media;
    }

    public ArrayList<Media> getData(){
        return mData;
    }
}
