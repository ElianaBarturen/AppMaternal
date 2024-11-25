package com.example.appmaternal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.bumptech.glide.Glide;
import com.example.appmaternal.databinding.ActivityMenuBinding;
import com.example.appmaternal.model.Sesion;
import com.example.appmaternal.retrofit.RetrofitClient;
import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenuBinding binding;

    /*Variables para la cabecera del menu*/
    CircleImageView imagenUsuario;
    TextView nombreUsuario, emailUsuario;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMenu.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_Bienvenido, R.id.nav_home, R.id.nav_historial, R.id.nav_recordatorios, R.id.nav_reportes, R.id.nav_informativo, R.id.nav_cerrar_sesion)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Enlazar los controles de la cabecera del menú
        View view = navigationView.getHeaderView(0);
        imagenUsuario = view.findViewById(R.id.imagenUsuario);
        nombreUsuario = view.findViewById(R.id.nombreUsuario);
        emailUsuario = view.findViewById(R.id.emailUsuario);

        // Mostrar los datos del usuario que han iniciado sesión
        nombreUsuario.setText(Sesion.DATOS_SESION.getNombres());
        emailUsuario.setText(Sesion.DATOS_SESION.getEmail());

        Log.e("URL Imagen", RetrofitClient.urlComercial.baseUrl() + Sesion.DATOS_SESION.getImagen());
        Glide.with(this)
                .load(RetrofitClient.urlComercial.baseUrl() + Sesion.DATOS_SESION.getImagen())
                .into(imagenUsuario);

        // Manejar la navegación y el paso de datos
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    // Recuperar los datos del intent
                    Intent intent = getIntent();
                    int usuariaId = intent.getIntExtra("USER_ID", -1);
                    String nombre = intent.getStringExtra("USER_NAME");
                    String email = intent.getStringExtra("USER_EMAIL");
                    int gestanteId = intent.getIntExtra("GEST_ID", -1);

                    // Pasar los datos al fragmento nav_home
                    Bundle bundle = new Bundle();
                    bundle.putInt("USER_ID", usuariaId);
                    bundle.putString("USER_NAME", nombre);
                    bundle.putString("USER_EMAIL", email);
                    bundle.putInt("GEST_ID", gestanteId);
                    navController.navigate(R.id.nav_home, bundle);
                } else if (id == R.id.nav_recordatorios) {
                    // Recuperar los datos del intent
                    Intent intent = getIntent();
                    int usuariaId = intent.getIntExtra("USER_ID", -1);
                    int gestanteId = intent.getIntExtra("GEST_ID", -1);

                    // Pasar los datos al fragmento nav_recordatorio
                    Bundle bundle = new Bundle();
                    bundle.putInt("USER_ID", usuariaId);
                    bundle.putInt("GEST_ID", gestanteId);
                    navController.navigate(R.id.nav_recordatorios, bundle);
                } else if (id == R.id.nav_historial) {
                    // Recuperar los datos del intent
                    Intent intent = getIntent();
                    int gestanteId = intent.getIntExtra("GEST_ID", -1);

                    // Pasar los datos al fragmento nav_historial
                    Bundle bundle = new Bundle();
                    bundle.putInt("GEST_ID", gestanteId);
                    navController.navigate(R.id.nav_historial, bundle);
                } else if (id == R.id.nav_reportes) {
                    // Recuperar los datos del intent
                    Intent intent = getIntent();
                    int gestanteId = intent.getIntExtra("GEST_ID", -1);

                    // Pasar los datos al fragmento nav_reportes
                    Bundle bundle = new Bundle();
                    bundle.putInt("GEST_ID", gestanteId);
                    navController.navigate(R.id.nav_reportes, bundle);
                } else if (id == R.id.nav_Bienvenido) {
                    // Recuperar los datos del intent
                    Intent intent = getIntent();
                    int gestanteId = intent.getIntExtra("GEST_ID", -1);

                    // Pasar los datos al fragmento nav_Bienvenido
                    Bundle bundle = new Bundle();
                    bundle.putInt("GEST_ID", gestanteId);
                    navController.navigate(R.id.nav_Bienvenido, bundle);
                } else if (id == R.id.nav_cerrar_sesion) {
                    showLogoutConfirmationDialog();
                } else if (id == R.id.nav_personalizar_notificacion) {
                    navController.navigate(R.id.nav_personalizar_notificacion);
                }else if (id == R.id.nav_informativo) {
                    navController.navigate(R.id.nav_informativo);
                } else {
                    navController.navigate(id);
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // Pasar los datos al fragmento Bienvenido directamente al iniciar MenuActivity
        Intent intent = getIntent();
        int gestanteId = intent.getIntExtra("GEST_ID", -1);
        int usuariaId = intent.getIntExtra("USER_ID", -1);
        Bundle bundle = new Bundle();
        bundle.putInt("GEST_ID", gestanteId);
        bundle.putInt("USER_ID", usuariaId);
        navController.navigate(R.id.nav_Bienvenido, bundle);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Está segura que desea cerrar sesión?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logoutAndReturnToMainActivity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Solo cerrar el diálogo
                    }
                })
                .create() // Asegúrate de crear el AlertDialog
                .show(); // Y luego mostrarlo
    }


    private void logoutAndReturnToMainActivity() {
        // Eliminar datos de sesión o autenticación
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear(); // Limpiar todas las preferencias compartidas
        editor.apply();

        // Regresar a MainActivity
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpiar la pila de actividades
        startActivity(intent);

        // Opcional: Finalizar la actividad actual
        finish();
    }
}





