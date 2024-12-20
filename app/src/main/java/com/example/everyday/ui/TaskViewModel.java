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
    private final MutableLiveData<List<Task>> allTasks = new MutableLiveData<>();
    private final TaskRepository repository;
    private Boolean currentFilter = null;

    public TaskViewModel(Application application) {
        super(application);
        repository = new TaskRepository(application);
        allTasks.setValue(repository.getAllTasks());
    }

    public LiveData<List<Task>> getTaskList() {
        return allTasks;
    }

    public void setCurrentFilter(Boolean filter) {
        this.currentFilter = filter;
    }

    public Boolean getCurrentFilter() {
        return currentFilter;
    }

    public LiveData<List<Task>> getTasksForDate(int year, int month, int day) {
        MutableLiveData<List<Task>> filteredTasks = new MutableLiveData<>();
        List<Task> allTasksList = allTasks.getValue();

        if (allTasksList != null) {
            List<Task> filteredList = new ArrayList<>();
            for (Task task : allTasksList) {
                if (task.getYear() == year && task.getMonth() == month && task.getDay() == day) {
                    filteredList.add(task);
                }
            }
            filteredTasks.setValue(filteredList);
        }
        return filteredTasks;
    }

    public void addTask(Task task) {
        List<Task> currentTasks = new ArrayList<>(Objects.requireNonNull(allTasks.getValue()));
        int index = Collections.binarySearch(currentTasks, task);
        if (index < 0) {
            index = -index - 1;
        }
        currentTasks.add(index, task);
        allTasks.postValue(currentTasks);
        scheduleNotifications(task);
        repository.addTask(task);
    }

    public void updateTask(Task updatedTask) {
        List<Task> currentTasks = new ArrayList<>(Objects.requireNonNull(allTasks.getValue()));
        for (int i = 0; i < currentTasks.size(); i++) {
            Task task = currentTasks.get(i);
            if (task.getId().equals(updatedTask.getId())) {
                boolean isNowDone = updatedTask.isDone();

                if (!isNowDone) {
                    scheduleNotifications(updatedTask);
                } else {
                    removeNotifications(updatedTask);
                }

                repository.updateTask(updatedTask);
                currentTasks.set(i, updatedTask);
                allTasks.setValue(currentTasks);
                break;
            }
        }
    }

    public void removeTask(Task task) {
        List<Task> currentTasks = new ArrayList<>(Objects.requireNonNull(allTasks.getValue()));
        removeNotifications(task);
        repository.deleteTask(task.getId());
        currentTasks.removeIf(t -> t.getId().equals(task.getId()));
        allTasks.postValue(currentTasks);
    }

    private void scheduleNotifications(Task task) {
        scheduleNotification(task, "first_notification_" + task.getId(), generateNotificationId(task.getId(), 1), -1, Calendar.DAY_OF_MONTH,
                "Напоминание о задаче", "Не забудь выполнить свою задачу: " + task.getDescription());

        scheduleNotification(task, "second_notification_" + task.getId(), generateNotificationId(task.getId(), 2), -1, Calendar.HOUR_OF_DAY,
                "Поспеши", "У тебя остался всего час чтобы выполнить задачу: " + task.getDescription());

        scheduleNotification(task, "third_notification_" + task.getId(), generateNotificationId(task.getId(), 3), 1, Calendar.HOUR_OF_DAY,
                "Задача просрочена", "Ты уже просрочил на 1 час задачу: " + task.getDescription());

        scheduleNotification(task, "fourth_notification_" + task.getId(), generateNotificationId(task.getId(), 4), 1, Calendar.DAY_OF_MONTH,
                "Задача просрочена", "Ты уже просрочил на 1 день задачу: " + task.getDescription());
    }

    private int generateNotificationId(String taskId, int notificationNumber) {
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
                .addTag(tag)
                .build();

        WorkManager.getInstance(getApplication()).enqueue(notificationRequest);
    }

    public void removeNotifications(Task task) {
        WorkManager workManager = WorkManager.getInstance(getApplication());
        workManager.cancelAllWorkByTag("first_notification_" + task.getId());
        workManager.cancelAllWorkByTag("second_notification_" + task.getId());
        workManager.cancelAllWorkByTag("third_notification_" + task.getId());
        workManager.cancelAllWorkByTag("fourth_notification_" + task.getId());

        NotificationManager notificationManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(generateNotificationId(task.getId(), 1));
        notificationManager.cancel(generateNotificationId(task.getId(), 2));
        notificationManager.cancel(generateNotificationId(task.getId(), 3));
        notificationManager.cancel(generateNotificationId(task.getId(), 4));
    }
}
