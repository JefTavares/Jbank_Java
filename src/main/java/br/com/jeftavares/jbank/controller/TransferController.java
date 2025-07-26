package br.com.jeftavares.jbank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jeftavares.jbank.controller.dto.TransferMoneyDTO;
import br.com.jeftavares.jbank.service.TransferService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping()
    public ResponseEntity<Void> transfer(@RequestBody @Valid TransferMoneyDTO dto) {
        // @Valid ativa a validação do DTO, verificando se os campos estão corretos.

        transferService.transferMoney(dto);

        return ResponseEntity.ok().build();

    }

}
