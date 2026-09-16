package com.orcamentos.orcamento;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class CalculadoraOrcamentoImpl implements CalculadoraOrcamento {

    @Override
    public BigDecimal calcularSubtotal(List<ItemOrcamento> itens) {
        return itens.stream()
                .map(ItemOrcamento::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calcularTotal(BigDecimal subtotal, BigDecimal desconto) {
        BigDecimal descontoSeguro = desconto == null ? BigDecimal.ZERO : desconto;
        BigDecimal total = subtotal.subtract(descontoSeguro);
        return (total.signum() < 0 ? BigDecimal.ZERO : total).setScale(2, RoundingMode.HALF_UP);
    }

}
