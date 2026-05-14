package com.dailytracker.app.data;

public class DbPaths {
    public static final String USERS = "users";
    public static final String ROUTINES = "routines";
    public static final String DAILY_LOGS = "daily_logs";
    public static final String TIPS = "tips";

    public static String userPath(String userId) {
        return USERS + "/" + userId;
    }

    public static String userRoutinesPath(String userId) {
        return ROUTINES + "/" + userId;
    }

    public static String routineLogPath(String userId, String routineId, String date) {
        return DAILY_LOGS + "/" + userId + "/" + routineId + "/" + date;
    }

    public static String tipsPath(String category) {
        return TIPS + "/" + category;
    }
}
