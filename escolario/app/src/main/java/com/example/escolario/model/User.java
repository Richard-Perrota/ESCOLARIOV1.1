package com.example.escolario.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "users", indices = {
        @Index(value = "name"),
        @Index(value = "email", unique = true),
        @Index(value = "cpf", unique = true)
})
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String name;

    @NonNull
    @ColumnInfo(collate = ColumnInfo.NOCASE)
    public String email;

    @NonNull
    public String password;

    @NonNull
    public String cpf;

    public boolean isAdmin;

    // Construtor simplificado
    public User(@NonNull String name, @NonNull String email,
                @NonNull String password, @NonNull String cpf,
                boolean isAdmin) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.cpf = cpf;
        this.isAdmin = isAdmin;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getEmail() {
        return email;
    }
}