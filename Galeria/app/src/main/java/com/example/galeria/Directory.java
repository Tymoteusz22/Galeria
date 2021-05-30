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

    public static Comparator<Directory> dirNameCompare = new Comparator<Directory>() {
        @Override
        public int compare(Directory o1, Directory o2) {
            if (o1.getName().toLowerCase().equals(o2.getName().toLowerCase())){
                if (o1.getName().equals(o2.getName())){
                    return o2.getSize()-o1.getSize(); //ewentualnie go for rozmiar
                }
                return o1.getName().compareTo(o2.getName()); //potem wielkosc liter
            }
            return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase()); //najpierw litery
        }
    };
    public static Comparator<Directory> dirSizeCompare = new Comparator<Directory>() {
        @Override
        public int compare(Directory o1, Directory o2) {
            if(o1.getSize()==o2.getSize()){
                if (o1.getName().toLowerCase().equals(o2.getName().toLowerCase())){
                    return o1.getName().compareTo(o2.getName()); //ewentualnie wielkosc liter
                }
                return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase()); //potem litery
            }
            return o2.getSize()-o1.getSize(); //najpierw rozmiar
        }
    };
}