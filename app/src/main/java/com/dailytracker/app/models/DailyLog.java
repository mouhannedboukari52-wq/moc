package com.dailytracker.app.models;

public class DailyLog {
    private String routineId;
    private String date; // format: yyyy-MM-dd
    private boolean done;

    public DailyLog() {}

    public DailyLog(String routineId, String date, boolean done) {
        this.routineId = routineId;
        this.date = date;
        this.done = done;
    }

    public String getRoutineId() { return routineId; }
    public void setRoutineId(String routineId) { this.routineId = routineId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }
}
