package com.example.everyday.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class TaskAdapter extends ListAdapter<Task, TaskAdapter.TaskViewHolder> {

    // Конструктор адаптера, который использует DiffUtil для оптимизации обновлений
    public TaskAdapter() {
        super(DIFF_CALLBACK); // Передаем DiffUtil для отслеживания изменений
    }

    // Создание нового ViewHolder для каждого элемента списка
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Инфлейтируем layout для одного элемента списка (например, simple_list_item_2)
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new TaskViewHolder(itemView);
    }

    // Привязка данных к элементам интерфейса
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        // Получаем задачу по позиции
        Task task = getItem(position);

        // Привязываем данные к элементам списка
        holder.taskDescription.setText(task.getDescription());
        holder.taskTime.setText(String.format("%02d:%02d", task.getHour(), task.getMinute()));
    }

    // Используем DiffUtil для отслеживания изменений в списке
    private static final DiffUtil.ItemCallback<Task> DIFF_CALLBACK = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            // Проверяем, одинаковые ли элементы (например, по ID задачи)
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            // Проверяем, одинаковое ли содержимое элемента (например, по содержимому задачи)
            return oldItem.equals(newItem);
        }
    };

    // Внутренний класс ViewHolder для одного элемента списка
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        // Элементы пользовательского интерфейса
        public TextView taskDescription;
        public TextView taskTime;

        // Конструктор ViewHolder
        public TaskViewHolder(View itemView) {
            super(itemView);
            taskDescription = itemView.findViewById(android.R.id.text1);  // описание задачи
            taskTime = itemView.findViewById(android.R.id.text2);  // время задачи
        }
    }
}
