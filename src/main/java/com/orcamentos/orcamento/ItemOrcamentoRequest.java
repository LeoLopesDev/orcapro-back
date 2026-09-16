package com.orcamentos.orcamento;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ItemOrcamentoRequest(
        @NotBlank(message = "Descreva o item")
        String descricao,
        @NotNull @DecimalMin(value = "0.01", message = "Quantidade deve ser maior que zero")
        BigDecimal quantidade,
        @NotNull @DecimalMin(value = "0.0", message = "Preço não pode ser negativo")
        BigDecimal precoUnitario
) {
}
