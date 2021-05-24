package com.example.galeria.Screen;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galeria.Adapters.DirectoryIconAdapter;
import com.example.galeria.Directory;
import com.example.galeria.Functions_and_Interfaces.ItemClickInterface;
import com.example.galeria.R;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;


public class DirectoriesScreen extends AppCompatActivity implements ItemClickInterface {

    static int READ_EXTERNAL_STORAGE = 1;
    static int WRITE_EXTERNAL_STORAGE = 1;
    static int MANAGE_EXTERNAL_STORAGE = 1;

    RecyclerView dirRecyclerView;
    DirectoryIconAdapter directoryIconAdapter;
    MenuItem menuItemRename, menuItemDeleteButton, menuItemSelectAll, menuItemDeselectAll;

    private ArrayList<Directory> directories = new ArrayList<>();
    private ArrayList<Integer> selected = new ArrayList<>();
    private ArrayList<Integer> canWriteIdxs = new ArrayList<>();

    String dataFilename = "data";
    int dirColumns, lastDirSortingMethod, themeUsed;
    boolean deleteAfterEmptying;
    Dialog dialog;


    private final String[] extensions = {".jfif",".jpg",".jpeg",".png",".mp4",".avi",".gif",".tif",".tiff",".bmp",".webm",".flv",".amv",".m4p"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkPermissions();
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
        setContentView(R.layout.recyclerview_dirs_activity);
        dirRecyclerView = findViewById(R.id.recyclerView_folders);
        setTitle(getResources().getString(R.string.all_folders));

    }
    @Override
    protected void onResume() {
        super.onResume();
        getData();
        fulfillDirectories();
        setView();
    }
    @Override
    protected void onPause(){
        super.onPause();
        saveData();
    }

