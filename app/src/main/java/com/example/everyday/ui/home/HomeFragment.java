package com.example.everyday.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.everyday.R;
import com.example.everyday.ui.Task;
import com.example.everyday.ui.TaskAdapter;
import com.example.everyday.ui.TaskViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private TaskViewModel taskViewModel;
    private TaskAdapter taskAdapter;
    private List<Task> allTasks;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerViewTasks = root.findViewById(R.id.recyclerViewTasks);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        taskAdapter = new TaskAdapter(new TaskAdapter.TaskAdapterListener() {
            @Override
            public void onTaskStatusChanged(Task task) {
                taskViewModel.updateTask(task);
            }

            @Override
            public void onTaskDeleted(Task task) {
                taskViewModel.removeTask(task);
            }
        });
        recyclerViewTasks.setAdapter(taskAdapter);

        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        taskViewModel.getTaskList().observe(getViewLifecycleOwner(), tasks -> {
            allTasks = new ArrayList<>(tasks);
            filterTasks(taskViewModel.getCurrentFilter()); // Показываем все задачи при загрузке
        });

        Button buttonShowAll = root.findViewById(R.id.buttonShowAll);
        Button buttonShowCompleted = root.findViewById(R.id.buttonShowCompleted);
        Button buttonShowUncompleted = root.findViewById(R.id.buttonShowUncompleted);

        buttonShowAll.setOnClickListener(v -> filterTasks(null));
        buttonShowCompleted.setOnClickListener(v -> filterTasks(true));
        buttonShowUncompleted.setOnClickListener(v -> filterTasks(false));

        return root;
    }

    private void filterTasks(Boolean isCompleted) {
        taskViewModel.setCurrentFilter(isCompleted); // Устанавливаем текущий фильтр
        List<Task> filteredTasks = new ArrayList<>();
        if (isCompleted == null) {
            filteredTasks.addAll(allTasks);
        } else {
            for (Task task : allTasks) {
                if (task.isDone() == isCompleted) {
                    filteredTasks.add(task);
                }
            }
        }
        taskAdapter.submitList(filteredTasks);
    }
}