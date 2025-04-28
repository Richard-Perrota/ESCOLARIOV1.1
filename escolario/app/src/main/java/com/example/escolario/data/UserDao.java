package com.example.escolario.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.escolario.model.User;

import java.util.List;

/**
 * Interface DAO para operações de banco de dados relacionadas a usuários.
 *
 * Responsável por todas as operações CRUD (Create, Read, Update, Delete)
 * na tabela de usuários, incluindo:
 *
 *   Inserção de novos usuários
 *   Consulta por email
 *   Autenticação (login)
 *   Listagem de usuários comuns
 *   Remoção de usuários<
 *
 */
@Dao
public interface UserDao {

    /**
     * Insere um novo usuário no banco de dados.
     *
     * Política de conflito: ABORT - aborta a operação se já existir
     * um usuário com o mesmo email (chave única).
     *
     * @param user Objeto User a ser persistido
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(User user);

    /**
     * Realiza a autenticação do usuário.
     *
     * param email Email do usuário
     * param password Senha não hasheada (comparação deve ser feita após hash)
     * return Objeto User se autenticação for bem-sucedida, null caso contrário
     * deprecated Este método não é seguro - deve-se usar verificação com hash
     */
    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    User login(String email, String password);

    /**
     * Busca um usuário pelo email.
     *
     * param email Email a ser pesquisado
     * return Objeto User se encontrado, null caso contrário
     */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User findByEmail(String email);

    /**
     * Recupera todos os usuários não-administradores.
     *
     * return LiveData contendo lista observável de usuários comuns
     */
    @Query("SELECT * FROM users WHERE isAdmin = 0")
    LiveData<List<User>> getAllRegularUsers();

    /**
     * Remove um usuário do banco de dados.
     *
     * param user Objeto User a ser removido
     */

    @Query("SELECT * FROM users WHERE isAdmin = 0 AND name LIKE :searchQuery")
    LiveData<List<User>> searchUsers(String searchQuery);
    @Delete
    void delete(User user);
}