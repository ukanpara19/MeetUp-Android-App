package com.example.darshilbhayani.meetup_group1mobileapp;

public class rowDataListPlans {
    private int id;
    private String eventNm;
    private int imgSrc;
    private String date;
    private String time;
    private String duration;
    private int imgSrcNav;

    public rowDataListPlans(int id, String eventNm, int imgSrc, String date, String time, String duration, int imgSrcNav) {
        this.id = id;
        this.eventNm = eventNm;
        this.imgSrc = imgSrc;
        this.imgSrcNav = imgSrcNav;
        this.date = date;
        this.time = time;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventNm() {
        return eventNm;
    }

    public void setEventNm(String eventNm) {
        this.eventNm = eventNm;
    }

    public int getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(int imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getImgSrcNav() {
        return imgSrcNav;
    }

    public void setImgSrcNav(int imgSrcNav) {
        this.imgSrcNav = imgSrcNav;
    }
}
