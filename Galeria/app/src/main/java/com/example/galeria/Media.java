package com.example.galeria;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Comparator;

public class Media {
    private String path, name;
    private final double size;
    private boolean selected;
    private FileTime date;
    private final int image_video_gif;

    private final String[] imageExtensions = {".webp",".jfif",".jpg",".jpeg",".png",".tif",".tiff",".bmp"};
    //private final String[] videoExtensions = {".webm",".flv",".gif",".amv",".mp4",".m4p",".avi"};

    public Media(String path) throws IOException {
        this.path = path;
        this.name = path.substring(path.lastIndexOf("/")+1);
        this.date = Files.readAttributes(Paths.get(path),BasicFileAttributes.class).lastModifiedTime();
        this.size = new File(path).length();
        image_video_gif = setType(name);
        this.selected=false;
    }

    public String getPath() {
        return path;
    }
    public String getName() {
        return name;
    }
    public double getSize() {
        return size;
    }
    public FileTime getDate() {
        return date;
    }
    public int getType() {
        return image_video_gif;
    }
    public boolean getSelected(){
        return selected;
    }

    private int setType(String name) {
        for (String s:imageExtensions){
            if (name.toLowerCase().endsWith(s)){
                return 1;
            }
        }
        if (name.toLowerCase().endsWith(".gif")){
            return 3;
        }
        return 2;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setSelected(boolean value){
        this.selected = value;
    }


    public static Comparator<Media> mediaNameCompare = new Comparator<Media>() {
        @Override
        public int compare(Media o1, Media o2) {
            if (o1.getName().toLowerCase().equals(o2.getName().toLowerCase())){
                return o2.getDate().compareTo(o1.getDate());
            }
            return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
        }
    };
    public static Comparator<Media> mediaDateCompare = new Comparator<Media>() {
        @Override
        public int compare(Media o1, Media o2) {
            return o2.getDate().compareTo(o1.getDate());
        }
    };
    public static Comparator<Media> mediaSizeCompare = new Comparator<Media>() {
        @Override
        public int compare(Media o1, Media o2) {
            if (o2.getSize() == o1.getSize()){
                if (o1.getName().toLowerCase().equals(o2.getName().toLowerCase())){
                    if (o1.getName().equals(o2.getName())){
                        return o2.getDate().compareTo(o1.getDate());
                    }
                    return o1.getName().compareTo(o2.getName());
                }
                return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
            }
            return (int) (o2.getSize() - o1.getSize());
        }
    };
}
