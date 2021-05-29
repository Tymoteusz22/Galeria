package com.example.galeria;

import java.io.File;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.Comparator;

public class Directory{

    private String path, name;
    private int size = 0;
    private boolean selected;

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
        String type = URLConnection.guessContentTypeFromName(fileName);
        if (type == null){
            return false;
        }
        if (type.startsWith("image") || type.startsWith("video") || type.startsWith("gif")){
            return true;
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

								//tutaj wstaw komparatory
}