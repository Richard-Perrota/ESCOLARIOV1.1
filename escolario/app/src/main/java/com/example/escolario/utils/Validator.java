package com.example.escolario.utils;

import java.util.regex.Pattern;

/**
 * Classe utilitária para validação de dados comuns
 *
 * Implementa validações para:
 * - E-mails
 * - CPFs
 * - Datas no formato brasileiro
 */
public class Validator {
    // Padrão para validação de e-mails (case insensitive)
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     * Valida se uma string é um e-mail válido
     * param email String a ser validada
     * return true se for um e-mail válido, false caso contrário
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Valida se uma string é um CPF válido (apenas dígitos)
     * param cpf String a ser validada
     * return true se tiver 11 dígitos, false caso contrário
     */
    public static boolean isValidCPF(String cpf) {
        if (cpf == null) return false;
        String cleaned = cpf.replaceAll("[^0-9]", "");
        return cleaned.length() == 11;
    }

    /**
     * Valida se uma string está no formato de data brasileiro (dd/MM/yyyy)
     * param date String a ser validada
     * return true se estiver no formato correto, false caso contrário
     */
    public static boolean isValidDate(String date) {
        return date != null && date.matches("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$");
    }
}