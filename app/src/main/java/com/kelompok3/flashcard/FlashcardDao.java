package com.kelompok3.flashcard;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface FlashcardDao {

    // Mengambil semua data kartu
    @Query("SELECT * FROM flashcards")
    List<Flashcard> getAllCards();

    // Menyimpan kartu baru
    @Insert
    void insertCard(Flashcard flashcard);

    @Delete
    void deleteCard(Flashcard flashcard);
}