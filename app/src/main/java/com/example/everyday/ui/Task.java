package com.example.everyday.ui;

import java.util.UUID;

public class Task {

    private String id;  // Уникальный идентификатор задачи
    private String description;
    private int year, month, day, hour, minute;
    private boolean done;  // Добавляем поле для отслеживания состояния выполнения задачи

    // Конструктор
    public Task(String description, int year, int month, int day, int hour, int minute) {
        this.id = UUID.randomUUID().toString();  // Генерация уникального идентификатора для каждой задачи
        this.description = description;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.done = false; // Задача по умолчанию не выполнена
    }

    // Геттеры и сеттеры для всех полей
    public String getId() {
        return id;  // Геттер для уникального идентификатора
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    // Геттер для состояния выполнения задачи
    public boolean isDone() {
        return done;
    }

    // Сеттер для изменения состояния выполнения задачи
    public void setDone(boolean done) {
        this.done = done;
    }

    // Метод для сравнения задач по идентификатору (если необходимо)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Task task = (Task) obj;

        return id.equals(task.id);  // Сравниваем задачи по уникальному идентификатору
    }

    @Override
    public int hashCode() {
        return id.hashCode();  // Используем id для расчета хэш-кода
    }
}
