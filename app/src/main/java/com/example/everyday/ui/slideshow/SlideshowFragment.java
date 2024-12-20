package com.example.everyday.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.everyday.databinding.FragmentSlideshowBinding;
import com.example.everyday.ui.Task;
import com.example.everyday.ui.TaskAdapter;
import com.example.everyday.ui.TaskViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SlideshowFragment extends Fragment implements TaskAdapter.TaskAdapterListener {

    private FragmentSlideshowBinding binding;
    private TaskViewModel taskViewModel; // Подключаем ViewModel
    private TaskAdapter taskAdapter;
    private String selectedDate; // Выбранная дата

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Инициализация ViewModel
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // Установка текущей даты как выбранной по умолчанию
        selectedDate = getCurrentDate();

        // Инициализация CalendarView
        CalendarView calendarView = binding.calendarView;
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            updateTaskList(); // Обновляем список задач для выбранной даты
        });

        // Инициализация RecyclerView
        binding.taskRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskAdapter = new TaskAdapter(this);
        binding.taskRecyclerView.setAdapter(taskAdapter);

        // Загрузка задач для текущей даты
        updateTaskList();

        return root;
    }

    private String getCurrentDate() {
        // Получение текущей даты в формате dd/MM/yyyy
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(new Date());
    }

    private void updateTaskList() {
        // Преобразуем дату в компоненты для поиска
        String[] dateParts = selectedDate.split("/");
        int day = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);

        // Запрашиваем задачи для выбранной даты из ViewModel
        taskViewModel.getTasksForDate(year, month, day).observe(getViewLifecycleOwner(), tasks -> {
            if (tasks != null) {
                taskAdapter.submitList(new ArrayList<>(tasks));
                if (tasks.isEmpty()) {
                    Toast.makeText(getContext(), "Нет задач на " + selectedDate, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onTaskStatusChanged(Task task) {
        // Обновляем задачу в ViewModel
        taskViewModel.updateTask(task);
    }

    @Override
    public void onTaskDeleted(Task task) {
        // Удаляем задачу через ViewModel
        taskViewModel.removeTask(task);
    }

    public void addTask(Task newTask) {
        // Добавляем задачу через ViewModel
        taskViewModel.addTask(newTask);
        updateTaskList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
