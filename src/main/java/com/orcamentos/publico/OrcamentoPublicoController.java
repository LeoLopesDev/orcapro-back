package com.orcamentos.publico;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/publico/orcamentos")
@RequiredArgsConstructor
public class OrcamentoPublicoController {

    private final OrcamentoPublicoService orcamentoPublicoService;

    @GetMapping("/{token}")
    public OrcamentoPublicoResponse obter(@PathVariable String token) {
        return orcamentoPublicoService.visualizar(token);
    }

    @PostMapping("/{token}/resposta")
    public void responder(@PathVariable String token, @Valid @RequestBody RespostaOrcamentoRequest resposta) {
        orcamentoPublicoService.responder(token, resposta);
    }

}
