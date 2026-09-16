package com.orcamentos.notificacao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Consumidor do evento de orcamento. Hoje so registra em log; e aqui que entraria
 * o envio real de e-mail/push. Trocar esta classe por uma integracao real nao exige
 * nenhuma mudanca no modulo de orcamentos, que so conhece a interface NotificacaoService.
 */
@Component
@Slf4j
public class NotificacaoListener {

    @Async
    @EventListener
    public void aoReceberEvento(OrcamentoEvento evento) {
        String mensagem = switch (evento.tipo()) {
            case ORCAMENTO_ENVIADO -> "Orçamento #%s enviado ao cliente.";
            case ORCAMENTO_VISUALIZADO -> "Orçamento #%s foi visualizado pelo cliente.";
            case ORCAMENTO_APROVADO -> "Orçamento #%s foi aprovado!";
            case ORCAMENTO_RECUSADO -> "Orçamento #%s foi recusado.";
        };
        log.info("[notificacao empresa={}] " + mensagem, evento.empresaId(), evento.numeroOrcamento());
    }

}
