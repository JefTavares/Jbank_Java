package br.com.jeftavares.jbank.exception;

import org.springframework.http.ProblemDetail;

/**
 * Custom exception class for JBank application.
 * This class extends RuntimeException and serves as a base class for all custom exceptions in the application.
 * Exceção pai generica que não pode ser instanciada.
 * Quem for utilizar vai precisar criar uma exceção filha.
 */
public abstract class JBankException extends RuntimeException {

    public JBankException(String message) {
        super(message);
    }

    public JBankException(Throwable cause) {
        super(cause);
    }

    //faz conversão do ProblemDetail para o ResponseEntity
    // Em todas as exceções filhas sobrescrever esse método para as duas especfiiciddes
    // Por exemplo na WalletDataAlreadyExistsException
    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(500); //se ninguem fazer uma tratativa de erro, vai retornar 500
        pd.setTitle("JBank Internal Server Error");
        pd.setDetail("Contact JBank support");
        //pd.setDetail(getMessage());
        return pd;
    }
}