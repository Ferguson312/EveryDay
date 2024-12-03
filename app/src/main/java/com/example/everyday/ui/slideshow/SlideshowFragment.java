package com.example.everyday.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.everyday.TaskRepository;
import com.example.everyday.databinding.FragmentSlideshowBinding;
import com.example.everyday.ui.Task;
import com.example.everyday.ui.TaskAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlideshowFragment extends Fragment implements TaskAdapter.TaskAdapterListener {

    private FragmentSlideshowBinding binding;

    // Map для хранения задач по дате
    private final Map<String, List<Task>> taskMap = new HashMap<>();
    private TaskAdapter taskAdapter; // Адаптер для RecyclerView
    private String selectedDate; // Выбранная дата

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Установка текущей даты как выбранной по умолчанию
        selectedDate = getCurrentDate();

        // Инициализация CalendarView
        CalendarView calendarView = binding.calendarView;
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            updateTaskList(); // Обновляем список задач для выбранной даты
        });

        // Инициализация RecyclerView
        TaskRepository repository = new TaskRepository(requireContext());
        binding.taskRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskAdapter = new TaskAdapter(this, repository); // Передаём фрагмент как listener
        binding.taskRecyclerView.setAdapter(taskAdapter);

        // Загрузка задач для текущей даты
        updateTaskList();

        return root;
    }

    private String getCurrentDate() {
        // Получение текущей даты в формате dd/MM/yyyy
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(new java.util.Date());
    }

    private void updateTaskList() {
        // Получаем задачи для выбранной даты
        List<Task> tasks = taskMap.getOrDefault(selectedDate, new ArrayList<>());
        taskAdapter.submitList(new ArrayList<>(tasks)); // Устанавливаем новый список задач

        // Показ сообщения, если задач нет
        if (tasks.isEmpty()) {
            Toast.makeText(getContext(), "Нет задач на " + selectedDate, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTaskStatusChanged(Task task) {
        // Обновляем задачу в списке задач
        List<Task> tasks = taskMap.get(selectedDate);
        if (tasks != null) {
            int index = tasks.indexOf(task);
            if (index != -1) {
                tasks.set(index, task); // Обновляем задачу
                taskMap.put(selectedDate, tasks);
                taskAdapter.submitList(new ArrayList<>(tasks)); // Переприсваиваем список
            }
        }
    }

    @Override
    public void onTaskDeleted(Task task) {
        // Удаляем задачу из списка задач
        List<Task> tasks = taskMap.get(selectedDate);
        if (tasks != null) {
            tasks.remove(task);
            taskMap.put(selectedDate, tasks);
            taskAdapter.submitList(new ArrayList<>(tasks));
        }
    }

    public void addTask(Task newTask) {
        // Добавляем задачу к текущей дате
        List<Task> tasks = taskMap.getOrDefault(selectedDate, new ArrayList<>());
        tasks.add(newTask);
        taskMap.put(selectedDate, tasks);
        updateTaskList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}