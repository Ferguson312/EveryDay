package com.example.everyday;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.everyday.ui.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteRepository {
    private final SQLiteDatabase database;

    public NoteRepository(Context context) {
        NoteDatabaseHelper dbHelper = new NoteDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public void addNote(Note note) {
        ContentValues values = new ContentValues();
        values.put(NoteDatabaseHelper.COLUMN_TITLE, note.getTitle());
        values.put(NoteDatabaseHelper.COLUMN_CONTENT, note.getContent());

        database.insert(NoteDatabaseHelper.TABLE_NOTES, null, values);
    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();

        Cursor cursor = database.query(
                NoteDatabaseHelper.TABLE_NOTES,
                null,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            Note note = new Note(
                    cursor.getInt(cursor.getColumnIndexOrThrow(NoteDatabaseHelper.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(NoteDatabaseHelper.COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(NoteDatabaseHelper.COLUMN_CONTENT))
            );
            notes.add(note);
        }
        cursor.close();
        return notes;
    }

    public void updateNote(Note note) {
        ContentValues values = new ContentValues();
        values.put(NoteDatabaseHelper.COLUMN_TITLE, note.getTitle());
        values.put(NoteDatabaseHelper.COLUMN_CONTENT, note.getContent());

        database.update(NoteDatabaseHelper.TABLE_NOTES, values, NoteDatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(note.getId())});
    }

    public void deleteNote(int noteId) {
        database.delete(NoteDatabaseHelper.TABLE_NOTES, NoteDatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(noteId)});
    }

    public Note getNoteById(int noteId) {
        String selection = NoteDatabaseHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(noteId)};

        Cursor cursor = database.query(
                NoteDatabaseHelper.TABLE_NOTES,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        Note note = null;
        if (cursor.moveToFirst()) {
            note = new Note(
                    cursor.getInt(cursor.getColumnIndexOrThrow(NoteDatabaseHelper.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(NoteDatabaseHelper.COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(NoteDatabaseHelper.COLUMN_CONTENT))
            );
        }
        cursor.close();
        return note;
    }
}