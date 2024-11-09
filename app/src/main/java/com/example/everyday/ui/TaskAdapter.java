package com.example.everyday.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.everyday.R;

public class TaskAdapter extends ListAdapter<Task, TaskAdapter.TaskViewHolder> {

    private final TaskAdapterListener listener;

    public interface TaskAdapterListener {
        void onTaskStatusChanged(Task task);
    }

    public TaskAdapter(TaskAdapterListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false); // Используем layout для элемента списка
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = getItem(position); // Получаем текущую задачу
        holder.bind(task); // Привязываем данные к ViewHolder
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox checkBoxCompleted;  // Поменяли на checkBoxCompleted
        private final TextView textViewDescription;
        private final TextView textViewDate;
        private final TextView textViewTime;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxCompleted = itemView.findViewById(R.id.checkBoxCompleted); // Исправили id
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewTime = itemView.findViewById(R.id.textViewTime);

            checkBoxCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
                Task task = getItem(getAdapterPosition());
                task.setDone(isChecked); // Обновляем состояние задачи
                listener.onTaskStatusChanged(task); // Информируем об изменении
            });
        }

        public void bind(Task task) {
            textViewDescription.setText(task.getDescription()); // Отображаем описание задачи
            // Форматируем и отображаем дату и время
            String date = task.getDay() + "/" + task.getMonth() + "/" + task.getYear();
            textViewDate.setText(date);
            String time = task.getHour() + ":" + String.format("%02d", task.getMinute());
            textViewTime.setText(time);
            checkBoxCompleted.setChecked(task.isDone()); // Устанавливаем состояние флажка
        }
    }

    private static final DiffUtil.ItemCallback<Task> DIFF_CALLBACK = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.equals(newItem); // Проверка на равенство по ID или другим данным
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.isDone() == newItem.isDone(); // Сравниваем содержимое задачи
        }
    };
}

