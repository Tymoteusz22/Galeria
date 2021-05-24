package com.example.galeria.Screen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galeria.Functions_and_Interfaces.ItemClickInterface;
import com.example.galeria.Functions_and_Interfaces.MediaWrapperForBinder;
import com.example.galeria.Media;
import com.example.galeria.R;
import com.example.galeria.Adapters.MediaIconAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class AllMediaScreen extends AppCompatActivity implements ItemClickInterface{

    RecyclerView allRecyclerView;
    MediaIconAdapter mediaIconAdapter;
    MenuItem menuItemDeleteButton, menuItemSelectAll, menuItemDeselectAll;

    ArrayList<Media> allMedia = new ArrayList<>();
    ArrayList<Integer> selected = new ArrayList<>();
    private final String[] extensions = {".jfif",".jpg",".jpeg",".png",".tif",".tiff",".bmp",".webm",".flv",".gif",".amv",".mp4",".m4p",".avi"};

    String dataFilename = "data";
    int imgColumns, matchingPercentage, lastMediaSortingMethod, comparingPercentage, themeUsed;
    boolean perfectMatch;

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
        setContentView(R.layout.recyclerview_all_activity);
        allRecyclerView = findViewById(R.id.recyclerView_all_images);

        setTitle(getResources().getString(R.string.all_images));


    }
    @Override
    protected void onResume() {
        super.onResume();
        getData();
        try {
            setContent();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_all_media),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        setView();
    }
    @Override
    protected void onPause(){
        super.onPause();
        saveData();
    }

    //screen functions
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu = add items to the action bar
        getMenuInflater().inflate(R.menu.menu_all_media, menu);
        menuItemDeleteButton = menu.findItem(R.id.menu_allMedia_delete_button);
        menuItemSelectAll = menu.findItem(R.id.menu_select_all);
        menuItemDeselectAll = menu.findItem(R.id.menu_deselect_all);
        return true;
    }
    private void setView() {
        switch (lastMediaSortingMethod) {
            case 5:
                allMedia.sort(Media.mediaDateCompare);
                break;
            case 1:
                allMedia.sort(Media.mediaNameCompare);
                break;
            case 3:
                allMedia.sort(Media.mediaSizeCompare.reversed());
                break;
            case 6:
                allMedia.sort(Media.mediaDateCompare.reversed());
                break;
            case 2:
                allMedia.sort(Media.mediaNameCompare.reversed());
                break;
            default:
                allMedia.sort(Media.mediaSizeCompare);
                break;
        }

        mediaIconAdapter = new MediaIconAdapter(allMedia, imgColumns, this);
        allRecyclerView.setAdapter(mediaIconAdapter);
        allRecyclerView.setLayoutManager(new GridLayoutManager(this, imgColumns));
    }


    //menu functions
    public void menuSortNameAscFunc(MenuItem item) {
        if (lastMediaSortingMethod != 1) {
            lastMediaSortingMethod = 1;
        }
        setView();
    }
    public void menuSortNameDescFunc(MenuItem item) {
        if (lastMediaSortingMethod != 2) {
            lastMediaSortingMethod = 2;
        }
        setView();
    }
    public void menuSortSizeAscFunc(MenuItem item) {
        if (lastMediaSortingMethod != 3) {
            lastMediaSortingMethod = 3;
        }
        setView();
    }
    public void menuSortSizeDescFunc(MenuItem item) {
        if (lastMediaSortingMethod != 4) {
            lastMediaSortingMethod = 4;
        }
        setView();
    }
    public void menuSortDateAscFunc(MenuItem item) {
        if (lastMediaSortingMethod != 5) {
            lastMediaSortingMethod = 5;
        }
        setView();
    }
    public void menuSortDateDescFunc(MenuItem item) {
        if (lastMediaSortingMethod != 6) {
            lastMediaSortingMethod = 6;
        }
        setView();
    }

    public void menuShowDirsFunc(MenuItem item) {
        super.onBackPressed();
    }
    public void menuLaunchSettingsActivity(MenuItem item) {
        launchSettingsActivity();
    }

    public void menuStartComparing(MenuItem item) {
        compareAllMedia();
    }

    public void selectAll(MenuItem item) {
        addAllToSelected();
    }
    public void menuDeselectAll(MenuItem item) {
        removeAllFromSelected();
    }

    public void menuDeleteMedia(MenuItem item) {
        deleteMediaFiles();
    }

    //content functions
    private void setContent() throws IOException {
        allMedia.clear();
        ArrayList<String> dirPaths = getIntent().getStringArrayListExtra("directoriesPaths");
        for (String dirPath : dirPaths){
            File directory = new File(dirPath);
            if (directory.listFiles()!=null){
                for (File f : directory.listFiles()) {
                    if (isMedia(f.getName())) {
                        allMedia.add(new Media(f.getAbsolutePath()));
                    }
                }
            }
        }
    }
    private boolean isMedia(String name) {
        for (String s:extensions){
            if (name.endsWith(s)){
                return true;
            }
        }
        return false;
    }

    private void addAllToSelected(){
        selected.clear();
        for (int i = 0; i<allMedia.size(); i++){
            allMedia.get(i).setSelected(true);
            selected.add(i);
            mediaIconAdapter.notifyItemChanged(i);
        }
        menuItemDeleteButton.setVisible(true);
        menuItemSelectAll.setVisible(false);
        menuItemDeselectAll.setVisible(true);
    }
    private void removeAllFromSelected(){
        for (int i = allMedia.size()-1; i>=0; i--){
            allMedia.get(i).setSelected(false);
            mediaIconAdapter.notifyItemChanged(i);
        }
        selected.clear();
        menuItemDeleteButton.setVisible(false);
        menuItemDeselectAll.setVisible(false);
        menuItemSelectAll.setVisible(true);
    }

    private void deleteMediaFiles() {
        Collections.sort(selected);

        for (int i=selected.size()-1; i>=0; i--){
            //selected.get(i) == position
            File file = new File(allMedia.get(selected.get(i)).getPath());
            file.delete();
            allMedia.remove(allMedia.get(selected.get(i)));
            mediaIconAdapter.notifyItemRemoved(selected.get(i));
            selected.remove(i);
        }
        if (allMedia.size()>0) {
            menuItemSelectAll.setVisible(true);
        }
        menuItemDeselectAll.setVisible(false);
        menuItemDeleteButton.setVisible(false);


    }

    //activity functions
    private void launchSettingsActivity(){
        removeAllFromSelected();
        startActivity(new Intent(this, SettingsScreen.class));
    }


    //helping functions
    private void compareAllMedia() {
        ArrayList<Media> similarImages = new ArrayList<>();
        ArrayList<Media> copiedMedia = new ArrayList<>(allMedia);

        for (int i = copiedMedia.size() - 1; i >= 1; i--){
            if (copiedMedia.get(i).getType()==1){
                boolean firstTry = true;
                for (int j = i - 1; j >= 0; j--) {
                    if (copiedMedia.get(j).getType()==1) {
                        if (similar(copiedMedia.get(i),copiedMedia.get(j))){
                            if (firstTry){
                                similarImages.add(copiedMedia.get(i));
                                copiedMedia.remove(i);
                                firstTry=false;
                                i--;
                            }
                            similarImages.add(copiedMedia.get(j));
                            copiedMedia.remove(j);
                            i--;
                        }
                    } else {
                        copiedMedia.remove(j);
                        j--;
                        i--;
                    }
                }
            } else {
                copiedMedia.remove(i);
            }
        }

        if (similarImages.size() > 0) {
            removeAllFromSelected();
            Bundle bundle = new Bundle();
            bundle.putBinder("similarImages", new MediaWrapperForBinder(similarImages));
            Intent i = new Intent(this, SimilarImagesScreen.class);
            i.putExtras(bundle);
            startActivity(i);
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_similar_images), Toast.LENGTH_SHORT).show();
        }
    }
    private boolean similar(Media media1, Media media2) {
        if (perfectMatch){
            Bitmap img1 = BitmapFactory.decodeFile(media1.getPath());
            Bitmap img2 = BitmapFactory.decodeFile(media2.getPath());
            return img1.sameAs(img2);
        }
        else {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(media1.getPath(),options);
            int width1 = options.outWidth;
            int height1 = options.outHeight;

            options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(media1.getPath(),options);
            int width2 = options.outWidth;
            int height2 = options.outHeight;

            options.outWidth = Math.max(width1, width2);
            options.outHeight = Math.max(height1, height2);

            options.inJustDecodeBounds=false;
            Bitmap img1 = BitmapFactory.decodeFile(media1.getPath(),options);
            Bitmap img2 = BitmapFactory.decodeFile(media2.getPath(),options);

            Random random = new Random();
            int matching=0;
            int overall = (int) (options.outWidth*options.outHeight*comparingPercentage/100);
            for (int i=0;i<overall;i++){
                int x = random.nextInt(options.outWidth - 1);
                int y = random.nextInt(options.outHeight - 1);
                if (img1.getPixel(x, y) == img2.getPixel(x, y)) {
                    matching = matching + 1;
                }
            }
            if ((double) (matching/overall)>(double)(matchingPercentage/100)){
                return true;
            }
        }
        return false;
    } //TODO patrz DirContentScreen


    //data functions
    private void getData(){
        SharedPreferences sharedPreferences = getSharedPreferences(dataFilename, MODE_PRIVATE);
        imgColumns = sharedPreferences.getInt("imgColumns", getResources().getInteger(R.integer.imgColumns));
        lastMediaSortingMethod = sharedPreferences.getInt("lastMediaSortingMethod", getResources().getInteger(R.integer.lastImgSortingMethod));
        perfectMatch = sharedPreferences.getBoolean("perfectMatch",getResources().getBoolean(R.bool.perfectMatch));
        matchingPercentage = sharedPreferences.getInt("matchingPercentage",getResources().getInteger(R.integer.matchingPercentage));
        comparingPercentage = sharedPreferences.getInt("comparingPercentage", getResources().getInteger(R.integer.comparingPercentage));
        themeUsed = sharedPreferences.getInt("themeUsed", getResources().getInteger(R.integer.themeUsed));
    }
    private void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(dataFilename, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("lastMediaSortingMethod", lastMediaSortingMethod);
        editor.apply();
    }


    //click methods
    @Override
    public void onItemClick(int position) {
        if (!allMedia.get(position).getSelected()){
            if (selected.size()==0){
                removeAllFromSelected();
                final Bundle bundle = new Bundle();
                bundle.putBinder("media", new MediaWrapperForBinder(allMedia));

                Intent i = new Intent(this, FullScreen.class);
                i.putExtras(bundle);
                i.putExtra("choosenMediaIdx", position);

                startActivity(i);
            }
            if (selected.size()>0) {
                selected.add(position);
                allMedia.get(position).setSelected(true);
                mediaIconAdapter.notifyItemChanged(position);
            }
            if (selected.size()==allMedia.size()){
                menuItemSelectAll.setVisible(false);
            }
        } else {
            selected.remove((Integer) (position));
            allMedia.get(position).setSelected(false);
            if (selected.size()==0){
                menuItemDeleteButton.setVisible(false);
                menuItemDeselectAll.setVisible(false);
            }if (selected.size()<allMedia.size()){
                menuItemSelectAll.setVisible(true);
            }
            mediaIconAdapter.notifyItemChanged(position);
        }

    }
    @Override
    public void onLongItemClick(int position) {
        if (selected.size()==0){
            selected.add(position);
            allMedia.get(position).setSelected(true);

            menuItemDeleteButton.setVisible(true); //enable deleting
            mediaIconAdapter.notifyItemChanged(position);
            menuItemDeselectAll.setVisible(true);
        }
    }
}
