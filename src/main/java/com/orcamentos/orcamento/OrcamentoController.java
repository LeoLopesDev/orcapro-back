package com.orcamentos.orcamento;

import com.orcamentos.comum.ContextoAutenticacao;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orcamentos")
@RequiredArgsConstructor
public class OrcamentoController {

    private final OrcamentoService orcamentoService;

    @PostMapping
    public Orcamento criar(@Valid @RequestBody OrcamentoRequest request) {
        return orcamentoService.criar(ContextoAutenticacao.empresaId(), request);
    }

    @PutMapping("/{id}")
    public Orcamento atualizar(@PathVariable Long id, @Valid @RequestBody OrcamentoRequest request) {
        return orcamentoService.atualizar(ContextoAutenticacao.empresaId(), id, request);
    }

    @PostMapping("/{id}/duplicar")
    public Orcamento duplicar(@PathVariable Long id) {
        return orcamentoService.duplicar(ContextoAutenticacao.empresaId(), id);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        orcamentoService.excluir(ContextoAutenticacao.empresaId(), id);
    }

    @GetMapping("/{id}")
    public Orcamento obter(@PathVariable Long id) {
        return orcamentoService.buscarPorId(ContextoAutenticacao.empresaId(), id);
    }

    @GetMapping
    public List<Orcamento> listar(
            @RequestParam(required = false) StatusOrcamento status,
            @RequestParam(required = false) Long clienteId
    ) {
        return orcamentoService.listar(ContextoAutenticacao.empresaId(), status, clienteId);
    }

    @PostMapping("/{id}/enviar")
    public Orcamento enviar(@PathVariable Long id) {
        return orcamentoService.enviar(ContextoAutenticacao.empresaId(), id);
    }

    @PostMapping("/{id}/reabrir")
    public Orcamento reabrir(@PathVariable Long id) {
        return orcamentoService.reabrirParaEdicao(ContextoAutenticacao.empresaId(), id);
    }

    @GetMapping("/dashboard")
    public DashboardResponse dashboard() {
        return orcamentoService.obterDashboard(ContextoAutenticacao.empresaId());
    }

}
