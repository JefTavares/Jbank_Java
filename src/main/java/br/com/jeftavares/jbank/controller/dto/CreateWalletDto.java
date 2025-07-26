package br.com.jeftavares.jbank.controller.dto;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateWalletDto(
                @NotBlank @CPF String cpf,
                @NotBlank @Email String email,
                @NotBlank String name) {
}