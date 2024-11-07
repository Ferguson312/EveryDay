package com.example.everyday.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

// ViewModel для хранения списка задач
public class TaskViewModel extends ViewModel {

    // MutableLiveData для хранения списка задач
    private final MutableLiveData<List<Task>> taskListLiveData = new MutableLiveData<>(new ArrayList<>());

    // Получить список задач
    public LiveData<List<Task>> getTaskList() {
        return taskListLiveData;
    }

    // Метод для добавления задачи
    public void addTask(Task task) {
        List<Task> currentTasks = taskListLiveData.getValue(); // Получаем текущий список задач
        if (currentTasks != null) {
            currentTasks.add(task);  // Добавляем новую задачу
            taskListLiveData.setValue(currentTasks); // Обновляем LiveData с новым списком
        }
    }
//    public void addTask(Task task) {
//        List<Task> currentTasks = taskListLiveData.getValue(); // Получаем текущий список задач
//        if (currentTasks != null) {
//            currentTasks.add(task); // Добавляем новую задачу
//            taskListLiveData.setValue(new ArrayList<>(currentTasks)); // Обновляем LiveData с новым списком
//        }
//    }
}
