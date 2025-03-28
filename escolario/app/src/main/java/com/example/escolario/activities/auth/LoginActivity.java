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

/**
 * Activity responsável pela autenticação de usuários no sistema.
 * Gerencia o processo de login, redirecionando para as telas apropriadas
 * conforme o tipo de usuário (admin ou comum).
 */
public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configura os listeners dos botões
        binding.btnLogin.setOnClickListener(v -> attemptLogin());
        binding.btnRegister.setOnClickListener(v -> openRegisterScreen());
    }

    /**
     * Realiza a tentativa de login com as credenciais fornecidas.
     * Valida os campos e verifica no banco de dados em uma thread separada.
     */
    private void attemptLogin() {
        String email = binding.etEmail.getText().toString().trim().toLowerCase();
        String password = binding.etPassword.getText().toString();

        // Validação básica dos campos
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try {
                AppDatabase db = AppDatabase.getDatabase(this);
                // Busca usuário por email (sem aplicar hash na busca)
                User user = db.userDao().findByEmail(email);

                runOnUiThread(() -> {
                    // Verifica se o usuário existe e se a senha corresponde
                    if (user != null && PasswordUtils.verify(password, user.password)) {
                        redirectUser(user);
                    } else {
                        Toast.makeText(this, "Credenciais inválidas", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    /**
     * Redireciona o usuário para a tela apropriada conforme seu tipo (admin ou comum).
     * @param user Objeto User contendo os dados do usuário autenticado
     */
    private void redirectUser(User user) {
        Intent intent;
        if (user.isAdmin) {
            intent = new Intent(this, UserListActivity.class);  // Tela de administração
        } else {
            intent = new Intent(this, NoteActivity.class);      // Tela de usuário comum
        }
        // Passa dados do usuário para a próxima activity
        intent.putExtra("USER_ID", user.id);
        intent.putExtra("USER_NAME", user.name);
        startActivity(intent);
        finish();  // Finaliza a activity atual para evitar retorno não desejado
    }

    /**
     * Abre a tela de cadastro de novos usuários.
     */
    private void openRegisterScreen() {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}