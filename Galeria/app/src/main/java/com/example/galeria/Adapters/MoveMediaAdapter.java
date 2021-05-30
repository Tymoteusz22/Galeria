package com.example.galeria.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.galeria.Functions_and_Interfaces.ItemClickInterface;
import com.example.galeria.Media;
import com.example.galeria.R;

import java.io.File;
import java.util.ArrayList;

public class MoveMediaAdapter extends RecyclerView.Adapter<MoveMediaAdapter.ViewHolder> {

    ArrayList<String> dirs, mediaToPass;
    Context context;
    Dialog dialog;

    public MoveMediaAdapter(Context context, ArrayList<String> dirs, ArrayList<String> mediaToPass, Dialog dialog) {
        this.dirs = dirs;
        this.context = context;
        this.mediaToPass = mediaToPass;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public MoveMediaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        this.context = parent.getContext();
        View view = layoutInflater.inflate(R.layout.dir_in_popup, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoveMediaAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(R.drawable.ic_baseline_folder_64).into(holder.imageView);
        holder.text.setText(dirs.get(position).substring(dirs.get(position).lastIndexOf("/")+1));
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String path : mediaToPass) {
                    File oldFile = new File(path);
                    String newFile = dirs.get(position)+"/"+oldFile.getName();
                    oldFile.renameTo(new File(newFile));
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dirs.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView text;

        public ViewHolder(@NonNull View item) {
            super(item);
            this.imageView = item.findViewById(R.id.imageView_folder_popup_icon);
            this.text = item.findViewById(R.id.textView_folder_popup_name);
        }
    }
}