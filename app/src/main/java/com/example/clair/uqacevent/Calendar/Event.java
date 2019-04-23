package com.example.clair.uqacevent.Calendar;

public class Event {
    private String date;
    private String description;
    private String place;
    private String title;
    private String organizer;
    private String type;
    private String organizerId;

    public Event(String date, String description, String place, String title, String organizer, String type, String organizerId) {
        this.date = date;
        this.description = description;
        this.place = place;
        this.title = title;
        this.organizer = organizer;
        this.type = type;
        this.organizerId = organizerId;
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

    public String getOrganizerId() {
        return organizerId;
    }
}
