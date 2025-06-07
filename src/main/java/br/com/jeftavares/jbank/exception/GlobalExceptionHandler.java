package br.com.jeftavares.jbank.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JBankException.class)
    public ProblemDetail handlerJBankException(JBankException e) {
        return e.toProblemDetail();
    }

}