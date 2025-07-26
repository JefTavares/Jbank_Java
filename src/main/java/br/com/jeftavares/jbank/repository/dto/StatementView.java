package br.com.jeftavares.jbank.repository.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//Isso é uma JPA Projection, ou seja, uma projeção de uma query SQL.
public interface StatementView {

    // nome dos campos deve ser igual ao nome do alias da query
    String getStatementId();

    String getType();

    BigDecimal getStatementValue();

    String getWalletReceiver();

    String getWalletSender();

    LocalDateTime getStatementDateTime();

}
