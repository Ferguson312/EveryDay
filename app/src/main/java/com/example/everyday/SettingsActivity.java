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

                // Устанавливаем фрагмент настроек
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(android.R.id.content, new SettingsFragment())
                        .commit();

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

        // Внутренний класс для фрагмента настроек
        public static class SettingsFragment extends PreferenceFragmentCompat {
                @Override
                public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
                        // Загружаем настройки из XML
                        setPreferencesFromResource(R.xml.preference, rootKey);
                }
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
                        case "sky":
                                setTheme(R.style.Theme_Sky);
                                break;
                        case "coffee":
                                setTheme(R.style.Theme_Coffee);
                                break;
                        case "lollipop":
                                setTheme(R.style.Theme_Lollipop);
                                break;
                        case "candy":
                                setTheme(R.style.Theme_Candy);
                                break;
                }
        }
}
