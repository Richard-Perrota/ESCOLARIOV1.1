package com.example.escolario.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Classe responsável por gerenciar a sessão do usuário no aplicativo.
 * Utiliza SharedPreferences para armazenar de forma persistente:
 * - ID do usuário
 * - Tipo de conta (admin/comum)
 * - Nome do usuário
 *
 * Padrão: Singleton (uma única instância por contexto)
 */
public class SessionManager {
    // Nome do arquivo de preferências (deve ser único para o app)
    private static final String PREF_NAME = "ESCOLARIO_SESSION";

    // Chaves para acesso dos valores (evita "magic strings")
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_IS_ADMIN = "is_admin";
    private static final String KEY_USER_NAME = "user_name";

    // Instância do SharedPreferences para leitura
    private final SharedPreferences pref;

    // Editor para escrita nas preferências
    private final SharedPreferences.Editor editor;

    /**
     * Construtor privado para evitar instâncias diretas.
     * @param context Contexto da aplicação (usualmente Activity)
     */
    public SessionManager(Context context) {
        // Obtém ou cria o arquivo de preferências (modo privado)
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Habilita edição das preferências
        editor = pref.edit();
    }

    /**
     * Cria uma nova sessão de usuário com os dados fornecidos.
     * @param userId ID único do usuário no banco de dados
     * @param isAdmin Indica se é um usuário administrativo
     * @param userName Nome completo para exibição
     */
    public void createSession(int userId, boolean isAdmin, String userName) {
        // Armazena os dados no Editor
        editor.putInt(KEY_USER_ID, userId);
        editor.putBoolean(KEY_IS_ADMIN, isAdmin);
        editor.putString(KEY_USER_NAME, userName);

        // Aplica as mudanças (assincronamente)
        editor.apply(); // Preferível sobre commit() para não bloquear a UI
    }

    /**
     * Recupera o nome do usuário logado.
     * @return Nome do usuário ou "Usuário" se não encontrado
     */
    public String getUserName() {
        return pref.getString(KEY_USER_NAME, "Usuário");
    }

    /**
     * Verifica se há um usuário autenticado.
     * @return true se existir um ID de usuário válido
     */
    public boolean isLoggedIn() {
        // -1 é o valor padrão quando a chave não existe
        return pref.getInt(KEY_USER_ID, -1) != -1;
    }

    /**
     * Encerra a sessão atual removendo todos os dados.
     * Deve ser chamado no logout.
     */
    public void logout() {
        // Limpa todos os pares chave-valor
        editor.clear();

        // Aplica as mudanças
        editor.apply();
    }
}