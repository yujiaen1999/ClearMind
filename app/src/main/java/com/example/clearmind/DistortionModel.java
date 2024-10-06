package com.example.clearmind;

public class DistortionModel {

    // string course_name for storing course_name
    // and imgid for storing image id.
    private String name;
    private int imgid;

    public DistortionModel(String course_name, int imgid) {
        this.name = course_name;
        this.imgid = imgid;
    }

    public String get_name() {
        return name;
    }

    public void set_name(String course_name) {
        this.name = course_name;
    }

    public int getImgid() {
        return imgid;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }
}