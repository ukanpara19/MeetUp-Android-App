package com.example.darshilbhayani.meetup_group1mobileapp;

public class Event {


    private String email_id;
    private String event_date;
    private String event_dest;
    private String event_duration;
    public String event_name;
    private String event_source;
    private String event_time;
    public String event_type;
    public String lan_dest;
    public String lan_source;
    public String lat_dest;
    public String lat_source;
    private String ppl_joined;


    public String getPpl_joined() {
        return ppl_joined;
    }

    public void setPpl_joined(String ppl_joined) {
        this.ppl_joined = ppl_joined;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getEvent_dest() {
        return event_dest;
    }

    public void setEvent_dest(String event_dest) {
        this.event_dest = event_dest;
    }

    public String getEvent_duration() {
        return event_duration;
    }

    public void setEvent_duration(String event_duration) {
        this.event_duration = event_duration;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_source() {
        return event_source;
    }

    public void setEvent_source(String event_source) {
        this.event_source = event_source;
    }

    public String getEvent_time() {
        return event_time;
    }

    public void setEvent_time(String event_time) {
        this.event_time = event_time;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getLan_dest() {
        return lan_dest;
    }

    public void setLan_dest(String lan_dest) {
        this.lan_dest = lan_dest;
    }

    public String getLan_source() {
        return lan_source;
    }

    public void setLan_source(String lan_source) {
        this.lan_source = lan_source;
    }

    public String getLat_dest() {
        return lat_dest;
    }

    public void setLat_dest(String lat_dest) {
        this.lat_dest = lat_dest;
    }

    public String getLat_source() {
        return lat_source;
    }

    public void setLat_source(String lat_source) {
        this.lat_source = lat_source;
    }

    public Event(){

    }

    public Event(String email_id, String event_date, String event_dest, String event_duration,
                 String event_name, String event_source, String event_time, String event_type,
                 String lan_dest, String lan_source, String lat_dest, String lat_source, String ppl_joined) {
        this.email_id = email_id;
        this.event_date = event_date;
        this.event_dest = event_dest;
        this.event_duration = event_duration;
        this.event_name = event_name;
        this.event_source = event_source;
        this.event_time = event_time;
        this.event_type = event_type;
        this.lan_dest = lan_dest;
        this.lan_source = lan_source;
        this.lat_dest = lat_dest;
        this.lat_source = lat_source;
        this.ppl_joined = ppl_joined;

    }
}
