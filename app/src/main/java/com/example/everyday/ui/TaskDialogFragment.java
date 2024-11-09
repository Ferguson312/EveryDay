package com.example.everyday.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.everyday.R;

import java.util.Calendar;

public class TaskDialogFragment extends DialogFragment {

    private EditText editTextTask;
    private EditText editTextDate;
    private EditText editTextTime;
    private Button buttonSaveTask;
    private Button buttonSelectDate;
    private Button buttonSelectTime;

    private TaskDialogListener listener;

    public interface TaskDialogListener {
        void onTaskSaved(Task task);
    }

    // Метод для установки слушателя
    public void setTaskDialogListener(TaskDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_task, container, false);

        editTextTask = rootView.findViewById(R.id.editTextTask);
        editTextDate = rootView.findViewById(R.id.editTextDate);
        editTextTime = rootView.findViewById(R.id.editTextTime);
        buttonSaveTask = rootView.findViewById(R.id.buttonSaveTask);
        buttonSelectDate = rootView.findViewById(R.id.buttonSelectDate);
        buttonSelectTime = rootView.findViewById(R.id.buttonSelectTime);

        // Настройка масок ввода для даты и времени
        setupDateMask();
        setupTimeMask();

        buttonSaveTask.setOnClickListener(v -> {
            String taskDescription = editTextTask.getText().toString().trim();
            if (taskDescription.isEmpty()) {
                Toast.makeText(getContext(), "Введите описание задачи", Toast.LENGTH_SHORT).show();
                return;
            }

            String dateText = editTextDate.getText().toString().trim();
            String timeText = editTextTime.getText().toString().trim();

            int day = 0, month = 0, year = 0;
            if (!dateText.isEmpty()) {
                String[] dateParts = dateText.split("\\.");
                if (dateParts.length == 3) {
                    try {
                        day = Integer.parseInt(dateParts[0]);
                        month = Integer.parseInt(dateParts[1]) - 1; // Месяцы начинаются с 0
                        year = Integer.parseInt(dateParts[2]);

                        if (!isValidDate(year, month, day)) {
                            Toast.makeText(getContext(), "Некорректная дата. Пожалуйста, введите правильную дату.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Некорректный формат даты. Пожалуйста, используйте формат ДД.ММ.ГГГГ.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(getContext(), "Некорректный формат даты. Пожалуйста, используйте формат ДД.ММ.ГГГГ.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            int hour = 0, minute = 0;
            if (!timeText.isEmpty()) {
                String[] timeParts = timeText.split(":");
                if (timeParts.length == 2) {
                    try {
                        hour = Integer.parseInt(timeParts[0]);
                        minute = Integer.parseInt(timeParts[1]);

                        if (!isValidTime(hour, minute)) {
                            Toast.makeText(getContext(), "Некорректное время. Пожалуйста, введите время в формате ЧЧ:ММ (00:00 - 23:59).", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Некорректный формат времени. Пожалуйста, используйте формат ЧЧ:ММ.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(getContext(), "Некорректный формат времени. Пожалуйста, используйте формат ЧЧ:ММ.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // Создаем объект задачи с введенной датой и временем
            Task newTask = new Task(taskDescription, year, month, day, hour, minute);

            // Передаем задачу обратно в активность или фрагмент
            if (listener != null) {
                listener.onTaskSaved(newTask);
            }

            // Закрываем диалог
            dismiss();
        });

        buttonSelectDate.setOnClickListener(v -> showDatePickerDialog());
        buttonSelectTime.setOnClickListener(v -> showTimePickerDialog());

        return rootView;
    }

    // Маска для ввода даты (ДД.ММ.ГГГГ)
    private void setupDateMask() {
        editTextDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
                String input = charSequence.toString();
                if (input.length() == 2 || input.length() == 5) {
                    editTextDate.append(".");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    // Маска для ввода времени (ЧЧ:ММ)
    private void setupTimeMask() {
        editTextTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
                String input = charSequence.toString();
                if (input.length() == 2) {
                    editTextTime.append(":");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    // Проверка на валидность даты
    private boolean isValidDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.get(Calendar.YEAR) == year &&
                calendar.get(Calendar.MONTH) == month &&
                calendar.get(Calendar.DAY_OF_MONTH) == day;
    }

    // Проверка на валидность времени
    private boolean isValidTime(int hour, int minute) {
        return hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59;
    }

    // Показать диалог для выбора даты
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year, month, dayOfMonth) -> {
                    // Устанавливаем выбранную дату в поле ввода
                    editTextDate.setText(String.format("%02d.%02d.%04d", dayOfMonth, month + 1, year));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    // Показать диалог для выбора времени
    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                (view, hourOfDay, minute) -> {
                    // Устанавливаем выбранное время в поле ввода
                    editTextTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }
}
