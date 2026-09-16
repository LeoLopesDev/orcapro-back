package com.orcamentos.empresa;

import com.orcamentos.comum.ContextoAutenticacao;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/empresa")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;

    @GetMapping
    public EmpresaResponse obter() {
        Long empresaId = ContextoAutenticacao.empresaId();
        return EmpresaResponse.de(empresaService.buscarPorId(empresaId));
    }

    @PutMapping
    public EmpresaResponse atualizar(@Valid @RequestBody EmpresaRequest request) {
        Long empresaId = ContextoAutenticacao.empresaId();
        return EmpresaResponse.de(empresaService.atualizar(empresaId, request));
    }

    @PutMapping("/logotipo")
    public EmpresaResponse atualizarLogotipo(@RequestBody LogotipoRequest request) {
        Long empresaId = ContextoAutenticacao.empresaId();
        return EmpresaResponse.de(empresaService.atualizarLogotipo(empresaId, request.logotipoUrl()));
    }

    public record LogotipoRequest(String logotipoUrl) {
    }

}
