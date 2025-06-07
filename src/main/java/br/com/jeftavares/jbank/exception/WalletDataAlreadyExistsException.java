package br.com.jeftavares.jbank.exception;

import org.springframework.http.ProblemDetail;

//@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class WalletDataAlreadyExistsException extends JBankException {

    private final String detail;

    public WalletDataAlreadyExistsException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(422);
        pd.setTitle("Wallet data already exists");
        pd.setDetail(this.detail);
        return pd;
    }

    
}