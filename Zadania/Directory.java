package com.example.galeria;

import java.io.File;
import java.nio.file.Path;
import java.util.Comparator;

public class Directory{

    private String path, name;
    private int size = 0;
    private boolean selected;

    private final String[] extensions = {".webp",".jfif",".jpg",".jpeg",".png",".mp4",".avi",".gif",".tif",".tiff",".bmp",".webm",".flv",".amv",".m4p"};

    public Directory(Path p){
        path = p.toString();
        name = path.substring(path.lastIndexOf("/")+1);
        this.selected = false;
        setSize();
    }

    private void setSize() {
        File currentDir = new File(String.valueOf(path));
        File[] filesList = currentDir.listFiles();
        for (File f : filesList){
            if (isMedia(f.getName())) {
                this.size++;
            }
        }
    }
    private boolean isMedia(String fileName) {
        for (String s : extensions){
            if (fileName.endsWith(s)){
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
        this.path = path.substring(0,path.lastIndexOf("/")+1)+name;
    }
    public void setSelected(boolean selected){
        this.selected = selected;
    }
	
								//tutaj wstaw komparatory
}