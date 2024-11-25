package com.example.appmaternal;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log; // Asegúrate de importar la clase Log
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.appmaternal.model.AlarmReceiver;

import java.util.Calendar;

public class SetNotificacionFragment extends Fragment {
    private static final String TAG = "SetNotificacionFragment"; // Definición de TAG
    private int hour, minute;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_set_notificacion, container, false);

        Button setTimeButton = view.findViewById(R.id.set_time_button);
        setTimeButton.setOnClickListener(v -> showTimePickerDialog());

        return view; // Devuelve la vista inflada
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), (view, hourOfDay, minute) -> {
            this.hour = hourOfDay;
            this.minute = minute;
            scheduleNotification();
        }, hour, minute, true);
        timePickerDialog.show();
    }

    private void scheduleNotification() {
        // Guarda el horario en SharedPreferences
        SharedPreferences preferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        preferences.edit().putInt("notification_hour", hour).putInt("notification_minute", minute).apply();

        // Log para confirmar que se guardaron las preferencias
        Log.d(TAG, "Horario de notificación programado: " + hour + ":" + minute);

        // Configura el AlarmManager para enviar la notificación
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // Si la hora ya pasó, programa para el día siguiente
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Configura la alarma para que se repita cada día
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        // Imprimir la zona horaria actual
        String timeZone = java.util.TimeZone.getDefault().getID();
        Log.d(TAG, "Zona horaria actual: " + timeZone);

        // Log para confirmar que se programó la notificación
        Log.d(TAG, "Notificación programada para: " + calendar.getTime());
    }
}
