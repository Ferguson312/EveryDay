package com.example.everyday.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.everyday.R;

import java.util.Calendar;

public class TaskAdapter extends ListAdapter<Task, TaskAdapter.TaskViewHolder> {

    private final TaskAdapterListener listener;

    public interface TaskAdapterListener {
        void onTaskStatusChanged(Task task);

        void onTaskDeleted(Task task); // Метод для удаления заметки
    }

    public TaskAdapter(TaskAdapterListener listener) {
        super(new TaskDiffCallback());
        this.listener = listener;
    }

    private static class TaskDiffCallback extends DiffUtil.ItemCallback<Task> {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.equals(newItem);
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = getItem(position);
        holder.bind(task);

        // Обработчик нажатия на кнопку "Удалить"
        holder.buttonDelete2.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTaskDeleted(task);
            }
        });
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox checkBoxCompleted;
        private final TextView textViewDescription;
        private final TextView textViewDate;
        private final TextView textViewTime;
        private final Button buttonDelete2;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxCompleted = itemView.findViewById(R.id.checkBoxCompleted);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            buttonDelete2 = itemView.findViewById(R.id.buttonDelete2);

            checkBoxCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
                Task task = new Task(getItem(getAdapterPosition()));
                task.setDone(isChecked);
                listener.onTaskStatusChanged(task);
            });
        }

        @SuppressLint("ResourceAsColor")
        public void bind(Task task) {
            textViewDescription.setText(task.getDescription());
            String date = task.getDay() + "/" + task.getMonth() + "/" + task.getYear();
            textViewDate.setText(date);
            String time = task.getHour() + ":" + String.format("%02d", task.getMinute());
            textViewTime.setText(time);
            checkBoxCompleted.setChecked(task.isDone());

            // Показать кнопку удаления только для невыполненных задач
            buttonDelete2.setVisibility(task.isDone() ? View.GONE : View.VISIBLE);

            // Проверка на выполненность задачи
            if (task.isDone()) {
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.light_green));
            }
            // Проверка на просроченность задачи
            else if (isTaskOverdue(task)) {
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.light_red));
            } else {
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
            }
        }

        private boolean isTaskOverdue(Task task) {
            Calendar now = Calendar.getInstance();
            Calendar taskTime = Calendar.getInstance();
            taskTime.set(task.getYear(), task.getMonth() - 1, task.getDay(), task.getHour(), task.getMinute());

            return !task.isDone() && now.after(taskTime); // Просрочено и не выполнено
        }

    }

    private boolean isTaskOverdue(Task task) {
        Calendar now = Calendar.getInstance();
        Calendar taskTime = Calendar.getInstance();
        taskTime.set(task.getYear(), task.getMonth() - 1, task.getDay(), task.getHour(), task.getMinute());

        return !task.isDone() && now.after(taskTime); // Просрочено и не выполнено
    }
}
