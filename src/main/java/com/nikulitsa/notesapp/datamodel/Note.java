package com.nikulitsa.notesapp.datamodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;

public class Note {

    private StringProperty noteText;
    private ObjectProperty<LocalDateTime> noteDate;

    public String getNoteText() {
        return noteText.get();
    }
    public void setNoteText(String noteText) {
        this.noteText.set(noteText);
    }
    public StringProperty noteTextProperty() {
        return noteText;
    }

    public LocalDateTime getNoteDate() {
        return noteDate.get();
    }
    public void setNoteDate(LocalDateTime noteDate) {
        this.noteDate.set(noteDate);
    }
    public ObjectProperty<LocalDateTime> noteDateProperty() {
        return noteDate;
    }

    public Note (){
        this(null, null);
    }
    public Note (String noteText, LocalDateTime noteDate) {
        this.noteText = new SimpleStringProperty(noteText);
        this.noteDate = new SimpleObjectProperty<LocalDateTime>(noteDate);
    }
}
