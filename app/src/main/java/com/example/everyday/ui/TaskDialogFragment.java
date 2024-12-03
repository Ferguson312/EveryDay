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

    public void setTaskDialogListener(TaskDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        // Маски для ввода даты и времени
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
                        month = Integer.parseInt(dateParts[1]);
                        year = Integer.parseInt(dateParts[2]);
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Некорректный формат даты", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }

            int hour = 0, minute = 0;
            if (!timeText.isEmpty()) {
                String[] timeParts = timeText.split(":");
                if (timeParts.length == 2) {
                    try {
                        hour = Integer.parseInt(timeParts[0]);
                        minute = Integer.parseInt(timeParts[1]);
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Некорректный формат времени", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }

            Task newTask = new Task(taskDescription, year, month, day, hour, minute);

            if (listener != null) {
                listener.onTaskSaved(newTask);
            }
            dismiss();
        });

        buttonSelectDate.setOnClickListener(v -> showDatePickerDialog());
        buttonSelectTime.setOnClickListener(v -> showTimePickerDialog());

        return rootView;
    }

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

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year, month, dayOfMonth) -> editTextDate.setText(String.format("%02d.%02d.%04d", dayOfMonth, month + 1, year)),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                (view, hourOfDay, minute) -> editTextTime.setText(String.format("%02d:%02d", hourOfDay, minute)),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }
}
