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
        AddCardDialogFragment dialogFragment = new AddCardDialogFragment();

        // Pasang listener untuk menangani data yang dikirim dari Fragment
        dialogFragment.setAddCardListener((question, syntax) -> {
            Flashcard newCard = new Flashcard(question, syntax);

            // Simpan ke Database
            database.flashcardDao().insertCard(newCard);

            // Update UI
            flashcardList.add(newCard);
            adapter.notifyItemInserted(flashcardList.size() - 1);
            recyclerView.smoothScrollToPosition(flashcardList.size() - 1);
        });

        // Tampilkan Dialog menggunakan FragmentManager
        dialogFragment.show(getSupportFragmentManager(), "AddCardDialog");
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