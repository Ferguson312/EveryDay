package com.example.everyday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.Toast;

import com.example.everyday.ui.Task;
import com.example.everyday.ui.TaskDialogFragment;
import com.example.everyday.ui.TaskViewModel;
import com.example.everyday.ui.AddNoteDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import com.example.everyday.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private TaskViewModel taskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Получаем предпочтения для темы
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = preferences.getString("theme_preference", "system");

        // Применяем тему ко всему приложению
        applyTheme(theme);

        // Настройка привязки макета
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        // Получаем ViewModel для задач
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // Обработка нажатия на FAB (Floating Action Button)
        binding.appBarMain.fab.setOnClickListener(view -> {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            String currentFragmentLabel = navController.getCurrentDestination().getLabel().toString();

            if ("Задачи".equals(currentFragmentLabel)) {
                openTaskDialog();
            } else if ("Заметки".equals(currentFragmentLabel)) {
                showAddNoteDialog();
            } else {
                Snackbar.make(view, "Функции у этой кнопки пока нет", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab)
                        .show();
            }
        });

        // Настройка Navigation Drawer
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

    // Метод для применения темы
    private void applyTheme(String theme) {
        switch (theme) {
            case "dark":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case "light":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }

    // Метод для открытия TaskDialogFragment
    private void openTaskDialog() {
        TaskDialogFragment taskDialogFragment = new TaskDialogFragment();
        taskDialogFragment.setTaskDialogListener(task -> {
            taskViewModel.addTask(task);
        });
        taskDialogFragment.show(getSupportFragmentManager(), "TaskDialog");
    }

    // Метод для отображения AddNoteDialogFragment
    private void showAddNoteDialog() {
        AddNoteDialogFragment addNoteDialogFragment = new AddNoteDialogFragment();
        addNoteDialogFragment.setOnNoteAddedListener((title, content) -> {
            // TODO: Добавить логику для сохранения заметки
            Toast.makeText(this, "Заметка добавлена: " + title, Toast.LENGTH_SHORT).show();
        });
        addNoteDialogFragment.show(getSupportFragmentManager(), "AddNoteDialog");
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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }
}
