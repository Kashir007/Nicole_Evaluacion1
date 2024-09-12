package com.niko.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Este es tu layout principal

        // Vincular el botón "Agregar Tarea"
        Button agregarTareaButton = findViewById(R.id.button);
        agregarTareaButton.setOnClickListener(v -> {
            // Abrir la actividad para agregar una tarea
            Intent intent = new Intent(MainActivity.this, AddTareas.class);
            startActivity(intent);
        });

        // Vincular el botón "Ver tareas"
        Button verTareasButton = findViewById(R.id.button2);
        verTareasButton.setOnClickListener(v -> {
            // Abrir la actividad de tareas guardadas
            Intent intent = new Intent(MainActivity.this, Tareas.class);
            startActivity(intent);
        });


        // Vincular el botón "Salir"
        Button salirButton = findViewById(R.id.button3);
        salirButton.setOnClickListener(v -> {
            // Cerrar la aplicación
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


            finish();
        });
    }
}

