package com.orcamentos.publico;

public interface OrcamentoPublicoService {

    OrcamentoPublicoResponse visualizar(String tokenPublico);

    void responder(String tokenPublico, RespostaOrcamentoRequest resposta);

}
