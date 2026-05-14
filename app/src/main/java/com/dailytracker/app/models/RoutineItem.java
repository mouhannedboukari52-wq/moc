package com.dailytracker.app.models;

public class RoutineItem {
    private String id;
    private String userId;
    private String title;
    private String category; // FOOD or SPORT
    private int hour;
    private int minute;

    public RoutineItem() {}

    public RoutineItem(String id, String userId, String title, String category, int hour, int minute) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.category = category;
        this.hour = hour;
        this.minute = minute;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getHour() { return hour; }
    public void setHour(int hour) { this.hour = hour; }

    public int getMinute() { return minute; }
    public void setMinute(int minute) { this.minute = minute; }
}
