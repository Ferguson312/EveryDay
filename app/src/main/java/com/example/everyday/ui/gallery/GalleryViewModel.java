package com.example.everyday.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.everyday.ui.Note;

import java.util.ArrayList;
import java.util.List;

public class GalleryViewModel extends ViewModel {

    // Поле для текста, которое у вас уже было
    private final MutableLiveData<String> mText;

    // Поле для заметок
    private final MutableLiveData<List<Note>> notesList;

    public GalleryViewModel() {
        // Инициализация текста
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");

        // Инициализация списка заметок
        notesList = new MutableLiveData<>();
        notesList.setValue(new ArrayList<>());
    }

    // Метод для получения текста
    public LiveData<String> getText() {
        return mText;
    }

    // Метод для получения всех заметок
    public LiveData<List<Note>> getAllNotes() {
        return notesList;
    }

    // Метод для добавления новой заметки
    public void addNote(Note note) {
        List<Note> currentNotes = notesList.getValue();
        if (currentNotes != null) {
            currentNotes.add(note);
            notesList.setValue(currentNotes);
        }
    }
}
