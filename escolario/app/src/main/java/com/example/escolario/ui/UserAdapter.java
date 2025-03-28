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
 *
 * <p>Responsável por:
 * <ul>
 *   <li>Gerenciar a lista de usuários</li>
 *   <li>Inflar o layout de item</li>
 *   <li>Tratar eventos de clique</li>
 * </ul>
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> users;
    private final OnUserClickListener listener;

    /**
     * Interface para comunicação de eventos de clique
     */
    public interface OnUserClickListener {
        /**
         * Chamado quando um usuário é clicado
         * @param user Usuário selecionado
         */
        void onUserClick(User user);
    }

    /**
     * Constrói o adapter com um listener para eventos de clique
     * @param listener Listener para tratar cliques nos itens
     */
    public UserAdapter(OnUserClickListener listener) {
        this.listener = listener;
    }

    /**
     * Atualiza a lista de usuários exibidos
     * @param users Nova lista de usuários
     */
    public void submitList(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla o layout usando ViewBinding
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
        // Preenche os dados do usuário no layout
        holder.binding.tvName.setText(user.name);
        holder.binding.tvEmail.setText(user.email);

        // Configura o clique no item
        holder.itemView.setOnClickListener(v -> listener.onUserClick(user));
    }

    @Override
    public int getItemCount() {
        return users != null ? users.size() : 0;
    }

    /**
     * ViewHolder que mantém a referência para os views de cada item
     */
    static class UserViewHolder extends RecyclerView.ViewHolder {
        final ItemUserBinding binding;

        public UserViewHolder(ItemUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}