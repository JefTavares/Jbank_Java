package br.com.jeftavares.jbank.service;

import br.com.jeftavares.jbank.controller.dto.CreateWalletDto;
import br.com.jeftavares.jbank.entities.Wallet;
import br.com.jeftavares.jbank.exception.WalletDataAlreadyExistsException;
import br.com.jeftavares.jbank.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
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
}