package com.example.appmaternal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.example.appmaternal.model.AlarmReceiver;

import java.util.Calendar;

public class SetNotificationActivity1 extends AppCompatActivity {

    private int hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_notification);

        Button setTimeButton = findViewById(R.id.set_time_button);
        setTimeButton.setOnClickListener(v -> showTimePickerDialog());
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            this.hour = hourOfDay;
            this.minute = minute;
            scheduleNotification();
        }, hour, minute, true);
        timePickerDialog.show();
    }

    private void scheduleNotification() {
        // Guarda el horario en SharedPreferences
        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        preferences.edit().putInt("notification_hour", hour).putInt("notification_minute", minute).apply();

        // Configura el AlarmManager para enviar la notificación
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // Si la hora ya pasó, programa para el día siguiente
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}