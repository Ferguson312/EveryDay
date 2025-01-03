package com.example.everyday.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.everyday.databinding.FragmentGalleryBinding;
import com.example.everyday.ui.Note;
import com.example.everyday.ui.NoteAdapter;
import com.example.everyday.ui.NoteViewModel;

public class GalleryFragment extends Fragment {

    private NoteViewModel noteViewModel;
    private NoteAdapter noteAdapter;
    private FragmentGalleryBinding binding; // ViewBinding для фрагмента

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Получаем binding для фрагмента
        binding = FragmentGalleryBinding.inflate(inflater, container, false);

        // Инициализация ViewModel
        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
        // Настройка RecyclerView
        binding.recyclerViewNotes.setLayoutManager(new LinearLayoutManager(getContext()));
        NoteAdapter noteAdapter = new NoteAdapter(noteViewModel);
        binding.recyclerViewNotes.setAdapter(noteAdapter);
        // Наблюдатель за изменениями в списке заметок
        // Обновление данных в адаптере
        noteViewModel.getNotes().observe(getViewLifecycleOwner(), noteAdapter::setNotes);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;  // Обнуляем binding, чтобы избежать утечек памяти
    }

}
