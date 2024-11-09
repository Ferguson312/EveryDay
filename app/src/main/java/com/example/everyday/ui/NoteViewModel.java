package com.example.everyday.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class NoteViewModel extends ViewModel {
    private final MutableLiveData<List<Note>> notesLiveData = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Note>> getNotes() {
        return notesLiveData;
    }

    public void addNote(Note note) {
        List<Note> currentNotes = notesLiveData.getValue();
        if (currentNotes != null) {
            currentNotes.add(note);
            notesLiveData.setValue(currentNotes);
        }
    }
}
