package com.orcamentos.publico;

import com.orcamentos.cliente.Cliente;
import com.orcamentos.cliente.ClienteService;
import com.orcamentos.comum.RecursoNaoEncontradoException;
import com.orcamentos.comum.RegraDeNegocioException;
import com.orcamentos.empresa.Empresa;
import com.orcamentos.empresa.EmpresaService;
import com.orcamentos.notificacao.NotificacaoService;
import com.orcamentos.notificacao.OrcamentoEvento;
import com.orcamentos.notificacao.TipoNotificacao;
import com.orcamentos.orcamento.Orcamento;
import com.orcamentos.orcamento.OrcamentoRepository;
import com.orcamentos.orcamento.StatusOrcamento;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrcamentoPublicoServiceImpl implements OrcamentoPublicoService {

    private static final Set<StatusOrcamento> VISIVEIS_PUBLICAMENTE = Set.of(
            StatusOrcamento.ENVIADO, StatusOrcamento.VISUALIZADO, StatusOrcamento.APROVADO, StatusOrcamento.RECUSADO
    );

    // Nota: este serviço lê o OrcamentoRepository diretamente (em vez de passar
    // pelo OrcamentoService, que exige empresaId) porque o acesso público é
    // identificado só pelo token, sem empresa autenticada no contexto.
    private final OrcamentoRepository orcamentoRepository;
    private final EmpresaService empresaService;
    private final ClienteService clienteService;
    private final NotificacaoService notificacaoService;

    @Override
    @Transactional
    public OrcamentoPublicoResponse visualizar(String tokenPublico) {
        Orcamento orcamento = buscarOrcamentoVisivel(tokenPublico);

        if (orcamento.getStatus() == StatusOrcamento.ENVIADO) {
            orcamento.setStatus(StatusOrcamento.VISUALIZADO);
            orcamento.setVisualizadoEm(LocalDateTime.now());
            orcamento = orcamentoRepository.save(orcamento);

            notificacaoService.publicar(new OrcamentoEvento(
                    orcamento.getEmpresaId(), orcamento.getId(), orcamento.getNumero(),
                    TipoNotificacao.ORCAMENTO_VISUALIZADO));
        }

        Empresa empresa = empresaService.buscarPorId(orcamento.getEmpresaId());
        Cliente cliente = clienteService.buscarPorId(orcamento.getEmpresaId(), orcamento.getClienteId());

        return new OrcamentoPublicoResponse(
                orcamento.getNumero(),
                orcamento.getStatus(),
                empresa.getNome(),
                empresa.getLogotipoUrl(),
                empresa.getTelefone(),
                cliente.getNome(),
                orcamento.getItens().stream()
                        .map(OrcamentoPublicoResponse.ItemResumo::de)
                        .collect(Collectors.toList()),
                orcamento.getSubtotal(),
                orcamento.getDesconto(),
                orcamento.getTotal(),
                orcamento.getValidade(),
                orcamento.getPrazoExecucao(),
                orcamento.getCondicoesPagamento(),
                orcamento.getObservacoes()
        );
    }

    @Override
    @Transactional
    public void responder(String tokenPublico, RespostaOrcamentoRequest resposta) {
        Orcamento orcamento = buscarOrcamentoVisivel(tokenPublico);

        if (orcamento.getStatus() == StatusOrcamento.APROVADO || orcamento.getStatus() == StatusOrcamento.RECUSADO) {
            throw new RegraDeNegocioException("Este orçamento já recebeu uma resposta.");
        }

        orcamento.setStatus(resposta.aprovado() ? StatusOrcamento.APROVADO : StatusOrcamento.RECUSADO);
        orcamento.setRespondidoEm(LocalDateTime.now());
        if (!resposta.aprovado()) {
            orcamento.setObservacaoRecusa(resposta.observacaoRecusa());
        }
        orcamentoRepository.save(orcamento);

        notificacaoService.publicar(new OrcamentoEvento(
                orcamento.getEmpresaId(), orcamento.getId(), orcamento.getNumero(),
                resposta.aprovado() ? TipoNotificacao.ORCAMENTO_APROVADO : TipoNotificacao.ORCAMENTO_RECUSADO));
    }

    private Orcamento buscarOrcamentoVisivel(String tokenPublico) {
        Orcamento orcamento = orcamentoRepository.findByTokenPublico(tokenPublico)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Orçamento não encontrado."));

        if (!VISIVEIS_PUBLICAMENTE.contains(orcamento.getStatus())) {
            throw new RecursoNaoEncontradoException("Orçamento não encontrado.");
        }
        return orcamento;
    }

}
