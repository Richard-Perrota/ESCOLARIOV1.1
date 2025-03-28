package com.example.escolario.activities.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.escolario.data.AppDatabase;
import com.example.escolario.data.UserDao;
import com.example.escolario.databinding.ActivityRegisterBinding;
import com.example.escolario.model.User;
import com.example.escolario.utils.PasswordUtils;
import com.example.escolario.utils.Validator;

/**
 * Activity responsável pelo cadastro de novos usuários no sistema.
 * Realiza validações dos dados de entrada e persiste o novo usuário no banco de dados.
 */
public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupListeners();
    }

    /**
     * Configura os listeners dos botões da interface.
     */
    private void setupListeners() {
        binding.btnRegister.setOnClickListener(v -> attemptRegistration());
        binding.btnBack.setOnClickListener(v -> finish());  // Volta para a tela anterior
    }

    /**
     * Inicia o processo de registro após coletar os dados dos campos de entrada.
     */
    private void attemptRegistration() {
        String name = binding.etName.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim().toLowerCase();
        String cpf = binding.etCpf.getText().toString().trim();
        String password = binding.etPassword.getText().toString();

        if (!validateInputs(name, email, cpf, password)) {
            return;  // Interrompe se as validações falharem
        }

        registerNewUser(name, email, cpf, password);
    }

    /**
     * Valida os dados de entrada conforme as regras de negócio.
     * @return true se todos os campos são válidos, false caso contrário
     */
    private boolean validateInputs(String name, String email, String cpf, String password) {
        if (name.isEmpty()) {
            showError("Digite seu nome completo");
            return false;
        }

        if (!Validator.isValidEmail(email)) {
            showError("Formato de email inválido");
            return false;
        }

        if (!Validator.isValidCPF(cpf)) {
            showError("CPF inválido");
            return false;
        }

        if (password.length() < 6) {
            showError("Senha deve ter no mínimo 6 caracteres");
            return false;
        }

        return true;
    }

    /**
     * Registra um novo usuário no banco de dados em uma thread secundária.
     * Realiza hash da senha antes do armazenamento.
     */
    private void registerNewUser(String name, String email, String cpf, String password) {
        // Mostra progresso e desabilita o botão durante o processamento
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnRegister.setEnabled(false);

        new Thread(() -> {
            try {
                AppDatabase db = AppDatabase.getDatabase(this);
                UserDao userDao = db.userDao();

                // Verificação de email duplicado
                if (userDao.findByEmail(email) != null) {
                    runOnUiThread(() -> {
                        showError("Email já cadastrado");
                        resetRegistrationState();
                    });
                    return;
                }

                // Cria novo usuário com senha hasheada
                User newUser = new User(
                        name,
                        email,
                        PasswordUtils.hash(password),  // Aplica BCrypt na senha
                        cpf.replaceAll("[^0-9]", ""),  // Remove formatação do CPF
                        false  // Define como usuário comum (não admin)
                );

                userDao.insert(newUser);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Cadastro realizado!", Toast.LENGTH_SHORT).show();
                    finish();  // Retorna para a tela de login
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    showError("Erro: " + e.getMessage());
                    resetRegistrationState();
                });
            }
        }).start();
    }

    /**
     * Restaura o estado inicial da interface após tentativa de registro.
     */
    private void resetRegistrationState() {
        binding.progressBar.setVisibility(View.GONE);
        binding.btnRegister.setEnabled(true);
    }

    /**
     * Exibe mensagens de erro para o usuário.
     * param message Texto da mensagem a ser exibida
     */
    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}