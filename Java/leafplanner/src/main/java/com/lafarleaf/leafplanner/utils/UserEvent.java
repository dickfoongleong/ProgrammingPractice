package com.lafarleaf.leafplanner.utils;

import java.util.Date;
import java.util.List;

import com.lafarleaf.leafplanner.models.Alert;
import com.lafarleaf.leafplanner.models.Event;

public class UserEvent extends Event {
    private List<Alert> alerts;

    public UserEvent(int id, int userID, String title, String desc, String location, Date startTime, Date endTime,
            String repeat, List<Alert> alerts) {
        super(id, userID, title, desc, location, startTime, endTime, repeat);
        this.alerts = alerts;
    }

    public List<Alert> getAlerts() {
        return alerts;
    }
}
