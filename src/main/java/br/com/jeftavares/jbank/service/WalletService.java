package br.com.jeftavares.jbank.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.jeftavares.jbank.controller.dto.CreateWalletDto;
import br.com.jeftavares.jbank.controller.dto.DepositMoneyDTO;
import br.com.jeftavares.jbank.controller.dto.PaginationDto;
import br.com.jeftavares.jbank.controller.dto.StatementDto;
import br.com.jeftavares.jbank.controller.dto.StatementItemDto;
import br.com.jeftavares.jbank.controller.dto.StatementOperation;
import br.com.jeftavares.jbank.controller.dto.WalletDto;
import br.com.jeftavares.jbank.entities.Deposit;
import br.com.jeftavares.jbank.entities.Wallet;
import br.com.jeftavares.jbank.exception.DeleteWalletException;
import br.com.jeftavares.jbank.exception.StatementException;
import br.com.jeftavares.jbank.exception.WalletDataAlreadyExistsException;
import br.com.jeftavares.jbank.exception.WalletNotFoundException;
import br.com.jeftavares.jbank.repository.DepositRepository;
import br.com.jeftavares.jbank.repository.WalletRepository;
import br.com.jeftavares.jbank.repository.dto.StatementView;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final DepositRepository depositRepository;

    public WalletService(DepositRepository depositRepository, WalletRepository walletRepository) {
        this.depositRepository = depositRepository;
        this.walletRepository = walletRepository;
    }

    public Wallet createWallet(CreateWalletDto dto) {

        var walletDb = walletRepository.findByCpfOrEmail(dto.cpf(), dto.email());

        if (walletDb.isPresent()) {
            throw new WalletDataAlreadyExistsException("cpf or email already exists");
        }

        var wallet = new Wallet();
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setName(dto.name());
        wallet.setCpf(dto.cpf());
        wallet.setEmail(dto.email());

        return walletRepository.save(wallet);
    }

    public boolean deleteWallet(UUID walletId) {

        var wallet = walletRepository.findById(walletId);

        if (wallet.isPresent()) {

            // Verifica se o saldo é maior que zero
            if (wallet.get().getBalance().compareTo(BigDecimal.ZERO) != 0) {
                throw new DeleteWalletException(
                        "The balance is not zero. The current amount is: $" + wallet.get().getBalance());
            }

            walletRepository.deleteById(walletId);
        }

        return wallet.isPresent();

    }

    @Transactional
    public void depositMoney(UUID walletId, DepositMoneyDTO dto, String ipAddress) {

        var wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("thereis no Wallet with this id: " + walletId));

        var deposit = new Deposit();
        deposit.setWallet(wallet);
        deposit.setDepositValue(dto.value());
        deposit.setDepositDateTime(LocalDateTime.now());
        deposit.setIpAddress(ipAddress);

        depositRepository.save(deposit);

        // Simulating a runtime exception to test rollback
        // if (1 == 1) throw new RuntimeException("Simulating a runtime exception to
        // test rollback");

        wallet.setBalance(wallet.getBalance().add(dto.value()));
        walletRepository.save(wallet);
    }

    public StatementDto getStatement(UUID walletId, Integer page, Integer pageSize) {

        var wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("there is no Wallet with this id: " + walletId));

        var pageRequest = PageRequest.of(page, pageSize, Sort.Direction.DESC, "statement_date_time");

        var statements = walletRepository.findStatements(walletId.toString(), pageRequest)
                .map(view -> mapToDto(walletId, view));

        return new StatementDto(
                new WalletDto(wallet.getId(), wallet.getCpf(), wallet.getName(), wallet.getEmail(),
                        wallet.getBalance()),
                statements.getContent(),
                new PaginationDto(
                        statements.getNumber(),
                        statements.getSize(),
                        statements.getTotalElements(),
                        statements.getTotalPages()));
    }

    private StatementItemDto mapToDto(UUID walletId, StatementView view) {

        if (view.getType().equalsIgnoreCase("deposit")) {
            return mapToDeposit(view);
        }

        // é uma transferência de quem enviou
        if (view.getType().equalsIgnoreCase("transfer")
                && view.getWalletSender().equalsIgnoreCase(walletId.toString())) {
            return mapWhenTransferSender(walletId, view);
        }

        if (view.getType().equalsIgnoreCase("transfer")
                && view.getWalletReceiver().equalsIgnoreCase(walletId.toString())) {
            return mapWhenTransferReceiver(walletId, view);
        }

        throw new StatementException("Invalid type " + view.getType());
    }

    private StatementItemDto mapWhenTransferReceiver(UUID walletId, StatementView view) {
        return new StatementItemDto(
                view.getStatementId(),
                view.getType(),
                "money received from " + view.getWalletSender(),
                view.getStatementValue(),
                view.getStatementDateTime(),
                StatementOperation.CREDIT);
    }

    private StatementItemDto mapWhenTransferSender(UUID walletId, StatementView view) {
        return new StatementItemDto(
                view.getStatementId(),
                view.getType(),
                "money send to " + view.getWalletReceiver(),
                view.getStatementValue(),
                view.getStatementDateTime(),
                StatementOperation.DEBIT);
    }

    private StatementItemDto mapToDeposit(StatementView view) {
        return new StatementItemDto(
                view.getStatementId(),
                view.getType(),
                "money deposit",
                view.getStatementValue(),
                view.getStatementDateTime(),
                StatementOperation.CREDIT);
    }
}