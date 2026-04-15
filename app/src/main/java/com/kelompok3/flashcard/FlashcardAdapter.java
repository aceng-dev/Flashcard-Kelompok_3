package com.kelompok3.flashcard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FlashcardAdapter extends RecyclerView.Adapter<FlashcardAdapter.FlashcardViewHolder> {

    private List<Flashcard> flashcardList;
    private OnCardLongClickListener longClickListener;

    public interface OnCardLongClickListener {
        void onCardLongClick(Flashcard card, int position);
    }

    public FlashcardAdapter(List<Flashcard> flashcardList, OnCardLongClickListener longClickListener) {
        this.flashcardList = flashcardList;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public FlashcardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flashcard, parent, false);
        return new FlashcardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardViewHolder holder, int position) {
        Flashcard card = flashcardList.get(position);

        if (card.isFlipped()) {
            holder.tvContent.setText(card.getSyntaxAnswer());
            holder.tvContent.setTextColor(android.graphics.Color.parseColor("#FFD700"));
        } else {
            holder.tvContent.setText(card.getQuestion());
            holder.tvContent.setTextColor(android.graphics.Color.parseColor("#00FFCC"));
        }

        // Deteksi klik biasa (Flip kartu)
        holder.itemView.setOnClickListener(v -> {
            card.setFlipped(!card.isFlipped());
            notifyItemChanged(position);
        });

        // Deteksi tekan tahan (Hapus kartu)
        holder.itemView.setOnLongClickListener(v -> {
            longClickListener.onCardLongClick(card, position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return flashcardList.size();
    }

    public static class FlashcardViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent;

        public FlashcardViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tvContent);
        }
    }
}