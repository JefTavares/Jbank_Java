package br.com.jeftavares.jbank.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.jeftavares.jbank.controller.dto.TransferMoneyDTO;
import br.com.jeftavares.jbank.entities.Transfer;
import br.com.jeftavares.jbank.entities.Wallet;
import br.com.jeftavares.jbank.exception.TransferException;
import br.com.jeftavares.jbank.exception.WalletNotFoundException;
import br.com.jeftavares.jbank.repository.TransferRepository;
import br.com.jeftavares.jbank.repository.WalletRepository;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final WalletRepository walletRepository;

    public TransferService(TransferRepository transferRepository, WalletRepository walletRepository) {
        this.transferRepository = transferRepository;
        this.walletRepository = walletRepository;
    }

    @Transactional
    public void transferMoney(TransferMoneyDTO dto) {

        var sender = walletRepository.findById(dto.sender())
                .orElseThrow(() -> new WalletNotFoundException("Sender wallet not found"));

        var receiver = walletRepository.findById(dto.receiver())
                .orElseThrow(() -> new WalletNotFoundException("Receiver wallet not found"));

        if (sender.getBalance().compareTo(dto.value()) == -1) {
            throw new TransferException("Insufficient balance. you current balance is $" + sender.getBalance());
        }

        persistTransfer(dto, sender, receiver);

        updateWallet(dto, sender, receiver);

    }

    private void updateWallet(TransferMoneyDTO dto, Wallet sender, Wallet receiver) {
        sender.setBalance(sender.getBalance().subtract(dto.value()));
        receiver.setBalance(receiver.getBalance().add(dto.value()));
        walletRepository.save(sender);
        walletRepository.save(receiver);
    }

    private void persistTransfer(TransferMoneyDTO dto, Wallet sender, Wallet receiver) {
        var transfer = new Transfer();
        transfer.setReceiver(receiver);
        transfer.setSender(sender);
        transfer.setTransferValue(dto.value());
        transfer.setTransferDateTime(LocalDateTime.now());

        transferRepository.save(transfer);
    }

}
