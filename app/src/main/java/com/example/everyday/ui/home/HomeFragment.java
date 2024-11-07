package com.example.everyday.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.everyday.databinding.FragmentHomeBinding;
import com.example.everyday.ui.Task;
import com.example.everyday.ui.TaskAdapter;
import com.example.everyday.ui.TaskDialogFragment;
import com.example.everyday.ui.TaskViewModel;

import java.util.List;

public class HomeFragment extends Fragment implements TaskDialogFragment.TaskDialogListener {

    private FragmentHomeBinding binding;
    private TaskAdapter taskAdapter;
    private TaskViewModel taskViewModel; // ViewModel для задач

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Получаем ViewModel для задач
        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        // Инициализация RecyclerView
        RecyclerView recyclerViewTasks = binding.recyclerViewTasks;
        taskAdapter = new TaskAdapter();
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewTasks.setAdapter(taskAdapter);

        // Подписываемся на изменения в списке задач
        taskViewModel.getTaskList().observe(getViewLifecycleOwner(), tasks -> {
            taskAdapter.submitList(tasks); // Обновляем RecyclerView
        });

        return root;
    }

//    @Override
//    public void onTaskSaved(Task task) {
//        // Добавляем новую задачу в ViewModel
//        taskViewModel.addTask(task);
//    }
@Override
public void onTaskSaved(Task task) {
    taskViewModel.addTask(task);  // Добавляем задачу в ViewModel
    // Этот код гарантирует, что задача отобразится сразу
    //taskAdapter.submitList(taskViewModel.getTaskList().getValue());
    new Handler(Looper.getMainLooper()).postDelayed(() -> {
        taskAdapter.submitList(taskViewModel.getTaskList().getValue());
    }, 500); // Полсекунды задержки
}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

