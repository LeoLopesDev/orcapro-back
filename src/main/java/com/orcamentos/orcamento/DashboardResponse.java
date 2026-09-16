package com.orcamentos.orcamento;

import java.math.BigDecimal;

public record DashboardResponse(
        long orcamentosNoPeriodo,
        BigDecimal valorEnviado,
        BigDecimal valorAprovado,
        long aguardandoResposta,
        BigDecimal taxaAprovacao
) {
}
