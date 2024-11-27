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

    public int getMonth() {        return month;
    }

    public void setMonth(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Месяц должен быть в диапазоне от 1 до 12");
        }
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        if (day < 1 || day > getDaysInMonth(month, year)) {
            throw new IllegalArgumentException("Некорректный день для данного месяца");
        }
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("Час должен быть в диапазоне от 0 до 23");
        }
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Минуты должны быть в диапазоне от 0 до 59");
        }
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

    // Метод для переключения состояния выполнения задачи
    public void toggleDone() {
        this.done = !this.done;
    }

    // Метод toString для удобного вывода информации о задаче
    @Override
    public String toString() {
        return "Task{" +
                "id='" + id +
        ", description='" + description +
        ", date=" + String.format("%02d/%02d/%04d", day, month, year) +
                ", time=" + String.format("%02d:%02d", hour, minute) +
                ", done=" + done +
                '}';
    }

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

    private int getDaysInMonth(int month, int year) {
        switch (month) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                return 31;
            case 4: case 6: case 9: case 11:
                return 30;
            case 2:
                return (isLeapYear(year) ? 29 : 28);
            default:
                throw new IllegalArgumentException("Некорректный месяц");
        }
    }

    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
}
