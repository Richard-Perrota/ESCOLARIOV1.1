package com.example.escolario.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

/**
 * Entidade que representa uma nota acadêmica no banco de dados.
 *
 * Relacionamento: Cada nota pertence a um usuário (relação 1:N)
 */
@Entity(tableName = "notes",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = CASCADE))
public class Note {
    /**
     * ID único da nota (auto-incrementado)
     */
    @PrimaryKey(autoGenerate = true)
    public int id;

    /**
     * ID do usuário dono da nota (chave estrangeira)
     */
    public int userId;

    /**
     * Matéria/disciplina da nota
     */
    @NonNull
    public String subject;

    /**
     * Tipo de atividade (prova, trabalho, etc)
     */
    @NonNull
    public String type;

    /**
     * Conteúdo/descrição da nota
     */
    @NonNull
    public String content;

    /**
     * Data da atividade (formato dd/MM/yyyy)
     */
    @NonNull
    public String date;

    /**
     * Construtor para criação de novas notas
     *
     * param userId ID do usuário dono da nota
     * param subject Matéria/disciplina
     * param type Tipo de atividade
     * param content Descrição completa
     * param date Data no formato dd/MM/yyyy
     */
    public Note(int userId, @NonNull String subject,
                @NonNull String type, @NonNull String content,
                @NonNull String date) {
        this.userId = userId;
        this.subject = subject;
        this.type = type;
        this.content = content;
        this.date = date;
    }
}