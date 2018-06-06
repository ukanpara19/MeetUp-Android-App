package com.example.darshilbhayani.meetup_group1mobileapp;

public class Event {
    public String event_type, event_name;
    public String source_lat;
    public String source_lon;
    public String dest_lat;
    public String dest_lon;
    public String sourceLocation;
    public String destLocation;
    private int duration;
    private String time;
    private String date;

    public Event(){

    }

    public void setSourceLocation(String sourceLocation) {
        this.sourceLocation = sourceLocation;
    }
    public void setEvent_type (String event_type){
        this.event_type = event_type;
    }

    public void setEvent_name(String event_name){
        this.event_name = event_name;
    }

    public void  setSourceLat(String source_lat){
        this.source_lat = source_lat;
    }
    public void setSourceLon(String source_lon){
        this.source_lon = source_lon;
    }

    public void setDestLat(String dest_lat){
        this.dest_lat = dest_lat;
    }

    public void setDestLon(String dest_lon){
        this.dest_lon = dest_lon;
    }

    public void event_setDate(String date){
        this.date = date;
    }

    public void event_setTime(String time){
        this.time = time;
    }

    public void event_setDuration(int duration){
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getEvent_type() {
        return event_type;
    }

    public String getEvent_name() {
        return event_name;
    }

    public String getSource_lat() {
        return source_lat;
    }

    public String getSource_lon() {
        return source_lon;
    }

    public String getDest_lon() {
        return dest_lon;
    }

    public String getDest_lat() {
        return dest_lat;
    }

    public String getDestLocation() {
        return destLocation;
    }

    public String getSourceLocation() {
        return sourceLocation;
    }

    public void setDestLocation(String destLocation) {
        this.destLocation = destLocation;
    }
}
