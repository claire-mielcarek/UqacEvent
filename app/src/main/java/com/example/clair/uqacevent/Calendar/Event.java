package com.example.clair.uqacevent.Calendar;

public class Event {
    String date;
    String description;
    String place;
    String title;
    String organizer;
    String type;

    public Event(String date, String description, String place, String title, String organizer) {
        this.date = date;
        this.description = description;
        this.place = place;
        this.title = title;
        this.organizer = organizer;
    }

    public Event(String date, String description, String place, String title, String organizer, String type) {
        this.date = date;
        this.description = description;
        this.place = place;
        this.title = title;
        this.organizer = organizer;
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getPlace() {
        return place;
    }

    public String getTitle() {
        return title;
    }

    public String getOrganizer() {
        return organizer;
    }

    public String getType() {
        return type;
    }
}
