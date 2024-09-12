package com.niko.splashscreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.TareaViewHolder> {

    private List<Tarea> tareaList;
    private Set<Integer> selectedTasks = new HashSet<>(); // Para almacenar las tareas seleccionadas
    private boolean isSelectionMode = false; // Modo selección
    private Tareas activity; // Referencia al Activity

    // Constructor actualizado
    public TareaAdapter(List<Tarea> tareaList, Tareas activity) {
        this.tareaList = tareaList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarea, parent, false);
        return new TareaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        Tarea tarea = tareaList.get(position);
        holder.tituloTextView.setText(tarea.getTitulo());
        holder.descripcionTextView.setText(tarea.getDescripcion());
        holder.prioridadTextView.setText(tarea.getPrioridad());

        // Manejar la visibilidad del CheckBox según el modo de selección
        if (isSelectionMode) {
            holder.checkBox.setVisibility(View.VISIBLE); // Mostrar el CheckBox en modo selección
            holder.checkBox.setChecked(selectedTasks.contains(position)); // Marcar si está seleccionado
        } else {
            holder.checkBox.setVisibility(View.GONE); // Ocultar el CheckBox si no estamos en modo selección
            holder.checkBox.setChecked(false); // Desmarcar cuando se oculta
        }

        // Manejar la selección del CheckBox
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedTasks.add(position); // Agregar tarea seleccionada
            } else {
                selectedTasks.remove(position); // Remover tarea si se deselecciona
            }
        });

        // Manejar el evento de mantener presionado para activar el modo selección
        holder.itemView.setOnLongClickListener(v -> {
            isSelectionMode = true; // Activar el modo de selección
            notifyDataSetChanged(); // Refrescar la lista para mostrar los CheckBox
            activity.mostrarBotonEliminar(); // Llamar al método del Activity para mostrar el botón
            Toast.makeText(v.getContext(), "Modo de selección activado", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return tareaList.size();
    }

    // Eliminar las tareas seleccionadas
    public void eliminarTareasSeleccionadas() {
        // Crear una nueva lista temporal para las tareas que no han sido seleccionadas
        List<Tarea> tareasRestantes = new ArrayList<>();

        for (int i = 0; i < tareaList.size(); i++) {
            if (!selectedTasks.contains(i)) {
                tareasRestantes.add(tareaList.get(i)); // Agregar las tareas que no están seleccionadas
            }
        }

        // Actualizar la lista original con las tareas restantes
        tareaList = tareasRestantes;
        selectedTasks.clear(); // Limpiar las tareas seleccionadas
        isSelectionMode = false; // Desactivar el modo selección
        notifyDataSetChanged(); // Refrescar el adaptador para mostrar los cambios

        // Eliminar las tareas de SharedPreferences pasando las tareas restantes
        activity.eliminarTareasDeSharedPreferences(tareasRestantes);
    }


    public class TareaViewHolder extends RecyclerView.ViewHolder {
        TextView tituloTextView;
        TextView descripcionTextView;
        TextView prioridadTextView;
        CheckBox checkBox;

        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTextView = itemView.findViewById(R.id.tituloTextView);
            descripcionTextView = itemView.findViewById(R.id.descripcionTextView);
            prioridadTextView = itemView.findViewById(R.id.prioridadTextView);
            checkBox = itemView.findViewById(R.id.checkBox); // CheckBox en cada tarea
        }
    }
}


