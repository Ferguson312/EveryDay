package com.example.everyday.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.everyday.R;
import com.example.everyday.ui.NoteAdapter;
import com.example.everyday.ui.NoteViewModel;

import java.util.List;

public class GalleryFragment extends Fragment {

    private NoteViewModel noteViewModel;
    private NoteAdapter noteAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Инициализируем адаптер
        noteAdapter = new NoteAdapter();
        recyclerView.setAdapter(noteAdapter);

        // Инициализируем ViewModel
        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);

        // Наблюдаем за изменениями в списке заметок
        noteViewModel.getNotes().observe(getViewLifecycleOwner(), notes -> {
            // Обновляем данные адаптера
            noteAdapter.setNotes(notes);
        });

        return view;
    }
}
