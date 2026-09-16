package com.orcamentos.publico;

import com.orcamentos.orcamento.ItemOrcamento;
import com.orcamentos.orcamento.StatusOrcamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record OrcamentoPublicoResponse(
        String numero,
        StatusOrcamento status,
        String nomeEmpresa,
        String logotipoUrl,
        String telefoneEmpresa,
        String nomeCliente,
        List<ItemResumo> itens,
        BigDecimal subtotal,
        BigDecimal desconto,
        BigDecimal total,
        LocalDate validade,
        String prazoExecucao,
        String condicoesPagamento,
        String observacoes
) {
    public record ItemResumo(String descricao, BigDecimal quantidade, BigDecimal precoUnitario, BigDecimal subtotal) {
        public static ItemResumo de(ItemOrcamento item) {
            return new ItemResumo(item.getDescricao(), item.getQuantidade(), item.getPrecoUnitario(), item.getSubtotal());
        }
    }
}
