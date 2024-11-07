package com.example.everyday.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.everyday.R;

public class TaskDialogFragment extends DialogFragment {

    private EditText editTextTask;
    private TimePicker timePicker;
    private Button buttonSaveTask;

    private TaskDialogListener listener;

    // Интерфейс для передачи данных обратно в HomeFragment
    public interface TaskDialogListener {
        void onTaskSaved(Task task);
    }

    // Конструктор с передачей слушателя (HomeFragment)
    public TaskDialogFragment(TaskDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Инфлейтируем layout для диалога
        View rootView = inflater.inflate(R.layout.fragment_add_task, container, false);

        // Инициализация UI элементов
        editTextTask = rootView.findViewById(R.id.editTextTask);
        timePicker = rootView.findViewById(R.id.timePicker);
        buttonSaveTask = rootView.findViewById(R.id.buttonSaveTask);

        // Устанавливаем слушатель на кнопку "Сохранить"
        buttonSaveTask.setOnClickListener(v -> {
            String taskDescription = editTextTask.getText().toString().trim();
            if (taskDescription.isEmpty()) {
                Toast.makeText(getContext(), "Введите описание задачи", Toast.LENGTH_SHORT).show();
                return;
            }

            // Получаем выбранное время из TimePicker
            int hour = timePicker.getCurrentHour();
            int minute = timePicker.getCurrentMinute();

            // Создаём объект задачи
            Task newTask = new Task(taskDescription, hour, minute);

            // Передаём задачу обратно в HomeFragment
            listener.onTaskSaved(newTask);

            // Закрываем диалог
            dismiss();
        });

        return rootView;
    }
}
