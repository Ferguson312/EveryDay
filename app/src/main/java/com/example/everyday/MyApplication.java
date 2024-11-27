package com.example.everyday;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Получаем настройки из SharedPreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = preferences.getString("theme_preference", "system");

        // Применяем тему для всего приложения
        applyTheme(theme);
    }

    private void applyTheme(String theme) {
        switch (theme) {
            case "dark":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case "light":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "system":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            case "Sky":
                setTheme(R.style.Theme_Sky);
                break;
            case "Coffee":
                setTheme(R.style.Theme_Coffee);
                break;
            case "Lollipop":
                setTheme(R.style.Theme_Lollipop);
                break;
            case "Candy":
                setTheme(R.style.Theme_Candy);
                break;
        }
    }
}
