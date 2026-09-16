package com.orcamentos.publico;

import jakarta.validation.constraints.NotNull;

public record RespostaOrcamentoRequest(
        @NotNull(message = "Informe se o orçamento foi aprovado ou recusado")
        boolean aprovado,
        String observacaoRecusa
) {
}
