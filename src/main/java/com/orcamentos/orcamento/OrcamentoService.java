package com.orcamentos.orcamento;

import java.util.List;

public interface OrcamentoService {

    Orcamento criar(Long empresaId, OrcamentoRequest request);

    Orcamento atualizar(Long empresaId, Long orcamentoId, OrcamentoRequest request);

    Orcamento duplicar(Long empresaId, Long orcamentoId);

    void excluir(Long empresaId, Long orcamentoId);

    Orcamento buscarPorId(Long empresaId, Long orcamentoId);

    List<Orcamento> listar(Long empresaId, StatusOrcamento filtroStatus, Long filtroClienteId);

    Orcamento enviar(Long empresaId, Long orcamentoId);

    Orcamento reabrirParaEdicao(Long empresaId, Long orcamentoId);

    DashboardResponse obterDashboard(Long empresaId);

}
