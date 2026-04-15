package com.kelompok3.flashcard;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FlashcardAdapter adapter;
    private List<Flashcard> flashcardList;
    private AppDatabase database; // Variabel Database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "flashcoder-db")
                .allowMainThreadQueries()
                .build();

        // 2. Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.recyclerViewFlashcards);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 3. Ambil data dari Database
        flashcardList = database.flashcardDao().getAllCards();

        // Jika database masih kosong, tambahkan 1 kartu default
        if (flashcardList.isEmpty()) {
            Flashcard defaultCard = new Flashcard("Apa itu FlashCoder?", "Aplikasi Hafalan Sintaks!");
            database.flashcardDao().insertCard(defaultCard);
            flashcardList.add(defaultCard);
        }

        // 4. Pasang Adapter
        adapter = new FlashcardAdapter(flashcardList, (card, position) -> {
            showDeleteDialog(card, position);
        });
        recyclerView.setAdapter(adapter);

        // 5. Logika Tombol Tambah (FAB)
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> showAddDialog());
    }

    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_card, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        EditText etQuestion = dialogView.findViewById(R.id.etQuestion);
        EditText etSyntax = dialogView.findViewById(R.id.etSyntax);
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            String question = etQuestion.getText().toString().trim();
            String syntax = etSyntax.getText().toString().trim();

            // Cek apakah pertanyaan kosong
            if (question.isEmpty()) {
                etQuestion.setError("Pertanyaan tidak boleh kosong");
                etQuestion.requestFocus(); // Arahkan kursor ke sini
                return; // Hentikan proses simpan
            }

            // Cek apakah sintaks kosong
            if (syntax.isEmpty()) {
                etSyntax.setError("Sintaks tidak boleh kosong");
                etSyntax.requestFocus(); // Arahkan kursor ke sini
                return; // Hentikan proses simpan
            }

            Flashcard newCard = new Flashcard(question, syntax);

            database.flashcardDao().insertCard(newCard);

            flashcardList.add(newCard);
            adapter.notifyItemInserted(flashcardList.size() - 1);
            recyclerView.smoothScrollToPosition(flashcardList.size() - 1);

            dialog.dismiss();
        });

        dialog.show();
    }

    // Fungsi untuk memunculkan pop-up peringatan hapus
    private void showDeleteDialog(Flashcard card, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Kartu?")
                .setMessage("Yakin ingin menghapus kartu:\n\n\"" + card.getQuestion() + "\"?")
                .setPositiveButton("Ya, Hapus", (dialog, which) -> {

                    // 1. Hapus dari database Room
                    database.flashcardDao().deleteCard(card);

                    // 2. Hapus dari daftar memori aplikasi
                    flashcardList.remove(position);

                    // 3. Beri tahu adapter agar kartu hilang dari layar dengan animasi
                    adapter.notifyItemRemoved(position);

                })
                .setNegativeButton("Batal", (dialog, which) -> {
                    dialog.dismiss(); // Tutup pop-up jika batal
                })
                .show();
    }
}