package com.example.galeria.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.CancellationSignal;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.galeria.Functions_and_Interfaces.ItemClickInterface;
import com.example.galeria.Media;
import com.example.galeria.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MediaIconAdapter extends RecyclerView.Adapter<MediaIconAdapter.ViewHolder> {

    ArrayList<Media> media;
    Context context;
    ItemClickInterface itemClickInterface;
    int imgColumns;

    public MediaIconAdapter(ArrayList<Media> media, int imgColumns, ItemClickInterface itemClickInterface){
        this.media = media;
        this.imgColumns = imgColumns;
        this.itemClickInterface = itemClickInterface;
    }

    @NonNull
    @Override
    public MediaIconAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        this.context = parent.getContext();
        View view = layoutInflater.inflate(R.layout.icon_media, parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //
        Glide.with(context).asBitmap().load(media.get(position).getPath()).centerCrop().into(holder.imageView);
        if (media.get(position).getSelected()){
            holder.box.setVisibility(View.VISIBLE);
        } else {
            holder.box.setVisibility(View.INVISIBLE);
        }
        //
        //version myMaxEffi
        /*
        double maxIconWidth = (double) Resources.getSystem().getDisplayMetrics().widthPixels/imgColumns;
        switch(media.get(position).getType()) {
            case 1:

                //dont get unnecessary info
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(media.get(position).getPath(),options);
                int imgWidth = options.outWidth; //file width <- important

                //inSampleSize = ile razy zmniejszyc szerokosc i wysokosc -> width = width / inSampleSize
                options.inSampleSize = ((int) (imgWidth/maxIconWidth)); //+1 dla dodatkowego zysku, choć prawie niezauważalny
                options.inJustDecodeBounds = false;

                Bitmap img = BitmapFactory.decodeFile(media.get(position).getPath(),options);
                holder.imageView.setImageBitmap(img);
                if (media.get(position).getSelected()) {
                    holder.box.setVisibility(View.VISIBLE);
                }
                holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                break;
            case 2:

                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(media.get(position).getPath());
                int width = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                int height = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                retriever.release();
                double scale = (double) (width/maxIconWidth);
                try {
                    Bitmap video = ThumbnailUtils.createVideoThumbnail(
                            new File(media.get(position).getPath()),
                            new Size((int) maxIconWidth,(int) (height/scale)),
                            new CancellationSignal()
                    );
                    holder.imageView.setImageBitmap(video);
                    if (media.get(position).getSelected()) {
                        holder.box.setVisibility(View.VISIBLE);
                    }
                    holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                Log.d("TUTAJ","Tutaj byłby gifImageView");
                break;
        }
        if (media.get(position).getSelected()){
            holder.box.setVisibility(View.VISIBLE);
        } else {
            holder.box.setVisibility(View.INVISIBLE);
        }
        */
        //version myFirstTry
        /*
        double maxIconWidth = (double) Resources.getSystem().getDisplayMetrics().widthPixels/imgColumns;
        switch(media.get(position).getType()) {
            case 1:
                Bitmap img = BitmapFactory.decodeFile(media.get(position).getPath());
                double scale = (double) (img.getWidth()/maxIconWidth);
                img = Bitmap.createScaledBitmap(img, (int) (maxIconWidth), (int) (img.getHeight()/scale),false);

                holder.imageView.setImageBitmap(img);
                if (media.get(position).getSelected()) {
                    holder.box.setVisibility(View.VISIBLE);
                }
                holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                break;
            case 2:
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(media.get(position).getPath());
                int width = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                int height = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                retriever.release();
                scale = (double) (width/maxIconWidth);
                try {
                    Bitmap video = ThumbnailUtils.createVideoThumbnail(
                            new File(media.get(position).getPath()),
                            new Size((int) maxIconWidth,(int) (height/scale)),
                            new CancellationSignal()
                    );
                    holder.imageView.setImageBitmap(video);
                    if (media.get(position).getSelected()) {
                        holder.box.setVisibility(View.VISIBLE);
                    }
                    holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                Log.d("TUTAJ","Tutaj będzie gifImageView");
                break;
        }
        if (media.get(position).getSelected()){
            holder.box.setVisibility(View.VISIBLE);
        } else {
            holder.box.setVisibility(View.INVISIBLE);
        }
        */
    }

    @Override
    public int getItemCount() {
        return media.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        ImageView box;

        public ViewHolder(@NonNull View item){
            super(item);
            this.imageView = item.findViewById(R.id.imageView_media_icon);
            this.box = item.findViewById(R.id.imageView_media_icon_checked_box);
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
