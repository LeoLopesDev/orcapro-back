package com.orcamentos.notificacao;

/**
 * Evento interno publicado pelo modulo de orcamentos sempre que o status muda.
 * Quem consome (NotificacaoListener, e no futuro um modulo de relatorios,
 * por exemplo) nao precisa ser conhecido pelo publicador — mesmo desacoplamento
 * que teriamos com um broker externo, sem sair do processo da aplicacao.
 */
public record OrcamentoEvento(
        Long empresaId,
        Long orcamentoId,
        String numeroOrcamento,
        TipoNotificacao tipo
) {
}
