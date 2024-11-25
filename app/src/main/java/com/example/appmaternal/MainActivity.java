package com.example.appmaternal;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.example.appmaternal.model.Gestante;
import com.example.appmaternal.model.Sesion;
import com.example.appmaternal.response.GestanteConsultarResponse;
import com.example.appmaternal.response.HistorialGestanteResponse;
import com.example.appmaternal.response.LoginResponse;

import com.example.appmaternal.retrofit.ApiService;
import com.example.appmaternal.retrofit.RetrofitClient;
import com.example.appmaternal.util.Helper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.navigation.NavController;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Tag;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextInputEditText txtEmail, txtClave;
    MaterialButton btnIngresar, btnRegistrarse, btnMedico;

    private LinearLayout loginLayout;
    int gestante_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtEmail = findViewById(R.id.txtEmail);
        txtClave = findViewById(R.id.txtClave);
        btnIngresar = findViewById(R.id.btnIngresar);
        btnIngresar.setOnClickListener(this);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        btnRegistrarse.setOnClickListener(this);
        btnMedico = findViewById(R.id.btnMedico);
        btnMedico.setOnClickListener(this);

        loginLayout = findViewById(R.id.loginlayout);
        loginLayout.setVisibility(View.VISIBLE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "canal_notificaciones",
                    "Nombre del Canal",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Descripción del Canal");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        // Obtener el token de Firebase Cloud Messaging (FCM)
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Obtener el token de FCM
                        String token = task.getResult();

                        // Mostrar token sin getString() si no quieres usar el recurso de strings.xml
                        String msg = "Token de registro FCM: " + token;
                        //Log.d(msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


    }


    private void mostrarFragmento() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new UsuarioRegistrarFragment())
                .commit();
        // fragmentContainer.setVisibility(View.VISIBLE); // Hacer visible el contenedor del fragmento si es necesario
    }


    @Override
    public void onClick(final View view) {
        if (view.getId() == R.id.btnIngresar) {
            login();
            loginLayout.setVisibility(View.GONE);
        } else if (view.getId() == R.id.btnRegistrarse){
            //registrarUsuario(view);
            mostrarFragmento();
            loginLayout.setVisibility(View.GONE);
        }else if(view.getId() == R.id.btnMedico){
            mostrarFragmentoMedico();
            loginLayout.setVisibility(View.GONE);
        }
    }

    private void mostrarFragmentoMedico() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_medico, new MainMedicoFragment())
                .commit();
    }

    private void login() {
        ApiService apiService = RetrofitClient.urlComercial.create(ApiService.class);
        String email = txtEmail.getText().toString();
        String clave = Helper.convertPassMd5(txtClave.getText().toString());

        Call<LoginResponse> call = apiService.login(email, clave);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(final Call<LoginResponse> call, final Response<LoginResponse> response) {
                if (response.isSuccessful()) { // Verificar si la respuesta fue exitosa
                    LoginResponse loginResponse = response.body();
                    boolean status = loginResponse.isStatus();

                    if (status) { // Login exitoso
                        RetrofitClient.API_TOKEN = loginResponse.getData().getToken();

                        // Obtener datos del usuario
                        String nombre = loginResponse.getData().getNombres();
                        int usuariaId = loginResponse.getData().getId();
                        String userEmail = loginResponse.getData().getEmail();
                        Log.e("Usuaria Id", String.valueOf(usuariaId));
                        Log.e("Usuaria nombre", nombre);
                        Log.e("Usuaria email", userEmail);

                        // Guardar datos de sesión
                        Sesion.DATOS_SESION = loginResponse.getData();

                        // Crear intent para pasar a MenuActivity
                        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                        intent.putExtra("USER_ID", usuariaId); // Pasar el ID del usuario
                        intent.putExtra("USER_NAME", nombre); // Pasar el nombre del usuario
                        intent.putExtra("USER_EMAIL", userEmail); // Pasar el email del usuario

                        // Obtener el ID de gestante usando el callback
                        idGestanteUsuaria(usuariaId, new GestanteIdCallback() {
                            @Override
                            public void onGestanteIdReceived(int gestanteId) {
                                Log.e("IDGESTANTE", String.valueOf(gestanteId));

                                // Pasar el ID de gestante, o -1 si no está definido
                                intent.putExtra("GEST_ID", gestanteId);

                                // Iniciar MenuActivity
                                startActivity(intent);
                                Toast.makeText(MainActivity.this, "Bienvenida a tu aplicación: " + nombre, Toast.LENGTH_SHORT).show();

                                // Cerrar MainActivity
                                MainActivity.this.finish();
                            }

                            @Override
                            public void onError(String errorMessage) {
                                Log.e("MainActivity", errorMessage);
                                // En caso de error al obtener el ID de gestante, seguir con la navegación sin ID de gestante
                                intent.putExtra("GEST_ID", -1); // Indicar que no hay ID de gestante definido

                                // Iniciar MenuActivity
                                startActivity(intent);
                                Toast.makeText(MainActivity.this, "Bienvenida a tu aplicación: " + nombre, Toast.LENGTH_SHORT).show();

                                // Cerrar MainActivity
                                MainActivity.this.finish();
                            }
                        });
                    } else {
                        // Si el login no fue exitoso, mostrar mensaje de credenciales incorrectas
                        reloadMainActivity();
                        Toast.makeText(MainActivity.this, "Credenciales incorrectas. Vuelve a intentarlo", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Si la respuesta no fue exitosa, mostrar mensaje de error
                    reloadMainActivity();
                    //Toast.makeText(MainActivity.this, "Error en la respuesta del servidor. Vuelve a intentarlo", Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "Credenciales incorrectas. Vuelve a intentarlo", Toast.LENGTH_SHORT).show();
                    Log.e("Error ===>", response.message());
                }
            }

            @Override
            public void onFailure(final Call<LoginResponse> call, final Throwable t) {
                // En caso de fallo en la llamada, mostrar mensaje de error
                Log.e("Fail ===>", t.getMessage());
                reloadMainActivity();
                Toast.makeText(MainActivity.this, "Fallo en la conexión. Inténtalo de nuevo", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public interface GestanteIdCallback {
        void onGestanteIdReceived(int gestanteId);
        void onError(String errorMessage);
    }



    private void idGestanteUsuaria(int idUser, GestanteIdCallback callback) {
        ApiService apiService = RetrofitClient.createService();
        Call<GestanteConsultarResponse> call = apiService.consultarIdGestante(idUser);
        call.enqueue(new Callback<GestanteConsultarResponse>() {
            @Override
            public void onResponse(Call<GestanteConsultarResponse> call, Response<GestanteConsultarResponse> response) {
                if (response.code() == 200) {
                    boolean status = response.body().isStatus();
                    if (status) {
                        // Obtener el ID de la gestante de la respuesta
                        int gestanteId = response.body().getData().getId(); // Asegúrate de que este es el campo correcto
                        Log.e("id_gestante pasa a recordatorios", String.valueOf(gestanteId));
                        callback.onGestanteIdReceived(gestanteId); // Llama al callback con el gestanteId obtenido
                    } else {
                        callback.onError("Error al acceder al servicio web: " + response.message());
                    }
                } else {
                    callback.onError("Error al acceder al servicio web: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<GestanteConsultarResponse> call, Throwable t) {
                callback.onError("Falla al conectarse al servicio web: " + t.getMessage());
            }
        });
    }


    private void reloadMainActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


/*cerrar sesion*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_cerrar_sesion) {
            logoutAndReturnToMain();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        // Eliminar datos de sesión o autenticación
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    private void logoutAndReturnToMain() {
        // Cerrar sesión
        logout();

        // Intent para volver a MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        // Opcional: Finalizar la actividad actual si es necesario
        finish();
    }


}