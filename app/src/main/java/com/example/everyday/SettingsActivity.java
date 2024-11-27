package com.example.everyday;

import android.content.Intent;
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
                                String theme = sharedPreferences.getString(key, "light"); // значение по умолчанию "light"
                                applyTheme(theme);

                                // Уведомление о перезагрузке приложения
                                showRestartAppDialog();
                        }
                });

                // Применяем тему сразу при старте активности
                String theme = preferences.getString("theme_preference", "light");
                applyTheme(theme);
        }

        // Внутренний класс для фрагмента настроек
        public static class SettingsFragment extends PreferenceFragmentCompat {
                @Override
                public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
                        // Загружаем настройки из XML
                        setPreferencesFromResource(R.xml.preference, rootKey);
                }
        }

        // Метод для применения выбранной темы
        private void applyTheme(String theme) {
                switch (theme) {
                        case "dark":
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                                break;
                        case "light":
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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

        // Метод для отображения диалогового окна с просьбой перезагрузить приложение
        private void showRestartAppDialog() {
                new android.app.AlertDialog.Builder(this)
                        .setTitle("Перезагрузка приложения")
                        .setMessage("Для применения новой темы необходимо перезагрузить приложение.")
                        .setPositiveButton("OK", (dialog, which) -> restartApp())  // Перезагружаем приложение
                        .setCancelable(false)  // Не даем пользователю отменить диалог
                        .show();
        }

        // Метод для перезагрузки приложения
        private void restartApp() {
                // Получаем интент для перезапуска приложения
                Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                if (intent != null) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  // Закрывает все активные активности
                        startActivity(intent);  // Перезапускаем приложение
                }
        }
}
