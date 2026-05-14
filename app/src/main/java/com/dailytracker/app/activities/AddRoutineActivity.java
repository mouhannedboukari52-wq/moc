package com.dailytracker.app.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dailytracker.app.data.FirebaseRepository;
import com.dailytracker.app.databinding.ActivityAddRoutineBinding;
import com.dailytracker.app.models.RoutineItem;
import com.dailytracker.app.receiver.ReminderReceiver;
import com.dailytracker.app.utils.SessionManager;

import java.util.Calendar;
import java.util.UUID;

public class AddRoutineActivity extends AppCompatActivity {

    private ActivityAddRoutineBinding binding;
    private FirebaseRepository repository;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddRoutineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add Routine");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        repository = new FirebaseRepository();
        session = new SessionManager(this);

        binding.btnSave.setOnClickListener(v -> saveRoutine());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void saveRoutine() {
        String title = binding.etTitle.getText().toString().trim();
        String category = binding.rgCategory.getCheckedRadioButtonId() == R.id.rb_food ? "FOOD" : "SPORT";
        int hour = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
                ? binding.timePicker.getHour() : binding.timePicker.getCurrentHour();
        int minute = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
                ? binding.timePicker.getMinute() : binding.timePicker.getCurrentMinute();

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = session.getUserId();
        String id = UUID.randomUUID().toString();
        RoutineItem item = new RoutineItem(id, userId, title, category, hour, minute);

        binding.btnSave.setEnabled(false);
        repository.saveRoutine(userId, item, () -> {
            scheduleReminder(item);
            Toast.makeText(this, "Routine added!", Toast.LENGTH_SHORT).show();
            finish();
        }, err -> {
            binding.btnSave.setEnabled(true);
            Toast.makeText(this, "Error: " + err, Toast.LENGTH_SHORT).show();
        });
    }

    private void scheduleReminder(RoutineItem item) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.putExtra(ReminderReceiver.EXTRA_ROUTINE_TITLE, item.getTitle());
        intent.putExtra(ReminderReceiver.EXTRA_ROUTINE_ID, item.getId());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                item.getId().hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, item.getHour());
        calendar.set(Calendar.MINUTE, item.getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        if (alarmManager != null) {
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent);
        }
    }
}
