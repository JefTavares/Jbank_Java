package br.com.jeftavares.jbank.controller.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record DepositMoneyDTO(@NotNull @DecimalMin("1.00") BigDecimal value) {

}
