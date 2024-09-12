package com.niko.splashscreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddTareas extends AppCompatActivity {

    private EditText editTextTitle, editTextText;
    private Spinner spinnerPrioridad;
    private Button buttonGuardar, buttonLimpiar, buttonVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tareas);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextText = findViewById(R.id.editTextText);
        spinnerPrioridad = findViewById(R.id.spinnerprioridad);
        buttonGuardar = findViewById(R.id.button6);
        buttonLimpiar = findViewById(R.id.button5); // Botón Limpiar
        buttonVolver = findViewById(R.id.button4);  // Botón Volver

        // Configurar el Spinner con las opciones de prioridad
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.prioridades_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPrioridad.setAdapter(adapter);

        // Listener para el botón "Guardar"
        buttonGuardar.setOnClickListener(v -> {
            String titulo = editTextTitle.getText().toString().trim();  // Eliminar espacios en blanco
            String descripcion = editTextText.getText().toString().trim();
            String importancia = spinnerPrioridad.getSelectedItem().toString();

            // Validar que los campos no estén vacíos
            if (titulo.isEmpty() || descripcion.isEmpty() || importancia.equals("Selecciona una opción")) {
                // Mostrar un mensaje de error
                Toast.makeText(AddTareas.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            } else {
                // Guardar tarea en SharedPreferences
                guardarTarea(titulo, descripcion, importancia);

                // Volver a la pantalla de tareas
                Intent intent = new Intent(AddTareas.this, Tareas.class);
                startActivity(intent);
            }
        });

        // Listener para el botón "Limpiar"
        buttonLimpiar.setOnClickListener(v -> {
            // Limpiar el contenido de los EditText
            editTextTitle.setText("");  // Limpiar el campo del título
            editTextText.setText("");   // Limpiar el campo de la descripción

            // Reiniciar el Spinner (colocar la primera opción)
            spinnerPrioridad.setSelection(0);  // Establecer la primera opción en el Spinner
        });

        // Listener para el botón "Volver"
        buttonVolver.setOnClickListener(v -> {
            // Volver al menú inicial
            Intent intent = new Intent(AddTareas.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void guardarTarea(String titulo, String descripcion, String importancia) {
        SharedPreferences sharedPreferences = getSharedPreferences("TareasPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int id = sharedPreferences.getInt("tarea_id", 0);  // Obtener el ID actual
        editor.putString("tarea_" + id, titulo + ";" + descripcion + ";" + importancia);  // Guardar tarea con ID único
        editor.putInt("tarea_id", id + 1);  // Incrementar el ID para la próxima tarea
        editor.apply();  // Aplicar los cambios
    }
}
