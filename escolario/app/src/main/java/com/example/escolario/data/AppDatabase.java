package com.example.escolario.data;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.content.Context;

import com.example.escolario.model.Note;
import com.example.escolario.model.User;

/**
 * Classe principal do banco de dados Room para a aplicação Escolario.
 * <p>
 * Define a configuração do banco de dados e fornece acesso aos DAOs.
 * Implementa o padrão Singleton para garantir uma única instância do banco de dados.
 */
@Database(entities = {User.class, Note.class}, version = 5)
public abstract class AppDatabase extends RoomDatabase {

    // DAOs disponíveis
    public abstract UserDao userDao();

    public abstract NoteDao noteDao();

    // Instância Singleton
    private static volatile AppDatabase INSTANCE;

    /**
     * Obtém a instância única do banco de dados.
     * <p>
     * param context Contexto da aplicação
     * return Instância do AppDatabase
     */
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "escolario_db"  // Nome do arquivo de banco de dados
                            )
                            // Migração destrutiva - recria o banco se a versão mudar
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}