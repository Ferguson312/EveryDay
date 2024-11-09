package com.example.everyday.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class TaskViewModel extends ViewModel {
    private final MutableLiveData<List<Task>> taskList = new MutableLiveData<>();

    public TaskViewModel() {
        // Инициализируем список задач, если это необходимо
        taskList.setValue(new ArrayList<>());
    }

    public LiveData<List<Task>> getTaskList() {
        return taskList;
    }

    // Метод для добавления задачи
    public void addTask(Task task) {
        List<Task> currentTasks = taskList.getValue();
        if (currentTasks != null) {
            currentTasks.add(task);
            taskList.setValue(currentTasks); // Обновляем LiveData
        }
    }

    // Метод для обновления задачи
    public void updateTask(Task updatedTask) {
        List<Task> currentTasks = taskList.getValue();
        if (currentTasks != null) {
            for (int i = 0; i < currentTasks.size(); i++) {
                Task task = currentTasks.get(i);
                if (task.getId().equals(updatedTask.getId())) {
                    currentTasks.set(i, updatedTask); // Обновляем задачу в списке
                    break;
                }
            }
            taskList.setValue(currentTasks); // Обновляем LiveData
        }
    }
}
