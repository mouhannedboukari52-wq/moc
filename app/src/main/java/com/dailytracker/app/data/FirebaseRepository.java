package com.dailytracker.app.data;

import com.dailytracker.app.models.DailyLog;
import com.dailytracker.app.models.RoutineItem;
import com.dailytracker.app.models.Tip;
import com.dailytracker.app.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseRepository {

    private final DatabaseReference dbRef;

    public FirebaseRepository() {
        dbRef = FirebaseDatabase.getInstance().getReference();
    }

    // ---- User ----

    public interface UserCallback {
        void onResult(User user);
        void onError(String message);
    }

    public void saveUser(User user, Runnable onSuccess, java.util.function.Consumer<String> onError) {
        dbRef.child(DbPaths.userPath(user.getUid())).setValue(user)
                .addOnSuccessListener(unused -> onSuccess.run())
                .addOnFailureListener(e -> onError.accept(e.getMessage()));
    }

    public void getUserByName(String name, UserCallback callback) {
        dbRef.child(DbPaths.USERS).orderByChild("name").equalTo(name)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                User user = child.getValue(User.class);
                                callback.onResult(user);
                                return;
                            }
                        }
                        callback.onResult(null);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        callback.onError(error.getMessage());
                    }
                });
    }

    // ---- Routines ----

    public interface RoutinesCallback {
        void onResult(List<RoutineItem> items);
        void onError(String message);
    }

    public void saveRoutine(String userId, RoutineItem item,
                             Runnable onSuccess, java.util.function.Consumer<String> onError) {
        DatabaseReference ref = dbRef.child(DbPaths.userRoutinesPath(userId)).child(item.getId());
        ref.setValue(item)
                .addOnSuccessListener(unused -> onSuccess.run())
                .addOnFailureListener(e -> onError.accept(e.getMessage()));
    }

    public void getUserRoutines(String userId, RoutinesCallback callback) {
        dbRef.child(DbPaths.userRoutinesPath(userId))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        List<RoutineItem> items = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            RoutineItem item = child.getValue(RoutineItem.class);
                            if (item != null) items.add(item);
                        }
                        callback.onResult(items);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        callback.onError(error.getMessage());
                    }
                });
    }

    // ---- Daily Logs ----

    public void markRoutineDone(String userId, String routineId, String date,
                                 Runnable onSuccess, java.util.function.Consumer<String> onError) {
        DailyLog log = new DailyLog(routineId, date, true);
        dbRef.child(DbPaths.routineLogPath(userId, routineId, date)).setValue(log)
                .addOnSuccessListener(unused -> onSuccess.run())
                .addOnFailureListener(e -> onError.accept(e.getMessage()));
    }

    public interface DoneCallback {
        void onResult(boolean isDone);
    }

    public void isRoutineDoneToday(String userId, String routineId, String date, DoneCallback callback) {
        dbRef.child(DbPaths.routineLogPath(userId, routineId, date))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            DailyLog log = snapshot.getValue(DailyLog.class);
                            callback.onResult(log != null && log.isDone());
                        } else {
                            callback.onResult(false);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        callback.onResult(false);
                    }
                });
    }

    // ---- Tips ----

    public interface TipsCallback {
        void onResult(List<Tip> tips);
        void onError(String message);
    }

    public void getTipsByCategory(String category, TipsCallback callback) {
        dbRef.child(DbPaths.tipsPath(category))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        List<Tip> tips = new ArrayList<>();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Tip tip = child.getValue(Tip.class);
                            if (tip != null) tips.add(tip);
                        }
                        callback.onResult(tips);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        callback.onError(error.getMessage());
                    }
                });
    }

    public String generateId(String path) {
        return dbRef.child(path).push().getKey();
    }
}
