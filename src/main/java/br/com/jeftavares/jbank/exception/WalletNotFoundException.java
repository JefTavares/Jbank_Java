package br.com.jeftavares.jbank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class WalletNotFoundException extends JBankException {

    private final String detail;

    public WalletNotFoundException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        pd.setTitle("Wallet not found");
        pd.setDetail(detail);
        // TODO Remover esse property que são testes
        pd.setProperty("type", "wallet_not_found");
        pd.setProperty("status", HttpStatus.NOT_FOUND.value());
        pd.setProperty("title", "Wallet not found");

        return pd;
    }

}
