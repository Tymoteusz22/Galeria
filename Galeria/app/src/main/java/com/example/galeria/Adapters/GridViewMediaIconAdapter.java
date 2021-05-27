package com.example.galeria.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.CancellationSignal;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.galeria.Functions_and_Interfaces.ItemClickInterface;
import com.example.galeria.Media;
import com.example.galeria.R;
import com.example.galeria.Screen.AllMediaScreen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GridViewMediaIconAdapter extends BaseAdapter {

    ArrayList<Media> allMedia;
    int imgColumns;
    ItemClickInterface itemClickInterface;

    public GridViewMediaIconAdapter(ArrayList<Media> allMedia, int imgColumns, ItemClickInterface itemClickInterface) {
        this.allMedia = allMedia;
        this.imgColumns = imgColumns;
        this.itemClickInterface = itemClickInterface;
    }

    @Override
    public int getCount() {
        return allMedia.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.icon_media,parent,false);
        } else {
            view = convertView;
        }

        ImageView imageView = view.findViewById(R.id.imageView_media_icon);
        ImageView box = view.findViewById(R.id.imageView_media_icon_checked_box);

        if (allMedia.get(position).getType()!=2) {

            Bitmap img = BitmapFactory.decodeFile(allMedia.get(position).getPath());

            imageView.setImageBitmap(img);
            if (allMedia.get(position).getSelected()) {
                box.setVisibility(View.VISIBLE);
            } else {
                box.setVisibility(View.INVISIBLE);
            }
        } else {
            File f = new File(allMedia.get(position).getPath());
            try {
                Bitmap img = ThumbnailUtils.createVideoThumbnail(f, new Size(200,200), new CancellationSignal());
                imageView.setImageBitmap(img);
                if (allMedia.get(position).getSelected()) {
                    box.setVisibility(View.VISIBLE);
                } else {
                    box.setVisibility(View.INVISIBLE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return view;
    }
}
