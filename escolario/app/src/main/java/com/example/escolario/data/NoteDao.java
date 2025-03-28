package com.example.escolario.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.escolario.model.Note;

import java.util.List;

/**
 * Interface DAO (Data Access Object) para operações com notas no banco de dados.
 *
 * Define os métodos para:
 *
 *   Inserir novas notas
 *   Recuperar notas por usuário
 *
 *
 * Utiliza anotações do Room para mapeamento SQLite.
 */
@Dao
public interface NoteDao {

    /**
     * Insere uma nova nota no banco de dados.
     * param note Objeto Note a ser persistido
     */
    @Insert
    void insert(Note note);

    /**
     * Recupera todas as notas de um usuário específico.
     * param userId ID do usuário para filtro
     * return LiveData contendo lista de notas, observável para atualizações
     */
    @Query("SELECT * FROM notes WHERE userId = :userId")
    LiveData<List<Note>> getNotesByUser(int userId);
}