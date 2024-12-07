package com.example.everyday.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.everyday.TaskRepository;
import com.example.everyday.databinding.FragmentHomeBinding;
import com.example.everyday.ui.Task;
import com.example.everyday.ui.TaskAdapter;
import com.example.everyday.ui.TaskDialogFragment;
import com.example.everyday.ui.TaskViewModel;

import java.util.List;

public class HomeFragment extends Fragment implements TaskDialogFragment.TaskDialogListener, TaskAdapter.TaskAdapterListener {

    private FragmentHomeBinding binding;
    private TaskAdapter taskAdapter;
    private TaskViewModel taskViewModel; // ViewModel для задач

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Инициализация привязки фрагмента
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        binding.recyclerViewTasks.setLayoutManager(new LinearLayoutManager(requireContext()));
        taskAdapter = new TaskAdapter(this);
        binding.recyclerViewTasks.setAdapter(taskAdapter);

        // Получаем ViewModel для задач
        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);


        // Подписываемся на изменения в списке задач
        taskViewModel.getTaskList().observe(getViewLifecycleOwner(), tasks -> {

            taskAdapter.submitList(tasks);
            // Обновляем RecyclerView
        });

        return binding.getRoot(); // Возвращаем корневое представление
    }

    // Метод для открытия диалога добавления задачи
    public void openTaskDialog() {
        TaskDialogFragment taskDialog = new TaskDialogFragment();
        taskDialog.setTaskDialogListener(this);  // Устанавливаем слушателя для диалога
        taskDialog.show(getParentFragmentManager(), "TaskDialogFragment");
    }

    @Override
    public void onTaskSaved(Task task) {
        if (task != null) {
            taskViewModel.addTask(task);
        }
    }

    @Override
    public void onTaskStatusChanged(Task task) {
        if (task != null) {
            taskViewModel.updateTask(task);
        }
    }

    @Override
    public void onTaskDeleted(Task task) {
        if (task != null) {
            taskViewModel.removeTask(task);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}