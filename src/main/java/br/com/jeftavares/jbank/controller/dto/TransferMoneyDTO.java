package br.com.jeftavares.jbank.controller.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record TransferMoneyDTO(
                @NotNull UUID sender,
                @NotNull @DecimalMin("0.01") BigDecimal value,
                @NotNull UUID receiver) {
}
