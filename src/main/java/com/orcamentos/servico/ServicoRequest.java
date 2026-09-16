package com.orcamentos.servico;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ServicoRequest(
        @NotBlank(message = "Informe o nome do serviço")
        String nome,
        String descricao,
        @NotNull(message = "Informe o preço")
        @DecimalMin(value = "0.0", message = "O preço não pode ser negativo")
        BigDecimal preco,
        String unidade
) {
}
