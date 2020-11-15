package com.lafarleaf.leafplanner.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "event")
public class Event {
    @Id
    @Column(name = "ID")
    private int id;

    @Column(name = "USER_ID")
    private int userId;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String desc;

    @Column(name = "LOCATION")
    private String location;

    @Column(name = "START")
    private Date startTime;

    @Column(name = "END")
    private Date endTime;

    @Column(name = "REPEAT_TIME")
    private String repeat;

    public Event(int id, int userId, String title, String desc, String location, Date startTime, Date endTime,
            String repeat) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.desc = desc;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.repeat = repeat;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getLocation() {
        return location;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public String getRepeat() {
        return repeat;
    }
}
