package com.example.galeria.Screen;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.galeria.R;

import java.util.ArrayList;

public class GraphicsScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner listThemes, listTextColor, listBgColor, listStatusBarColor, listActionBarColor, listNavigationBarColor;
    String dataFilename = "data";
    int themeUsed, textColorUsed, bgColorUsed, statusBarColorUsed, actionBarColorUsed, navigationBarColorUsed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getData();
        switch (themeUsed){
            case 1:
                setTheme(R.style.GalleryThemeDay);
                break;
            case 3:
                //settingCustomTheme();
                setTheme(R.style.GalleryThemeDay);
                break;
            default:
                //setTheme(R.style.GalleryThemeNight);
                setTheme(R.style.GalleryThemeDay);
                break;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphics_screen);
        getSpinners();
        //setSpinners();
        setTitle(getResources().getString(R.string.graphics_screen));
    }
    //'visible' functions
    private void setSpinners(){
        listTextColor.setSelection(textColorUsed);
        listBgColor.setSelection(bgColorUsed);
        listActionBarColor.setSelection(actionBarColorUsed);
        listNavigationBarColor.setSelection(navigationBarColorUsed);
        listStatusBarColor.setSelection(statusBarColorUsed);
    }


    //helping functions
    private void getSpinners(){
        listThemes = findViewById(R.id.themes_list);
        listThemes.setOnItemSelectedListener(this);
        listTextColor = findViewById(R.id.text_color);
        listBgColor = findViewById(R.id.bg_color);
        listStatusBarColor = findViewById(R.id.statusBar_color);
        listActionBarColor = findViewById(R.id.actionBar_color);
        listNavigationBarColor = findViewById(R.id.navigationBar_color);
        if (themeUsed == 3){
            setItemsClickable();
        } else {
            setItemsNotClickable();
        }
    }
    private void setItemsClickable(){
        listTextColor.setEnabled(true);
        listBgColor.setEnabled(true);
        listStatusBarColor.setEnabled(true);
        listActionBarColor.setEnabled(true);
        listNavigationBarColor.setEnabled(true);
    }
    private void setItemsNotClickable(){
        listTextColor.setEnabled(false);
        listBgColor.setEnabled(false);
        listStatusBarColor.setEnabled(false);
        listActionBarColor.setEnabled(false);
        listNavigationBarColor.setEnabled(false);
    }


    //data functions
    public void changeParameters(View view) {
        switch (themeUsed){
            case 1:
                setTheme(R.style.GalleryThemeDay);
                break;
            case 3:
                //settingCustomTheme();
                setTheme(R.style.GalleryThemeDay);
                break;
            default:
                //setTheme(R.style.GalleryThemeNight);
                setTheme(R.style.GalleryThemeDay);
                break;
        }
        saveData();
    }

    private void getData() {
        SharedPreferences sharedPreferences = getSharedPreferences(dataFilename, MODE_PRIVATE);
        themeUsed = sharedPreferences.getInt("themeUsed", getResources().getInteger(R.integer.themeUsed));
        }
    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(dataFilename, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("themeUsed",themeUsed);
        editor.apply();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        switch (text){
            case "Custom":
                setItemsClickable();
                themeUsed = 3;
                break;
            case "Day":
                setItemsNotClickable();
                themeUsed = 1;
                break;
            default:
                setItemsNotClickable();
                themeUsed = 2;
                break;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        setItemsNotClickable();
        themeUsed = 2;
    }
}
