package com.example.escolario.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.escolario.data.AppDatabase;
import com.example.escolario.activities.admin.UserListActivity;
import com.example.escolario.activities.user.NoteActivity;
import com.example.escolario.databinding.ActivityLoginBinding;
import com.example.escolario.model.User;
import com.example.escolario.utils.PasswordUtils;
import com.example.escolario.utils.SessionManager;

/**
 * Tela de login do aplicativo Escolario.
 * Responsável por:
 * - Autenticar usuários com credenciais válidas
 * - Redirecionar para telas específicas (admin/aluno)
 * - Gerenciar erros de autenticação
 *
 * Fluxo principal:
 * 1. Valida campos de entrada
 * 2. Consulta banco de dados em background
 * 3. Verifica credenciais com BCrypt
 * 4. Inicia sessão e redireciona
 */
public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding; // ViewBinding para acesso seguro às views

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configuração inicial da view usando ViewBinding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configuração dos listeners
        setupButtonListeners();
    }

    /**
     * Configura os listeners para os botões da interface.
     * Padrão: lambda expressions para código conciso.
     */
    private void setupButtonListeners() {
        binding.btnLogin.setOnClickListener(v -> attemptLogin());
        binding.btnRegister.setOnClickListener(v -> openRegisterScreen());
    }

    /**
     * Processa a tentativa de login com validações.
     * Fluxo:
     * 1. Obtém valores dos campos
     * 2. Validações básicas
     * 3. Consulta assíncrona ao banco
     * 4. Verificação de senha com BCrypt
     */
    private void attemptLogin() {
        // Normaliza email (remove espaços e converte para minúsculas)
        String email = binding.etEmail.getText().toString().trim().toLowerCase();
        String password = binding.etPassword.getText().toString();

        if (!validateInputs(email, password)) {
            return;
        }

        // Thread secundária para operações de I/O
        new Thread(() -> {
            try {
                AppDatabase db = AppDatabase.getDatabase(this);
                User user = db.userDao().findByEmail(email);

                runOnUiThread(() -> handleLoginResult(user, password));
            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    /**
     * Valida os campos de entrada antes da consulta ao banco.
     * @return true se os campos são válidos
     */
    private boolean validateInputs(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Processa o resultado da consulta ao banco de dados.
     * @param user Usuário encontrado (ou null)
     * @param password Senha em texto puro para verificação
     */
    private void handleLoginResult(User user, String password) {
        if (user != null && PasswordUtils.verify(password, user.password)) {
            redirectUser(user);
        } else {
            // Mensagem genérica por segurança (não revela se email existe)
            Toast.makeText(this, "Credenciais inválidas", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Redireciona o usuário para a tela apropriada e inicia sessão.
     * @param user Usuário autenticado
     */
    private void redirectUser(User user) {
        // Cria/atualiza a sessão
        new SessionManager(this).createSession(user.id, user.isAdmin, user.name);

        Intent intent = user.isAdmin
                ? new Intent(this, UserListActivity.class)
                : new Intent(this, NoteActivity.class);

        startActivity(intent);
        finish(); // Impede retorno à tela de login com back button
    }

    /**
     * Navega para a tela de cadastro.
     */
    private void openRegisterScreen() {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}