package com.example.galeria.Screen;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.galeria.R;

public class GraphicsScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getData();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphics_screen);
        setTitle(getResources().getString(R.string.graphics_screen));

        setViewData();
    }

    private void setViewData() {
        Spinner dropMenu = findViewById(R.id.themes_list);
        dropMenu.setOnItemSelectedListener(this);

    }

    public void getData(){



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        switch (text){
            case "Day":
                setTheme(R.style.GalleryThemeDay);
                break;
            case "Night":
                setTheme(R.style.GalleryThemeDay);
                break;
            default:
                setTheme(R.style.GalleryThemeDay);
                break;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
