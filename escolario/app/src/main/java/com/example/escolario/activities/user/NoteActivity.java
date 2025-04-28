package com.example.escolario.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.escolario.activities.auth.LoginActivity;
import com.example.escolario.data.AppDatabase;
import com.example.escolario.databinding.ActivityNoteBinding;
import com.example.escolario.model.Note;
import com.example.escolario.utils.SessionManager;
import com.example.escolario.utils.Validator;

/**
 * Activity responsável pelo gerenciamento de notas acadêmicas.
 *
 * Permite aos usuários autenticados:
 *
 *   Criar novas notas com matéria, tipo, data e conteúdo
 *   Validar campos antes do armazenamento
 *   Salvar notas no banco de dados local
 *
 *
 * Utiliza ViewBinding para interação com as views e Room Database para persistência.
 */
public class NoteActivity extends AppCompatActivity {
    private ActivityNoteBinding binding;
    private int userId;  // Armazena o ID do usuário logado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Verifica sessão
        SessionManager session = new SessionManager(this);
        if (!session.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setupUI(); // ← Chamada sem argumentos
    }

    /**
     * Configura os elementos da interface do usuário.
     * param userName Nome do usuário para exibição personalizada
     */
    // 1. Deixe o método sem parâmetros
    private void setupUI() {
        SessionManager session = new SessionManager(this);
        String userName = session.getUserName();

        binding.tvWelcome.setText(String.format("Olá, %s!", userName));
        binding.btnSave.setOnClickListener(v -> saveNote());
        binding.btnLogout.setOnClickListener(v -> {
            new SessionManager(this).logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    /**
     * Valida e salva uma nova nota no banco de dados.
     *   Fluxo principal:
     *
     *   Valida campos obrigatórios
     *   Cria objeto Note
     *   Armazena em thread secundária
     *   Fornece feedback ao usuário
     *
     */
    private void saveNote() {
        // Obtém valores dos campos
        String subject = binding.etSubject.getText().toString().trim();
        String type = binding.spType.getSelectedItem().toString();
        String date = binding.etDate.getText().toString().trim();
        String content = binding.etContent.getText().toString().trim();

        // Validações
        if (!validateInputs(subject, date, type, content)) {
            return;
        }

        saveNoteToDatabase(subject, type, date, content);
    }

    /**
     * Valida os campos de entrada.
     * return true se todos os campos são válidos, false caso contrário
     */
    private boolean validateInputs(String subject, String date, String content, String s) {
        if (subject.isEmpty()) {
            showToast("Informe a matéria");
            return false;
        }

        if (!Validator.isValidDate(date)) {
            showToast("Data inválida! Use o formato dd/mm/aaaa");
            return false;
        }

        if (content.isEmpty()) {
            showToast("Escreva uma descrição");
            return false;
        }

        return true;
    }

    /**
     * Armazena a nota no banco de dados em background.
     */
    private void saveNoteToDatabase(String subject, String type, String date, String content) {
        new Thread(() -> {
            Note newNote = new Note(
                    userId,    // Vincula a nota ao usuário
                    subject,  // Matéria/Disciplina
                    type,     // Tipo (prova, trabalho, etc)
                    content,  // Conteúdo descritivo
                    date      // Data formatada
            );

            AppDatabase.getDatabase(this).noteDao().insert(newNote);

            runOnUiThread(() -> {
                showToast("Nota salva com sucesso!");
                resetForm();
            });
        }).start();
    }

    /**
     * Reinicia o formulário para nova entrada.
     */
    private void resetForm() {
        binding.etSubject.setText("");
        binding.etDate.setText("");
        binding.etContent.setText("");
        binding.spType.setSelection(0);
    }

    /**
     * Exibe mensagem toast formatada.
     * param message Mensagem a ser exibida
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}