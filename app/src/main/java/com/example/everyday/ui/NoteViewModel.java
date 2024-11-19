package com.example.everyday.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class NoteViewModel extends ViewModel {

    private final MutableLiveData<List<Note>> notes = new MutableLiveData<>();

    public NoteViewModel() {
        // Инициализация списка заметок (это можно заменить на реальный источник данных)
        List<Note> initialNotes = new ArrayList<>();
        initialNotes.add(new Note(1, "Заметка 1", "Содержание заметки 1"));
        initialNotes.add(new Note(2, "Заметка 2", "Содержание заметки 2"));
        notes.setValue(initialNotes);
    }

    public LiveData<List<Note>> getNotes() {
        return notes;
    }

    public void addNote(Note note) {
        List<Note> currentNotes = notes.getValue();
        if (currentNotes != null) {
            currentNotes.add(note);
            notes.setValue(currentNotes);
        }
    }
}
