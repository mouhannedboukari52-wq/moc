package com.dailytracker.app.models;

public class Tip {
    private String id;
    private String category; // FOOD or SPORT
    private String title;
    private String description;

    public Tip() {}

    public Tip(String id, String category, String title, String description) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.description = description;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
