package com.example.appmaternal.model;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.appmaternal.MainActivity; // Asegúrate de que la importación sea correcta
import com.example.appmaternal.R;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context);
        // Puedes agregar la lógica para reprogramar la alarma aquí si lo deseas
    }

    private void showNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "your_channel_id";

        // Crea el canal de notificación (requerido en Android 8.0 y superior)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Daily Notification", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Crea un Intent para abrir la actividad cuando se toca la notificación
        Intent notificationIntent = new Intent(context, MainActivity.class); // Cambia esto según tu actividad
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Construye la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.campana) // Cambia esto por tu icono
                .setContentTitle("AppMaternal")
                .setContentText("No olvides registrar tus síntomas del día en AppMaternal")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent) // Establece el Intent de la notificación
                .setAutoCancel(true); // La notificación se eliminará al tocarla

        // Muestra la notificación
        notificationManager.notify(0, builder.build());
    }
}
