package com.orcamentos.orcamento;

import com.orcamentos.cliente.ClienteService;
import com.orcamentos.comum.RecursoNaoEncontradoException;
import com.orcamentos.comum.RegraDeNegocioException;
import com.orcamentos.notificacao.NotificacaoService;
import com.orcamentos.notificacao.OrcamentoEvento;
import com.orcamentos.notificacao.TipoNotificacao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrcamentoServiceImpl implements OrcamentoService {

    private final OrcamentoRepository orcamentoRepository;
    private final ClienteService clienteService;
    private final CalculadoraOrcamento calculadoraOrcamento;
    private final NotificacaoService notificacaoService;

    @Value("${app.orcamento.validade-padrao-dias:7}")
    private int validadePadraoDias;

    @Override
    @Transactional
    public Orcamento criar(Long empresaId, OrcamentoRequest request) {
        // garante que o cliente pertence a esta empresa antes de vincular
        clienteService.buscarPorId(empresaId, request.clienteId());

        Orcamento orcamento = new Orcamento();
        orcamento.setEmpresaId(empresaId);
        orcamento.setClienteId(request.clienteId());
        orcamento.setNumero(proximoNumero(empresaId));
        orcamento.setStatus(StatusOrcamento.RASCUNHO);
        orcamento.setTokenPublico(UUID.randomUUID().toString());

        aplicarDadosDoRequest(orcamento, request);

        return orcamentoRepository.save(orcamento);
    }

    @Override
    @Transactional
    public Orcamento atualizar(Long empresaId, Long orcamentoId, OrcamentoRequest request) {
        Orcamento orcamento = buscarPorId(empresaId, orcamentoId);

        if (orcamento.getStatus() != StatusOrcamento.RASCUNHO) {
            throw new RegraDeNegocioException(
                    "Este orçamento já foi enviado. Volte-o para rascunho antes de editar.");
        }

        clienteService.buscarPorId(empresaId, request.clienteId());
        orcamento.setClienteId(request.clienteId());
        aplicarDadosDoRequest(orcamento, request);

        return orcamentoRepository.save(orcamento);
    }

    @Override
    @Transactional
    public Orcamento duplicar(Long empresaId, Long orcamentoId) {
        Orcamento original = buscarPorId(empresaId, orcamentoId);

        Orcamento copia = new Orcamento();
        copia.setEmpresaId(empresaId);
        copia.setClienteId(original.getClienteId());
        copia.setNumero(proximoNumero(empresaId));
        copia.setStatus(StatusOrcamento.RASCUNHO);
        copia.setTokenPublico(UUID.randomUUID().toString());
        copia.setDesconto(original.getDesconto());
        copia.setValidade(original.getValidade());
        copia.setPrazoExecucao(original.getPrazoExecucao());
        copia.setCondicoesPagamento(original.getCondicoesPagamento());
        copia.setObservacoes(original.getObservacoes());

        List<ItemOrcamento> itensCopiados = original.getItens().stream()
                .map(item -> new ItemOrcamento(null, null, item.getDescricao(), item.getQuantidade(), item.getPrecoUnitario()))
                .collect(Collectors.toList());
        copia.definirItens(itensCopiados);
        recalcular(copia);

        return orcamentoRepository.save(copia);
    }

    @Override
    @Transactional
    public void excluir(Long empresaId, Long orcamentoId) {
        Orcamento orcamento = buscarPorId(empresaId, orcamentoId);
        orcamentoRepository.delete(orcamento);
    }

    @Override
    public Orcamento buscarPorId(Long empresaId, Long orcamentoId) {
        return orcamentoRepository.findByIdAndEmpresaId(orcamentoId, empresaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Orçamento não encontrado."));
    }

    @Override
    public List<Orcamento> listar(Long empresaId, StatusOrcamento filtroStatus, Long filtroClienteId) {
        if (filtroStatus != null && filtroClienteId != null) {
            return orcamentoRepository.findByEmpresaIdAndStatusAndClienteIdOrderByIdDesc(empresaId, filtroStatus, filtroClienteId);
        }
        if (filtroStatus != null) {
            return orcamentoRepository.findByEmpresaIdAndStatusOrderByIdDesc(empresaId, filtroStatus);
        }
        if (filtroClienteId != null) {
            return orcamentoRepository.findByEmpresaIdAndClienteIdOrderByIdDesc(empresaId, filtroClienteId);
        }
        return orcamentoRepository.findByEmpresaIdOrderByIdDesc(empresaId);
    }

    @Override
    @Transactional
    public Orcamento enviar(Long empresaId, Long orcamentoId) {
        Orcamento orcamento = buscarPorId(empresaId, orcamentoId);

        if (orcamento.getStatus() != StatusOrcamento.RASCUNHO) {
            throw new RegraDeNegocioException("Este orçamento já foi enviado.");
        }
        if (orcamento.getItens().isEmpty()) {
            throw new RegraDeNegocioException("Adicione ao menos um item antes de enviar.");
        }

        orcamento.setStatus(StatusOrcamento.ENVIADO);
        orcamento.setEnviadoEm(LocalDateTime.now());
        orcamento = orcamentoRepository.save(orcamento);

        notificacaoService.publicar(new OrcamentoEvento(
                empresaId, orcamento.getId(), orcamento.getNumero(), TipoNotificacao.ORCAMENTO_ENVIADO));

        return orcamento;
    }

    @Override
    @Transactional
    public Orcamento reabrirParaEdicao(Long empresaId, Long orcamentoId) {
        Orcamento orcamento = buscarPorId(empresaId, orcamentoId);

        if (orcamento.getStatus() == StatusOrcamento.APROVADO || orcamento.getStatus() == StatusOrcamento.RECUSADO) {
            throw new RegraDeNegocioException("Um orçamento já respondido pelo cliente não pode voltar para rascunho.");
        }

        orcamento.setStatus(StatusOrcamento.RASCUNHO);
        return orcamentoRepository.save(orcamento);
    }

    @Override
    public DashboardResponse obterDashboard(Long empresaId) {
        long totalOrcamentos = orcamentoRepository.countByEmpresaId(empresaId);
        BigDecimal valorEnviado = orcamentoRepository.somarValorEnviado(empresaId);
        BigDecimal valorAprovado = orcamentoRepository.somarValorAprovado(empresaId);
        long aguardando = orcamentoRepository.countByEmpresaIdAndStatusIn(
                empresaId, List.of(StatusOrcamento.ENVIADO, StatusOrcamento.VISUALIZADO));
        long enviados = orcamentoRepository.countByEmpresaIdAndStatusIn(
                empresaId, List.of(StatusOrcamento.ENVIADO, StatusOrcamento.VISUALIZADO, StatusOrcamento.APROVADO, StatusOrcamento.RECUSADO));
        long aprovados = orcamentoRepository.countByEmpresaIdAndStatus(empresaId, StatusOrcamento.APROVADO);

        BigDecimal taxaAprovacao = enviados == 0
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(aprovados)
                    .divide(BigDecimal.valueOf(enviados), 4, java.math.RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(1, java.math.RoundingMode.HALF_UP);

        return new DashboardResponse(totalOrcamentos, valorEnviado, valorAprovado, aguardando, taxaAprovacao);
    }

    // ---------- privados ----------

    private void aplicarDadosDoRequest(Orcamento orcamento, OrcamentoRequest request) {
        List<ItemOrcamento> itens = request.itens().stream()
                .map(i -> new ItemOrcamento(null, null, i.descricao(), i.quantidade(), i.precoUnitario()))
                .collect(Collectors.toList());
        orcamento.definirItens(itens);

        orcamento.setDesconto(request.desconto() == null ? BigDecimal.ZERO : request.desconto());
        orcamento.setValidade(request.validade() != null
                ? request.validade()
                : java.time.LocalDate.now().plusDays(validadePadraoDias));
        orcamento.setPrazoExecucao(request.prazoExecucao());
        orcamento.setCondicoesPagamento(request.condicoesPagamento());
        orcamento.setObservacoes(request.observacoes());

        recalcular(orcamento);
    }

    private void recalcular(Orcamento orcamento) {
        BigDecimal subtotal = calculadoraOrcamento.calcularSubtotal(orcamento.getItens());
        BigDecimal total = calculadoraOrcamento.calcularTotal(subtotal, orcamento.getDesconto());
        orcamento.setSubtotal(subtotal);
        orcamento.setTotal(total);
    }

    private String proximoNumero(Long empresaId) {
        long quantidade = orcamentoRepository.countByEmpresaId(empresaId) + 1;
        return String.format("%04d", quantidade);
    }

}
