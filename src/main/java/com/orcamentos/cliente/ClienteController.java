package com.orcamentos.cliente;

import com.orcamentos.comum.ContextoAutenticacao;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public Cliente criar(@Valid @RequestBody ClienteRequest request) {
        return clienteService.criar(ContextoAutenticacao.empresaId(), request);
    }

    @PutMapping("/{id}")
    public Cliente atualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequest request) {
        return clienteService.atualizar(ContextoAutenticacao.empresaId(), id, request);
    }

    @GetMapping("/{id}")
    public Cliente obter(@PathVariable Long id) {
        return clienteService.buscarPorId(ContextoAutenticacao.empresaId(), id);
    }

    @GetMapping
    public List<Cliente> listar(@RequestParam(required = false) String nome) {
        return clienteService.listar(ContextoAutenticacao.empresaId(), nome);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        clienteService.excluir(ContextoAutenticacao.empresaId(), id);
    }

}
