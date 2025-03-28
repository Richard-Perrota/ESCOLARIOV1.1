package com.example.escolario.activities.admin;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.escolario.data.AppDatabase;
import com.example.escolario.databinding.ActivityUserListBinding;
import com.example.escolario.model.User;
import com.example.escolario.ui.UserAdapter;

/**
 * Activity responsável por listar e gerenciar usuários (visualização e exclusão).
 * Utiliza RecyclerView para exibição eficiente e Room Database para persistência.
 */
public class UserListActivity extends AppCompatActivity {
    private ActivityUserListBinding binding; // ViewBinding para acesso seguro às views
    private UserAdapter adapter; // Adapter para o RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configura o RecyclerView para exibir a lista de usuários
        setupRecyclerView();

        // Botão de voltar - finaliza a Activity atual
        binding.btnBack.setOnClickListener(v -> finish());
    }

    /**
     * Configura o RecyclerView com seu LayoutManager e Adapter.
     * Também carrega os usuários do banco de dados e os exibe.
     */
    private void setupRecyclerView() {
        // Inicializa o adapter com um callback para exclusão de usuários
        adapter = new UserAdapter(this::showDeleteDialog);

        // Define um LinearLayoutManager (lista vertical)
        binding.rvUsers.setLayoutManager(new LinearLayoutManager(this));
        binding.rvUsers.setAdapter(adapter);

        // Observa a lista de usuários do banco de dados (Room + LiveData)
        AppDatabase.getDatabase(this).userDao()
                .getAllRegularUsers()
                .observe(this, users -> {
                    if (users != null && !users.isEmpty()) {
                        adapter.submitList(users); // Atualiza a lista no adapter
                    } else {
                        // Mensagem se não houver usuários cadastrados
                        Toast.makeText(this, "Nenhum usuário cadastrado", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Exibe um diálogo de confirmação antes de excluir um usuário.
     * param user Usuário selecionado para exclusão.
     */
    private void showDeleteDialog(User user) {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Confirmar exclusão")
                .setMessage("Excluir usuário " + user.name + "?")
                .setPositiveButton("Excluir", (dialog, which) -> deleteUser(user))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    /**
     * Exclui um usuário do banco de dados em uma thread secundária.
     * param user Usuário a ser removido.
     */
    private void deleteUser(User user) {
        new Thread(() -> {
            AppDatabase.getDatabase(this).userDao().delete(user); // Operação de exclusão
            runOnUiThread(() ->
                    Toast.makeText(this, "Usuário excluído", Toast.LENGTH_SHORT).show()
            );
        }).start();
    }
}