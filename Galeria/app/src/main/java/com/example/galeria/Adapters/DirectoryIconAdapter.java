package com.example.galeria.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galeria.Directory;
import com.example.galeria.Functions_and_Interfaces.ItemClickInterface;
import com.example.galeria.R;

import java.util.ArrayList;

public class DirectoryIconAdapter extends RecyclerView.Adapter<DirectoryIconAdapter.ViewHolder> {

    private final ArrayList<Directory> dirs;
    private final ItemClickInterface itemClickInterface;

    public DirectoryIconAdapter(ArrayList<Directory> dirs, ItemClickInterface itemClickInterface){
        this.dirs = dirs;
        this.itemClickInterface = itemClickInterface;
    }

    @NonNull
    @Override
    public DirectoryIconAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.icon_dir, parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.imageView.setImageResource(R.drawable.ic_baseline_folder_64);
        holder.textView.setText(dirs.get(position).getName());
        holder.dirSize.setText(String.valueOf(dirs.get(position).getSize()));
        if (dirs.get(position).getSelected()){
            holder.box.setVisibility(View.VISIBLE);
        } else {
            holder.box.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return dirs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView, dirSize;
        ImageView box;

        public ViewHolder(View item){
            super(item);
            imageView = item.findViewById(R.id.imageView_folder_icon);
            textView = item.findViewById(R.id.textView_folder_name);
            dirSize = item.findViewById(R.id.textView_folder_size);
            box = item.findViewById(R.id.imageView_folder_selected);
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
