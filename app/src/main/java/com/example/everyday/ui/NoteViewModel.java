package com.example.everyday.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.everyday.NoteRepository;

import java.util.ArrayList;
import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private final NoteRepository noteRepository;
    private final MutableLiveData<List<Note>> notes = new MutableLiveData<>();

    public NoteViewModel(Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
        notes.setValue(noteRepository.getAllNotes());
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
        noteRepository.addNote(note);
        notes.postValue(currentNotes); // Устанавливаем новый список
    }
    public void deleteNote(Note note) {
        noteRepository.deleteNote(note.getId());
    }

}