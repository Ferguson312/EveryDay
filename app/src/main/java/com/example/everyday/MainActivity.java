package com.example.everyday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;

import com.example.everyday.ui.Task;
import com.example.everyday.ui.TaskDialogFragment;
import com.example.everyday.ui.TaskViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.everyday.databinding.ActivityMainBinding;
import androidx.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private TaskViewModel taskViewModel; // ViewModel для задач

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Получаем предпочтение темы
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = preferences.getString("theme_preference", "system");

        // Применяем тему ко всему приложению
        applyTheme(theme);

        com.example.everyday.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        // Получаем ViewModel для задач
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // Обработка нажатия на FAB
        binding.appBarMain.fab.setOnClickListener(view -> {
            // Получаем текущий активный фрагмент
            NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
            String currentFragmentTag = navController.getCurrentDestination().getLabel().toString();

            if ("Задачи".equals(currentFragmentTag)) {
                // Открыть диалоговое окно для добавления задачи
                openTaskDialog();
            } else {
                Snackbar.make(view, "Функции у этой кнопки пока нет", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show();
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
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
        }
    }

    // Метод для открытия TaskDialogFragment
    private void openTaskDialog() {
        TaskDialogFragment taskDialogFragment = new TaskDialogFragment(this::addTaskToRecyclerView);
        taskDialogFragment.show(getSupportFragmentManager(), "TaskDialog");
    }

    // Метод для добавления задачи в RecyclerView
    private void addTaskToRecyclerView(Task task) {
        taskViewModel.addTask(task);  // Добавляем задачу в ViewModel
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            // Открыть экран настроек
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
