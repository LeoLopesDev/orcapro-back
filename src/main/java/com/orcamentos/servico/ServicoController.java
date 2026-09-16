package com.orcamentos.servico;

import com.orcamentos.comum.ContextoAutenticacao;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicos")
@RequiredArgsConstructor
public class ServicoController {

    private final ServicoService servicoService;

    @PostMapping
    public Servico criar(@Valid @RequestBody ServicoRequest request) {
        return servicoService.criar(ContextoAutenticacao.empresaId(), request);
    }

    @PutMapping("/{id}")
    public Servico atualizar(@PathVariable Long id, @Valid @RequestBody ServicoRequest request) {
        return servicoService.atualizar(ContextoAutenticacao.empresaId(), id, request);
    }

    @GetMapping
    public List<Servico> listar(@RequestParam(required = false) String nome) {
        return servicoService.listar(ContextoAutenticacao.empresaId(), nome);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        servicoService.excluir(ContextoAutenticacao.empresaId(), id);
    }

}
