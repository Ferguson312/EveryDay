package com.example.everyday.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.everyday.R;

public class AddNoteDialogFragment extends DialogFragment {

    private EditText editTextTitle;
    private EditText editTextContent;
    private OnNoteAddedListener listener;

    // Интерфейс для передачи данных обратно в Activity или ViewModel
    public interface OnNoteAddedListener {
        void onNoteAdded(String title, String content);
    }

    // Устанавливаем слушателя
    public void setOnNoteAddedListener(OnNoteAddedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflating layout для диалога
        View view = inflater.inflate(R.layout.dialog_add_note, container, false);

        // Инициализируем EditText
        editTextTitle = view.findViewById(R.id.editTextTitle);
        editTextContent = view.findViewById(R.id.editTextContent);

        // Проверка на null перед использованием
        if (editTextTitle == null || editTextContent == null) {
            throw new NullPointerException("EditText fields are not properly initialized");
        }

        // Кнопка сохранения
        view.findViewById(R.id.saveButton).setOnClickListener(v -> {
            String titleText = editTextTitle.getText().toString().trim();
            String contentText = editTextContent.getText().toString().trim();

            if (TextUtils.isEmpty(titleText) || TextUtils.isEmpty(contentText)) {
                Toast.makeText(getContext(), "Заголовок и содержимое не могут быть пустыми", Toast.LENGTH_SHORT).show();
            } else {
                // Передаем данные обратно через интерфейс
                if (listener != null) {
                    listener.onNoteAdded(titleText, contentText);
                }
                dismiss(); // Закрываем диалог
            }
        });

        return view;
    }
}
