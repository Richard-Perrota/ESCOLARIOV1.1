package com.example.escolario.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.escolario.databinding.ItemUserBinding;
import com.example.escolario.model.User;
import java.util.List;

/**
 * Adapter para exibição de usuários em um RecyclerView.
 * Implementa padrão ViewHolder para eficiência de memória e
 * suporta operações de filtragem e clique.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    // Lista atual de usuários (pode ser filtrada)
    private List<User> users;

    // Listener para eventos de clique (injetado via construtor)
    private final OnUserClickListener listener;

    /**
     * Interface para comunicação de eventos de clique.
     * Segrega a responsabilidade de lidar com cliques
     * da lógica de renderização do Adapter.
     */
    public interface OnUserClickListener {
        /**
         * Disparado quando um item da lista é selecionado.
         * @param user Usuário correspondente ao item clicado
         */
        void onUserClick(User user);
    }

    /**
     * Constrói o adapter com dependências necessárias.
     * @param listener Implementação para tratar eventos de clique
     */
    public UserAdapter(OnUserClickListener listener) {
        this.listener = listener;
    }

    /**
     * Atualiza a lista de usuários exibidos.
     * Notifica automaticamente as mudanças para o RecyclerView.
     * @param users Nova lista de usuários (pode ser null)
     */
    public void submitList(List<User> users) {
        this.users = users;
        notifyDataSetChanged(); // Notificação bruta - ideal para substituição completa
    }

    /**
     * Filtra a lista atual de usuários por critérios externos.
     * @param filteredList Lista já filtrada (não modifica a lista original)
     */
    public void filterList(List<User> filteredList) {
        this.users = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla o layout usando ViewBinding (melhor performance que findViewById)
        ItemUserBinding binding = ItemUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);

        // Preenche os dados do usuário
        holder.binding.tvName.setText(user.name);
        holder.binding.tvEmail.setText(user.email);

        // Configura o clique (delega para a Activity/Fragment)
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onUserClick(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users != null ? users.size() : 0;
    }

    /**
     * ViewHolder padrão que cacheia as views para performance.
     * Padrão: static para evitar vazamentos de memória.
     */
    static class UserViewHolder extends RecyclerView.ViewHolder {
        final ItemUserBinding binding;

        UserViewHolder(ItemUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}