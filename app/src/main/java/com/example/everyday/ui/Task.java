package com.example.everyday.ui;

import java.util.Objects;

public class Task {
    private int id;  // Уникальный идентификатор задачи
    private String description;
    private int hour;
    private int minute;

    // Конструктор
    public Task(String description, int hour, int minute) {
        this.id = id;
        this.description = description;
        this.hour = hour;
        this.minute = minute;
    }

    // Геттер для id
    public int getId() {
        return id;
    }

    // Остальные геттеры и сеттеры для других полей
    public String getDescription() {
        return description;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id &&
                hour == task.hour &&
                minute == task.minute &&
                description.equals(task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, hour, minute);
    }
}
