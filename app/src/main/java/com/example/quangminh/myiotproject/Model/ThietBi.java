package com.example.quangminh.myiotproject.Model;

public class ThietBi {
    String name;
    int level;

    public ThietBi() {
    }

    public ThietBi(String name,int level) {

        this.name = name;

        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
