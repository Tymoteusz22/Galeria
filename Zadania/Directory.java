package com.example.galeria;

import java.io.File;
import java.nio.file.Path;
import java.util.Comparator;

public class Directory{

    private String path, name;
    private int size=0;
    private boolean selected;

    private final String[] extensions = {".jfif",".jpg",".jpeg",".png",".tif",".tiff",".bmp",".webm",".flv",".gif",".amv",".mp4",".m4p",".avi"};

    public Directory(Path p){
        path = p.toString();
        name = ; 													// tutaj przypisz nazwę folderu
        this.selected = false;
        setSize();
    }

    private void setSize() {
        File currentDir = new File(String.valueOf(path));
        File[] filesList = currentDir.listFiles();
        for (File f : filesList){
            if (isMedia(f)) {
                this.size++;
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

    public String getPath(){ return path; }
    public String getName(){ return name; }
    public int getSize(){ return size; }
    public boolean getSelected(){
        return selected;
    }

    public void setName(String name){
        this.name = name;
        this.path = path.substring(0,path.lastIndexOf("/"))+name;
    }
    public void setSelected(boolean selected){
        this.selected = selected;
    }

																	// najlepiej będę widział komparatory w tym miejscu, ale nie nalegam
}