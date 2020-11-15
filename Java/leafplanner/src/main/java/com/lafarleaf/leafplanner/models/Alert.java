package com.lafarleaf.leafplanner.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "alert")
public class Alert {
    @Id
    @Column(name = "ID")
    private int id;

    @Column(name = "EVENT_ID")
    private int eventId;

    @Column(name = "REMINDER")
    private Date reminderTime;

    public int getId() {
        return id;
    }

    public int getEventId() {
        return eventId;
    }

    public Date getReminderTime() {
        return reminderTime;
    }

}
