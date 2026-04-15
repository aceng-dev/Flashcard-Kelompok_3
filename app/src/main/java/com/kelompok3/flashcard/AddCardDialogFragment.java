package com.kelompok3.flashcard;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AddCardDialogFragment extends DialogFragment {

    public interface AddCardListener {
        void onCardAdded(String question, String syntax);
    }

    private AddCardListener listener;

    public void setAddCardListener(AddCardListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_card, null);

        EditText etQuestion = view.findViewById(R.id.etQuestion);
        EditText etSyntax = view.findViewById(R.id.etSyntax);
        Button btnSave = view.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            String question = etQuestion.getText().toString().trim();
            String syntax = etSyntax.getText().toString().trim();

            if (question.isEmpty()) {
                etQuestion.setError("Pertanyaan tidak boleh kosong");
                return;
            }
            if (syntax.isEmpty()) {
                etSyntax.setError("Sintaks tidak boleh kosong");
                return;
            }

            if (listener != null) {
                listener.onCardAdded(question, syntax);
            }
            dismiss();
        });

        builder.setView(view);
        return builder.create();
    }
}
