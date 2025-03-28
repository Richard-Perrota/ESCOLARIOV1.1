package com.example.escolario.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utilitário para manipulação segura de senhas usando BCrypt.
 *
 * Implementa as melhores práticas para:
 *
 *   Hash de senhas com salt automático
 *   Verificação segura de credenciais
 *   Proteção contra ataques de força bruta
 *
 */
public class PasswordUtils {
    private static final int BCRYPT_COST_FACTOR = 12; // Fator de custo balanceado entre segurança e performance

    /**
     * Gera um hash seguro para armazenamento de senhas.
     *
     * param password Senha em texto puro (será hasheada)
     * return String com o hash seguro (contém salt incorporado)
     * throws IllegalArgumentException Se a senha for nula ou vazia
     */
    public static String hash(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Senha não pode ser vazia");
        }
        return BCrypt.hashpw(password, BCrypt.gensalt(BCRYPT_COST_FACTOR));
    }

    /**
     * Verifica se uma senha corresponde a um hash armazenado.
     *
     * param password Senha em texto puro para verificação
     * param hash Hash armazenado para comparação
     * return true se a senha corresponder ao hash, false caso contrário
     * throws IllegalArgumentException Se qualquer parâmetro for inválido
     */
    public static boolean verify(String password, String hash) {
        if (password == null || hash == null ||
                password.trim().isEmpty() || hash.trim().isEmpty()) {
            throw new IllegalArgumentException("Parâmetros não podem ser nulos/vazios");
        }

        try {
            return BCrypt.checkpw(password, hash);
        } catch (Exception e) {
            // Registre o erro adequadamente em produção
            return false;
        }
    }
}