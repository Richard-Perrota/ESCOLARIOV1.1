package com.example.escolario.activities.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.escolario.data.AppDatabase;
import com.example.escolario.databinding.ActivityUserListBinding;
import com.example.escolario.model.User;
import com.example.escolario.ui.UserAdapter;

/**
 * Tela de administração para gerenciamento de usuários.
 * Funcionalidades principais:
 * - Listagem de usuários com RecyclerView
 * - Busca/filtro em tempo real
 * - Exclusão de usuários com confirmação
 */
public class UserListActivity extends AppCompatActivity {
    // Binding para acesso seguro às views (ViewBinding)
    private ActivityUserListBinding binding;

    // Adapter para a lista de usuários
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configuração inicial do ViewBinding
        binding = ActivityUserListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configura a lista de usuários
        setupRecyclerView();

        // Botão de voltar - finaliza a activity atual
        binding.btnBack.setOnClickListener(v -> finish());

        // Configura a barra de pesquisa
        setupSearchView();
    }

    /**
     * Configura o RecyclerView e seus componentes:
     * - LayoutManager (linear vertical)
     * - Adapter com os dados
     * - Observador para atualizações do banco
     */
    private void setupRecyclerView() {
        // Cria o adapter com callback para exclusão
        adapter = new UserAdapter(this::showDeleteDialog);

        // Define o layout como lista linear vertical
        binding.rvUsers.setLayoutManager(new LinearLayoutManager(this));
        binding.rvUsers.setAdapter(adapter);

        // Observa mudanças na lista de usuários não-administradores
        AppDatabase.getDatabase(this).userDao()
                .getAllRegularUsers()
                .observe(this, users -> {
                    if (users != null && !users.isEmpty()) {
                        adapter.submitList(users); // Atualiza a lista
                    } else {
                        showEmptyState(); // Mostra estado vazio
                    }
                });
    }

    /**
     * Mostra feedback quando não há usuários cadastrados
     */
    private void showEmptyState() {
        Toast.makeText(this, "Nenhum usuário cadastrado", Toast.LENGTH_SHORT).show();
    }

    /**
     * Configura a funcionalidade de busca/filtro
     */
    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // Não necessário para busca em tempo real
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterUsers(newText);
                return true;
            }
        });
    }

    /**
     * Filtra usuários conforme texto digitado
     * @param searchText Texto para filtro (pode ser vazio)
     */
    private void filterUsers(String searchText) {
        if (searchText.isEmpty()) {
            // Mostra lista completa se vazio
            AppDatabase.getDatabase(this)
                    .userDao()
                    .getAllRegularUsers()
                    .observe(this, adapter::submitList);
        } else {
            // Aplica filtro com wildcard (%)
            AppDatabase.getDatabase(this)
                    .userDao()
                    .searchUsers("%" + searchText + "%")
                    .observe(this, adapter::submitList);
        }
    }

    /**
     * Mostra diálogo de confirmação para exclusão
     * @param user Usuário a ser excluído
     */
    private void showDeleteDialog(User user) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar exclusão")
                .setMessage("Excluir usuário " + user.name + "?")
                .setPositiveButton("Excluir", (dialog, which) -> deleteUser(user))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    /**
     * Exclui usuário do banco de dados em thread secundária
     * @param user Usuário a ser removido
     */
    private void deleteUser(User user) {
        new Thread(() -> {
            AppDatabase.getDatabase(this).userDao().delete(user);

            // Feedback na thread principal
            runOnUiThread(() ->
                    Toast.makeText(this, "Usuário excluído", Toast.LENGTH_SHORT).show()
            );
        }).start();
    }
}