package com.example.everyday;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
        // Устанавливаем разметку
        setContentView(R.layout.activity_settings);

        // Получаем настройки
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Слушаем изменения настроек
        preferences.registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> {
            if ("theme_preference".equals(key)) {
                String theme = sharedPreferences.getString(key, "system");
                applyTheme(theme);
                recreate();  // Перезапускаем активность для применения новой темы
            }

        });
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preference, rootKey);
        }
    }

    private void applyTheme(String theme) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putString("theme_preference", theme).apply();

        // Применяем выбранную тему
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
        }
    }
}








