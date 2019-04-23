package com.example.clair.uqacevent.Calendar;

public class Event {
    String date;
    String description;
    String place;
    String title;
    String organizer;

    public Event(String date, String description, String place, String title, String organizer) {
        this.date = date;
        this.description = description;
        this.place = place;
        this.title = title;
        this.organizer = organizer;
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
}
