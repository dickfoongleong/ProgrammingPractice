package com.lafarleaf.leafplanner.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "alert")
public class Alert {
    @Id
    @Column(name = "ID")
    private int id;

    @Column(name = "REMINDER")
    private Date reminderTime;

    @ManyToOne
    @JoinColumn(name = "EVENT_ID")
    private Event event;

    public int getId() {
        return id;
    }

    public Event getEvent() {
        return event;
    }

    public Date getReminderTime() {
        return reminderTime;
    }

}
