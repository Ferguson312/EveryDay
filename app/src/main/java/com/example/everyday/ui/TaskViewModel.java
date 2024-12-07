package com.example.everyday.ui;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.everyday.TaskRepository;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
        List<Task> currentTasks = new ArrayList<>(taskList.getValue() != null ? taskList.getValue() : Collections.emptyList());
        int index = Collections.binarySearch(currentTasks, task);
        if (index < 0) {
            index = -index - 1;
        }
        currentTasks.add(index, task);
        taskList.postValue(currentTasks);
        scheduleNotifications(task);
    }

    private void scheduleNotifications(Task task) {
        // Первое уведомление за один день до даты задачи
        scheduleNotification(task, "first_notification_" + task.getId(), generateNotificationId(task.getId(), 1), -1, Calendar.DAY_OF_MONTH,
                "Напоминание о задаче", "Не забудь выполнить свою задачу: " + task.getDescription());

        // Второе уведомление за один час до даты задачи
        scheduleNotification(task, "second_notification_" + task.getId(), generateNotificationId(task.getId(), 2), -1, Calendar.HOUR_OF_DAY,
                "Поспеши", "У тебя остался всего час чтобы выполнить задачу: " + task.getDescription());
    }

    private int generateNotificationId(String taskId, int notificationNumber) {
        // Используем комбинацию идентификатора задачи и порядкового номера уведомления
        return (taskId + "_" + notificationNumber).hashCode();
    }

    private void scheduleNotification(Task task, String tag, int notificationId, int amount, int field, String title, String text) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(task.getYear(), task.getMonth() - 1, task.getDay(), task.getHour(), task.getMinute());
        calendar.add(field, amount);

        long triggerTime = calendar.getTimeInMillis();
        if (triggerTime < System.currentTimeMillis()) {
            return;
        }

        Data inputData = new Data.Builder()
                .putString("task_id", task.getId())
                .putInt("notification_id", notificationId)
                .putString("notification_title", title)
                .putString("notification_text", text)
                .build();

        OneTimeWorkRequest notificationRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInputData(inputData)
                .setInitialDelay(triggerTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addTag(tag) // Добавляем уникальный тег для отмены работы
                .build();

        WorkManager.getInstance(getApplication())
                .enqueue(notificationRequest);
    }
    public void removeNotifications(Task task) {
        // Отмена работ в WorkManager
        WorkManager workManager = WorkManager.getInstance(getApplication());
        workManager.cancelAllWorkByTag("first_notification_" + task.getId());
        workManager.cancelAllWorkByTag("second_notification_" + task.getId());

        // Отмена уведомлений
        NotificationManager notificationManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(generateNotificationId(task.getId(), 1)); // Отменяем первое уведомление
        notificationManager.cancel(generateNotificationId(task.getId(), 2)); // Отменяем второе уведомление
    }


    // Метод для обновления задачи
    public void updateTask(Task updatedTask) {
        List<Task> currentTasks = new ArrayList<>(Objects.requireNonNull(taskList.getValue()));
        for (int i = 0; i < currentTasks.size(); i++) {
            Task task = currentTasks.get(i);
            if (task.getId().equals(updatedTask.getId())) {

                boolean isNowDone = updatedTask.isDone();

                // Если задача была выполнена и теперь не выполнена, создаем уведомление
                if (!isNowDone) {
                    scheduleNotifications(updatedTask);
                }
                // Если задача была не выполнена и теперь выполнена, отменяем уведомление
                else if (isNowDone) {
                    removeNotifications(updatedTask);
                }

                repository.updateTask(updatedTask);
                currentTasks.set(i, updatedTask); // Обновляем задачу в списке
                break;
            }
        }
        taskList.setValue(currentTasks); // Обновляем LiveData
    }

    // Метод для удаления задачи
    public void removeTask(Task task) {
        List<Task> currentTasks = new ArrayList<>(Objects.requireNonNull(taskList.getValue()));
        // Удаляем уведомление
        removeNotifications(task);

        // Удаляем задачу из репозитория
        repository.deleteTask(task.getId());
        currentTasks.removeIf(t -> t.getId().equals(task.getId()));
        taskList.setValue(currentTasks); // Обновляем LiveData
    }
}