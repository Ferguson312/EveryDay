package com.example.everyday.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextWatcher;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.everyday.R;

public class AddNoteDialogFragment extends DialogFragment {

    private EditText editTextTitle, editTextContent;
    private Button buttonSaveNote;
    private OnNoteAddedListener listener;

    public interface OnNoteAddedListener {
        void onNoteAdded(String title, String content);
    }

    public void setOnNoteAddedListener(OnNoteAddedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Устанавливаем стиль диалога для полноэкранного отображения

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_add_note, container, false);

        // Инициализация элементов
        editTextTitle = rootView.findViewById(R.id.editTextTitle);
        editTextContent = rootView.findViewById(R.id.editTextContent);
        buttonSaveNote = rootView.findViewById(R.id.saveButton );

        // Обработчик кнопки "Сохранить"
        buttonSaveNote.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString().trim();
            String content = editTextContent.getText().toString().trim();

            if (listener != null) {
                listener.onNoteAdded(title, content);
            }

            dismiss();
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Получаем окно диалога
        Dialog dialog = getDialog();
        if (dialog != null) {
            // Устанавливаем размеры диалога на весь экран
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }
}

