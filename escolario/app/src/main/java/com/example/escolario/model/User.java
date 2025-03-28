package com.example.escolario.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entidade que representa um usuário no sistema.
 *
 * Armazena informações de autenticação e perfil dos usuários,
 * com restrições de unicidade para email e CPF.
 */
@Entity(tableName = "users", indices = {
        @Index(value = "email", unique = true),  // Email deve ser único
        @Index(value = "cpf", unique = true)    // CPF deve ser único
})
public class User {
    /**
     * ID único auto-incrementado
     */
    @PrimaryKey(autoGenerate = true)
    public int id;

    /**
     * Nome completo do usuário
     */
    @NonNull
    public String name;

    /**
     * Email de acesso (case-insensitive)
     */
    @NonNull
    @ColumnInfo(collate = ColumnInfo.NOCASE)  // Permite login sem diferenciar maiúsculas/minúsculas
    public String email;

    /**
     * Senha criptografada (deve armazenar apenas o hash)
     */
    @NonNull
    public String password;

    /**
     * CPF (apenas números, sem formatação)
     */
    @NonNull
    public String cpf;

    /**
     * Indica se usuário tem privilégios administrativos
     */
    public boolean isAdmin;

    /**
     * Construtor para criação de usuários
     *
     * param name Nome completo
     * param Email Email válido
     * param password Senha (deve ser hasheada antes do armazenamento)
     * param cpf CPF válido (apenas dígitos)
     * param isAdmin Define privilégios administrativos
     */
    public User(@NonNull String name, @NonNull String email,
                @NonNull String password, @NonNull String cpf,
                boolean isAdmin) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.cpf = cpf;
        this.isAdmin = isAdmin;
    }
}