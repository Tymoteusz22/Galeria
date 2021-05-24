package com.example.galeria.Screen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galeria.Functions_and_Interfaces.ItemClickInterface;
import com.example.galeria.Functions_and_Interfaces.MediaWrapperForBinder;
import com.example.galeria.Media;
import com.example.galeria.R;
import com.example.galeria.Adapters.SimilarImageIconAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;


public class SimilarImagesScreen extends AppCompatActivity implements ItemClickInterface {

    MenuItem menuItemDeleteButton, menuItemDeselectAll;

    ArrayList<Media> media = new ArrayList<>();
    ArrayList<Integer> selected = new ArrayList<>();
    SimilarImageIconAdapter similarImageIconAdapter;
    RecyclerView recyclerView;

    String dataFilename = "data";
    int imgColumns, themeUsed;

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

        media = ((MediaWrapperForBinder) getIntent().getExtras().getBinder("similarImages")).getData(); // za kazdym razem sprawdz od nowa

        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_similar_activity);
        recyclerView = findViewById(R.id.recyclerView_similar_images);
    }
    @Override
    protected void onResume(){
        super.onResume();
        setView();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu = add items to the action bar
        getMenuInflater().inflate(R.menu.menu_similar_media, menu);
        menuItemDeleteButton = menu.findItem(R.id.menu_similarMedia_delete_button);
        menuItemDeselectAll = menu.findItem(R.id.menu_deselect_all);
        return true;
    }
    private void setView() {
        setTitle(getResources().getString(R.string.similar));

        similarImageIconAdapter = new SimilarImageIconAdapter(this, media,imgColumns+1, (ItemClickInterface) (this));
        recyclerView.setAdapter(similarImageIconAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,imgColumns+1));
    }


    //menu functions
    public void menuDeselectAll(MenuItem item) {
        removeAllFromSelected();
    }
    public void menuDeleteMedia(MenuItem item) {
        deleteMedia();
    }


    //content functions
    private void removeAllFromSelected(){
        for (int i = media.size()-1; i>=0; i--){
            media.get(i).setSelected(false);
            similarImageIconAdapter.notifyItemChanged(i);
        }
        selected.clear();
        menuItemDeleteButton.setVisible(false);
        menuItemDeselectAll.setVisible(false);
    }
    private void deleteMedia(){
        Collections.sort(selected);

        for (int i=selected.size()-1; i>=0; i--){
            //selected.get(i) == position
            File file = new File(media.get(selected.get(i)).getPath());
            file.delete();
            media.remove(media.get(selected.get(i)));
            similarImageIconAdapter.notifyItemRemoved(selected.get(i));
            selected.remove(i);
        }
    }


    //data functions
    private void getData() {
        SharedPreferences sharedPreferences = getSharedPreferences(dataFilename, MODE_PRIVATE);
        imgColumns = sharedPreferences.getInt("imgColumns", getResources().getInteger(R.integer.imgColumns));
        themeUsed = sharedPreferences.getInt("themeUsed", getResources().getInteger(R.integer.themeUsed));
    }


    //click listeners
    @Override
    public void onItemClick(int position) {
        if (!media.get(position).getSelected()){
            if (selected.size()==0){
                final Bundle bundle = new Bundle();
                bundle.putBinder("media", new MediaWrapperForBinder(media));

                Intent i = new Intent(this, FullScreen.class);
                i.putExtras(bundle);
                i.putExtra("choosenMediaIdx", position);

                startActivity(i);
            }
            if (selected.size()>0) {
                selected.add(position);
                media.get(position).setSelected(true);
                similarImageIconAdapter.notifyItemChanged(position);
            }
        } else {
            selected.remove((Integer) (position));
            media.get(position).setSelected(false);
            if (selected.size()==0){
                menuItemDeleteButton.setVisible(false);
                menuItemDeselectAll.setVisible(false);
            }
            similarImageIconAdapter.notifyItemChanged(position);
        }
    }
    @Override
    public void onLongItemClick(int position) {
        if (selected.size()==0){
            selected.add(position);
            media.get(position).setSelected(true);

            similarImageIconAdapter.notifyItemChanged(position);
            menuItemDeleteButton.setVisible(true); //enable deleting
            menuItemDeselectAll.setVisible(true);
        }
    }
}
