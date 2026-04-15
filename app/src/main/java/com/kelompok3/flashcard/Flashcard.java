package com.kelompok3.flashcard;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "flashcards")
public class Flashcard {

    @PrimaryKey(autoGenerate = true) // ID otomatis
    private int id;

    private String question;
    private String syntaxAnswer;

    @Ignore // Kolom ini tidak disimpan ke database (hanya untuk status UI saat aplikasi berjalan)
    private boolean isFlipped;

    // Constructor untuk Database
    public Flashcard(String question, String syntaxAnswer) {
        this.question = question;
        this.syntaxAnswer = syntaxAnswer;
        this.isFlipped = false;
    }

    // Getter dan Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getQuestion() { return question; }
    public String getSyntaxAnswer() { return syntaxAnswer; }
    public boolean isFlipped() { return isFlipped; }
    public void setFlipped(boolean flipped) { isFlipped = flipped; }
}