package com.lafarleaf.leafplanner.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name = "event")
public class Event {
    @Id
    @Column(name = "ID")
    private int id;

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

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private Set<Alert> alerts = new HashSet<>();

    public Event(int id, User user, String title, String desc, String location, Date startTime, Date endTime,
            String repeat) {
        this.id = id;
        this.user = user;
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

    public User getUser() {
        return user;
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
