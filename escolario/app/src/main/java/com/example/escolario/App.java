package com.example.escolario;

import android.app.Application;
import com.example.escolario.data.AppDatabase;
import com.example.escolario.model.User;
import com.example.escolario.utils.PasswordUtils;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        createAdminUser(); // Cria o usuário admin ao iniciar o app
    }

    private void createAdminUser() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);

            // Verifica se o admin já existe
            if (db.userDao().login("admin@escolario.com",
                    PasswordUtils.hash("Admin123")) == null) {

                User admin = new User(
                        "Administrador",
                        "admin@escolario.com",
                        PasswordUtils.hash("Admin123"),
                        "00000000000",
                        true
                );
                db.userDao().insert(admin);
            }
        }).start();
    }
}