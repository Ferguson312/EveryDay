package com.example.everyday;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.everyday.databinding.ActivityMainBinding;
import com.example.everyday.ui.Note;
import com.example.everyday.ui.TaskDialogFragment;
import com.example.everyday.ui.AddNoteDialogFragment;
import com.example.everyday.ui.TaskViewModel;
import com.example.everyday.ui.NoteViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import androidx.preference.PreferenceManager;
import android.Manifest;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private NoteViewModel noteViewModel;
    private TaskViewModel taskViewModel;

    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_NOTIFICATION_PERMISSION
                );
            }
        }

        // Чтение настроек темы из SharedPreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = preferences.getString("theme_preference", "system");
        applyTheme(theme);

        // Инициализация binding
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //setNavigationIconsBasedOnTheme(binding, theme);

        // Настройка Toolbar
        setSupportActionBar(binding.appBarMain.toolbar);

        // Инициализация ViewModel
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);  // Инициализируем TaskViewModel

        // Настройка FAB для добавления заметки или задачи
        binding.appBarMain.fab.setOnClickListener(view -> {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            String currentFragmentLabel = navController.getCurrentDestination().getLabel().toString();

            if ("Задачи".equals(currentFragmentLabel)) {
                openTaskDialog();
            } else if ("Заметки".equals(currentFragmentLabel)) {
                showAddNoteDialog();
            } else if ("Календарь".equals(currentFragmentLabel)) {
                openTaskDialog();
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

//    private void setNavigationIconsBasedOnTheme(ActivityMainBinding binding, String theme) {
//        int homeIcon = 0;
//        int galleryIcon = 0;
//        int slideshowIcon = 0;
////иконки на нужные поменять
//        switch (theme) {
//            case "Темная":
//                homeIcon = R.drawable.ic_tasks_black;
//                galleryIcon = R.drawable.ic_notes_black;
//                slideshowIcon = R.drawable.ic_calendar_black;
//                break;
//            case "Светлая":
//                homeIcon = R.drawable.ic_tasks_white;
//                galleryIcon = R.drawable.ic_notes_white;
//                slideshowIcon = R.drawable.ic_calendar_white;
//                break;
//            case "Sky":
//                homeIcon = R.drawable.ic_tasks_sky;
//                galleryIcon = R.drawable.ic_notes_sky;
//                slideshowIcon = R.drawable.ic_calendar_sky;
//                break;
//            case "Coffee":
//                homeIcon = R.drawable.ic_tasks_coffee;
//                galleryIcon = R.drawable.ic_notes_coffee;
//                slideshowIcon = R.drawable.ic_calendar_coffee;
//            case "Lollipop":
//                homeIcon = R.drawable.ic_tasks_lollipop;
//                galleryIcon = R.drawable.ic_notes_lollipop;
//                slideshowIcon = R.drawable.ic_calendar_lollipop;
//                break;
//            case "Candy":
//                homeIcon = R.drawable.ic_tasks_candy;
//                galleryIcon = R.drawable.ic_notes_candy;
//                slideshowIcon = R.drawable.ic_calendar_candy;
//                break;
//            default:
//
//                break;
//        }
//
//        setNavigationIcons(binding, homeIcon, galleryIcon, slideshowIcon);
//    }
//
//    private void setNavigationIcons(ActivityMainBinding binding, int homeIcon, int galleryIcon, int slideshowIcon) {
//        NavigationView navigationView = binding.navView;
//        Menu menu = navigationView.getMenu();
//
//        MenuItem homeItem = menu.findItem(R.id.nav_home);
//        homeItem.setIcon(homeIcon);
//
//        MenuItem galleryItem = menu.findItem(R.id.nav_gallery);
//        galleryItem.setIcon(galleryIcon);
//
//        MenuItem slideshowItem = menu.findItem(R.id.nav_slideshow);
//        slideshowItem.setIcon(slideshowIcon);
//    }
    private void applyTheme(String theme) {
        switch (theme) {
            case "Темная":
               // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); просто чёрный экран
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case "Светлая":
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

    // Открытие диалога для добавления новой задачи
    private void openTaskDialog() {
        TaskDialogFragment taskDialogFragment = new TaskDialogFragment();
        taskDialogFragment.setTaskDialogListener(task -> {
            taskViewModel.addTask(task);  // Используем taskViewModel для задач
        });
        taskDialogFragment.show(getSupportFragmentManager(), "TaskDialog");
    }

    // Открытие диалога для добавления новой заметки
    private void showAddNoteDialog() {
        AddNoteDialogFragment addNoteDialogFragment = new AddNoteDialogFragment();
        addNoteDialogFragment.setOnNoteAddedListener((title, content) -> {
            Note newNote = new Note(0, title, content);  // Создаем новую заметку
            noteViewModel.addNote(newNote);  // Добавляем в ViewModel для заметок
            Toast.makeText(MainActivity.this, "Заметка добавлена: " + title, Toast.LENGTH_SHORT).show();
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
