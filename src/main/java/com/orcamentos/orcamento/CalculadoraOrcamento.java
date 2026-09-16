package com.orcamentos.orcamento;

import java.math.BigDecimal;
import java.util.List;

public interface CalculadoraOrcamento {

    BigDecimal calcularSubtotal(List<ItemOrcamento> itens);

    BigDecimal calcularTotal(BigDecimal subtotal, BigDecimal desconto);

}
