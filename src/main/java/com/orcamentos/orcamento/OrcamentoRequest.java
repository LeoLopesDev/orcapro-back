package com.orcamentos.orcamento;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record OrcamentoRequest(
        @NotNull(message = "Selecione um cliente")
        Long clienteId,
        @NotEmpty(message = "Adicione ao menos um item")
        @Valid
        List<ItemOrcamentoRequest> itens,
        @DecimalMin(value = "0.0", message = "Desconto não pode ser negativo")
        BigDecimal desconto,
        LocalDate validade,
        String prazoExecucao,
        String condicoesPagamento,
        String observacoes
) {
}
