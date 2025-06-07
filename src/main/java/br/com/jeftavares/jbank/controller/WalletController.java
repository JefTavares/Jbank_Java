package br.com.jeftavares.jbank.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jeftavares.jbank.controller.dto.CreateWalletDto;
import br.com.jeftavares.jbank.service.WalletService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping()
    public ResponseEntity<Void> createWallet(@RequestBody @Valid CreateWalletDto dto) {

        var wallet = walletService.createWallet(dto);

        return ResponseEntity.created(URI.create("/wallets" + wallet.getId().toString())).build();
    }

    // Foi para a GlobalExceptionHandler
    // @ExceptionHandler(WalletDataAlreadyExistsException.class)
    // public ProblemDetail
    // handleWalletDataAlreadyExistsException(WalletDataAlreadyExistsException e) {
    // //É possível retornar um ResponseEntity<MeuDto> e customizar ele totalmente
    // para retornar as exceptions
    // var pd = ProblemDetail.forStatus(422);
    // pd.setTitle("Wallet data already exists");
    // pd.setDetail(e.getMessage());
    // return pd;

    // }
}