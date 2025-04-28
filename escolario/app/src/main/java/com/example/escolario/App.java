package com.example.escolario;

import android.app.Application;
import com.example.escolario.data.AppDatabase;
import com.example.escolario.model.User;
import com.example.escolario.utils.PasswordUtils;

/**
 * Classe principal da aplicação que estende Application.
 *
 * Responsável por inicializar componentes globais e garantir
 * a existência de um usuário administrador padrão.
 */
public class App extends Application {

    // Credenciais padrão do administrador
    private static final String ADMIN_EMAIL = "admin@escolario.com";
    private static final String ADMIN_PASSWORD = "Admin123";
    private static final String ADMIN_CPF = "00000000000";

    @Override
    public void onCreate() {
        super.onCreate();
        initializeAdminUser();
    }

    /**
     * Inicializa o usuário administrador padrão em uma thread separada.
     *
     * Verifica se o admin já existe no banco de dados antes de criar.
     * Executa em background para não bloquear a thread principal.
     */
    private void initializeAdminUser() {
        new Thread(() -> {
            AppDatabase database = AppDatabase.getDatabase(this);

            // Verifica se o admin já está cadastrado
            if (database.userDao().findByEmail(ADMIN_EMAIL) == null) {
                createAdminUser(database);
            }
        }).start();
    }

    /**
     * Cria um novo usuário administrador no banco de dados.
     *
     * param database Instância do banco de dados Room
     */
    private void createAdminUser(AppDatabase database) {
        User admin = new User(
                "Administrador",
                ADMIN_EMAIL,
                PasswordUtils.hash(ADMIN_PASSWORD), // Senha hasheada
                ADMIN_CPF,
                true // Flag de administrador
        );
        database.userDao().insert(admin);
    }
}