    //'visible' functions
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu = add items to the action bar
        getMenuInflater().inflate(R.menu.menu_dirs, menu);
        menuItemRename = menu.findItem(R.id.menu_rename_dir);
        menuItemDeleteButton = menu.findItem(R.id.menu_dir_delete_button);
        menuItemSelectAll = menu.findItem(R.id.menu_select_all);
        menuItemDeselectAll = menu.findItem(R.id.menu_deselect_all);
        return true;
    }

    private void setView() {
        switch (lastDirSortingMethod) {
            case 1:
                directories.sort(Directory.dirNameCompare);
                break;
            case 4:
                directories.sort(Directory.dirSizeCompare);
                break;
            case 2:
                directories.sort(Directory.dirNameCompare.reversed());
                break;
            default:
                directories.sort(Directory.dirSizeCompare.reversed());
                break;
        }

        directoryIconAdapter = new DirectoryIconAdapter(directories, this);
        dirRecyclerView.setAdapter(directoryIconAdapter);
        dirRecyclerView.setLayoutManager(new GridLayoutManager(this,dirColumns));
    }

    //menu functions
    public void menuSortNameAscFunc(MenuItem item) {
        if (lastDirSortingMethod != 1) {
            lastDirSortingMethod=1;
        }
        setView();
    }
    public void menuSortNameDescFunc(MenuItem item) {
        if (lastDirSortingMethod != 2) {
            lastDirSortingMethod=2;
        }
        setView();
    }
    public void menuSortSizeAscFunc(MenuItem item) {
        if (lastDirSortingMethod != 3) {
            lastDirSortingMethod=3;
        }
        setView();
    }
    public void menuSortSizeDescFunc(MenuItem item) {
        if (lastDirSortingMethod != 4) {
            lastDirSortingMethod=4;
        }
        setView();
    }
    public void menuShowAllFunc(MenuItem item) {
        startAllMediaActivity();
    }
    public void menuLaunchSettingsActivity(MenuItem item) {
        launchSettingsActivity();
    }
    public void menuChangeDirName(MenuItem item) {
        changeDirectoryName();
    }
    public void menuDeleteDirectories(MenuItem item) {
        deleteSelectedDirectories();
    }
    public void selectAll(MenuItem item) {
        addAllToSelected();
    }
    public void deselectAll(MenuItem item) {
        removeAllFromSelected();
    }


    //content functions
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
        }
        if (Build.VERSION.SDK_INT >= 30) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE}, MANAGE_EXTERNAL_STORAGE);
            }
        }
    }

    private void fulfillDirectories() {
        directories.clear();



        getDirectoriesPaths("/storage/emulated/0/Download"); //domyslne miejsce zapisu
        getDirectoriesPaths("/storage/emulated/0/DCIM"); //ss i camera
        getDirectoriesPaths("/storage/emulated/0/Pictures"); //tutaj niektore aplikacje tworza swoje foldery i zapisuja pliki, ale tez to powinno byc miejsce zapisu interesujacych plikow

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File[] pathsToAppSpecificMedia = this.getExternalMediaDirs();
            for (File f:pathsToAppSpecificMedia){
                if (!f.getAbsolutePath().startsWith("/storage/emulated/0")) {
                    String path = f.getAbsolutePath();
                    int idx = path.indexOf("/");
                    String tmp = path.substring(idx + 1);
                    idx += tmp.indexOf("/") + 1;
                    tmp = tmp.substring(idx+1);
                    idx += tmp.indexOf("/") + 1;
                    path = path.substring(0, idx + 1);
                    getDirectoriesPaths(path + "/Download");
                    getDirectoriesPaths(path + "/DCIM");
                    getDirectoriesPaths(path + "/Pictures");
                }
            }
        }
        /*
        directories.sort(Directory.dirNameCompare);
        //TODO check - WRITE PERMISSIONS
        canWriteIdxs.clear();
        for (int i=0; i<directories.size(); i++){
            Log.d("aaaa",directories.get(i).getPath());
            File f = new File(directories.get(i).getPath());
            Log.d("aaaa", directories.size()+" "+String.valueOf(f.canWrite()));
            if (f.canWrite()){
                canWriteIdxs.add(i);
            }
        }
        Log.d("aaaa", directories.size()+" "+String.valueOf(canWriteIdxs));
        */
    }

    private void addAllToSelected(){
        selected.clear();
        for (int i = 0; i<directories.size(); i++){
            directories.get(i).setSelected(true);
            selected.add(i);
            directoryIconAdapter.notifyItemChanged(i);
        }
        menuItemDeleteButton.setVisible(true);
        menuItemSelectAll.setVisible(false);
        menuItemDeselectAll.setVisible(true);
    }
    private void removeAllFromSelected(){
        for (int i = directories.size()-1; i>=0; i--){
            directories.get(i).setSelected(false);
            directoryIconAdapter.notifyItemChanged(i);
        }
        selected.clear();
        menuItemDeleteButton.setVisible(false);
        menuItemDeselectAll.setVisible(false);
        menuItemSelectAll.setVisible(true);
    }

    private void changeDirectoryName() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_rename);

        dialog.setTitle(getResources().getString(R.string.rename));

        EditText text = dialog.findViewById(R.id.rename);
        String dirName = directories.get(selected.get(0)).getName();
        text.setText(dirName);

        dialog.show();
    }

    private void deleteSelectedDirectories() {
        Collections.sort(selected);

        for (int i=selected.size()-1; i>=0; i--){
            deleteDir(new File(directories.get(selected.get(i)).getPath()));
            directories.remove(directories.get(i));
            directoryIconAdapter.notifyItemRemoved(selected.get(i));
            selected.remove(i);
        }
        menuItemDeleteButton.setVisible(false);
        menuItemDeselectAll.setVisible(false);
        if (directories.size()>0) {
            menuItemSelectAll.setVisible(true);
        }
    }
    private void deleteDir(File file) {
        if (file.listFiles() != null) {
            for (File f : file.listFiles()) {
                if (isMedia(f.getName())) {
                    f.delete();
                }
            }
        }
        if (file.listFiles()==null && deleteAfterEmptying){
            file.delete();
        }
    }


    // new activities
    private void startAllMediaActivity() {
        removeAllFromSelected();
        ArrayList<String> paths = new ArrayList<>();
        for (Directory d:directories){
            paths.add(d.getPath());
        }
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("directoriesPaths", paths);
        Intent i = new Intent(this, AllMediaScreen.class);
        i.putExtras(bundle);
        startActivity(i); //rozpocznij nowa aktywnosc (nowy Screen) przekazujac dalej foldery
    }
    private void launchSettingsActivity(){
        removeAllFromSelected();
        startActivity(new Intent(this, SettingsScreen.class));
    }


    //helping functions
    private void getDirectoriesPaths(String currentPath){
        File dirFromPath = new File(currentPath);
        if (containsMedia(dirFromPath)) {
            directories.add(new Directory(Paths.get(currentPath)));
        }
        File[] filesList = dirFromPath.listFiles();
        if (filesList != null) {
            for (File f : filesList) {
                if (f.isDirectory()) {
                    getDirectoriesPaths(f.getAbsolutePath());
                }
            }
        }
    }
    private boolean containsMedia(File currentDir) {
        if (currentDir.getName().startsWith(".") || currentDir.listFiles()==null) {
            return false;
        } //hidden files //empty directory
        for (File f : currentDir.listFiles()) {
            if (f.getName().startsWith(".")){
                return false;
            }
            if (isMedia(f.getName().toLowerCase())) {
                return true;
            }
        }
        return false;
    }
    private boolean isMedia(String name) {
        for (String s:extensions){
            if (name.endsWith(s)){
                return true;
            }
        }
        return false;
    }

    public void confirm(View view) {
        EditText text = dialog.findViewById(R.id.rename);
        String newName = text.getText().toString();

        if (newName.length()>=5) {
            File dir = new File(directories.get(selected.get(0)).getPath());
            directories.get(selected.get(0)).setName(newName);
            String oldPath = directories.get(selected.get(0)).getPath();
            oldPath = oldPath.substring(0,oldPath.lastIndexOf("/")+1);
            dir.renameTo(new File(oldPath+newName));

            directories.get(selected.get(0)).setSelected(false);
            directoryIconAdapter.notifyItemChanged(selected.get(0));
            selected.remove(0);
            menuItemRename.setVisible(false);
            menuItemDeleteButton.setVisible(false);
            menuItemDeselectAll.setVisible(false);
        }
        dialog.dismiss();
    }


    //data functions
    private void getData() {
        SharedPreferences sharedPreferences = getSharedPreferences(dataFilename, MODE_PRIVATE);
        dirColumns = sharedPreferences.getInt("dirColumns",getResources().getInteger(R.integer.dirColumns));
        lastDirSortingMethod = sharedPreferences.getInt("lastDirSortingMethod", getResources().getInteger(R.integer.lastDirSortingMethod));
        deleteAfterEmptying = sharedPreferences.getBoolean("deleteAfterEmptying",getResources().getBoolean(R.bool.deleteAfterEmptying));
        themeUsed = sharedPreferences.getInt("themeUsed",getResources().getInteger(R.integer.themeUsed));
        //sdcardPath = sharedPreferences.getString("sdcardPath", null);
    }
    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(dataFilename, MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("lastDirSortingMethod", lastDirSortingMethod);
        edit.apply();
    }


    //Click methods
    @Override
    public void onItemClick(int position) {
        if (!directories.get(position).getSelected()){
            if (selected.size() == 0) {
                ArrayList<String> allDirectoryPaths = new ArrayList<>();
                for (Directory d : directories) {
                    allDirectoryPaths.add(d.getPath());
                }
                Intent i = new Intent(getApplicationContext(), DirectoryContentScreen.class);
                i.putExtra("directoryPath", directories.get(position).getPath());
                i.putStringArrayListExtra("directories", allDirectoryPaths);
                startActivity(i);
            } //start new activity
            if (selected.size()>1){
                selected.add(position);
                directories.get(position).setSelected(true);
                directoryIconAdapter.notifyItemChanged(position);
            } // was 2, will be more == just show check
            if (selected.size()==1){
                selected.add(position);
                directories.get(position).setSelected(true);
                menuItemRename.setVisible(false);
                directoryIconAdapter.notifyItemChanged(position);
            } // was 1, will be 2 == dont show to rename
            if (selected.size()==directories.size()){
                menuItemSelectAll.setVisible(false);
            }
        } else {
            selected.remove((Integer) position);
            directories.get(position).setSelected(false);
            if (selected.size()==0){
                menuItemDeleteButton.setVisible(false);
                menuItemRename.setVisible(false);
                menuItemDeselectAll.setVisible(false);
            } //was 1 will be 0
            if (selected.size()==1){
                menuItemRename.setVisible(true);
            } //was 2 will be 1
            directoryIconAdapter.notifyItemChanged(position);
        }
    }
    @Override
    public void onLongItemClick(int position) {
        if (selected.size()==0){
            selected.add(position);
            directories.get(position).setSelected(true);

            menuItemRename.setVisible(true);
            menuItemDeleteButton.setVisible(true);
            directoryIconAdapter.notifyItemChanged(position);
            menuItemDeselectAll.setVisible(true);
        }
    }

}