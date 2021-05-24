package com.example.galeria.Screen;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
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

import com.example.galeria.Adapters.MediaIconAdapter;
import com.example.galeria.Adapters.MoveMediaAdapter;
import com.example.galeria.Functions_and_Interfaces.ItemClickInterface;
import com.example.galeria.Functions_and_Interfaces.MediaWrapperForBinder;
import com.example.galeria.Media;
import com.example.galeria.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class DirectoryContentScreen extends AppCompatActivity implements ItemClickInterface {

    RecyclerView contentRecyclerView;
    MediaIconAdapter mediaIconAdapter;
    MenuItem menuItemMove, menuItemDeleteButton, menuItemSelectAll, menuItemDeselectAll;

    ArrayList<Media> media = new ArrayList<>();
    ArrayList<Integer> selected = new ArrayList<>();
    private final String[] extensions = {".jfif",".jpg",".jpeg",".png",".tif",".tiff",".bmp",".webm",".flv",".gif",".amv",".mp4",".m4p",".avi"};
    ArrayList<String> directories = new ArrayList<>();
    ArrayList<String> mediaToPass = new ArrayList<>();
    private String directoryPath;

    String dataFilename = "data";
    int imgColumns, dirColumns, lastMediaSortingMethod, matchingPercentage, comparingPercentage, themeUsed;
    boolean deleteAfterEmptying, perfectMatch;

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
        setContentView(R.layout.recyclerview_content_activity);
        contentRecyclerView = findViewById(R.id.recyclerView_folder_content);

        directoryPath = getIntent().getStringExtra("directoryPath");
        directories = getIntent().getStringArrayListExtra("directories");

        setTitle(directoryPath.substring(directoryPath.lastIndexOf("/")+1));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    protected void onResume() {
        super.onResume();
        getData();
        try {
            setContent();
        } catch (IOException e) {
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
        getMenuInflater().inflate(R.menu.menu_dir_content, menu);
        menuItemMove = menu.findItem(R.id.menu_move_selected_media);
        menuItemDeleteButton = menu.findItem(R.id.menu_dirContent_delete_button);
        menuItemSelectAll = menu.findItem(R.id.menu_select_all);
        menuItemDeselectAll = menu.findItem(R.id.menu_deselect_all);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem menuItem){
        super.onOptionsItemSelected(menuItem);
        if (menuItem.getItemId()==android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(menuItem);
    } //back button

    private void setView() {
        switch (lastMediaSortingMethod) {
            case 5:
                media.sort(Media.mediaDateCompare);
                break;
            case 1:
                media.sort(Media.mediaNameCompare);
                break;
            case 3:
                media.sort(Media.mediaSizeCompare.reversed());
                break;
            case 6:
                media.sort(Media.mediaDateCompare.reversed());
                break;
            case 2:
                media.sort(Media.mediaNameCompare.reversed());
                break;
            default:
                media.sort(Media.mediaSizeCompare);
                break;
        }

        mediaIconAdapter = new MediaIconAdapter(media, imgColumns, this);
        contentRecyclerView.setAdapter(mediaIconAdapter);
        contentRecyclerView.setLayoutManager(new GridLayoutManager(this, imgColumns));
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
    public void menuStartComparing(MenuItem item) {
        compareAllImages();
    }
    public void menuLaunchSettingsActivity(MenuItem item) {
        launchSettingsActivity();
    }
    public void selectAll(MenuItem item) {
        addAllToSelected();
    }
    public void deselectAll(MenuItem item) {
        removeAllFromSelected();
    }
    public void menuDeleteMedia(MenuItem item) {
        deleteMediaFiles();
    }
    public void menuMoveMedia(MenuItem item){
        moveScreen();
    }


    //content functions
    public void setContent() throws IOException {
        media.clear();
        File directory = new File(directoryPath);
        for (File f:directory.listFiles()){
            if (isMedia(f)){
                media.add(new Media(f.getAbsolutePath()));
            }
        }
    }
    private boolean isMedia(File f) {
        for (String s:extensions){
            if (f.getName().toLowerCase().endsWith(s)){
                return true;
            }
        }
        return false;
    }

    private void addAllToSelected(){
        selected.clear();
        for (int i = 0; i<media.size(); i++){
            media.get(i).setSelected(true);
            selected.add(i);
            mediaIconAdapter.notifyItemChanged(i);
        }
        menuItemDeleteButton.setVisible(true);
        menuItemSelectAll.setVisible(false);
        menuItemDeselectAll.setVisible(true);
        menuItemMove.setVisible(true);
    }
    private void removeAllFromSelected(){
        for (int i = media.size()-1; i>=0; i--){
            media.get(i).setSelected(false);
            mediaIconAdapter.notifyItemChanged(i);
        }
        selected.clear();
        menuItemDeleteButton.setVisible(false);
        menuItemDeselectAll.setVisible(false);
        menuItemSelectAll.setVisible(true);
    }

    private void deleteMediaFiles() {
        Collections.sort(selected);
        for (int i=selected.size()-1;i>=0;i--) {
            //selected.get(i) = position
            File file = new File(media.get(selected.get(i)).getPath());
            if (file.delete()) {
                if (media.size() == 0) {
                    if (deleteAfterEmptying) {
                        file.getParentFile().delete();
                    }
                    super.onBackPressed();
                }
            }
            media.remove(media.get(selected.get(i)));
            mediaIconAdapter.notifyItemRemoved(selected.get(i));
            selected.remove(i);
        }
        menuItemDeleteButton.setVisible(false);
        menuItemDeselectAll.setVisible(false);
        if (media.size()>0){
            menuItemSelectAll.setVisible(true);
        }
    }

    private void fillMediaToPass(){
        for (Integer pos : selected){
            mediaToPass.add(media.get(pos).getPath());
        }
    }


    //helping functions
    private void compareAllImages() {
        ArrayList<Media> similarImages = new ArrayList<>();
        ArrayList<Media> copiedMedia = new ArrayList<>(media);

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
            int width, height;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(media1.getPath(),options);
            width = options.outWidth;
            height = options.outHeight;
            BitmapFactory.decodeFile(media2.getPath(),options);
            if (width != options.outWidth || height != options.outHeight){
                return false;
            }
            Bitmap img1 = BitmapFactory.decodeFile(media1.getPath());
            Bitmap img2 = BitmapFactory.decodeFile(media2.getPath());
            return img1.sameAs(img2);
        } else {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(media1.getPath(),options);
            int width = options.outWidth;
            int height = options.outHeight;

            options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(media1.getPath(),options);
            width = Math.max(width, options.outWidth);
            height = Math.max(height, options.outHeight);

            options.inJustDecodeBounds=false;
            Bitmap img1 = BitmapFactory.decodeFile(media1.getPath());
            img1 = Bitmap.createScaledBitmap(img1,width,height,false);
            Bitmap img2 = BitmapFactory.decodeFile(media2.getPath());
            img2 = Bitmap.createScaledBitmap(img2,width,height,false);

            Log.d("aaaa",img1.getWidth()+"<-1  width  2->"+img2.getWidth());
            Log.d("aaaa",img1.getHeight()+"<-1  height  2->"+img2.getHeight());
            Log.d("aaaa", String.valueOf(img1.sameAs(img2)));

            Random random = new Random();
            int matching=0;
            int overall = (int) (width*height*comparingPercentage/100);
            for (int i=0;i<overall;i++){
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                if (img1.getPixel(x, y) == img2.getPixel(x, y)) {
                    matching = matching + 1;
                }
            }
            if ((double) (matching/overall)>=(double)(matchingPercentage/100)){
                return true;
            }
        }
        return false;
    } //TODO effectiveness

    //new activity
    private void launchSettingsActivity(){
        removeAllFromSelected();
        startActivity(new Intent(this, SettingsScreen.class));
    }

    private void moveScreen(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_dir_to_choose);
        dialog.getWindow().setLayout(
                (int) (0.75 * Resources.getSystem().getDisplayMetrics().widthPixels),
                (int) (0.75 * Resources.getSystem().getDisplayMetrics().heightPixels));
        dialog.setTitle(getResources().getString(R.string.select_folder));

        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView_popup);
        fillMediaToPass();
        MoveMediaAdapter dirPopupAdapter = new MoveMediaAdapter(this, directories, mediaToPass);
        recyclerView.setAdapter(dirPopupAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, dirColumns));

        dialog.show();
    }


    //data functions
    private void getData() {
        SharedPreferences sharedPreferences = getSharedPreferences(dataFilename, MODE_PRIVATE);

        imgColumns = sharedPreferences.getInt("imgColumns", getResources().getInteger(R.integer.imgColumns));
        dirColumns = sharedPreferences.getInt("dirColumns", getResources().getInteger(R.integer.dirColumns));
        matchingPercentage = sharedPreferences.getInt("matchingPercentage",getResources().getInteger(R.integer.matchingPercentage));
        comparingPercentage = sharedPreferences.getInt("comparingPercentage", getResources().getInteger(R.integer.comparingPercentage));
        lastMediaSortingMethod = sharedPreferences.getInt("lastImgSortingMethod", getResources().getInteger(R.integer.lastImgSortingMethod));
        themeUsed = sharedPreferences.getInt("themeUsed", getResources().getInteger(R.integer.themeUsed));

        deleteAfterEmptying = sharedPreferences.getBoolean("deleteAfterEmptying",getResources().getBoolean(R.bool.deleteAfterEmptying));
        perfectMatch = sharedPreferences.getBoolean("perfectMatch",getResources().getBoolean(R.bool.perfectMatch));
    }
    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(dataFilename, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("lastImgSortingMethod", lastMediaSortingMethod);
        editor.apply();
    }


    // click methods
    @Override
    public void onItemClick(int position) {
        if (!media.get(position).getSelected()){
            if (selected.size()==0){
                final Bundle bundle = new Bundle();
                bundle.putBinder("media", new MediaWrapperForBinder(media));

                Intent i = new Intent(this, FullScreen.class);
                i.putExtras(bundle);
                i.putExtra("directoriesPaths", directories);
                i.putExtra("choosenMediaIdx", position);

                startActivity(i);
            }
            if (selected.size()>0) {
                selected.add(position);
                media.get(position).setSelected(true);
                mediaIconAdapter.notifyItemChanged(position);
            }
            if (selected.size()==media.size()){
                menuItemSelectAll.setVisible(false);
            }
        } else {
            selected.remove((Integer) (position));
            media.get(position).setSelected(false);
            if (selected.size()==0){
                menuItemMove.setVisible(false);
                menuItemDeleteButton.setVisible(false);
                menuItemDeselectAll.setVisible(false);
            }
            mediaIconAdapter.notifyItemChanged(position);
        }

    }
    @Override
    public void onLongItemClick(int position) {
        if (selected.size()==0){
            selected.add(position);
            media.get(position).setSelected(true);

            menuItemMove.setVisible(true); //enable moving
            menuItemDeleteButton.setVisible(true); //enable deleting
            mediaIconAdapter.notifyItemChanged(position);
            menuItemDeselectAll.setVisible(true);
        }
    }
}
