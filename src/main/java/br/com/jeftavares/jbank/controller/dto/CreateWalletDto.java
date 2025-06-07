package br.com.jeftavares.jbank.controller.dto;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateWalletDto(@CPF String cpf,
        @Email @NotBlank String email,
        @NotBlank String name) {
}