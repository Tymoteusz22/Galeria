package com.example.galeria.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.galeria.Functions_and_Interfaces.ItemClickInterface;
import com.example.galeria.Media;
import com.example.galeria.R;

import java.util.ArrayList;

public class SimilarImageIconAdapter extends RecyclerView.Adapter<SimilarImageIconAdapter.ViewHolder> {

    ArrayList<Media> media;
    Context context;
    ItemClickInterface itemClickInterface;
    int imgColumns;

    public SimilarImageIconAdapter(Context context, ArrayList<Media> media, int imgColumns, ItemClickInterface itemClickInterface){
        this.media = media;
        this.context = context;
        this.imgColumns = imgColumns;
        this.itemClickInterface = itemClickInterface;
    }

    @NonNull
    @Override
    public SimilarImageIconAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.icon_similar_image, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Glide.with(context).asBitmap().load(media.get(position).getPath()).centerCrop().into(holder.imageView);

        //set iconText
        String mediaPath = media.get(position).getPath(); //<-- skracam troche kod
        String dir = mediaPath.substring(0, mediaPath.lastIndexOf("/"));
        int poszukiwanyIdx = dir.lastIndexOf("/");
        String text = mediaPath.substring(poszukiwanyIdx + 1);
        holder.textView.setText(text);

        if (media.get(position).getSelected()){
            holder.box.setVisibility(View.VISIBLE);
        } else {
            holder.box.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return media.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView box;
        TextView textView;

        public ViewHolder(View item){
            super(item);
            imageView = item.findViewById(R.id.imageView_similar_media_icon);
            box = item.findViewById(R.id.imageView_media_icon_checked_box);
            textView = item.findViewById(R.id.textView_similar_media_path);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickInterface.onItemClick(getAdapterPosition());
                }
            });
            item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    itemClickInterface.onLongItemClick(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
