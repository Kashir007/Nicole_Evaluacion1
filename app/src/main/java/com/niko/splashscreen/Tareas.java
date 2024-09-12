package com.niko.splashscreen;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class Tareas extends AppCompatActivity {

    private RecyclerView recyclerViewTareas;
    private TareaAdapter adapter;
    private List<Tarea> tareaList;
    private Button buttonEliminar;

    @Override

    protected void onResume() {
        super.onResume();
        // Recargar las tareas cuando se vuelve a la actividad
        cargarTareas();
        adapter.notifyDataSetChanged(); // Actualizar el adaptador con los cambios
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tareas);

        recyclerViewTareas = findViewById(R.id.recyclerViewTareas);
        tareaList = new ArrayList<>(); // Asegúrate de inicializar la lista aquí
        buttonEliminar = findViewById(R.id.buttonEliminar);

        // Configurar el RecyclerView con el adaptador
        adapter = new TareaAdapter(tareaList, this);  // Inicializa el adaptador antes de cargar las tareas
        recyclerViewTareas.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTareas.setAdapter(adapter);

        // Cargar las tareas desde SharedPreferences
        cargarTareas(); // Mueve esta línea después de la inicialización del adapter

        // Listener para eliminar tareas seleccionadas
        buttonEliminar.setOnClickListener(v -> {
            adapter.eliminarTareasSeleccionadas();
            buttonEliminar.setVisibility(View.GONE); // Ocultar el botón después de eliminar
        });
    }

    // Método para mostrar el botón de eliminar desde el adaptador
    public void mostrarBotonEliminar() {
        buttonEliminar.setVisibility(View.VISIBLE); // Mostrar el botón cuando se active el modo selección
    }

    public void eliminarTareasDeSharedPreferences(List<Tarea> tareasRestantes) {
        SharedPreferences sharedPreferences = getSharedPreferences("TareasPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Borrar todos los datos almacenados

        // Guardar solo las tareas restantes en los SharedPreferences
        editor.putInt("tarea_id", tareasRestantes.size()); // Actualizar el número de tareas restantes
        for (int i = 0; i < tareasRestantes.size(); i++) {
            Tarea tarea = tareasRestantes.get(i);
            String tareaData = tarea.getTitulo() + ";" + tarea.getDescripcion() + ";" + tarea.getPrioridad();
            editor.putString("tarea_" + i, tareaData); // Guardar la tarea
        }

        editor.apply(); // Aplicar los cambios
    }



    private void cargarTareas() {
        SharedPreferences sharedPreferences = getSharedPreferences("TareasPrefs", MODE_PRIVATE);
        int tareaId = sharedPreferences.getInt("tarea_id", 0);  // Obtener la cantidad de tareas guardadas

        // Si no hay tareas guardadas, no intentes cargar nada
        if (tareaId == 0) {
            return;
        }

        tareaList.clear(); // Limpiar la lista de tareas antes de cargar las nuevas

        for (int i = 0; i < tareaId; i++) {
            String tarea = sharedPreferences.getString("tarea_" + i, null);
            if (tarea != null) {
                // Separar los datos de la tarea guardada (título, descripción, prioridad)
                String[] tareaData = tarea.split(";");
                if (tareaData.length == 3) {
                    String titulo = tareaData[0];
                    String descripcion = tareaData[1];
                    String prioridad = tareaData[2];

                    // Agregar la tarea a la lista
                    tareaList.add(new Tarea(titulo, descripcion, prioridad));
                }
            }
        }

        // Notifica al adaptador que los datos han cambiado
        if (adapter != null) {
            adapter.notifyDataSetChanged(); // Refrescar el adaptador para mostrar los datos actualizados
        }
    }

}


