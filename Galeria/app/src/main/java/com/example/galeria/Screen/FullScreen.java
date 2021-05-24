package com.example.galeria.Screen;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galeria.Adapters.MoveMediaAdapter;
import com.example.galeria.Fragments_and_Popups.gifFragment;
import com.example.galeria.Fragments_and_Popups.imgFragment;
import com.example.galeria.Fragments_and_Popups.videoFragment;
import com.example.galeria.Functions_and_Interfaces.MediaWrapperForBinder;
import com.example.galeria.Functions_and_Interfaces.OnMediaListener;
import com.example.galeria.Media;
import com.example.galeria.R;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FullScreen extends AppCompatActivity implements OnMediaListener {

    ActionBar actionBar;
    View decorView;

    ArrayList<Media> media = new ArrayList<>();
    ArrayList<String> directories = new ArrayList<>();
    private int mediaPathIdx, uiOptions;
    private boolean hiddenBars;
    Dialog dialog;
    int dirColumns, themeUsed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getData();
        switch (themeUsed){
            case 2:
                //setTheme(R.style.GalleryThemeNight);
                break;
            case 3:
                //setTheme(R.style.GalleryThemeCustom);
                break;
            default:
                setTheme(R.style.GalleryThemeDay);
                break;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        getFiles();
        actionBar = getSupportActionBar();
        decorView = getWindow().getDecorView();

        setTransparent();
    }
    @Override
    protected void onResume(){
        super.onResume();
        getData();
        //hideAllBars();
        setView();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu = add items to the action bar
        getMenuInflater().inflate(R.menu.menu_media, menu);
        return true;
    }

    //screen functions
    private void setTransparent(){
        if (actionBar!=null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                             WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
    private void hideAllBars(){
        if (Build.VERSION.SDK_INT > 30) {


        } else {
            actionBar.hide();
            decorView.setSystemUiVisibility(uiOptions);
            hiddenBars = true;
        }
    }
    private void showAllBars(){
        actionBar.show();
        getWindow().clearFlags(uiOptions);

        hiddenBars = false;
    }
    private void setView(){
        setTitle(media.get(mediaPathIdx).getName());
        int mediaType = media.get(mediaPathIdx).getType();

        switch (mediaType){
            case 1:
                //getSupportFragmentManager().beginTransaction().
                setFragment(new imgFragment(this, media, mediaPathIdx, (OnMediaListener) this));
                break;
            case 2:
                setFragment(new videoFragment(this, media, mediaPathIdx, (OnMediaListener) this));
                break;
            default:
                setFragment(new gifFragment(this, media, mediaPathIdx, (OnMediaListener) this));
        }
    }
    private void displayInfo() throws IOException {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_basic_info);
        dialog.setTitle(getResources().getString(R.string.basic_media_info));
        setDialogInfo(dialog);
        dialog.show();
    }


    //menu functions
    public void menuDisplayMediaInfo(MenuItem item) throws IOException {
        displayInfo();
    }
    public void menuRenameMedia(MenuItem item) {
        renameMedia();
    }
    public void menuMoveMedia(MenuItem item) {
        moveMedia();
    }
    public void menuDeleteMedia(MenuItem item) {
        deleteMedia();
    }


    //content
    private void getFiles() {
        media = ((MediaWrapperForBinder) getIntent().getExtras().getBinder("media")).getData();
        mediaPathIdx = getIntent().getIntExtra("choosenMediaIdx",0);
        directories = getIntent().getStringArrayListExtra("directoriesPaths");
    }

    private void setDialogInfo(Dialog dialog) throws IOException {
        BasicFileAttributes info = Files.readAttributes(Paths.get(media.get(mediaPathIdx).getPath()),BasicFileAttributes.class);

        TextView name = dialog.findViewById(R.id.textView_media_name);
        TextView path = dialog.findViewById(R.id.textView_media_path);
        TextView size = dialog.findViewById(R.id.textView_media_size);
        TextView dimensions = dialog.findViewById(R.id.textView_media_dimensions);
        TextView date = dialog.findViewById(R.id.textView_media_date);

        name.setText(media.get(mediaPathIdx).getName());
        path.setText(media.get(mediaPathIdx).getPath());
        String fileSize;
        if (info.size()<512){
            fileSize = info.size() + "B";
        } else {
            if (info.size()/1024<512){
                fileSize = info.size()/1024 + "kB";
            } else {
                if (info.size()/1048576<512){
                    fileSize = info.size()/1048576 + "MB";
                } else {
                    fileSize = info.size()/1073741824 +"GB";
                }
            }
        }
        size.setText(fileSize);
        String modified = String.valueOf(media.get(mediaPathIdx).getDate());
        modified = modified.replace("T"," ");
        modified = modified.replace("Z","");
        date.setText(modified);

        if (media.get(mediaPathIdx).getType()!=2) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(media.get(mediaPathIdx).getPath(), options);
            String dims = options.outWidth + " x " + options.outHeight;
            dimensions.setText(dims);
            dialog.findViewById(R.id.static_text_media_length).setVisibility(View.INVISIBLE);
            TextView length = dialog.findViewById(R.id.textView_media_length);
            length.setVisibility(View.INVISIBLE);
        }

        if (media.get(mediaPathIdx).getType()==2){
            dialog.findViewById(R.id.static_text_media_length).setVisibility(View.VISIBLE);
            TextView length = dialog.findViewById(R.id.textView_media_length);
            length.setVisibility(View.VISIBLE);
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(media.get(mediaPathIdx).getPath());

            int width = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            int height = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            String dims = width + " x " + height;
            dimensions.setText(dims);

            int timeMilisec = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            double timeSec = (double) (timeMilisec/1000);
            String duration = (int) (timeSec/3600) + ":" +
                              (int) ((timeSec%3600)/60) + ":" +
                              (int) (timeSec%60);
            length.setText(duration);
        }
    }
    private void renameMedia(){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_rename);

        dialog.setTitle(getResources().getString(R.string.rename));
        EditText text = dialog.findViewById(R.id.rename);
        String name = media.get(mediaPathIdx).getName();
        text.setText(name.substring(0,name.lastIndexOf(".")));
        dialog.show();
    }
    public void confirm(View view) {
        EditText text = dialog.findViewById(R.id.rename);
        String newName = text.getText().toString();
        String oldName = media.get(mediaPathIdx).getName();
        String oldPath = media.get(mediaPathIdx).getPath();
        String extension = oldName.substring(oldName.lastIndexOf(".")+1);

        if (newName.length()>=3){
            File mediaFile = new File(oldPath);
            mediaFile.renameTo(new File(oldPath.substring(0,oldPath.lastIndexOf("/")+1)+newName+"."+extension));
            media.get(mediaPathIdx).setName(newName);
            setTitle(newName+extension);
        }
        dialog.dismiss();

    }

    private void moveMedia(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_dir_to_choose);
        dialog.getWindow().setLayout(
                (int) (0.75 * Resources.getSystem().getDisplayMetrics().widthPixels),
                (int) (0.75 * Resources.getSystem().getDisplayMetrics().heightPixels));
        dialog.setTitle(getResources().getString(R.string.select_folder));

        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView_popup);
        ArrayList<String> mediaToPass = new ArrayList<>();
        mediaToPass.add(media.get(mediaPathIdx).getPath());
        MoveMediaAdapter dirPopupAdapter = new MoveMediaAdapter(this, directories, mediaToPass);
        recyclerView.setAdapter(dirPopupAdapter);

        recyclerView.setLayoutManager(new GridLayoutManager(this, dirColumns-1));

        dialog.show();
    }
    private void deleteMedia(){
        File file = new File(media.get(mediaPathIdx).getPath());
        file.delete();
        media.remove(mediaPathIdx);
        if (mediaPathIdx==media.size()){
            mediaPathIdx--;
            if (mediaPathIdx==-1) {
                super.onBackPressed();
            }
        }
        setView();
    }

    //helping functions
    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_single_media,fragment);
        fragmentTransaction.commit();
    }

    //data funcitons
    private void getData(){
        String dataFilename = "data";
        SharedPreferences sharedPreferences = getSharedPreferences(dataFilename, MODE_PRIVATE);
        dirColumns = sharedPreferences.getInt("dirColumns",getResources().getInteger(R.integer.dirColumns));
        themeUsed = sharedPreferences.getInt("themeUsed",getResources().getInteger(R.integer.themeUsed));
    }

    @Override
    public void returnIdx(int idx) {
        mediaPathIdx = idx;
        if (mediaPathIdx==media.size()){
            mediaPathIdx = 0;
        }
        if (mediaPathIdx == -1){
            mediaPathIdx = media.size()-1;
        }
        setView();
    }
    public void longClickPerformed(){
        if (hiddenBars){
            showAllBars();
            hiddenBars = false;
        } else {
            hideAllBars();
            hiddenBars = true;
        }
    }

}
