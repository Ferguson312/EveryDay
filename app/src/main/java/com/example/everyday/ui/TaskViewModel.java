package com.example.everyday.ui;
import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.everyday.TaskRepository;


import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TaskViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Task>> taskList = new MutableLiveData<>();
    private final TaskRepository repository;

    public TaskViewModel(Application application) {
        super(application);
        repository = new TaskRepository(application); // Используем application как Context
        taskList.setValue(repository.getAllTasks());
    }

    public LiveData<List<Task>> getTaskList() {
        return taskList;
    }

    // Метод для добавления задачи
    public void addTask(Task task) {
        repository.addTask(task);
        List<Task> currentTasks = taskList.getValue();
        if (currentTasks != null) {
            currentTasks.add(task);
            taskList.setValue(currentTasks);


            scheduleNotification(task);
        }
    }

    private void scheduleNotification(Task task) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(task.getYear(), task.getMonth() - 1, task.getDay(), task.getHour(), task.getMinute());
        calendar.add(Calendar.DAY_OF_MONTH, -1);

        long triggerTime = calendar.getTimeInMillis();
        if (triggerTime < System.currentTimeMillis()) {

            return;
        }

        Data inputData = new Data.Builder()
                .putString("task_id", task.getId())
                .build();

        OneTimeWorkRequest notificationRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInputData(inputData)
                .setInitialDelay(triggerTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(getApplication())
                .enqueue(notificationRequest);
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

    // Метод для удаления задачи
    public void removeTask(Task task) {
        List<Task> currentTasks = taskList.getValue();
        if (currentTasks != null) {
            currentTasks.removeIf(t -> t.getId().equals(task.getId()));
            taskList.setValue(currentTasks); // Обновляем LiveData
        }
    }
}