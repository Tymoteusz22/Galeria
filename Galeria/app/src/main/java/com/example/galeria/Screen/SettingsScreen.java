package com.example.galeria.Screen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.galeria.R;

public class SettingsScreen extends AppCompatActivity {

    String dataFilename = "data";
    private int dirColumns, imgColumns, daysBeforeDeleting, matchingPercentage, comparingPercentage;
    private boolean rotateWithGestures, deleteAfterEmptying, moveToBinBeforeDeleting, perfectMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);
        setTitle(getResources().getString(R.string.settings));

        getData();
    }

    public void openColorMenu(View view) {
        startActivity(new Intent(this, GraphicsScreen.class));
    }

    public void changeParameters(View view) {
        setBooleans();
        setIntegers();
        saveData();
    }
    private void setBooleans(){
        CheckBox checkBox = findViewById(R.id.box_rotate_with_gestures);
        rotateWithGestures = checkBox.isChecked();

        checkBox = findViewById(R.id.box_delete_after_emptying);
        deleteAfterEmptying = checkBox.isChecked();

        checkBox = findViewById(R.id.box_move_to_bin_before_deleting);
        moveToBinBeforeDeleting = checkBox.isChecked();

        checkBox = findViewById(R.id.box_perfect_match);
        perfectMatch = checkBox.isChecked();
    }
    private void setIntegers(){
        EditText editText = findViewById(R.id.insert_dir_columns_number);
        if (!editText.getText().toString().equals("")) {
            dirColumns = Integer.parseInt(editText.getText().toString());
        } else {
            dirColumns = getResources().getInteger(R.integer.dirColumns);
        }
        editText = findViewById(R.id.insert_img_columns_number);
        if (!editText.getText().toString().equals("")) {
            imgColumns = Integer.parseInt(editText.getText().toString());
        } else {
            imgColumns = getResources().getInteger(R.integer.imgColumns);
        }

        editText = findViewById(R.id.insert_days_before_deleting);
        if (moveToBinBeforeDeleting && !editText.getText().toString().equals("")){
            daysBeforeDeleting = Integer.parseInt(editText.getText().toString());
        } else {
            daysBeforeDeleting = getResources().getInteger(R.integer.daysBeforeDeleting);
        }

        editText = findViewById(R.id.insert_comparison_percentage);
        if (!editText.getText().toString().equals("")){
            comparingPercentage = Integer.parseInt(editText.getText().toString());
            if (comparingPercentage>100){
                matchingPercentage = 100;
            }
            if (comparingPercentage<1){
                matchingPercentage = 1;
            }
        }
        editText = findViewById(R.id.insert_matching_percentage);
        if (!editText.getText().toString().equals("")) {
            matchingPercentage = Integer.parseInt(editText.getText().toString());
            if (matchingPercentage > 100) {
                matchingPercentage = 100;
            }
            if (matchingPercentage < 1) {
                matchingPercentage = 1;
            }
        }
    }

    private void fillData(){
        ((EditText) findViewById(R.id.insert_dir_columns_number)).setText(String.valueOf(dirColumns));
        ((EditText) findViewById(R.id.insert_img_columns_number)).setText(String.valueOf(imgColumns));

        ((CheckBox) findViewById(R.id.box_rotate_with_gestures)).setChecked(rotateWithGestures);
        ((CheckBox) findViewById(R.id.box_delete_after_emptying)).setChecked(deleteAfterEmptying);
        ((CheckBox) findViewById(R.id.box_move_to_bin_before_deleting)).setChecked(moveToBinBeforeDeleting);

        if (moveToBinBeforeDeleting){
            ((EditText) findViewById(R.id.insert_days_before_deleting)).setText(String.valueOf(daysBeforeDeleting));
        }

        ((EditText) findViewById(R.id.insert_matching_percentage)).setText(String.valueOf(matchingPercentage));
        ((EditText) findViewById(R.id.insert_comparison_percentage)).setText(String.valueOf(comparingPercentage));
        ((CheckBox) findViewById(R.id.box_perfect_match)).setChecked(perfectMatch);
    }

    private void getData(){
        SharedPreferences sharedPreferences = getSharedPreferences(dataFilename, MODE_PRIVATE);
        dirColumns = sharedPreferences.getInt("dirColumns", getResources().getInteger(R.integer.dirColumns));
        imgColumns = sharedPreferences.getInt("imgColumns", getResources().getInteger(R.integer.imgColumns));

        rotateWithGestures = sharedPreferences.getBoolean("rotateWithGestures",getResources().getBoolean(R.bool.rotateWithGestures));

        deleteAfterEmptying = sharedPreferences.getBoolean("deleteAfterEmptying",getResources().getBoolean(R.bool.deleteAfterEmptying));
        moveToBinBeforeDeleting = sharedPreferences.getBoolean("moveToBinBeforeDeleting",getResources().getBoolean(R.bool.moveToBinBeforeDeleting));
        daysBeforeDeleting = sharedPreferences.getInt("daysBeforeDeleting",getResources().getInteger(R.integer.daysBeforeDeleting));

        perfectMatch = sharedPreferences.getBoolean("perfectMatch",getResources().getBoolean(R.bool.perfectMatch));
        matchingPercentage = sharedPreferences.getInt("matchingPercentage",getResources().getInteger(R.integer.matchingPercentage));
        comparingPercentage = sharedPreferences.getInt("comparingPercentage", getResources().getInteger(R.integer.comparingPercentage));
        fillData();
    }
    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(dataFilename, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("dirColumns", dirColumns); //name:value
        editor.putInt("imgColumns", imgColumns);
        editor.putInt("daysBeforeDeleting", daysBeforeDeleting);
        editor.putInt("matchingPercentage", matchingPercentage);
        editor.putInt("comparingPercentage", comparingPercentage);
        editor.putBoolean("rotateWithGestures",rotateWithGestures);
        editor.putBoolean("deleteAfterEmptying",deleteAfterEmptying);
        editor.putBoolean("moveToBinBeforeDeleting",moveToBinBeforeDeleting);
        editor.putBoolean("perfectMatch", perfectMatch);
        editor.apply();
        super.onBackPressed();
    }

}
