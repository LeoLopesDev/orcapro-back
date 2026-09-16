package com.orcamentos.notificacao;

import org.springframework.context.ApplicationEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificacaoServiceImpl implements NotificacaoService {

    private final ApplicationEventPublisher publisher;

    @Override
    public void publicar(OrcamentoEvento evento) {
        publisher.publishEvent(evento);
    }

}
