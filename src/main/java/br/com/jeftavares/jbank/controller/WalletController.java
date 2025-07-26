package br.com.jeftavares.jbank.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.jeftavares.jbank.controller.dto.CreateWalletDto;
import br.com.jeftavares.jbank.controller.dto.DepositMoneyDTO;
import br.com.jeftavares.jbank.controller.dto.StatementDto;
import br.com.jeftavares.jbank.service.WalletService;
import jakarta.servlet.http.HttpServletRequest;
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

        return ResponseEntity.created(URI.create("/wallets/" + wallet.getId().toString())).build();
    }

    @DeleteMapping(path = "/{walletId}")
    public ResponseEntity<Void> deleteWallet(@PathVariable("walletId") UUID walletId) {

        var deleted = walletService.deleteWallet(walletId);

        // Retorna quando acha e retorna qunado não acha.
        // Se da erro, a GlobalExceptionHandler vai tratar
        // Ou o DeleteWalletException vai ser lançado
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PostMapping(path = "/{walletId}/deposits")
    public ResponseEntity<Void> depositMoney(
            @PathVariable("walletId") UUID walletId,
            @RequestBody @Valid DepositMoneyDTO dto,
            HttpServletRequest servletRequest) {
        // OBS: O @Valid é necessário para validar o DTO. Ele vai validar os
        // constraints do DTO, como o @NotNull e @DecimalMin.

        walletService.depositMoney(
                walletId,
                dto,
                servletRequest.getAttribute("x-user-ip").toString());

        return ResponseEntity.ok().build();

    }

    @GetMapping("/{walletId}/statements")
    public ResponseEntity<StatementDto> getStatement(
            @PathVariable("walletId") UUID walletId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        var statement = walletService.getStatement(walletId, page, pageSize);

        return ResponseEntity.ok(statement);

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