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
        notes.setValue(initialNotes);
    }

    public LiveData<List<Note>> getNotes() {
        return notes;
    }

    public void addNote(Note note) {
        List<Note> currentNotes = notes.getValue();
        if (currentNotes == null) {
            currentNotes = new ArrayList<>(); // Создаем новый список, если текущий равен null
        }
        currentNotes.add(note);
        notes.postValue(currentNotes); // Устанавливаем новый список
    }

    public void removeNote(Note note) {
        List<Note> currentNotes = notes.getValue();
        if (currentNotes != null) {
            currentNotes.removeIf(existingNote -> existingNote.getId() == note.getId()); // Удаляем заметку по идентификатору
            notes.postValue(currentNotes); // Устанавливаем новый список
        }
    }

    public void removeNoteById(int noteId) {
        List<Note> currentNotes = notes.getValue();
        if (currentNotes != null) {
            currentNotes.removeIf(existingNote -> existingNote.getId() == noteId); // Удаляем заметку по идентификатору
            notes.postValue(currentNotes); // Устанавливаем новый список
        }
    }
}