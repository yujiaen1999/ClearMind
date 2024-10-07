package com.example.clearmind;

public class CheckinModel {

    // string course_name for storing course_name
    // and imgid for storing image id.
    private String date;
    private int imgid;
    private String score;

    public CheckinModel(String date, int imgid, String score) {
        this.date = date;
        this.imgid = imgid;
        this.score = score;
    }

    public String get_date() {
        return date;
    }

    public void set_date(String date) {
        this.date = date;
    }

    public int getImgid() {
        return imgid;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }

    public String get_score() {
        return score;
    }

    public void set_score(String score) {
        this.score = score;
    }

    public void set_background() {

    }
}